package bf.gov.mtdpce.service;
import java.util.UUID;

import bf.gov.mtdpce.dto.response.ArticleImageResponse;
import bf.gov.mtdpce.dto.response.FacebookImageResponse;
import bf.gov.mtdpce.dto.request.ArticleRequest;
import bf.gov.mtdpce.dto.response.ArticleResponse;
import bf.gov.mtdpce.entity.*;
import bf.gov.mtdpce.event.ArticlePublishedEvent;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.repository.ArticleCategoryRepository;
import bf.gov.mtdpce.repository.ArticleRepository;
import bf.gov.mtdpce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleCategoryRepository articleCategoryRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private static final String DEFAULT_CATEGORY_CODE = "ACTUALITE";

    /** Résout une catégorie à partir de son code, avec repli sur la catégorie par défaut. */
    private ArticleCategory resolveCategory(String code) {
        if (code != null && !code.isBlank()) {
            return articleCategoryRepository.findByCode(code)
                    .orElseThrow(() -> new ResourceNotFoundException("Catégorie d'article", "code", code));
        }
        return articleCategoryRepository.findByCode(DEFAULT_CATEGORY_CODE)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie d'article", "code", DEFAULT_CATEGORY_CODE));
    }

    public Page<ArticleResponse> getAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable).map(this::convertToResponse);
    }

    public Page<ArticleResponse> getPublishedArticles(Pageable pageable) {
        return articleRepository.findByStatus(ArticleStatus.PUBLISHED, pageable).map(this::convertToResponse);
    }

    public Page<ArticleResponse> getArticlesByCategory(String categoryCode, Pageable pageable) {
        return articleRepository.findByStatusAndCategory_Code(ArticleStatus.PUBLISHED, categoryCode, pageable)
                .map(this::convertToResponse);
    }

    public Page<ArticleResponse> searchPublishedArticles(String search, Pageable pageable) {
        return articleRepository.searchPublishedArticles(search, ArticleStatus.PUBLISHED, pageable)
                .map(this::convertToResponse);
    }

    public List<ArticleResponse> getLatestArticles() {
        return articleRepository.findTop5ByStatusOrderByPublishedAtDesc(ArticleStatus.PUBLISHED)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ArticleResponse> getFeaturedArticles() {
        return articleRepository.findByFeaturedTrueAndStatusOrderByPublishedAtDesc(ArticleStatus.PUBLISHED)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ArticleResponse getArticleById(UUID id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", id));
        return convertToResponse(article);
    }

    @Transactional
    public ArticleResponse getPublishedArticleById(UUID id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", id));
        
        if (article.getStatus() != ArticleStatus.PUBLISHED) {
            throw new ResourceNotFoundException("Article", "id", id);
        }
        
        // Increment view count
        article.setViewCount(article.getViewCount() + 1);
        articleRepository.save(article);
        
        return convertToResponse(article);
    }

    @Transactional
    public ArticleResponse createArticle(ArticleRequest articleDTO, UUID authorId,List<String> imagePaths,List<String> imagePathsFacebook) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", authorId));

        Article article = Article.builder()
                .title(articleDTO.getTitle())
                .summary(articleDTO.getSummary())
                .content(articleDTO.getContent())
                .featuredImage(articleDTO.getFeaturedImage())
                .category(resolveCategory(articleDTO.getCategory()))
                .status(articleDTO.getStatus() != null ? articleDTO.getStatus() : ArticleStatus.DRAFT)
                .featured(articleDTO.getFeatured() != null ? articleDTO.getFeatured() : false)
                .publishToFacebook(articleDTO.getPublishToFacebook() != null ? articleDTO.getPublishToFacebook() : false)
                .facebookContent(articleDTO.getFacebookContent())
                .author(author)
                .viewCount(0)
                .images(new ArrayList<>())
                .imagesFacebook(new ArrayList<>())
                .build();

        if (article.getStatus() == ArticleStatus.PUBLISHED) {
            article.setPublishedAt(LocalDateTime.now());
        }

        if (imagePaths != null && !imagePaths.isEmpty()) {
            for (String path : imagePaths) {
                ArticleImage image = ArticleImage.builder()
                        .imageUrl(path)
                        .article(article)
                        .build();
                article.getImages().add(image);
            }
        }

        if (imagePathsFacebook != null && !imagePathsFacebook.isEmpty()) {
            for (String path : imagePathsFacebook) {
                FacebookImage facebookImage = FacebookImage.builder()
                        .imageUrl(path)
                        .article(article)
                        .build();
                article.getImagesFacebook().add(facebookImage);
            }
        }
        Article saved = articleRepository.save(article);

        // NOUVEAU : publier sur Facebook si statut PUBLISHED dès la création ET case cochée
        if (saved.getStatus() == ArticleStatus.PUBLISHED
                && Boolean.TRUE.equals(saved.getPublishToFacebook())) {
//            eventPublisher.publishEvent(new ArticlePublishedEvent(saved));
            Article articleAvecImages = articleRepository
                    .findWithFacebookImagesById(saved.getId())
                    .orElse(saved);
            eventPublisher.publishEvent(new ArticlePublishedEvent(articleAvecImages));
        }
        return convertToResponse(saved);
    }

    @Transactional
    public ArticleResponse updateArticle(UUID id, ArticleRequest articleDTO,List<String> imagePaths,List<String> imagePathsFacebook) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", id));

        // Mémoriser le statut avant modification
        ArticleStatus ancienStatut = article.getStatus();

        if (articleDTO.getTitle() != null) article.setTitle(articleDTO.getTitle());
        if (articleDTO.getSummary() != null) article.setSummary(articleDTO.getSummary());
        if (articleDTO.getContent() != null) article.setContent(articleDTO.getContent());
        if (articleDTO.getFeaturedImage() != null) article.setFeaturedImage(articleDTO.getFeaturedImage());
        if (articleDTO.getCategory() != null) article.setCategory(resolveCategory(articleDTO.getCategory()));
        if (articleDTO.getFeatured() != null) article.setFeatured(articleDTO.getFeatured());
        if (articleDTO.getPublishToFacebook() != null) article.setPublishToFacebook(articleDTO.getPublishToFacebook());
        if (articleDTO.getFacebookContent() != null) article.setFacebookContent(articleDTO.getFacebookContent());

        if (articleDTO.getStatus() != null) {
            if (articleDTO.getStatus() == ArticleStatus.PUBLISHED && article.getStatus() != ArticleStatus.PUBLISHED) {
                article.setPublishedAt(LocalDateTime.now());
            }
            article.setStatus(articleDTO.getStatus());
        }

        if (imagePaths != null && !imagePaths.isEmpty()) {
            for (String path : imagePaths) {
                ArticleImage image = ArticleImage.builder()
                        .imageUrl(path)
                        .article(article)
                        .build();
                article.getImages().add(image);
            }
        }

        if (imagePathsFacebook != null && !imagePathsFacebook.isEmpty()) {
            for (String path : imagePathsFacebook) {
                FacebookImage facebookImage = FacebookImage.builder()
                        .imageUrl(path)
                        .article(article)
                        .build();
                article.getImagesFacebook().add(facebookImage);
            }
        }

        Article saved = articleRepository.save(article);

        // NOUVEAU : publier sur Facebook uniquement si on passe de DRAFT → PUBLISHED
        boolean vientDEtrePublie = ancienStatut != ArticleStatus.PUBLISHED
                && saved.getStatus() == ArticleStatus.PUBLISHED;

        if (vientDEtrePublie && Boolean.TRUE.equals(saved.getPublishToFacebook())) {
//            eventPublisher.publishEvent(new ArticlePublishedEvent(saved));
            Article articleAvecImages = articleRepository
                    .findWithFacebookImagesById(saved.getId())
                    .orElse(saved);
            eventPublisher.publishEvent(new ArticlePublishedEvent(articleAvecImages));
        }

        return convertToResponse(saved);
    }

    /**
     * Force la publication d'un article sur Facebook, indépendamment de son statut.
     * Utilise le contenu Facebook spécifique s'il est renseigné.
     */
    @Transactional
    public ArticleResponse publishOnFacebook(UUID id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "id", id));

        Article articleAvecImages = articleRepository
                .findWithFacebookImagesById(id)
                .orElse(article);

        eventPublisher.publishEvent(new ArticlePublishedEvent(articleAvecImages));
        return convertToResponse(article);
    }

    @Transactional
    public void deleteArticle(UUID id) {
        if (!articleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Article", "id", id);
        }
        articleRepository.deleteById(id);
    }

    public Long countPublishedArticles() {
        return articleRepository.countByStatus(ArticleStatus.PUBLISHED);
    }

    private ArticleResponse convertToResponse(Article article) {

        List<ArticleImageResponse> images = article.getImages()
                .stream()
                .map(img -> ArticleImageResponse.builder()
                        .id(img.getId())
                        .imageUrl(img.getImageUrl())
                        .build())
                .toList();
        List<FacebookImageResponse> imagesFacebook = article.getImagesFacebook()
                .stream()
                .map(img -> FacebookImageResponse.builder()
                        .id(img.getId())
                        .imageUrl(img.getImageUrl())
                        .build())
                .toList();
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .summary(article.getSummary())
                .content(article.getContent())
                .featuredImage(article.getFeaturedImage())
                .category(article.getCategory() != null ? article.getCategory().getCode() : null)
                .categoryLabel(article.getCategory() != null ? article.getCategory().getLabel() : null)
                .categoryId(article.getCategory() != null ? article.getCategory().getId() : null)
                .status(article.getStatus())
                .viewCount(article.getViewCount())
                .featured(article.getFeatured())
                .publishToFacebook(article.getPublishToFacebook())
                .facebookContent(article.getFacebookContent())
                .authorName(article.getAuthor().getFirstName() + " " + article.getAuthor().getLastName())
                .authorId(article.getAuthor().getId())
                .publishedAt(article.getPublishedAt())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .images(images)
                .imagesFacebook(imagesFacebook)
                .build();
    }
}
