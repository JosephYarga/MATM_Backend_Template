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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
    private bf.gov.mtdpce.repository.BannerRepository bannerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired private ThemeRepository themeRepository;

    @Autowired private MinistereRepository ministereRepository;

    @Autowired private MinistreRepository ministreRepository;

    @Autowired private MissionRepository missionRepository;

    @Autowired private DomaineRepository domaineRepository;

    @Autowired private StructureRepository structureRepository;

    @Autowired private StructureRattacheRepository structureRattacheRepository;

    @Autowired private ServicesRepository servicesRepository;

    @Autowired private StatistiquePublicRepository statistiquePublicRepository;

    @Autowired private ProjetCategorieRepository projetCategorieRepository;

    @Autowired private TypeRepository typeRepository;

    @Autowired private PageVisitRepository pageVisitRepository;

    @Autowired private ContactRepository contactRepository;

    @Autowired private JobOfferRepository jobOfferRepository;

    @Autowired private NewsletterSubscriptionRepository newsletterSubscriptionRepository;

    @Autowired private AgendaRepository agendaRepository;

    @Autowired private ArticleCategoryRepository articleCategoryRepository;

    @Autowired private AlbumRepository albumRepository;

    @Autowired private MediaRepository mediaRepository;

    @Autowired private AgendaImageRepository agendaImageRepository;

    @Autowired private ArticleImageRepository articleImageRepository;

    @Autowired private FacebookImageRepository facebookImageRepository;

    @Autowired private FacebookConfigRepository facebookConfigRepository;

    @Override
    public void run(String... args) throws Exception {
        // Données de base
        initRoles();
        initUsers();
        initThemes();

        // Données institutionnelles du MTDPCE (Burkina Faso)
        Ministere ministere = initMinistere();
        List<Domaine> domaines = initDomaines();
        initMinistres(ministere);
        initMissions(ministere);
        initStructures(ministere);
        initStructuresRattachees(ministere, domaines);
        initStatistiques();

        // Données de fréquentation (démo) pour les graphiques du tableau de bord
        initAnalyticsDemo();

        // Contenus du site
        initArticleCategories();
        initServices();
        initArticles();
        initProjects();
        initDocuments();
        initEvents();
        initFAQs();
        initEServices();
        initFlashInfos();
        initBanners();
        initAgendas();
        initJobOffers();
        initContacts();
        initNewsletterSubscriptions();

        // Médiathèque, galeries d'images et configuration Facebook
        initAlbumsAndMedia();
        initAgendaImages();
        initArticleImages();
        initFacebookConfig();
    }

    private void initRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(Role.builder().name(ERole.ROLE_USER).description("Utilisateur standard").build());
            roleRepository.save(Role.builder().name(ERole.ROLE_MODERATOR).description("Modérateur de contenu").build());
            roleRepository.save(Role.builder().name(ERole.ROLE_ADMIN).description("Administrateur").build());
            roleRepository.save(Role.builder().name(ERole.ROLE_SUPER_ADMIN).description("Super Administrateur").build());
        }
    }

    private void initThemes() {
        if (themeRepository.count() == 0) {
            String[][] themes = {
                    {"Rouge & Or", "#f43f5e", "#ca8a04", "#6366f1", "#14b8a6"},
                    {"Vert Burkina", "#00843B", "#EF2B2D", "#FCD116", "#0f766e"},
                    {"Bleu Institutionnel", "#1d4ed8", "#0ea5e9", "#6366f1", "#14b8a6"},
                    {"Émeraude", "#059669", "#10b981", "#34d399", "#a7f3d0"},
                    {"Indigo Nuit", "#4338ca", "#6366f1", "#818cf8", "#c7d2fe"},
                    {"Ambre Sahel", "#d97706", "#f59e0b", "#fbbf24", "#fde68a"},
                    {"Cyan Digital", "#0891b2", "#06b6d4", "#22d3ee", "#a5f3fc"},
                    {"Violet Royal", "#7c3aed", "#8b5cf6", "#a78bfa", "#ddd6fe"},
                    {"Rose Moderne", "#db2777", "#ec4899", "#f472b6", "#fbcfe8"},
                    {"Ardoise Pro", "#0f172a", "#334155", "#64748b", "#94a3b8"}
            };
            for (String[] t : themes) {
                themeRepository.save(Theme.builder()
                        .title(t[0])
                        .primaryColor(t[1])
                        .accentColor(t[2])
                        .secondaryColor(t[3])
                        .tertiaryColor(t[4])
                        .build());
            }
        }
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            Role superAdminRole = roleRepository.findByName(ERole.ROLE_SUPER_ADMIN).orElseThrow();
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow();
            Role moderatorRole = roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow();
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow();

            saveUser("superadmin", "superadmin@mtdpce.gov.bf", "SuperAdmin2024", "Super", "Administrateur",
                    "Directeur Général", "Direction Générale", superAdminRole);
            saveUser("admin", "admin@mtdpce.gov.bf", "Admin2024", "Administrateur", "Système",
                    "Administrateur Système", "Direction des Systèmes d'Information", adminRole);
            saveUser("moderateur", "moderateur@mtdpce.gov.bf", "Moderateur2024", "Modérateur", "Contenu",
                    "Chargé de Communication", "Direction de la Communication", moderatorRole);
            saveUser("utilisateur", "utilisateur@mtdpce.gov.bf", "Utilisateur2024", "Utilisateur", "Standard",
                    "Agent", "Direction Technique", userRole);

            // Agents supplémentaires (pour atteindre au moins 10 enregistrements)
            Object[][] agents = {
                    {"aminata.ouedraogo", "Aminata", "Ouedraogo", "Chargée de projets", "Direction de la Transformation Digitale", moderatorRole},
                    {"boukary.sawadogo", "Boukary", "Sawadogo", "Ingénieur réseau", "Direction des Infrastructures", userRole},
                    {"clarisse.kabore", "Clarisse", "Kaboré", "Juriste", "Direction des Affaires Juridiques", moderatorRole},
                    {"david.compaore", "David", "Compaoré", "Analyste cybersécurité", "Direction des Systèmes d'Information", userRole},
                    {"edwige.zongo", "Edwige", "Zongo", "Chargée de communication", "Direction de la Communication", moderatorRole},
                    {"fabrice.nikiema", "Fabrice", "Nikiema", "Administrateur base de données", "Direction des Systèmes d'Information", userRole}
            };
            for (Object[] a : agents) {
                saveUser((String) a[0], a[0] + "@mtdpce.gov.bf", "Passw0rd2024",
                        (String) a[1], (String) a[2], (String) a[3], (String) a[4], (Role) a[5]);
            }
        }
    }

    private void saveUser(String username, String email, String rawPassword, String firstName,
                          String lastName, String position, String department, Role role) {
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        userRepository.save(User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .firstName(firstName)
                .lastName(lastName)
                .position(position)
                .department(department)
                .enabled(true)
                .accountNonLocked(true)
                .roles(roles)
                .build());
    }

    // ====================================================================
    //  Données institutionnelles — Ministère de la Transition Digitale,
    //  des Postes et des Communications Électroniques (MTDPCE) - Burkina Faso
    // ====================================================================

    private Ministere initMinistere() {
        if (ministereRepository.count() > 0) {
            return ministereRepository.findAll().get(0);
        }

        Ministere m = new Ministere();
        m.setNomGeneral("Ministère de la Transition Digitale, des Postes et des Communications Électroniques");
        m.setNomReel("Ministère de la Transition Digitale, des Postes et des Communications Électroniques");
        m.setAcronyme("MTDPCE");
        m.setMissionGeneral(
                "Le MTDPCE assure la mise en œuvre et le suivi de la politique du Gouvernement du Burkina Faso "
                        + "en matière de transformation digitale, de développement de l'économie numérique, des postes "
                        + "et des communications électroniques. Il œuvre à la modernisation de l'administration publique, "
                        + "à la généralisation de l'accès au numérique et au renforcement de la cybersécurité nationale.");
        m.setPresentationSynthetique(
                "Au cœur de la transformation numérique du Burkina Faso, le MTDPCE pilote la digitalisation des "
                        + "services publics, le développement des infrastructures numériques et la régulation du secteur "
                        + "des postes et des communications électroniques.");
        m.setPresentationGlobale(
                "Le Ministère de la Transition Digitale, des Postes et des Communications Électroniques (MTDPCE) "
                        + "est l'institution gouvernementale chargée de conduire la politique nationale de transformation "
                        + "digitale du Burkina Faso. Son action couvre plusieurs domaines stratégiques : la dématérialisation "
                        + "des procédures administratives (e-Gouvernement), le déploiement des infrastructures numériques "
                        + "(backbone national en fibre optique, points d'accès, data centers), la régulation et le développement "
                        + "du secteur postal et des communications électroniques, ainsi que la protection des données "
                        + "personnelles et la cybersécurité.\n\n"
                        + "Le Ministère s'appuie sur un cabinet, un secrétariat général, des directions générales et "
                        + "techniques, ainsi que sur plusieurs structures rattachées et établissements sous tutelle "
                        + "(ARCEP, ANSSI, ANPTIC, SONAPOST, APDP) pour mettre en œuvre la Stratégie Nationale de "
                        + "Transformation Digitale et faire du numérique un véritable levier de développement économique "
                        + "et social pour l'ensemble des citoyens burkinabè.");
        m.setCreatedAt(LocalDateTime.now());
        m.setUpdatedAt(LocalDateTime.now());
        return ministereRepository.save(m);
    }

    private List<Domaine> initDomaines() {
        if (domaineRepository.count() == 0) {
            String[] noms = {
                    "Transformation digitale",
                    "Économie numérique",
                    "Cybersécurité",
                    "Communications électroniques",
                    "Postes",
                    "Infrastructures numériques",
                    "e-Gouvernement",
                    "Protection des données personnelles",
                    "Inclusion numérique",
                    "Innovation et startups",
                    "Souveraineté numérique",
                    "Intelligence artificielle"
            };
            for (String nom : noms) {
                Domaine d = new Domaine();
                d.setNom(nom);
                domaineRepository.save(d);
            }
        }
        return domaineRepository.findAll();
    }

    private void initMinistres(Ministere ministere) {
        if (ministreRepository.count() == 0) {
            // Ministre en exercice
            saveMinistre(ministere, "Zerbo", "Aminata", "Ministre de la Transition Digitale, des Postes et des Communications Électroniques",
                    "Ministre de la Transition Digitale, des Postes et des Communications Électroniques. "
                            + "Forte d'une expertise reconnue dans le domaine des technologies de l'information et de la "
                            + "communication, elle pilote la mise en œuvre de la politique nationale de transformation digitale.",
                    // content = « Mot de la Ministre » (HTML riche affiché sur la page d'accueil)
                    "<p>Chers concitoyens, chers partenaires,</p>"
                            + "<p>La transformation digitale du Burkina Faso est aujourd'hui une <strong>priorité nationale</strong>. "
                            + "Elle constitue un puissant levier de modernisation de l'administration, de développement de "
                            + "l'économie numérique et d'inclusion sociale.</p>"
                            + "<p>Au sein du Ministère, nous œuvrons chaque jour à rapprocher les services publics des citoyens, "
                            + "à sécuriser notre cyberespace et à faire du numérique une opportunité pour <em>toutes et tous</em>.</p>"
                            + "<p>Ensemble, construisons un Burkina Faso numérique, souverain et prospère.</p>",
                    true, LocalDate.of(2022, 10, 1), null);

            // Anciens ministres (historique) — pour atteindre au moins 10 enregistrements
            Object[][] anciens = {
                    {"Ouattara", "Issouf", "Économiste", LocalDate.of(2020, 1, 10), LocalDate.of(2022, 9, 30)},
                    {"Traoré", "Fatoumata", "Spécialiste TIC", LocalDate.of(2018, 2, 15), LocalDate.of(2020, 1, 9)},
                    {"Sankara", "Boukary", "Ingénieur télécom", LocalDate.of(2016, 3, 1), LocalDate.of(2018, 2, 14)},
                    {"Diallo", "Mariam", "Juriste", LocalDate.of(2014, 6, 20), LocalDate.of(2016, 2, 28)},
                    {"Kaboré", "Salif", "Informaticien", LocalDate.of(2012, 4, 5), LocalDate.of(2014, 6, 19)},
                    {"Compaoré", "Rasmané", "Administrateur civil", LocalDate.of(2010, 1, 12), LocalDate.of(2012, 4, 4)},
                    {"Ouédraogo", "Aïcha", "Économiste", LocalDate.of(2008, 5, 30), LocalDate.of(2009, 12, 31)},
                    {"Nikiema", "Daouda", "Ingénieur réseau", LocalDate.of(2006, 2, 1), LocalDate.of(2008, 5, 29)},
                    {"Zongo", "Hamidou", "Spécialiste postal", LocalDate.of(2004, 1, 15), LocalDate.of(2006, 1, 31)}
            };
            for (Object[] a : anciens) {
                saveMinistre(ministere, (String) a[0], (String) a[1], (String) a[2],
                        "Ancien Ministre en charge du numérique et des communications électroniques du Burkina Faso.",
                        "A contribué au développement du secteur numérique et postal durant son mandat.",
                        false, (LocalDate) a[3], (LocalDate) a[4]);
            }
        }
    }

    private void saveMinistre(Ministere ministere, String nom, String prenom, String profession,
                              String biographie, String content, boolean actif, LocalDate debut, LocalDate fin) {
        Ministre ministre = new Ministre();
        ministre.setNom(nom);
        ministre.setPrenom(prenom);
        ministre.setProfession(profession);
        ministre.setBiographie(biographie);
        ministre.setContent(content);
        ministre.setIsActif(actif);
        ministre.setDateDebut(debut);
        ministre.setDateFin(fin);
        ministre.setMinistere(ministere);
        ministre.setCreatedAt(LocalDateTime.now());
        ministre.setUpdatedAt(LocalDateTime.now());
        ministreRepository.save(ministre);
    }

    private void initMissions(Ministere ministere) {
        if (missionRepository.count() == 0) {
            String[][] missions = {
                    {"Transformation digitale",
                            "Concevoir et mettre en œuvre la Stratégie Nationale de Transformation Digitale et coordonner la digitalisation des services de l'administration publique."},
                    {"Économie numérique",
                            "Promouvoir le développement de l'économie numérique, l'innovation, les startups et l'entrepreneuriat dans le secteur des technologies du numérique."},
                    {"Communications électroniques",
                            "Élaborer la réglementation et veiller au développement harmonieux du secteur des communications électroniques sur l'ensemble du territoire."},
                    {"Postes",
                            "Définir et mettre en œuvre la politique nationale en matière de services postaux et financiers postaux, et en assurer le suivi."},
                    {"Infrastructures numériques",
                            "Développer et sécuriser les infrastructures numériques nationales : backbone en fibre optique, data centers et points d'accès communautaires."},
                    {"Cybersécurité",
                            "Renforcer la sécurité des systèmes d'information de l'État et la protection des données personnelles des citoyens."},
                    {"Inclusion numérique",
                            "Réduire la fracture numérique en favorisant l'accès des populations rurales et vulnérables aux services et outils numériques."},
                    {"e-Gouvernement",
                            "Dématérialiser les procédures administratives et offrir des téléservices accessibles à tous les usagers."},
                    {"Innovation",
                            "Soutenir la recherche, l'innovation technologique et l'accompagnement des jeunes pousses du numérique."},
                    {"Coopération internationale",
                            "Développer les partenariats régionaux et internationaux pour le financement et le transfert de compétences dans le numérique."}
            };
            for (String[] mi : missions) {
                Mission mission = new Mission();
                mission.setCategorie(mi[0]);
                mission.setDescription(mi[1]);
                mission.setMinistere(ministere);
                missionRepository.save(mission);
            }
        }
    }

    private void initStructures(Ministere ministere) {
        if (structureRepository.count() == 0) {
            saveStructure(ministere, "Cabinet du Ministre", "Cabinet du Ministre", "CAB",
                    StructureType.CABINET, "Niveau 1");
            saveStructure(ministere, "Secrétariat Général", "Secrétariat Général", "SG",
                    StructureType.DIRECTION_GENERALE, "Niveau 1");
            saveStructure(ministere, "Direction Générale de la Transformation Digitale",
                    "Direction Générale de la Transformation Digitale", "DGTD",
                    StructureType.DIRECTION_GENERALE, "Niveau 2");
            saveStructure(ministere, "Direction Générale du Développement de l'Économie Numérique",
                    "Direction Générale du Développement de l'Économie Numérique", "DGDEN",
                    StructureType.DIRECTION_GENERALE, "Niveau 2");
            saveStructure(ministere, "Direction Générale des Postes",
                    "Direction Générale des Postes", "DGP",
                    StructureType.DIRECTION_GENERALE, "Niveau 2");
            saveStructure(ministere, "Direction Générale des Communications Électroniques",
                    "Direction Générale des Communications Électroniques", "DGCE",
                    StructureType.DIRECTION_GENERALE, "Niveau 2");
            saveStructure(ministere, "Direction des Systèmes d'Information",
                    "Direction des Systèmes d'Information", "DSI",
                    StructureType.DIRECTION, "Niveau 3");
            saveStructure(ministere, "Direction de la Communication et des Relations Presse",
                    "Direction de la Communication et des Relations Presse", "DCRP",
                    StructureType.DIRECTION, "Niveau 3");
            saveStructure(ministere, "Direction des Affaires Juridiques",
                    "Direction des Affaires Juridiques", "DAJ",
                    StructureType.DIRECTION, "Niveau 3");
            saveStructure(ministere, "Direction des Ressources Humaines",
                    "Direction des Ressources Humaines", "DRH",
                    StructureType.DIRECTION, "Niveau 3");
            saveStructure(ministere, "Direction des Marchés Publics",
                    "Direction des Marchés Publics", "DMP",
                    StructureType.DIRECTION, "Niveau 3");
        }
    }

    private void saveStructure(Ministere ministere, String title, String name, String acronym,
                               StructureType type, String niveau) {
        structureRepository.save(Structure.builder()
                .title(title)
                .name(name)
                .acronym(acronym)
                .niveau(niveau)
                .structureType(type)
                .email("contact@mtdpce.gov.bf")
                .phone("+226 25 30 00 00")
                .ministere(ministere)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
    }

    private void initStructuresRattachees(Ministere ministere, List<Domaine> domaines) {
        if (structureRattacheRepository.count() == 0) {
            saveStructureRattachee(ministere, domaines,
                    "Autorité de Régulation des Communications Électroniques et des Postes", "ARCEP",
                    "Autorité de régulation",
                    "Régule le secteur des communications électroniques et des postes au Burkina Faso et veille à une concurrence saine entre les opérateurs.",
                    "Ouagadougou, Burkina Faso", "+226 25 37 53 60", "contact@arcep.bf", "https://www.arcep.bf",
                    new String[]{"Communications électroniques", "Postes"});

            saveStructureRattachee(ministere, domaines,
                    "Agence Nationale de Sécurité des Systèmes d'Information", "ANSSI",
                    "Établissement public",
                    "Assure la protection des systèmes d'information de l'État et la coordination de la réponse aux incidents de cybersécurité.",
                    "Ouagadougou, Burkina Faso", "+226 25 49 00 24", "contact@anssi.bf", "https://www.anssi.bf",
                    new String[]{"Cybersécurité"});

            saveStructureRattachee(ministere, domaines,
                    "Agence Nationale de Promotion des Technologies de l'Information et de la Communication", "ANPTIC",
                    "Établissement public",
                    "Promeut et accompagne le développement des TIC et la mise en œuvre des projets d'administration électronique.",
                    "Ouagadougou, Burkina Faso", "+226 25 49 01 50", "info@anptic.gov.bf", "https://www.anptic.gov.bf",
                    new String[]{"Transformation digitale", "e-Gouvernement", "Infrastructures numériques"});

            saveStructureRattachee(ministere, domaines,
                    "Société Nationale des Postes", "SONAPOST",
                    "Société d'État",
                    "Opérateur postal national offrant les services courrier, colis, et services financiers postaux sur l'ensemble du territoire.",
                    "Ouagadougou, Burkina Faso", "+226 25 41 90 00", "contact@sonapost.bf", "https://www.sonapost.bf",
                    new String[]{"Postes"});

            saveStructureRattachee(ministere, domaines,
                    "Autorité de Protection des Données à caractère Personnel", "APDP",
                    "Autorité administrative indépendante",
                    "Veille au respect de la vie privée et à la protection des données à caractère personnel des citoyens.",
                    "Ouagadougou, Burkina Faso", "+226 25 37 89 60", "contact@apdp.bf", "https://www.apdp.bf",
                    new String[]{"Protection des données personnelles", "Cybersécurité"});

            saveStructureRattachee(ministere, domaines,
                    "Agence de Promotion de l'Économie Numérique", "APEN",
                    "Établissement public",
                    "Accompagne le développement des startups, de l'innovation et de l'entrepreneuriat numérique au Burkina Faso.",
                    "Ouagadougou, Burkina Faso", "+226 25 36 12 00", "contact@apen.bf", "https://www.apen.bf",
                    new String[]{"Économie numérique", "Innovation et startups"});

            saveStructureRattachee(ministere, domaines,
                    "Centre National de Données", "CND",
                    "Établissement public",
                    "Héberge et sécurise les données et applications de l'administration publique au sein du data center national.",
                    "Ouagadougou, Burkina Faso", "+226 25 33 44 55", "contact@cnd.gov.bf", "https://www.cnd.gov.bf",
                    new String[]{"Infrastructures numériques", "Souveraineté numérique"});

            saveStructureRattachee(ministere, domaines,
                    "École Nationale du Numérique", "ENN",
                    "Établissement de formation",
                    "Forme les cadres et techniciens de l'administration et du secteur privé aux métiers du numérique.",
                    "Bobo-Dioulasso, Burkina Faso", "+226 20 97 10 10", "contact@enn.bf", "https://www.enn.bf",
                    new String[]{"Transformation digitale", "Inclusion numérique"});

            saveStructureRattachee(ministere, domaines,
                    "Fonds de Développement du Numérique", "FDN",
                    "Fonds public",
                    "Finance les projets d'inclusion numérique et le déploiement des infrastructures dans les zones rurales.",
                    "Ouagadougou, Burkina Faso", "+226 25 30 77 88", "contact@fdn.bf", "https://www.fdn.bf",
                    new String[]{"Inclusion numérique", "Infrastructures numériques"});

            saveStructureRattachee(ministere, domaines,
                    "Agence Nationale de l'Intelligence Artificielle", "ANIA",
                    "Établissement public",
                    "Coordonne la stratégie nationale en intelligence artificielle et accompagne les usages responsables de l'IA.",
                    "Ouagadougou, Burkina Faso", "+226 25 31 99 00", "contact@ania.gov.bf", "https://www.ania.gov.bf",
                    new String[]{"Intelligence artificielle", "Innovation et startups"});
        }
    }

    private void saveStructureRattachee(Ministere ministere, List<Domaine> allDomaines, String name,
                                        String acronym, String type, String description, String address,
                                        String phone, String email, String website, String[] domaineNoms) {
        StructureRattache sr = new StructureRattache();
        sr.setName(name);
        sr.setAcronym(acronym);
        sr.setType(type);
        sr.setDescription(description);
        sr.setAddress(address);
        sr.setPhone(phone);
        sr.setEmail(email);
        sr.setWebsite(website);
        sr.setMinistere(ministere);

        List<String> wanted = Arrays.asList(domaineNoms);
        Set<Domaine> domaines = new HashSet<>();
        for (Domaine d : allDomaines) {
            if (wanted.contains(d.getNom())) {
                domaines.add(d);
            }
        }
        sr.setDomaines(domaines);
        structureRattacheRepository.save(sr);
    }

    private void initStatistiques() {
        if (statistiquePublicRepository.count() == 0) {
            saveStatistique("Régions couvertes", "13");
            saveStatistique("Structures sous tutelle", "10");
            saveStatistique("Services dématérialisés", "85+");
            saveStatistique("Taux de pénétration mobile", "118%");
            saveStatistique("Kilomètres de fibre optique déployés", "6 000+");
            saveStatistique("Agents formés au numérique", "2 500+");
            saveStatistique("Communes connectées", "240");
            saveStatistique("Startups accompagnées", "120+");
            saveStatistique("Bureaux de poste", "180");
            saveStatistique("Opérateurs télécom agréés", "8");
            saveStatistique("Data centers nationaux", "2");
            saveStatistique("Taux de couverture 4G", "92%");
        }
    }

    private void saveStatistique(String nom, String valeur) {
        StatistiquePublic s = new StatistiquePublic();
        s.setNom(nom);
        s.setValeur(valeur);
        statistiquePublicRepository.save(s);
    }

    private void initAnalyticsDemo() {
        if (pageVisitRepository.count() > 0) {
            return;
        }

        String[] paths = {
                "/", "/actualites", "/projets", "/services", "/contact",
                "/ministere/ministre", "/ministere/missions", "/ministere/organigramme",
                "/ministere/structures", "/agendas", "/communiques", "/documents",
                "/projets/1", "/projets/2", "/actualites/1", "/actualites/2"
        };
        String[] clickLabels = {
                "Découvrir les actualités", "Nous contacter", "Voir tout l'agenda",
                "Télécharger", "Voir les autres", "Voir tous les communiqués"
        };
        // Heures de pointe simulées (poids par heure : pics 8-12h et 14-18h)
        int[] hourWeights = {1,1,1,1,1,1,2,4,8,10,12,11,9,7,10,12,11,9,7,5,4,3,2,1};
        int totalWeight = 0;
        for (int w : hourWeights) totalWeight += w;

        java.util.Random rnd = new java.util.Random(42);
        List<PageVisit> batch = new java.util.ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int dayOffset = 89; dayOffset >= 0; dayOffset--) {
            LocalDate day = today.minusDays(dayOffset);
            // tendance croissante + variation week-end
            int base = 25 + (89 - dayOffset) / 3;
            boolean weekend = day.getDayOfWeek().getValue() >= 6;
            int visitors = (int) (base * (weekend ? 0.6 : 1.0) * (0.7 + rnd.nextDouble() * 0.6));

            for (int v = 0; v < visitors; v++) {
                String session = "demo-" + dayOffset + "-" + v;
                int pagesPerSession = 1 + rnd.nextInt(4);
                for (int p = 0; p < pagesPerSession; p++) {
                    int hour = pickHour(rnd, hourWeights, totalWeight);
                    LocalDateTime ts = day.atTime(hour, rnd.nextInt(60), rnd.nextInt(60));
                    batch.add(PageVisit.builder()
                            .path(paths[rnd.nextInt(paths.length)])
                            .sessionId(session)
                            .type("PAGE_VIEW")
                            .referrer(rnd.nextBoolean() ? "https://www.google.com" : null)
                            .visitedAt(ts)
                            .build());
                    // quelques clics
                    if (rnd.nextInt(4) == 0) {
                        batch.add(PageVisit.builder()
                                .path(paths[rnd.nextInt(paths.length)])
                                .sessionId(session)
                                .type("CLICK")
                                .label(clickLabels[rnd.nextInt(clickLabels.length)])
                                .visitedAt(ts)
                                .build());
                    }
                }
            }
            if (batch.size() > 2000) {
                pageVisitRepository.saveAll(batch);
                batch.clear();
            }
        }
        if (!batch.isEmpty()) {
            pageVisitRepository.saveAll(batch);
        }
    }

    private int pickHour(java.util.Random rnd, int[] weights, int totalWeight) {
        int r = rnd.nextInt(totalWeight);
        int acc = 0;
        for (int h = 0; h < weights.length; h++) {
            acc += weights[h];
            if (r < acc) return h;
        }
        return 12;
    }

    private void initServices() {
        if (servicesRepository.count() == 0) {
            String[][] services = {
                    {"Portail e-Burkina", "Plateforme unique d'accès aux téléprocédures et services publics en ligne du Burkina Faso.", "https://www.eburkina.gov.bf"},
                    {"ARCEP", "Autorité de Régulation des Communications Électroniques et des Postes.", "https://www.arcep.bf"},
                    {"ANPTIC", "Agence Nationale de Promotion des TIC.", "https://www.anptic.gov.bf"},
                    {"SONAPOST", "Société Nationale des Postes : courrier, colis et services financiers postaux.", "https://www.sonapost.bf"},
                    {"ANSSI", "Agence Nationale de Sécurité des Systèmes d'Information.", "https://www.anssi.bf"},
                    {"APDP", "Autorité de Protection des Données à caractère Personnel.", "https://www.apdp.bf"},
                    {"Registre .bf", "Service d'enregistrement et de gestion des noms de domaine nationaux en .bf.", "https://www.nic.bf"},
                    {"e-Impôts", "Plateforme de déclaration et de paiement des impôts en ligne.", "https://www.eimpots.gov.bf"},
                    {"Téléservices Mairie", "Demande d'actes d'état civil et de documents administratifs en ligne.", "https://www.mairie.gov.bf"},
                    {"Burkina Open Data", "Portail national des données ouvertes de l'administration publique.", "https://data.gov.bf"}
            };
            for (String[] s : services) {
                servicesRepository.save(Services.builder()
                        .name(s[0])
                        .description(s[1])
                        .url(s[2])
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build());
            }
        }
    }

    private void initArticleCategories() {
        if (articleCategoryRepository.count() == 0) {
            String[][] cats = {
                    {"ACTUALITE", "Actualité", "Nouvelles et actualités du ministère."},
                    {"COMMUNIQUE", "Communiqué", "Communiqués officiels et de presse."},
                    {"EVENEMENT", "Événement", "Annonces d'événements."},
                    {"PROJET", "Projet", "Articles liés aux projets et grands chantiers."},
                    {"RAPPORT", "Rapport", "Rapports, études et bilans."},
                    {"DISCOURS", "Discours", "Discours et allocutions officielles."}
            };
            int order = 1;
            for (String[] c : cats) {
                articleCategoryRepository.save(ArticleCategory.builder()
                        .code(c[0])
                        .label(c[1])
                        .description(c[2])
                        .displayOrder(order++)
                        .build());
            }
        }
    }

    private void initArticles() {
        if (articleRepository.count() == 0) {
            User author = userRepository.findByUsername("moderateur").orElseThrow();

            // Résolution des catégories par code
            java.util.Map<String, ArticleCategory> catMap = new java.util.HashMap<>();
            for (ArticleCategory c : articleCategoryRepository.findAll()) {
                catMap.put(c.getCode(), c);
            }

            Object[][] articles = {
                    {"Lancement du Programme National de Transformation Digitale",
                            "Le Ministère lance un ambitieux programme de transformation digitale pour moderniser l'administration publique du Burkina Faso.",
                            "Le Ministère de la Transition Digitale, des Postes et des Communications Électroniques a officiellement lancé le Programme National de Transformation Digitale (PNTD). Ce programme vise à moderniser l'ensemble des services publics en intégrant les technologies numériques dans tous les aspects de l'administration.",
                            "ACTUALITE", true, 150},
                    {"Signature d'un partenariat stratégique avec l'Union Africaine",
                            "Un accord de coopération a été signé pour renforcer les infrastructures numériques du pays.",
                            "Le Burkina Faso a signé un accord de partenariat stratégique avec l'Union Africaine dans le cadre de l'initiative Africa Digital.",
                            "COMMUNIQUE", true, 89},
                    {"Forum National sur la Cybersécurité 2026",
                            "Le ministère organise le premier forum national dédié à la cybersécurité les 15 et 16 février 2026.",
                            "Le Ministère de la Transition Digitale organise le premier Forum National sur la Cybersécurité qui se tiendra à Ouagadougou.",
                            "EVENEMENT", false, 45},
                    {"Inauguration du Data Center National",
                            "Le premier data center souverain du Burkina Faso est désormais opérationnel.",
                            "Le data center national, infrastructure clé de la souveraineté numérique, a été inauguré à Ouagadougou. Il hébergera les données et applications critiques de l'administration.",
                            "ACTUALITE", true, 210},
                    {"Plus de 2 500 agents formés au numérique",
                            "Le programme de renforcement des compétences numériques franchit un cap important.",
                            "Dans le cadre de la modernisation de l'administration, plus de 2 500 agents publics ont été formés aux outils et usages du numérique sur l'ensemble du territoire.",
                            "ACTUALITE", false, 76},
                    {"Communiqué : extension de la couverture 4G aux zones rurales",
                            "Un plan d'extension de la couverture mobile à haut débit est lancé.",
                            "Le Ministère annonce un vaste plan d'extension de la couverture 4G afin de connecter les communes rurales et réduire la fracture numérique.",
                            "COMMUNIQUE", false, 134},
                    {"Discours du Ministre à l'ouverture du Salon du Numérique",
                            "Retrouvez l'intégralité de l'allocution du Ministre lors du SITIC Africa.",
                            "À l'ouverture du Salon International des TIC, le Ministre a réaffirmé l'engagement du Gouvernement en faveur de la transformation digitale et de l'innovation.",
                            "DISCOURS", false, 58},
                    {"Rapport annuel 2025 du secteur numérique",
                            "Bilan des réalisations et perspectives du secteur du numérique au Burkina Faso.",
                            "Le rapport annuel 2025 présente les avancées majeures du secteur : déploiement d'infrastructures, dématérialisation des services et développement de l'économie numérique.",
                            "RAPPORT", false, 92},
                    {"Le projet e-Gouvernement entre dans sa phase 2",
                            "Nouvelle étape pour la dématérialisation des procédures administratives.",
                            "Le projet e-Gouvernement Burkina aborde sa deuxième phase avec l'intégration de nouveaux téléservices destinés aux citoyens et aux entreprises.",
                            "PROJET", true, 167},
                    {"Lancement de la campagne d'inclusion numérique rurale",
                            "Des points d'accès communautaires ouvrent dans plusieurs régions.",
                            "Le Ministère déploie des espaces numériques communautaires dans les zones rurales pour rapprocher les services numériques des populations.",
                            "ACTUALITE", false, 64}
            };
            for (Object[] a : articles) {
                articleRepository.save(Article.builder()
                        .title((String) a[0])
                        .summary((String) a[1])
                        .content((String) a[2])
                        .category(catMap.get((String) a[3]))
                        .status(ArticleStatus.PUBLISHED)
                        .featured((Boolean) a[4])
                        .author(author)
                        .publishedAt(LocalDateTime.now().minusDays(((Integer) a[5]) % 30))
                        .viewCount((Integer) a[5])
                        .build());
            }

            // 20 communiqués pour tester la pagination (du plus récent au plus ancien)
            String[] sujetsComm = {
                    "la régulation du secteur des télécommunications",
                    "le déploiement du backbone national en fibre optique",
                    "le renforcement de la cybersécurité nationale",
                    "la modernisation des services postaux",
                    "la dématérialisation des procédures administratives",
                    "la protection des données à caractère personnel",
                    "l'inclusion numérique des zones rurales",
                    "la stratégie nationale d'intelligence artificielle",
                    "le développement de l'économie numérique",
                    "les partenariats internationaux du secteur numérique"
            };
            for (int i = 0; i < 20; i++) {
                String sujet = sujetsComm[i % sujetsComm.length];
                articleRepository.save(Article.builder()
                        .title("Communiqué N°" + (i + 1) + " relatif à " + sujet)
                        .summary("Communiqué officiel du Ministère concernant " + sujet + ".")
                        .content("Le Ministère de la Transition Digitale, des Postes et des Communications Électroniques "
                                + "porte à la connaissance du public et de ses partenaires les dernières décisions et avancées "
                                + "concernant " + sujet + " au Burkina Faso.")
                        .category(catMap.get("COMMUNIQUE"))
                        .status(ArticleStatus.PUBLISHED)
                        .featured(false)
                        .author(author)
                        // i = 0 -> aujourd'hui (le plus récent), i = 19 -> il y a 19 jours (le plus ancien)
                        .publishedAt(LocalDateTime.now().minusDays(i))
                        .viewCount(200 - i * 5)
                        .build());
            }
        }
    }

    private ProjetCategorie initProjetCategorie(String name, String description) {
        return projetCategorieRepository.findAll().stream()
                .filter(c -> name.equals(c.getName()))
                .findFirst()
                .orElseGet(() -> projetCategorieRepository.save(
                        ProjetCategorie.builder().name(name).description(description).build()));
    }

    private void initProjects() {
        if (projectRepository.count() == 0) {
            User manager = userRepository.findByUsername("admin").orElseThrow();

            ProjetCategorie catEgov = initProjetCategorie("e-Gouvernement",
                    "Projets de dématérialisation et de services publics en ligne.");
            ProjetCategorie catInfra = initProjetCategorie("Infrastructures numériques",
                    "Projets de déploiement d'infrastructures numériques nationales.");
            ProjetCategorie catSec = initProjetCategorie("Cybersécurité",
                    "Projets de renforcement de la sécurité des systèmes d'information.");
            ProjetCategorie catIncl = initProjetCategorie("Inclusion numérique",
                    "Projets de réduction de la fracture numérique.");

            Object[][] projects = {
                    {"e-Gouvernement Burkina", "Plateforme intégrée de services publics en ligne.",
                            "Dématérialiser 80% des procédures administratives d'ici 2027", ProjectStatus.EN_COURS,
                            "2500000000", 45, LocalDate.of(2024, 1, 15), LocalDate.of(2027, 12, 31),
                            "Banque Mondiale", "Direction de la Modernisation", catEgov},
                    {"Backbone National Fibre Optique", "Déploiement d'un réseau national de fibre optique.",
                            "Connecter les 13 régions du Burkina Faso par fibre optique", ProjectStatus.EN_COURS,
                            "15000000000", 65, LocalDate.of(2023, 6, 1), LocalDate.of(2026, 6, 30),
                            "Union Européenne", "Direction des Infrastructures", catInfra},
                    {"Data Center National", "Construction d'un centre de données souverain.",
                            "Héberger localement 100% des données critiques de l'État", ProjectStatus.TERMINE,
                            "8000000000", 100, LocalDate.of(2022, 3, 1), LocalDate.of(2025, 2, 28),
                            "Banque Africaine de Développement", "Direction des Systèmes d'Information", catInfra},
                    {"SOC National de Cybersécurité", "Centre opérationnel de sécurité national.",
                            "Surveiller et répondre 24/7 aux incidents de cybersécurité", ProjectStatus.EN_COURS,
                            "3500000000", 30, LocalDate.of(2024, 9, 1), LocalDate.of(2026, 12, 31),
                            "Coopération française", "ANSSI", catSec},
                    {"Espaces Numériques Communautaires", "Déploiement de points d'accès en zone rurale.",
                            "Ouvrir 240 espaces numériques communautaires", ProjectStatus.EN_COURS,
                            "1800000000", 55, LocalDate.of(2023, 10, 1), LocalDate.of(2026, 3, 31),
                            "PNUD", "Fonds de Développement du Numérique", catIncl},
                    {"Identité Numérique Nationale", "Mise en place d'un système d'identité numérique.",
                            "Doter chaque citoyen d'une identité numérique sécurisée", ProjectStatus.PLANIFIE,
                            "12000000000", 5, LocalDate.of(2025, 7, 1), LocalDate.of(2029, 12, 31),
                            "Banque Mondiale", "Direction de la Transformation Digitale", catEgov},
                    {"Modernisation Postale", "Digitalisation des services de la SONAPOST.",
                            "Moderniser 180 bureaux de poste et leurs services financiers", ProjectStatus.EN_COURS,
                            "4200000000", 40, LocalDate.of(2024, 2, 1), LocalDate.of(2027, 1, 31),
                            "Union Postale Universelle", "Direction Générale des Postes", catEgov},
                    {"Formation 10 000 Talents du Numérique", "Programme national de formation aux métiers du numérique.",
                            "Former 10 000 jeunes aux métiers du numérique d'ici 2028", ProjectStatus.EN_COURS,
                            "2200000000", 25, LocalDate.of(2024, 5, 1), LocalDate.of(2028, 4, 30),
                            "GIZ", "École Nationale du Numérique", catIncl},
                    {"Plateforme Open Data", "Portail national des données ouvertes.",
                            "Publier 500 jeux de données ouvertes de l'administration", ProjectStatus.SUSPENDU,
                            "600000000", 35, LocalDate.of(2023, 1, 1), LocalDate.of(2025, 6, 30),
                            "Open Data Charter", "ANPTIC", catEgov},
                    {"Smart City Ouaga 2030", "Projet pilote de ville intelligente.",
                            "Déployer des solutions urbaines intelligentes à Ouagadougou", ProjectStatus.PLANIFIE,
                            "9500000000", 10, LocalDate.of(2025, 9, 1), LocalDate.of(2030, 12, 31),
                            "Smart Africa", "Direction de la Transformation Digitale", catInfra},
                    {"Cloud Souverain National", "Mise en place d'un cloud public souverain.",
                            "Offrir des services cloud sécurisés aux administrations", ProjectStatus.EN_COURS,
                            "7000000000", 20, LocalDate.of(2024, 11, 1), LocalDate.of(2027, 10, 31),
                            "Banque Mondiale", "Centre National de Données", catInfra},
                    {"Télémédecine Numérique", "Plateforme de consultation médicale à distance.",
                            "Déployer la télémédecine dans 50 centres de santé", ProjectStatus.EN_COURS,
                            "2800000000", 35, LocalDate.of(2024, 6, 15), LocalDate.of(2026, 12, 31),
                            "OMS", "Direction de la Transformation Digitale", catEgov},
                    {"e-Éducation", "Numérisation des ressources pédagogiques nationales.",
                            "Équiper 200 établissements en outils numériques", ProjectStatus.EN_COURS,
                            "3200000000", 50, LocalDate.of(2023, 9, 1), LocalDate.of(2026, 8, 31),
                            "UNICEF", "Direction de la Transformation Digitale", catIncl},
                    {"Registre National Biométrique", "Système biométrique d'identification.",
                            "Enregistrer 100% de la population adulte", ProjectStatus.PLANIFIE,
                            "11000000000", 8, LocalDate.of(2025, 10, 1), LocalDate.of(2029, 6, 30),
                            "Union Européenne", "Direction des Systèmes d'Information", catEgov},
                    {"Paiement Mobile Gouvernemental", "Plateforme de paiement des taxes par mobile money.",
                            "Permettre le paiement mobile des services publics", ProjectStatus.EN_COURS,
                            "1500000000", 60, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 6, 30),
                            "GIM-UEMOA", "Direction de la Modernisation", catEgov},
                    {"Réseau Wifi Public", "Déploiement de points d'accès Wifi gratuits.",
                            "Installer 500 hotspots dans les lieux publics", ProjectStatus.EN_COURS,
                            "900000000", 45, LocalDate.of(2024, 4, 1), LocalDate.of(2026, 3, 31),
                            "Banque Africaine de Développement", "Direction des Infrastructures", catInfra},
                    {"Cyber-Académie", "Centre de formation à la cybersécurité.",
                            "Former 1 000 experts en cybersécurité", ProjectStatus.PLANIFIE,
                            "2000000000", 5, LocalDate.of(2025, 11, 1), LocalDate.of(2028, 12, 31),
                            "Coopération française", "ANSSI", catSec},
                    {"Archivage Électronique National", "Dématérialisation des archives publiques.",
                            "Numériser les archives de l'administration centrale", ProjectStatus.SUSPENDU,
                            "1700000000", 22, LocalDate.of(2023, 3, 1), LocalDate.of(2026, 12, 31),
                            "UNESCO", "Secrétariat Général", catEgov},
                    {"Plateforme e-Recrutement Public", "Système national de recrutement en ligne.",
                            "Digitaliser les concours de la fonction publique", ProjectStatus.TERMINE,
                            "800000000", 100, LocalDate.of(2022, 5, 1), LocalDate.of(2024, 12, 31),
                            "PNUD", "Direction des Ressources Humaines", catEgov}
            };
            for (Object[] p : projects) {
                projectRepository.save(Project.builder()
                        .name((String) p[0])
                        .description((String) p[1])
                        .objectives((String) p[2])
                        .status((ProjectStatus) p[3])
                        .budget(new BigDecimal((String) p[4]))
                        .progressPercentage((Integer) p[5])
                        .startDate((LocalDate) p[6])
                        .endDate((LocalDate) p[7])
                        .partner((String) p[8])
                        .responsibleDepartment((String) p[9])
                        .projetCategorie((ProjetCategorie) p[10])
                        .manager(manager)
                        .build());
            }
        }
    }

    private Type initType(String name, String description) {
        return typeRepository.findAll().stream()
                .filter(t -> name.equals(t.getName()))
                .findFirst()
                .orElseGet(() -> typeRepository.save(
                        Type.builder().name(name).description(description).build()));
    }

    private void initDocuments() {
        if (documentRepository.count() == 0) {
            User uploader = userRepository.findByUsername("admin").orElseThrow();

            Type typeRapport = initType("Rapport", "Rapports et études.");
            Type typeGuide = initType("Guide", "Guides et procédures.");
            Type typeLoi = initType("Texte juridique", "Lois, décrets et arrêtés.");
            Type typeFormulaire = initType("Formulaire", "Formulaires administratifs.");

            Object[][] docs = {
                    {"Stratégie Nationale de Transformation Digitale 2025-2030",
                            "Document de référence présentant la vision et les axes stratégiques.",
                            "strategie_nationale_td_2025_2030.pdf", 2500000L, DocumentCategory.RAPPORT, typeRapport, 250},
                    {"Guide de demande d'agrément technique",
                            "Procédure et formulaires pour obtenir un agrément technique.",
                            "guide_agrement_technique.pdf", 1200000L, DocumentCategory.GUIDE, typeGuide, 180},
                    {"Loi sur la protection des données personnelles",
                            "Texte de loi relatif à la protection des données à caractère personnel.",
                            "loi_protection_donnees.pdf", 900000L, DocumentCategory.LOI, typeLoi, 320},
                    {"Décret portant régulation des communications électroniques",
                            "Décret fixant les modalités de régulation du secteur.",
                            "decret_regulation_comm.pdf", 750000L, DocumentCategory.DECRET, typeLoi, 140},
                    {"Arrêté relatif aux noms de domaine .bf",
                            "Arrêté encadrant l'enregistrement des noms de domaine nationaux.",
                            "arrete_noms_domaine.pdf", 400000L, DocumentCategory.ARRETE, typeLoi, 95},
                    {"Formulaire de déclaration d'opérateur télécom",
                            "Formulaire à remplir pour toute déclaration d'opérateur.",
                            "formulaire_operateur.pdf", 300000L, DocumentCategory.FORMULAIRE, typeFormulaire, 210},
                    {"Rapport annuel d'activités 2025",
                            "Bilan des activités et réalisations du Ministère pour l'année 2025.",
                            "rapport_annuel_2025.pdf", 3100000L, DocumentCategory.RAPPORT, typeRapport, 175},
                    {"Guide de cybersécurité pour les PME",
                            "Bonnes pratiques de sécurité informatique pour les petites entreprises.",
                            "guide_cybersecurite_pme.pdf", 1500000L, DocumentCategory.GUIDE, typeGuide, 260},
                    {"Circulaire sur l'usage des outils numériques dans l'administration",
                            "Circulaire encadrant l'utilisation des outils collaboratifs.",
                            "circulaire_outils_numeriques.pdf", 350000L, DocumentCategory.CIRCULAIRE, typeLoi, 88},
                    {"Charte nationale d'inclusion numérique",
                            "Document cadre pour la réduction de la fracture numérique.",
                            "charte_inclusion_numerique.pdf", 680000L, DocumentCategory.AUTRE, typeRapport, 130},
                    // --- Stratégie & Politiques (documents de politique publique) ---
                    {"Politique Sectorielle de l'Économie Numérique 2021-2025",
                            "Cadre d'orientation des politiques publiques du secteur numérique.",
                            "politique_sectorielle_num_2021_2025.pdf", 2100000L, DocumentCategory.RAPPORT, typeRapport, 190},
                    {"Plan National de Développement de la Cybersécurité",
                            "Feuille de route nationale pour le renforcement de la cybersécurité.",
                            "plan_national_cybersecurite.pdf", 1800000L, DocumentCategory.RAPPORT, typeRapport, 145},
                    // --- Règlementation (textes réglementaires) ---
                    {"Loi d'orientation de la société de l'information",
                            "Loi fixant les principes de développement de la société de l'information.",
                            "loi_orientation_si.pdf", 950000L, DocumentCategory.LOI, typeLoi, 175},
                    {"Décret portant attributions et organisation du secteur numérique",
                            "Décret précisant l'organisation institutionnelle du secteur.",
                            "decret_organisation_num.pdf", 720000L, DocumentCategory.DECRET, typeLoi, 110}
            };
            for (Object[] d : docs) {
                DocumentCategory category = (DocumentCategory) d[4];
                documentRepository.save(Document.builder()
                        .title((String) d[0])
                        .description((String) d[1])
                        .fileName((String) d[2])
                        .filePath("/documents/" + d[2])
                        .fileType("application/pdf")
                        .fileSize((Long) d[3])
                        .category(category)
                        .type((Type) d[5])
                        .typeDocument(typeForCategory(category))
                        .isPublic(true)
                        .downloadCount((Integer) d[6])
                        .uploadedBy(uploader)
                        .build());
            }
        }

        // Corrige les documents existants sans type (installations antérieures) afin
        // qu'ils apparaissent bien sur les pages Règlementation / Stratégie & Politiques.
        backfillDocumentTypes();
    }

    /**
     * Répartit un document entre « Règlementation » (DOCUMENT SIMPLE) et
     * « Stratégie & Politiques » (DOCUMENT POLITIQUE) selon sa catégorie.
     */
    private String typeForCategory(DocumentCategory category) {
        return switch (category) {
            case LOI, DECRET, ARRETE, CIRCULAIRE, FORMULAIRE -> "DOCUMENT SIMPLE";
            default -> "DOCUMENT POLITIQUE";
        };
    }

    private void backfillDocumentTypes() {
        java.util.List<Document> toFix = documentRepository.findAll().stream()
                .filter(d -> d.getTypeDocument() == null || d.getTypeDocument().isBlank())
                .toList();
        for (Document d : toFix) {
            d.setTypeDocument(typeForCategory(d.getCategory()));
        }
        if (!toFix.isEmpty()) {
            documentRepository.saveAll(toFix);
        }
    }

    private void initEvents() {
        if (eventRepository.count() == 0) {
            Object[][] events = {
                    {"Forum National sur la Cybersécurité",
                            "Premier forum national dédié à la cybersécurité au Burkina Faso.",
                            "Le Forum National sur la Cybersécurité réunit experts, institutions et entreprises autour des enjeux de protection des systèmes d'information et des données.",
                            "Ouagadougou, Centre International de Conférences", "Conférence",
                            LocalDateTime.of(2026, 7, 15, 8, 0), LocalDateTime.of(2026, 7, 17, 18, 0), 500, true},
                    {"Hackathon e-Gov 2026",
                            "Compétition de développement de solutions e-gouvernement.",
                            "Le Hackathon e-Gov 2026 invite développeurs et porteurs de projets à concevoir des solutions numériques innovantes au service de l'administration.",
                            "Ouagadougou, Université Joseph Ki-Zerbo", "Hackathon",
                            LocalDateTime.of(2026, 8, 20, 9, 0), LocalDateTime.of(2026, 8, 22, 18, 0), 200, true},
                    {"Journée Mondiale des Télécommunications",
                            "Célébration de la journée mondiale des télécommunications.",
                            "À l'occasion de la Journée Mondiale des Télécommunications, le Ministère met en lumière les avancées du secteur des communications électroniques.",
                            "Ouagadougou", "Célébration",
                            LocalDateTime.of(2026, 5, 17, 8, 0), LocalDateTime.of(2026, 5, 17, 18, 0), 0, false},
                    {"Salon International des TIC (SITIC Africa)",
                            "Grand rendez-vous des acteurs du numérique en Afrique.",
                            "Le SITIC Africa rassemble entreprises, startups et institutions autour des innovations technologiques du continent.",
                            "Ouagadougou, SIAO", "Salon",
                            LocalDateTime.of(2026, 9, 10, 8, 0), LocalDateTime.of(2026, 9, 12, 19, 0), 1000, true},
                    {"Atelier de formation à la protection des données",
                            "Formation des agents publics au respect du RGPD national.",
                            "Cet atelier forme les agents de l'administration aux obligations en matière de protection des données personnelles.",
                            "Ouagadougou, APDP", "Atelier",
                            LocalDateTime.of(2026, 6, 25, 9, 0), LocalDateTime.of(2026, 6, 26, 17, 0), 80, true},
                    {"Semaine du Numérique Rural",
                            "Sensibilisation des populations rurales aux usages du numérique.",
                            "Une semaine d'animations et de démonstrations pour rapprocher les services numériques des populations rurales.",
                            "Région des Hauts-Bassins", "Sensibilisation",
                            LocalDateTime.of(2026, 10, 5, 8, 0), LocalDateTime.of(2026, 10, 11, 18, 0), 0, false},
                    {"Conférence sur l'Intelligence Artificielle",
                            "Les enjeux et opportunités de l'IA pour le Burkina Faso.",
                            "Experts et décideurs échangent sur les usages responsables de l'intelligence artificielle au service du développement.",
                            "Ouagadougou, Hôtel Laico", "Conférence",
                            LocalDateTime.of(2026, 11, 18, 8, 30), LocalDateTime.of(2026, 11, 19, 17, 30), 300, true},
                    {"Journée Portes Ouvertes du MTDPCE",
                            "Découverte des missions et services du Ministère.",
                            "Le Ministère ouvre ses portes au public pour présenter ses missions, structures et services numériques.",
                            "Ouagadougou, siège du MTDPCE", "Portes ouvertes",
                            LocalDateTime.of(2026, 4, 12, 9, 0), LocalDateTime.of(2026, 4, 12, 16, 0), 0, false},
                    {"Bootcamp Startups Numériques",
                            "Accompagnement intensif des jeunes pousses du numérique.",
                            "Un programme intensif d'accompagnement pour aider les startups à passer à l'échelle.",
                            "Bobo-Dioulasso, École Nationale du Numérique", "Bootcamp",
                            LocalDateTime.of(2026, 7, 1, 8, 0), LocalDateTime.of(2026, 7, 5, 18, 0), 60, true},
                    {"Cérémonie de remise des agréments techniques",
                            "Remise officielle des agréments aux entreprises certifiées.",
                            "Le Ministère remet officiellement les agréments techniques aux entreprises ayant satisfait aux exigences.",
                            "Ouagadougou, siège du MTDPCE", "Cérémonie",
                            LocalDateTime.of(2026, 3, 28, 10, 0), LocalDateTime.of(2026, 3, 28, 13, 0), 150, true}
            };
            for (Object[] ev : events) {
                Event e = new Event();
                e.setTitle((String) ev[0]);
                e.setDescription((String) ev[1]);
                e.setContent((String) ev[2]);
                e.setStatut("Programmé");
                e.setLocation((String) ev[3]);
                e.setCategory((String) ev[4]);
                e.setStartDate((LocalDateTime) ev[5]);
                e.setEndDate((LocalDateTime) ev[6]);
                e.setIsPublic(true);
                int max = (Integer) ev[7];
                if (max > 0) {
                    e.setMaxParticipants(max);
                }
                e.setRegistrationRequired((Boolean) ev[8]);
                e.setStatus(EventStatus.UPCOMING);
                eventRepository.save(e);
            }
        }
    }

    private void initFAQs() {
        if (faqRepository.count() == 0) {
            Object[][] faqs = {
                    {"Comment obtenir un agrément technique pour mon entreprise informatique ?",
                            "Pour obtenir un agrément technique, vous devez soumettre un dossier comprenant : une demande adressée au Ministre, les statuts de l'entreprise, le RCCM, les diplômes des techniciens, et une attestation fiscale. Le délai de traitement est de 30 jours ouvrables.",
                            "Agrément", "agrément,entreprise,informatique"},
                    {"Quels sont les services disponibles en ligne ?",
                            "Le ministère propose plusieurs services en ligne : demande d'agrément technique, déclaration d'opérateur télécom, consultation des textes réglementaires, et inscription à la newsletter.",
                            "Services", "services,en ligne,digital"},
                    {"Comment contacter le ministère ?",
                            "Vous pouvez nous contacter par téléphone au +226 25 30 XX XX, par email à contact@mtdpce.gov.bf, ou en vous rendant à nos bureaux situés à Ouagadougou.",
                            "Contact", "contact,téléphone,email"},
                    {"Quelles sont les conditions pour devenir opérateur télécom ?",
                            "Pour devenir opérateur télécom, vous devez obtenir une licence auprès de l'ARCEP. Les conditions incluent : un capital social minimum, une expertise technique prouvée, et un plan d'affaires viable.",
                            "Télécommunications", "télécom,licence,opérateur"},
                    {"Comment signaler un incident de cybersécurité ?",
                            "Pour signaler un incident de cybersécurité, contactez l'ANSSI par email à incident@anssi.gov.bf ou par téléphone au numéro d'urgence.",
                            "Cybersécurité", "cybersécurité,incident,ANSSI"},
                    {"Comment enregistrer un nom de domaine en .bf ?",
                            "L'enregistrement d'un nom de domaine en .bf se fait en ligne auprès du registre national. Le coût est de 15 000 FCFA et le délai de traitement de 3 jours ouvrables.",
                            "Internet", "domaine,.bf,internet"},
                    {"Comment déposer une candidature à une offre d'emploi ?",
                            "Les candidatures se déposent par email à l'adresse indiquée dans l'offre, accompagnées d'un CV, d'une lettre de motivation et des copies des diplômes, avant la date limite.",
                            "Emploi", "emploi,candidature,recrutement"},
                    {"Mes données personnelles sont-elles protégées ?",
                            "Oui. Le traitement de vos données est encadré par la loi sur la protection des données personnelles et supervisé par l'APDP. Vous disposez d'un droit d'accès, de rectification et de suppression.",
                            "Protection des données", "données,vie privée,APDP"},
                    {"Comment m'inscrire à la newsletter du ministère ?",
                            "Rendez-vous sur la page d'accueil du site, renseignez votre adresse email dans le champ dédié à la newsletter et confirmez votre inscription via le lien reçu par email.",
                            "Newsletter", "newsletter,inscription,abonnement"},
                    {"Où consulter les textes réglementaires du secteur ?",
                            "Tous les textes réglementaires (lois, décrets, arrêtés, circulaires) sont disponibles dans la rubrique Ressources > Règlementation du site officiel du ministère.",
                            "Réglementation", "textes,réglementation,documents"}
            };
            int order = 1;
            for (Object[] f : faqs) {
                FAQ faq = new FAQ();
                faq.setQuestion((String) f[0]);
                faq.setAnswer((String) f[1]);
                faq.setCategory((String) f[2]);
                faq.setTags((String) f[3]);
                faq.setDisplayOrder(order++);
                faq.setIsPublished(true);
                faqRepository.save(faq);
            }
        }
    }

    private void initEServices() {
        if (eServiceRepository.count() == 0) {
            Object[][] eservices = {
                    {"Demande d'Agrément Technique", "Service de demande d'agrément technique pour les entreprises du secteur informatique.",
                            "Agrément", "Entreprises informatiques", "50000", "30 jours ouvrables",
                            "Demande adressée au Ministre|||Statuts de l'entreprise|||RCCM|||Diplômes des techniciens|||Attestation fiscale", true, "certificate"},
                    {"Déclaration d'Opérateur Télécom", "Déclaration obligatoire pour les opérateurs de télécommunications.",
                            "Télécommunications", "Opérateurs télécom", "100000", "45 jours ouvrables", null, false, "phone"},
                    {"Certification Cybersécurité", "Certification des systèmes d'information pour la conformité aux normes de sécurité.",
                            "Cybersécurité", "Entreprises et administrations", "200000", "60 jours ouvrables", null, true, "shield"},
                    {"Enregistrement de Nom de Domaine .bf", "Service d'enregistrement et de gestion des noms de domaine en .bf.",
                            "Internet", "Tous", "15000", "3 jours ouvrables", null, true, "globe"},
                    {"Autorisation d'Importation d'Équipements Télécom", "Autorisation pour l'importation d'équipements de télécommunications.",
                            "Télécommunications", "Importateurs", "25000", "15 jours ouvrables", null, false, "truck"},
                    {"Déclaration de Traitement de Données Personnelles", "Déclaration préalable auprès de l'APDP pour tout traitement de données.",
                            "Protection des données", "Entreprises et administrations", "0", "20 jours ouvrables", null, true, "lock"},
                    {"Suivi de Courrier et Colis", "Service de suivi en ligne des envois postaux de la SONAPOST.",
                            "Postes", "Tous", "0", "Instantané", null, true, "package"},
                    {"Demande de Licence d'Opérateur Postal", "Licence requise pour exercer une activité d'opérateur postal.",
                            "Postes", "Opérateurs postaux", "150000", "60 jours ouvrables", null, false, "mail"},
                    {"Inscription aux Formations du Numérique", "Inscription en ligne aux programmes de formation de l'ENN.",
                            "Formation", "Jeunes et professionnels", "0", "10 jours ouvrables", null, true, "academic"},
                    {"Demande de Subvention Startup", "Soumission de demande de financement pour les startups numériques.",
                            "Économie numérique", "Startups", "0", "45 jours ouvrables", null, true, "rocket"}
            };
            int order = 1;
            for (Object[] s : eservices) {
                EService es = new EService();
                es.setName((String) s[0]);
                es.setDescription((String) s[1]);
                es.setCategory((String) s[2]);
                es.setTargetAudience((String) s[3]);
                es.setCost(new BigDecimal((String) s[4]));
                es.setProcessingTime((String) s[5]);
                if (s[6] != null) {
                    es.setRequiredDocuments((String) s[6]);
                }
                es.setIsOnline((Boolean) s[7]);
                es.setIsActive(true);
                es.setDisplayOrder(order++);
                es.setIconName((String) s[8]);
                eServiceRepository.save(es);
            }
        }
    }

    private void initFlashInfos() {
        if (flashInfoRepository.count() == 0) {
            Object[][] flashes = {
                    {"Nouveau portail e-services disponible", "Le nouveau portail de services en ligne est maintenant opérationnel. Découvrez nos services digitaux.", "/services", "Accéder aux services", FlashInfoPriority.HIGH},
                    {"Inscription au Forum Cybersécurité ouverte", "Les inscriptions pour le Forum National sur la Cybersécurité 2026 sont ouvertes. Places limitées !", "/evenements", "S'inscrire maintenant", FlashInfoPriority.NORMAL},
                    {"Maintenance programmée", "Une maintenance est prévue le 30 juin 2026 de 22h à 6h. Certains services seront indisponibles.", null, null, FlashInfoPriority.LOW},
                    {"Data Center National inauguré", "Le premier data center souverain du Burkina Faso est désormais opérationnel.", "/actualites", "Lire l'article", FlashInfoPriority.MEDIUM},
                    {"Appel à candidatures : 10 000 Talents", "Le programme de formation aux métiers du numérique recrute. Postulez dès maintenant.", "/emplois", "Voir les offres", FlashInfoPriority.HIGH},
                    {"Alerte sécurité : campagne de phishing", "Soyez vigilants face à une campagne d'hameçonnage usurpant l'identité du ministère.", "/actualites", "En savoir plus", FlashInfoPriority.URGENT},
                    {"Extension de la couverture 4G", "La couverture 4G s'étend à de nouvelles communes rurales ce trimestre.", "/projets", "Découvrir le projet", FlashInfoPriority.NORMAL},
                    {"SITIC Africa 2026", "Le Salon International des TIC se tiendra du 10 au 12 septembre 2026 à Ouagadougou.", "/evenements", "Programme", FlashInfoPriority.MEDIUM},
                    {"Open Data : nouveaux jeux de données", "De nouveaux jeux de données publiques sont disponibles sur le portail Open Data.", "/documents", "Explorer", FlashInfoPriority.LOW},
                    {"Newsletter : abonnez-vous", "Recevez l'actualité du numérique burkinabè directement dans votre boîte mail.", "/", "S'abonner", FlashInfoPriority.NORMAL}
            };
            for (Object[] f : flashes) {
                FlashInfo fi = new FlashInfo();
                fi.setTitle((String) f[0]);
                fi.setContent((String) f[1]);
                if (f[2] != null) {
                    fi.setLinkUrl((String) f[2]);
                }
                if (f[3] != null) {
                    fi.setLinkText((String) f[3]);
                }
                fi.setPriority((FlashInfoPriority) f[4]);
                fi.setIsActive(true);
                flashInfoRepository.save(fi);
            }
        }
    }

    private void initBanners() {
        if (bannerRepository.count() == 0) {
            // {title, description, image, linkUrl, linkText, displayOrder, displayDuration(sec)}
            Object[][] banners = {
                    {"La transformation digitale du Burkina Faso",
                            "Le MTDPCE pilote la modernisation des services publics et le développement de l'économie numérique.",
                            "assets/images/home.jpg", "/ministere/missions", "Découvrir nos missions", 1, 6},
                    {"Des services publics accessibles en ligne",
                            "Plus de 85 téléservices dématérialisés pour rapprocher l'administration des citoyens.",
                            "assets/images/home.jpg", "/services", "Accéder aux e-services", 2, 5},
                    {"Un territoire connecté par la fibre optique",
                            "6 000 km de backbone national déployés pour connecter les 13 régions du pays.",
                            "assets/images/home.jpg", "/projets", "Voir nos projets", 3, 5},
                    {"Une nation cyber-sécurisée",
                            "Le renforcement de la cybersécurité et la protection des données au cœur de notre action.",
                            "assets/images/home.jpg", "/actualites", "Lire les actualités", 4, 7}
            };
            for (Object[] b : banners) {
                Banner banner = new Banner();
                banner.setTitle((String) b[0]);
                banner.setDescription((String) b[1]);
                banner.setImage((String) b[2]);
                banner.setLinkUrl((String) b[3]);
                banner.setLinkText((String) b[4]);
                banner.setDisplayOrder((Integer) b[5]);
                banner.setDisplayDuration((Integer) b[6]);
                banner.setIsActive(true);
                bannerRepository.save(banner);
            }
        }
    }

    private void initAgendas() {
        if (agendaRepository.count() == 0) {
            User author = userRepository.findByUsername("moderateur").orElseThrow();
            Object[][] agendas = {
                    {"Audience du Ministre avec les opérateurs télécom", "Rencontre d'échange sur la qualité de service.",
                            "Le Ministre reçoit les principaux opérateurs télécom pour faire le point sur la qualité de service et les engagements d'investissement.",
                            "Cabinet du Ministre, Ouagadougou", LocalDate.of(2026, 7, 2)},
                    {"Réunion du Comité de pilotage e-Gouvernement", "Suivi de l'avancement du projet e-Gov.",
                            "Le comité de pilotage se réunit pour examiner l'état d'avancement de la phase 2 du projet e-Gouvernement.",
                            "Salle de conférence, MTDPCE", LocalDate.of(2026, 7, 8)},
                    {"Visite de terrain : espaces numériques ruraux", "Inspection des points d'accès communautaires.",
                            "Une délégation du ministère visite les espaces numériques communautaires récemment ouverts dans la région.",
                            "Région du Centre-Ouest", LocalDate.of(2026, 7, 14)},
                    {"Conseil de cabinet hebdomadaire", "Réunion de coordination des directions.",
                            "Le conseil de cabinet réunit les responsables des directions pour la coordination des activités de la semaine.",
                            "Cabinet du Ministre", LocalDate.of(2026, 7, 21)},
                    {"Signature de convention avec l'ENN", "Partenariat pour la formation aux métiers du numérique.",
                            "Cérémonie de signature d'une convention de partenariat avec l'École Nationale du Numérique.",
                            "Bobo-Dioulasso", LocalDate.of(2026, 7, 25)},
                    {"Atelier de validation de la stratégie IA", "Concertation sur la stratégie nationale d'IA.",
                            "Atelier de validation de la stratégie nationale en matière d'intelligence artificielle avec les parties prenantes.",
                            "Hôtel Laico, Ouagadougou", LocalDate.of(2026, 8, 3)},
                    {"Réception d'une délégation internationale", "Coopération numérique régionale.",
                            "Le ministère reçoit une délégation d'un pays partenaire pour échanger sur la coopération numérique.",
                            "Cabinet du Ministre", LocalDate.of(2026, 8, 10)},
                    {"Lancement de la campagne newsletter", "Promotion de l'abonnement citoyen.",
                            "Lancement officiel de la campagne d'inscription à la newsletter du ministère.",
                            "Direction de la Communication", LocalDate.of(2026, 8, 17)},
                    {"Point presse mensuel", "Communication sur les activités du ministère.",
                            "Le ministère tient son point presse mensuel pour informer sur ses activités et réalisations.",
                            "Salle de presse, MTDPCE", LocalDate.of(2026, 8, 24)},
                    {"Comité de sécurité des systèmes d'information", "Revue des incidents et mesures.",
                            "Réunion du comité chargé de la sécurité des systèmes d'information de l'administration.",
                            "ANSSI, Ouagadougou", LocalDate.of(2026, 8, 31)},
                    {"Atelier de planification budgétaire 2027", "Préparation du budget du ministère.",
                            "Atelier interne de préparation et d'arbitrage du budget du ministère pour l'exercice 2027.",
                            "Salle de réunion, MTDPCE", LocalDate.of(2026, 9, 7)},
                    {"Rencontre avec les startups numériques", "Échange avec l'écosystème des jeunes pousses.",
                            "Le Ministre échange avec les startups accompagnées par l'agence de promotion de l'économie numérique.",
                            "Incubateur de Ouagadougou", LocalDate.of(2026, 9, 14)},
                    {"Inspection du data center national", "Visite des installations techniques.",
                            "Une délégation inspecte les installations et la sécurité du data center national.",
                            "Centre National de Données", LocalDate.of(2026, 9, 21)},
                    {"Conseil des ministres sectoriel", "Coordination interministérielle du numérique.",
                            "Réunion de coordination sur les projets numériques transversaux de l'administration.",
                            "Primature, Ouagadougou", LocalDate.of(2026, 9, 28)},
                    {"Forum des opérateurs postaux", "Concertation sur l'avenir du secteur postal.",
                            "Forum de concertation réunissant les acteurs publics et privés du secteur postal.",
                            "Hôtel Splendid, Ouagadougou", LocalDate.of(2026, 10, 5)},
                    {"Lancement de l'identité numérique", "Présentation du système d'identité numérique.",
                            "Cérémonie de lancement officiel du projet d'identité numérique nationale.",
                            "Palais des Congrès, Ouagadougou", LocalDate.of(2026, 10, 12)},
                    {"Atelier sur la régulation de l'IA", "Cadre réglementaire de l'intelligence artificielle.",
                            "Atelier technique sur l'élaboration d'un cadre de régulation des usages de l'IA.",
                            "ANIA, Ouagadougou", LocalDate.of(2026, 10, 19)},
                    {"Mission de terrain Sahel numérique", "Déploiement des services en zone Sahel.",
                            "Mission d'évaluation du déploiement des services numériques dans la région du Sahel.",
                            "Dori, région du Sahel", LocalDate.of(2026, 10, 26)},
                    {"Bilan annuel des structures rattachées", "Revue de performance des établissements.",
                            "Réunion de bilan annuel avec les responsables des structures rattachées au ministère.",
                            "Salle de conférence, MTDPCE", LocalDate.of(2026, 11, 2)},
                    {"Clôture du programme 10 000 Talents", "Cérémonie de remise des attestations.",
                            "Cérémonie de clôture et de remise des attestations aux lauréats du programme de formation.",
                            "École Nationale du Numérique, Bobo-Dioulasso", LocalDate.of(2026, 11, 9)}
            };
            for (Object[] a : agendas) {
                agendaRepository.save(Agenda.builder()
                        .title((String) a[0])
                        .summary((String) a[1])
                        .content((String) a[2])
                        .lieux((String) a[3])
                        .datePublication((LocalDate) a[4])
                        .status(AgendaStatus.PUBLISHED)
                        .author(author)
                        .publishedAt(((LocalDate) a[4]).atStartOfDay())
                        .build());
            }
        }
    }

    private void initJobOffers() {
        if (jobOfferRepository.count() == 0) {
            User creator = userRepository.findByUsername("admin").orElseThrow();
            Object[][] offers = {
                    {"Ingénieur DevOps", "Mise en place et exploitation des plateformes d'hébergement de l'État.", "CDI", "Direction des Systèmes d'Information", "Ouagadougou", "5 ans", "Bac+5 en informatique", "CI-2026-001", 2},
                    {"Analyste Cybersécurité", "Surveillance et réponse aux incidents de sécurité au sein du SOC national.", "CDI", "ANSSI", "Ouagadougou", "3 ans", "Bac+5 en sécurité informatique", "CI-2026-002", 3},
                    {"Chef de Projet Numérique", "Pilotage des projets de transformation digitale de l'administration.", "CDD", "Direction de la Transformation Digitale", "Ouagadougou", "7 ans", "Bac+5 en gestion de projet", "CI-2026-003", 1},
                    {"Développeur Full Stack", "Développement et maintenance des téléservices du portail e-Burkina.", "CDI", "ANPTIC", "Ouagadougou", "2 ans", "Bac+3 en développement", "CI-2026-004", 4},
                    {"Administrateur Base de Données", "Gestion et optimisation des bases de données du data center national.", "CDI", "Centre National de Données", "Ouagadougou", "4 ans", "Bac+4 en informatique", "CI-2026-005", 2},
                    {"Juriste TIC", "Élaboration et suivi des textes réglementaires du secteur numérique.", "CDI", "Direction des Affaires Juridiques", "Ouagadougou", "5 ans", "Bac+5 en droit du numérique", "CI-2026-006", 1},
                    {"Formateur Numérique", "Animation des formations aux métiers du numérique à l'ENN.", "CDD", "École Nationale du Numérique", "Bobo-Dioulasso", "3 ans", "Bac+4 et expérience pédagogique", "CI-2026-007", 5},
                    {"Chargé de Communication Digitale", "Gestion des canaux digitaux et de la newsletter du ministère.", "CDD", "Direction de la Communication", "Ouagadougou", "2 ans", "Bac+3 en communication", "CI-2026-008", 1},
                    {"Data Analyst", "Analyse des données de fréquentation et reporting pour la prise de décision.", "CDI", "Direction des Systèmes d'Information", "Ouagadougou", "3 ans", "Bac+5 en data science", "CI-2026-009", 2},
                    {"Technicien Réseau", "Déploiement et maintenance du backbone national en fibre optique.", "CDI", "Direction des Infrastructures", "Ouagadougou", "2 ans", "Bac+2 en réseaux télécom", "CI-2026-010", 6}
            };
            for (Object[] o : offers) {
                JobOffer job = new JobOffer();
                job.setTitle((String) o[0]);
                job.setDescription((String) o[1]);
                job.setContractType((String) o[2]);
                job.setDepartment((String) o[3]);
                job.setLocation((String) o[4]);
                job.setExperience((String) o[5]);
                job.setQualifications((String) o[6]);
                job.setRequirements("Diplôme requis, maîtrise des outils numériques et bonne capacité rédactionnelle.");
                job.setResponsibilities("Contribuer aux missions de la direction et aux projets numériques du ministère.");
                job.setReferenceNumber((String) o[7]);
                job.setNumberOfPositions((Integer) o[8]);
                job.setDeadline(LocalDate.of(2026, 8, 31));
                job.setSalary("Selon grille de la fonction publique");
                job.setApplicationEmail("recrutement@mtdpce.gov.bf");
                job.setRequiredDocuments("CV|||Lettre de motivation|||Copies des diplômes|||Pièce d'identité");
                job.setIsPublished(true);
                job.setStatus(JobOfferStatus.OPEN);
                job.setCreatedBy(creator);
                jobOfferRepository.save(job);
            }
        }
    }

    private void initContacts() {
        if (contactRepository.count() == 0) {
            User responder = userRepository.findByUsername("moderateur").orElseThrow();
            Object[][] contacts = {
                    {"Issa Kaboré", "issa.kabore@example.com", "+226 70 11 22 33", "Demande d'information sur l'agrément technique", "Bonjour, je souhaiterais connaître la procédure complète pour obtenir un agrément technique pour mon entreprise.", ContactStatus.NON_LU},
                    {"Awa Traoré", "awa.traore@example.com", "+226 76 44 55 66", "Problème d'accès au portail e-services", "Je n'arrive pas à me connecter au portail e-services depuis hier. Pouvez-vous m'aider ?", ContactStatus.LU},
                    {"Moussa Ouédraogo", "moussa.o@example.com", "+226 78 99 00 11", "Partenariat pour la formation numérique", "Notre association souhaite nouer un partenariat pour la formation des jeunes au numérique.", ContactStatus.EN_TRAITEMENT},
                    {"Fatou Sawadogo", "fatou.saw@example.com", "+226 70 22 33 44", "Inscription au forum cybersécurité", "Comment puis-je m'inscrire au prochain forum national sur la cybersécurité ?", ContactStatus.TRAITE},
                    {"Karim Zongo", "karim.zongo@example.com", "+226 76 55 66 77", "Signalement d'un site frauduleux", "Je voudrais signaler un site qui usurpe l'identité du ministère.", ContactStatus.EN_TRAITEMENT},
                    {"Mariam Diallo", "mariam.diallo@example.com", "+226 78 11 22 99", "Demande de stage", "Étudiante en informatique, je recherche un stage au sein de votre direction technique.", ContactStatus.NON_LU},
                    {"Boureima Nikiema", "b.nikiema@example.com", "+226 70 33 44 55", "Enregistrement d'un nom de domaine .bf", "Quelle est la démarche pour enregistrer un nom de domaine en .bf pour mon entreprise ?", ContactStatus.TRAITE},
                    {"Salimata Compaoré", "salimata.c@example.com", "+226 76 66 77 88", "Réclamation service postal", "Mon colis envoyé via la SONAPOST n'est pas arrivé. Comment procéder ?", ContactStatus.LU},
                    {"Hamado Sankara", "hamado.s@example.com", "+226 78 44 55 66", "Proposition de solution innovante", "Notre startup a développé une solution e-gouvernement que nous aimerions présenter.", ContactStatus.NON_LU},
                    {"Rasmata Ouattara", "rasmata.o@example.com", "+226 70 77 88 99", "Question sur la protection des données", "Quelles sont mes obligations en matière de protection des données personnelles de mes clients ?", ContactStatus.ARCHIVE}
            };
            for (Object[] c : contacts) {
                ContactStatus status = (ContactStatus) c[5];
                Contact contact = Contact.builder()
                        .name((String) c[0])
                        .email((String) c[1])
                        .phone((String) c[2])
                        .subject((String) c[3])
                        .message((String) c[4])
                        .status(status)
                        .build();
                if (status == ContactStatus.TRAITE) {
                    contact.setResponse("Bonjour, merci pour votre message. Vous trouverez les informations demandées sur notre site, rubrique correspondante. Cordialement.");
                    contact.setRespondedBy(responder);
                    contact.setRespondedAt(LocalDateTime.now().minusDays(1));
                }
                contactRepository.save(contact);
            }
        }
    }

    private void initNewsletterSubscriptions() {
        if (newsletterSubscriptionRepository.count() == 0) {
            Object[][] subs = {
                    {"abonne1@example.com", "Paul", "Kaboré", true},
                    {"abonne2@example.com", "Sophie", "Traoré", true},
                    {"abonne3@example.com", "Jean", "Ouédraogo", true},
                    {"abonne4@example.com", "Aïssata", "Sawadogo", false},
                    {"abonne5@example.com", "Marc", "Zongo", true},
                    {"abonne6@example.com", "Christine", "Diallo", true},
                    {"abonne7@example.com", "Ousmane", "Nikiema", false},
                    {"abonne8@example.com", "Bintou", "Compaoré", true},
                    {"abonne9@example.com", "Adama", "Sankara", true},
                    {"abonne10@example.com", "Nafissatou", "Ouattara", true}
            };
            for (Object[] s : subs) {
                NewsletterSubscription ns = new NewsletterSubscription();
                ns.setEmail((String) s[0]);
                ns.setFirstName((String) s[1]);
                ns.setLastName((String) s[2]);
                ns.setFrequency("WEEKLY");
                ns.setIsActive((Boolean) s[3]);
                ns.setIsVerified((Boolean) s[3]);
                newsletterSubscriptionRepository.save(ns);
            }
        }
    }

    // ====================================================================
    //  Médiathèque : albums + médias (images / vidéos)
    // ====================================================================
    private void initAlbumsAndMedia() {
        if (albumRepository.count() > 0) {
            return;
        }
        User creator = userRepository.findByUsername("moderateur").orElseThrow();

        // {nom, description, catégorie}
        String[][] albumsData = {
                {"Inauguration du Data Center National", "Photos officielles de l'inauguration du data center souverain.", "Événement"},
                {"Forum National sur la Cybersécurité", "Galerie des temps forts du forum cybersécurité.", "Événement"},
                {"Espaces Numériques Communautaires", "Reportage photo sur les points d'accès en zone rurale.", "Projet"},
                {"Visites de terrain du Ministre", "Déplacements et visites officielles du Ministre.", "Institution"},
                {"Salon International des TIC (SITIC Africa)", "Participation du Ministère au SITIC Africa.", "Événement"}
        };

        int albumOrder = 1;
        for (String[] a : albumsData) {
            Album album = new Album();
            album.setName(a[0]);
            album.setDescription(a[1]);
            album.setCategory(a[2]);
            album.setIsPublic(true);
            album.setDisplayOrder(albumOrder++);
            album.setCoverUrl("/uploads/media/" + slug(a[0]) + "/cover.jpg");
            album.setCreatedBy(creator);
            album.setCreatedAt(LocalDateTime.now());
            album.setUpdatedAt(LocalDateTime.now());

            List<Media> medias = new ArrayList<>();
            // 4 médias par album (3 images + 1 vidéo)
            for (int i = 1; i <= 4; i++) {
                Media m = new Media();
                boolean isVideo = (i == 4);
                m.setTitle(a[0] + " - " + (isVideo ? "Vidéo" : "Photo " + i));
                m.setDescription("Média de l'album « " + a[0] + " ».");
                m.setMediaType(isVideo ? MediaType.VIDEO : MediaType.IMAGE);
                m.setFileUrl("/uploads/media/" + slug(a[0]) + "/" + (isVideo ? "video.mp4" : "photo" + i + ".jpg"));
                m.setThumbnailUrl("/uploads/media/" + slug(a[0]) + "/thumb" + i + ".jpg");
                m.setCategory(a[2]);
                m.setTags("mtdpce," + slug(a[2]) + ",numérique");
                m.setIsPublic(true);
                m.setDisplayOrder(i);
                m.setViewCount((long) (50 * i));
                m.setDownloadCount((long) (5 * i));
                m.setMimeType(isVideo ? "video/mp4" : "image/jpeg");
                m.setFileSize(isVideo ? 15_000_000L : 850_000L);
                if (isVideo) {
                    m.setDuration(120);
                }
                m.setWidth(1920);
                m.setHeight(1080);
                m.setCreatedBy(creator);
                m.setCreatedAt(LocalDateTime.now());
                m.setUpdatedAt(LocalDateTime.now());
                m.setAlbum(album);
                medias.add(m);
            }
            album.setMedias(medias);
            albumRepository.save(album); // cascade -> enregistre aussi les médias
        }

        // Quelques médias autonomes (hors album) pour la galerie générale
        if (mediaRepository.count() <= 20) {
            String[][] standalone = {
                    {"Logo officiel du MTDPCE", "IMAGE", "Identité visuelle"},
                    {"Bannière de la Stratégie Nationale de Transformation Digitale", "IMAGE", "Communication"},
                    {"Spot de sensibilisation à la cybersécurité", "VIDEO", "Sensibilisation"},
                    {"Infographie : chiffres clés du numérique", "IMAGE", "Communication"}
            };
            int order = 1;
            for (String[] s : standalone) {
                Media m = new Media();
                m.setTitle(s[0]);
                m.setDescription(s[0] + ".");
                MediaType type = MediaType.valueOf(s[1]);
                m.setMediaType(type);
                boolean isVideo = type == MediaType.VIDEO;
                m.setFileUrl("/uploads/media/general/" + slug(s[0]) + (isVideo ? ".mp4" : ".jpg"));
                m.setThumbnailUrl("/uploads/media/general/" + slug(s[0]) + "-thumb.jpg");
                m.setCategory(s[2]);
                m.setTags("mtdpce," + slug(s[2]));
                m.setIsPublic(true);
                m.setDisplayOrder(order++);
                m.setViewCount(0L);
                m.setDownloadCount(0L);
                m.setMimeType(isVideo ? "video/mp4" : "image/jpeg");
                m.setFileSize(isVideo ? 8_000_000L : 320_000L);
                if (isVideo) {
                    m.setDuration(45);
                }
                m.setCreatedBy(creator);
                m.setCreatedAt(LocalDateTime.now());
                m.setUpdatedAt(LocalDateTime.now());
                mediaRepository.save(m);
            }
        }
    }

    // ====================================================================
    //  Images rattachées aux agendas (galerie d'un événement d'agenda)
    // ====================================================================
    private void initAgendaImages() {
        if (agendaImageRepository.count() > 0) {
            return;
        }
        List<Agenda> agendas = agendaRepository.findAll();
        // On illustre les 5 premiers agendas avec 2 images chacun
        int limit = Math.min(5, agendas.size());
        for (int a = 0; a < limit; a++) {
            Agenda agenda = agendas.get(a);
            for (int i = 1; i <= 2; i++) {
                agendaImageRepository.save(AgendaImage.builder()
                        .imageUrl("/uploads/agendas/" + agenda.getId() + "/image" + i + ".jpg")
                        .displayOrder(i)
                        .agenda(agenda)
                        .build());
            }
        }
    }

    // ====================================================================
    //  Images rattachées aux articles (galerie + images Facebook)
    // ====================================================================
    private void initArticleImages() {
        if (articleImageRepository.count() > 0 && facebookImageRepository.count() > 0) {
            return;
        }
        List<Article> articles = articleRepository.findAll();
        int limit = Math.min(8, articles.size());
        for (int a = 0; a < limit; a++) {
            Article article = articles.get(a);

            // 2 images de contenu par article (la 1re mise en avant)
            for (int i = 1; i <= 2; i++) {
                articleImageRepository.save(ArticleImage.builder()
                        .imageUrl("/uploads/articles/" + article.getId() + "/image" + i + ".jpg")
                        .isFeatured(i == 1)
                        .article(article)
                        .build());
            }
            // 1 image dédiée à la publication Facebook
            facebookImageRepository.save(FacebookImage.builder()
                    .imageUrl("/uploads/articles/" + article.getId() + "/facebook.jpg")
                    .isFeatured(true)
                    .article(article)
                    .build());
        }
    }

    // ====================================================================
    //  Configuration Facebook (publication automatique des articles)
    // ====================================================================
    private void initFacebookConfig() {
        if (facebookConfigRepository.count() > 0) {
            return;
        }
        facebookConfigRepository.save(FacebookConfig.builder()
                .label("Page officielle MTDPCE")
                .pageId("308567739007291")
                .accessToken("SEED_PLACEHOLDER_TOKEN_A_REMPLACER_EN_PRODUCTION")
                .tokenExpiresAt(LocalDateTime.now().plusMonths(2))
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
    }

    /** Génère un identifiant lisible pour les chemins de fichiers de démo. */
    private String slug(String input) {
        if (input == null) {
            return "media";
        }
        String s = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        return s.isEmpty() ? "media" : s;
    }
}
