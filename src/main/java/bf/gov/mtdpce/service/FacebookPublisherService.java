package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.FacebookImageDTO;
import bf.gov.mtdpce.entity.Article;
import bf.gov.mtdpce.entity.ArticleImage;
import bf.gov.mtdpce.entity.FacebookImage;
import bf.gov.mtdpce.event.ArticlePublishedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.io.ByteArrayResource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FacebookPublisherService {

    @Value("${facebook.page-id}")
    private String pageId;

    @Value("${facebook.access-token}")
    private String accessToken;

    @Value("${facebook.graph-api-url}")
    private String graphApiUrl;

    @Value("${facebook.base-url}")
    private String baseUrl;

    private static final String UPLOAD_BASE_PATH = "/opt/mtdpce/uploads";

    private final RestTemplate restTemplate = new RestTemplate();

    @EventListener
    @Async
    public void onArticlePublished(ArticlePublishedEvent event) {
        Article article = event.getArticle();
        log.info("Publication Facebook de l'article : {}", article.getId());

        try {
            // Construire la liste complète des chemins d'images
            List<String> chemins = collecterCheminsImages(article);

            log.info("Nombre d'images trouvées pour l'article {} : {}",
                    article.getId(), chemins.size());

            if (chemins.isEmpty()) {
                publierSansImage(article);
            } else if (chemins.size() == 1) {
                publierAvecUneImage(article, chemins.get(0));
            } else {
                publierAvecPlusieursImages(article, chemins);
            }

        } catch (Exception e) {
            log.error("Erreur publication Facebook article {} : {}",
                    article.getId(), e.getMessage());
        }
    }

    // Collecte featuredImage + images de la liste ArticleImage
    private List<String> collecterCheminsImages(Article article) {
        List<String> chemins = new ArrayList<>();

        // 1. Ajouter la featuredImage en premier si elle existe
        if (article.getFeaturedImage() != null
                && !article.getFeaturedImage().isBlank()) {
            chemins.add(article.getFeaturedImage());
        }

        // 2. Ajouter les images de la liste (en évitant les doublons)
        if (article.getImagesFacebook() != null) {
            // Mettre d'abord les images featured de la liste
            article.getImagesFacebook().stream()
                    .filter(img -> Boolean.TRUE.equals(img.getIsFeatured()))
                    .map(FacebookImage::getImageUrl)
                    .filter(url -> !chemins.contains(url))
                    .forEach(chemins::add);

            // Puis les autres images
            article.getImagesFacebook().stream()
                    .filter(img -> !Boolean.TRUE.equals(img.getIsFeatured()))
                    .map(FacebookImage::getImageUrl)
                    .filter(url -> !chemins.contains(url))
                    .forEach(chemins::add);
        }

        return chemins;
    }

    // Cas 1 image : publication directe via /photos
    private void publierAvecUneImage(Article article, String cheminRelatif) {
        try {
            log.info("Publication avec 1 image : {}", cheminRelatif);

            Path cheminFichier = construireCheminFichier(cheminRelatif);
            if (cheminFichier == null) {
                publierSansImage(article);
                return;
            }

            byte[] imageBytes = Files.readAllBytes(cheminFichier);
            String fileName = cheminFichier.getFileName().toString();

            String url = graphApiUrl + "/" + pageId + "/photos";

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("access_token", accessToken);
            body.add("published", "true");
            body.add("message", construireMessage(article));
            body.add("source", new ByteArrayResource(imageBytes) {
                @Override
                public String getFilename() { return fileName; }
            });

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    url, new HttpEntity<>(body, headers), String.class
            );
            log.info("Article {} publié avec 1 image : {}",
                    article.getId(), response.getBody());

        } catch (Exception e) {
            log.error("Erreur publication 1 image, fallback sans image : {}",
                    e.getMessage());
            publierSansImage(article);
        }
    }

    // Cas N images : upload chaque image puis post groupé
    private void publierAvecPlusieursImages(Article article, List<String> chemins) {
        log.info("Publication avec {} images", chemins.size());

        List<String> photoIds = new ArrayList<>();

        // 1. Upload images
        for (String chemin : chemins) {
            String photoId = uploaderImageNonPubliee(chemin);

            if (photoId != null) {
                photoIds.add(photoId);
                log.info("Image uploadée : {} → photoId: {}", chemin, photoId);
            }
        }

        if (photoIds.isEmpty()) {
            publierSansImage(article);
            return;
        }

        try {
            String url = graphApiUrl + "/" + pageId + "/feed";

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("access_token", accessToken);

            // ✅ TEXTE + lien DANS le message
            params.add("message", construireMessage(article));

            // ✅ Ajouter les images
            for (int i = 0; i < photoIds.size(); i++) {
                params.add(
                        "attached_media[" + i + "]",
                        "{\"media_fbid\":\"" + photoIds.get(i) + "\"}"
                );
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request =
                    new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    url,
                    request,
                    String.class
            );

            log.info("Article {} publié avec {} images : {}",
                    article.getId(), photoIds.size(), response.getBody());

        } catch (Exception e) {
            log.error("Erreur publication multi-images : {}", e.getMessage());
            publierSansImage(article);
        }
    }

    // Cas 0 image : post texte + lien
    private void publierSansImage(Article article) {
        try {
            String url = graphApiUrl + "/" + pageId + "/feed";

            Map<String, String> body = new HashMap<>();
            body.put("message", construireMessage(article));
            body.put("link", baseUrl + "/articles/" + article.getId());
            body.put("access_token", accessToken);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    url, body, String.class
            );
            log.info("Article {} publié sans image : {}",
                    article.getId(), response.getBody());

        } catch (Exception e) {
            log.error("Erreur publication sans image article {} : {}",
                    article.getId(), e.getMessage());
        }
    }

    // Upload une image en "non publiée" et retourne son photoId
    private String uploaderImageNonPubliee(String cheminRelatif) {
        try {
            Path cheminFichier = construireCheminFichier(cheminRelatif);
            if (cheminFichier == null) return null;

            byte[] imageBytes = Files.readAllBytes(cheminFichier);
            String fileName = cheminFichier.getFileName().toString();

            String url = graphApiUrl + "/" + pageId + "/photos";

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("access_token", accessToken);
            body.add("published", "false");
            body.add("source", new ByteArrayResource(imageBytes) {
                @Override
                public String getFilename() { return fileName; }
            });

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    url, new HttpEntity<>(body, headers), Map.class
            );

            if (response.getBody() != null
                    && response.getBody().containsKey("id")) {
                return response.getBody().get("id").toString();
            }

        } catch (Exception e) {
            log.error("Erreur upload image {} : {}", cheminRelatif, e.getMessage());
        }
        return null;
    }

    // Reconstruit le chemin physique depuis le chemin relatif
    private Path construireCheminFichier(String cheminRelatif) {
        try {
            // /uploads/images/2026/03/fichier.png
            // → /opt/mtdpce/uploads/images/2026/03/fichier.png
            String sousChemín = cheminRelatif.replace("/uploads", "");
            Path cheminFichier = Paths.get(UPLOAD_BASE_PATH + sousChemín);

            log.info("Chemin physique : '{}'", cheminFichier);

            if (!Files.exists(cheminFichier)) {
                log.error("Fichier introuvable : {}", cheminFichier);
                return null;
            }
            return cheminFichier;

        } catch (Exception e) {
            log.error("Erreur construction chemin : {}", e.getMessage());
            return null;
        }
    }

    private String construireMessage(Article article) {
        return String.format(
                "%s\n\n%s\n\nCategorie : %s\n\nLire la suite : %s",
                article.getTitle(),
                article.getSummary(),
                article.getCategory().name(),
                baseUrl + "/articles/" + article.getId()
        );
    }
}