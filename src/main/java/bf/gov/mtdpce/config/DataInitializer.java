package bf.gov.mtdpce.config;

import bf.gov.mtdpce.entity.*;
import bf.gov.mtdpce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private FAQRepository faqRepository;

    @Autowired
    private EServiceRepository eServiceRepository;

    @Autowired
    private FlashInfoRepository flashInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired private ThemeRepository themeRepository;

    @Override
    public void run(String... args) throws Exception {
        initRoles();
        initUsers();
        initTheme();
        //initArticles();
        //initProjects();
        //initDocuments();
        //initEvents();
        //initFAQs();
        //initEServices();
       // initFlashInfos();
    }

    private void initRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(Role.builder().name(ERole.ROLE_USER).description("Utilisateur standard").build());
            roleRepository.save(Role.builder().name(ERole.ROLE_MODERATOR).description("Modérateur de contenu").build());
            roleRepository.save(Role.builder().name(ERole.ROLE_ADMIN).description("Administrateur").build());
            roleRepository.save(Role.builder().name(ERole.ROLE_SUPER_ADMIN).description("Super Administrateur").build());
        }
    }

    public void initTheme()
    {
        if(themeRepository.count()==0)
        {
            themeRepository.save(Theme.builder().title("Rouge & Or").primaryColor("#f43f5e").accentColor("#ca8a04").secondaryColor("#6366f1").tertiaryColor("#14b8a6").build());
        }
    }
    private void initUsers() {
        if (userRepository.count() == 0) {
            Role superAdminRole = roleRepository.findByName(ERole.ROLE_SUPER_ADMIN).orElseThrow();
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow();
            Role moderatorRole = roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow();
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow();

            Set<Role> superAdminRoles = new HashSet<>();
            superAdminRoles.add(superAdminRole);

            User superAdmin = User.builder()
                    .username("superadmin")
                    .email("superadmin@mtdpce.gov.bf")
                    .password(passwordEncoder.encode("SuperAdmin2024"))
                    .firstName("Super")
                    .lastName("Administrateur")
                    .position("Directeur Général")
                    .department("Direction Générale")
                    .enabled(true)
                    .accountNonLocked(true)
                    .roles(superAdminRoles)
                    .build();
            userRepository.save(superAdmin);

            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);

            User admin = User.builder()
                    .username("admin")
                    .email("admin@mtdpce.gov.bf")
                    .password(passwordEncoder.encode("Admin2024"))
                    .firstName("Administrateur")
                    .lastName("Système")
                    .position("Administrateur Système")
                    .department("Direction des Systèmes d'Information")
                    .enabled(true)
                    .accountNonLocked(true)
                    .roles(adminRoles)
                    .build();
            userRepository.save(admin);

            Set<Role> modRoles = new HashSet<>();
            modRoles.add(moderatorRole);

            User moderator = User.builder()
                    .username("moderateur")
                    .email("moderateur@mtdpce.gov.bf")
                    .password(passwordEncoder.encode("Moderateur2024"))
                    .firstName("Modérateur")
                    .lastName("Contenu")
                    .position("Chargé de Communication")
                    .department("Direction de la Communication")
                    .enabled(true)
                    .accountNonLocked(true)
                    .roles(modRoles)
                    .build();
            userRepository.save(moderator);

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(userRole);

            User user = User.builder()
                    .username("utilisateur")
                    .email("utilisateur@mtdpce.gov.bf")
                    .password(passwordEncoder.encode("Utilisateur2024"))
                    .firstName("Utilisateur")
                    .lastName("Standard")
                    .position("Agent")
                    .department("Direction Technique")
                    .enabled(true)
                    .accountNonLocked(true)
                    .roles(userRoles)
                    .build();
            userRepository.save(user);
        }
    }

    private void initArticles() {
        if (articleRepository.count() == 0) {
            User author = userRepository.findByUsername("moderateur").orElseThrow();

            articleRepository.save(Article.builder()
                    .title("Lancement du Programme National de Transformation Digitale")
                    .summary("Le Ministère lance un ambitieux programme de transformation digitale pour moderniser l'administration publique du Burkina Faso.")
                    .content("Le Ministère de la Transition Digitale, des Postes et des Communications Électroniques a officiellement lancé le Programme National de Transformation Digitale (PNTD). Ce programme vise à moderniser l'ensemble des services publics en intégrant les technologies numériques dans tous les aspects de l'administration.")
                    .category(ArticleCategory.ACTUALITE)
                    .status(ArticleStatus.PUBLISHED)
                    .featured(true)
                    .author(author)
                    .publishedAt(LocalDateTime.now().minusDays(2))
                    .viewCount(150)
                    .build());

            articleRepository.save(Article.builder()
                    .title("Signature d'un partenariat stratégique avec l'Union Africaine")
                    .summary("Un accord de coopération a été signé pour renforcer les infrastructures numériques du pays.")
                    .content("Le Burkina Faso a signé un accord de partenariat stratégique avec l'Union Africaine dans le cadre de l'initiative Africa Digital.")
                    .category(ArticleCategory.COMMUNIQUE)
                    .status(ArticleStatus.PUBLISHED)
                    .featured(true)
                    .author(author)
                    .publishedAt(LocalDateTime.now().minusDays(5))
                    .viewCount(89)
                    .build());

            articleRepository.save(Article.builder()
                    .title("Forum National sur la Cybersécurité 2026")
                    .summary("Le ministère organise le premier forum national dédié à la cybersécurité les 15 et 16 février 2026.")
                    .content("Le Ministère de la Transition Digitale organise le premier Forum National sur la Cybersécurité qui se tiendra à Ouagadougou.")
                    .category(ArticleCategory.EVENEMENT)
                    .status(ArticleStatus.PUBLISHED)
                    .featured(false)
                    .author(author)
                    .publishedAt(LocalDateTime.now().minusDays(1))
                    .viewCount(45)
                    .build());
        }
    }

    private void initProjects() {
        if (projectRepository.count() == 0) {
            User manager = userRepository.findByUsername("admin").orElseThrow();

            projectRepository.save(Project.builder()
                    .name("e-Gouvernement Burkina")
                    .description("Plateforme intégrée de services publics en ligne.")
                    .objectives("Dématérialiser 80% des procédures administratives d'ici 2027")
                    .status(ProjectStatus.EN_COURS)
                    .budget(new BigDecimal("2500000000"))
                    .progressPercentage(45)
                    .startDate(LocalDate.of(2024, 1, 15))
                    .endDate(LocalDate.of(2027, 12, 31))
                    .partner("Banque Mondiale")
                    .responsibleDepartment("Direction de la Modernisation")
                    .manager(manager)
                    .build());

            projectRepository.save(Project.builder()
                    .name("Backbone National Fibre Optique")
                    .description("Déploiement d'un réseau national de fibre optique.")
                    .objectives("Connecter les 13 régions du Burkina Faso par fibre optique")
                    .status(ProjectStatus.EN_COURS)
                    .budget(new BigDecimal("15000000000"))
                    .progressPercentage(65)
                    .startDate(LocalDate.of(2023, 6, 1))
                    .endDate(LocalDate.of(2026, 6, 30))
                    .partner("Union Européenne")
                    .responsibleDepartment("Direction des Infrastructures")
                    .manager(manager)
                    .build());
        }
    }

    private void initDocuments() {
        if (documentRepository.count() == 0) {
            User uploader = userRepository.findByUsername("admin").orElseThrow();

            documentRepository.save(Document.builder()
                    .title("Stratégie Nationale de Transformation Digitale 2025-2030")
                    .description("Document de référence présentant la vision et les axes stratégiques.")
                    .fileName("strategie_nationale_td_2025_2030.pdf")
                    .filePath("/documents/strategie_nationale_td_2025_2030.pdf")
                    .fileType("application/pdf")
                    .fileSize(2500000L)
                    .category(DocumentCategory.RAPPORT)
                    .isPublic(true)
                    .downloadCount(250)
                    .uploadedBy(uploader)
                    .build());

            documentRepository.save(Document.builder()
                    .title("Guide de demande d'agrément technique")
                    .description("Procédure et formulaires pour obtenir un agrément technique.")
                    .fileName("guide_agrement_technique.pdf")
                    .filePath("/documents/guide_agrement_technique.pdf")
                    .fileType("application/pdf")
                    .fileSize(1200000L)
                    .category(DocumentCategory.GUIDE)
                    .isPublic(true)
                    .downloadCount(180)
                    .uploadedBy(uploader)
                    .build());
        }
    }

    private void initEvents() {
        if (eventRepository.count() == 0) {
            Event e1 = new Event();
            e1.setTitle("Forum National sur la Cybersécurité");
            e1.setDescription("Premier forum national dédié à la cybersécurité au Burkina Faso.");
            e1.setStartDate(LocalDateTime.of(2026, 3, 15, 8, 0));
            e1.setEndDate(LocalDateTime.of(2026, 3, 17, 18, 0));
            e1.setLocation("Ouagadougou, Centre International de Conférences");
            e1.setCategory("Conférence");
            e1.setIsPublic(true);
            e1.setMaxParticipants(500);
            e1.setRegistrationRequired(true);
            e1.setStatus(EventStatus.UPCOMING);
            eventRepository.save(e1);

            Event e2 = new Event();
            e2.setTitle("Hackathon e-Gov 2026");
            e2.setDescription("Compétition de développement de solutions e-gouvernement.");
            e2.setStartDate(LocalDateTime.of(2026, 4, 20, 9, 0));
            e2.setEndDate(LocalDateTime.of(2026, 4, 22, 18, 0));
            e2.setLocation("Ouagadougou, Université Joseph Ki-Zerbo");
            e2.setCategory("Hackathon");
            e2.setIsPublic(true);
            e2.setMaxParticipants(200);
            e2.setRegistrationRequired(true);
            e2.setStatus(EventStatus.UPCOMING);
            eventRepository.save(e2);

            Event e3 = new Event();
            e3.setTitle("Journée Mondiale des Télécommunications");
            e3.setDescription("Célébration de la journée mondiale des télécommunications.");
            e3.setStartDate(LocalDateTime.of(2026, 5, 17, 8, 0));
            e3.setEndDate(LocalDateTime.of(2026, 5, 17, 18, 0));
            e3.setLocation("Ouagadougou");
            e3.setCategory("Célébration");
            e3.setIsPublic(true);
            e3.setRegistrationRequired(false);
            e3.setStatus(EventStatus.UPCOMING);
            eventRepository.save(e3);
        }
    }

    private void initFAQs() {
        if (faqRepository.count() == 0) {
            FAQ f1 = new FAQ();
            f1.setQuestion("Comment obtenir un agrément technique pour mon entreprise informatique ?");
            f1.setAnswer("Pour obtenir un agrément technique, vous devez soumettre un dossier comprenant : une demande adressée au Ministre, les statuts de l'entreprise, le RCCM, les diplômes des techniciens, et une attestation fiscale. Le délai de traitement est de 30 jours ouvrables.");
            f1.setCategory("Agrément");
            f1.setTags("agrément,entreprise,informatique");
            f1.setDisplayOrder(1);
            f1.setIsPublished(true);
            faqRepository.save(f1);

            FAQ f2 = new FAQ();
            f2.setQuestion("Quels sont les services disponibles en ligne ?");
            f2.setAnswer("Le ministère propose plusieurs services en ligne : demande d'agrément technique, déclaration d'opérateur télécom, consultation des textes réglementaires, et inscription à la newsletter.");
            f2.setCategory("Services");
            f2.setTags("services,en ligne,digital");
            f2.setDisplayOrder(2);
            f2.setIsPublished(true);
            faqRepository.save(f2);

            FAQ f3 = new FAQ();
            f3.setQuestion("Comment contacter le ministère ?");
            f3.setAnswer("Vous pouvez nous contacter par téléphone au +226 25 30 XX XX, par email à contact@mtdpce.gov.bf, ou en vous rendant à nos bureaux situés à Ouagadougou.");
            f3.setCategory("Contact");
            f3.setTags("contact,téléphone,email");
            f3.setDisplayOrder(3);
            f3.setIsPublished(true);
            faqRepository.save(f3);

            FAQ f4 = new FAQ();
            f4.setQuestion("Quelles sont les conditions pour devenir opérateur télécom ?");
            f4.setAnswer("Pour devenir opérateur télécom, vous devez obtenir une licence auprès de l'ARCEP. Les conditions incluent : un capital social minimum, une expertise technique prouvée, et un plan d'affaires viable.");
            f4.setCategory("Télécommunications");
            f4.setTags("télécom,licence,opérateur");
            f4.setDisplayOrder(4);
            f4.setIsPublished(true);
            faqRepository.save(f4);

            FAQ f5 = new FAQ();
            f5.setQuestion("Comment signaler un incident de cybersécurité ?");
            f5.setAnswer("Pour signaler un incident de cybersécurité, contactez l'ANSSI (Agence Nationale de Sécurité des Systèmes d'Information) par email à incident@anssi.gov.bf ou par téléphone au numéro d'urgence.");
            f5.setCategory("Cybersécurité");
            f5.setTags("cybersécurité,incident,ANSSI");
            f5.setDisplayOrder(5);
            f5.setIsPublished(true);
            faqRepository.save(f5);
        }
    }

    private void initEServices() {
        if (eServiceRepository.count() == 0) {
            EService s1 = new EService();
            s1.setName("Demande d'Agrément Technique");
            s1.setDescription("Service de demande d'agrément technique pour les entreprises du secteur informatique.");
            s1.setCategory("Agrément");
            s1.setTargetAudience("Entreprises informatiques");
            s1.setCost(new BigDecimal("50000"));
            s1.setProcessingTime("30 jours ouvrables");
            s1.setRequiredDocuments("Demande adressée au Ministre|||Statuts de l'entreprise|||RCCM|||Diplômes des techniciens|||Attestation fiscale");
            s1.setIsOnline(true);
            s1.setIsActive(true);
            s1.setDisplayOrder(1);
            s1.setIconName("certificate");
            eServiceRepository.save(s1);

            EService s2 = new EService();
            s2.setName("Déclaration d'Opérateur Télécom");
            s2.setDescription("Déclaration obligatoire pour les opérateurs de télécommunications.");
            s2.setCategory("Télécommunications");
            s2.setTargetAudience("Opérateurs télécom");
            s2.setCost(new BigDecimal("100000"));
            s2.setProcessingTime("45 jours ouvrables");
            s2.setIsOnline(false);
            s2.setIsActive(true);
            s2.setDisplayOrder(2);
            s2.setIconName("phone");
            eServiceRepository.save(s2);

            EService s3 = new EService();
            s3.setName("Certification Cybersécurité");
            s3.setDescription("Certification des systèmes d'information pour la conformité aux normes de sécurité.");
            s3.setCategory("Cybersécurité");
            s3.setTargetAudience("Entreprises et administrations");
            s3.setCost(new BigDecimal("200000"));
            s3.setProcessingTime("60 jours ouvrables");
            s3.setIsOnline(true);
            s3.setIsActive(true);
            s3.setDisplayOrder(3);
            s3.setIconName("shield");
            eServiceRepository.save(s3);

            EService s4 = new EService();
            s4.setName("Enregistrement de Nom de Domaine .bf");
            s4.setDescription("Service d'enregistrement et de gestion des noms de domaine en .bf.");
            s4.setCategory("Internet");
            s4.setTargetAudience("Tous");
            s4.setCost(new BigDecimal("15000"));
            s4.setProcessingTime("3 jours ouvrables");
            s4.setIsOnline(true);
            s4.setIsActive(true);
            s4.setDisplayOrder(4);
            s4.setIconName("globe");
            eServiceRepository.save(s4);

            EService s5 = new EService();
            s5.setName("Autorisation d'Importation d'Équipements Télécom");
            s5.setDescription("Autorisation pour l'importation d'équipements de télécommunications.");
            s5.setCategory("Télécommunications");
            s5.setTargetAudience("Importateurs");
            s5.setCost(new BigDecimal("25000"));
            s5.setProcessingTime("15 jours ouvrables");
            s5.setIsOnline(false);
            s5.setIsActive(true);
            s5.setDisplayOrder(5);
            s5.setIconName("truck");
            eServiceRepository.save(s5);
        }
    }

    private void initFlashInfos() {
        if (flashInfoRepository.count() == 0) {
            FlashInfo f1 = new FlashInfo();
            f1.setTitle("Nouveau portail e-services disponible");
            f1.setContent("Le nouveau portail de services en ligne est maintenant opérationnel. Découvrez nos services digitaux.");
            f1.setLinkUrl("/services");
            f1.setLinkText("Accéder aux services");
            f1.setPriority(FlashInfoPriority.HIGH);
            f1.setIsActive(true);
            flashInfoRepository.save(f1);

            FlashInfo f2 = new FlashInfo();
            f2.setTitle("Inscription au Forum Cybersécurité ouverte");
            f2.setContent("Les inscriptions pour le Forum National sur la Cybersécurité 2026 sont ouvertes. Places limitées !");
            f2.setLinkUrl("/evenements");
            f2.setLinkText("S'inscrire maintenant");
            f2.setPriority(FlashInfoPriority.NORMAL);
            f2.setIsActive(true);
            flashInfoRepository.save(f2);

            FlashInfo f3 = new FlashInfo();
            f3.setTitle("Maintenance programmée");
            f3.setContent("Une maintenance est prévue le 30 janvier 2026 de 22h à 6h. Certains services seront indisponibles.");
            f3.setPriority(FlashInfoPriority.LOW);
            f3.setIsActive(true);
            flashInfoRepository.save(f3);
        }
    }
}
