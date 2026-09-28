package com.rvrjc.colorido.config;

import com.rvrjc.colorido.entity.*;
import com.rvrjc.colorido.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private FestInfoRepository festInfoRepository;

    @Autowired
    private EventCategoryRepository categoryRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ScheduleItemRepository scheduleItemRepository;

    @Autowired
    private MapLocationRepository mapLocationRepository;

    @Autowired
    private GalleryImageRepository galleryImageRepository;

    @Autowired
    private ContactInfoRepository contactInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed Admin if not exists
        if (adminRepository.count() == 0) {
            Admin admin = new Admin(
                    "admin",
                    passwordEncoder.encode("admin123"),
                    "COLORIDO Fest Administrator",
                    "ROLE_ADMIN"
            );
            adminRepository.save(admin);
            System.out.println(">>> Initialized default Admin: admin / admin123");
        }

        // 2. Seed FestInfo if not exists
        if (festInfoRepository.count() == 0) {
            FestInfo fest = new FestInfo();
            fest.setFestName("COLORIDO 2K26");
            fest.setCollegeName("R.V.R. & J.C. College of Engineering");
            fest.setCollegeLocation("Chowdavaram, Guntur, Andhra Pradesh - 522019");
            fest.setTagline("The Grand Symphony of Youth, Culture & Euphoria");
            fest.setStartDate("2026-03-12");
            fest.setEndDate("2026-03-14");
            fest.setEdition("Annual National Inter-Collegiate Cultural Festival");
            fest.setDescription("COLORIDO 2K26 is the premier annual youth and cultural festival of R.V.R. & J.C. College of Engineering, Guntur. Spanning 3 electrifying days, it unites thousands of students across the nation in an explosion of dance, music, fine arts, theatre, and athletics.");
            fest.setHistory("Established in 1985 under the patronage of Nagarjuna Education Society, R.V.R. & J.C. College of Engineering is an autonomous institution celebrated for academic excellence and vibrant student life. COLORIDO is its flagship annual cultural extravaganza, celebrating creativity, passion, and artistic talent across generations.");
            fest.setAboutContent("Set amidst the scenic 37.4-acre campus in Chowdavaram, Guntur, COLORIDO transforms RVRJC into a vibrant carnival of lights, rhythms, street performances, mega stages, food arenas, and high-energy competitions. With participants joining from premier institutions across Andhra Pradesh, Telangana, Karnataka, and Tamil Nadu, COLORIDO 2K26 promises to be the grandest edition yet.");
            fest.setImportantNotice("All registrations must be submitted via the official Google Forms linked under each event. College ID cards are mandatory for all registered participants. Spot registration is subject to slot availability. Decorum must be maintained on campus.");
            fest.setHeroBannerUrl("https://images.unsplash.com/photo-1492684223066-81342ee5ff30?auto=format&fit=crop&w=1920&q=80");
            fest.setThemeColor("#7928ca");
            festInfoRepository.save(fest);
            System.out.println(">>> Initialized COLORIDO FestInfo");
        }

        // 3. Seed Categories if empty (NO TECHNICAL EVENTS!)
        if (categoryRepository.count() == 0) {
            List<EventCategory> categories = Arrays.asList(
                    new EventCategory("Dance & Choreography", "dance-choreography", "Western group dance, classical solos, street 1v1 faceoffs, and folk fusion.", "Flame", 1),
                    new EventCategory("Music & Vocals", "music-vocals", "Battle of the Bands, Eastern and Western solo singing, unplugged acoustics, and rap battles.", "Music", 2),
                    new EventCategory("Literary & Dramatics", "literary-dramatics", "Street plays, mime, stand-up comedy, slam poetry, and creative dramatics.", "BookOpen", 3),
                    new EventCategory("Fine Arts & Design", "fine-arts", "Live canvas painting, digital sketching, rangoli, face painting, and short film showcase.", "Palette", 4),
                    new EventCategory("Sports & Athletics", "sports-athletics", "Gully Cricket Championship, box football, tug-of-war, badminton, and table tennis.", "Trophy", 5),
                    new EventCategory("Carnival & Informal Fun", "carnival-fun", "Campus treasure hunt, reels faceoff, spot talent, and open mic.", "Sparkles", 6)
            );
            categoryRepository.saveAll(categories);
            System.out.println(">>> Initialized Event Categories");
        }

        // 4. Seed Venues if empty
        if (venueRepository.count() == 0) {
            List<Venue> venues = Arrays.asList(
                    new Venue("Open Air Theatre (OAT)", "OAT", "Main open-air amphitheater with stadium seating for 2,500+ attendees. Venue for Band Clash, DJ Night, and Mega Group Dance.", 2500, "Central Quadrangle behind Silver Jubilee Block", 48.0, 42.0),
                    new Venue("Silver Jubilee Auditorium", "SJB-AUD", "Premier air-conditioned indoor auditorium equipped with acoustic audio and stage lighting for 1,200 seats.", 1200, "Silver Jubilee Block 1st Floor", 32.0, 28.0),
                    new Venue("Decennial Block Open Stage", "DEC-STAGE", "Elevated open stage ideal for street dance battles, theatrical plays, and mime.", 800, "Courtyard in front of Decennial Block", 65.0, 35.0),
                    new Venue("Cyber Block Seminar Hall 1", "CYB-SH1", "State-of-the-art auditorium with 4K projection for short films and fine arts workshops.", 350, "Cyber Block Ground Floor", 38.0, 62.0),
                    new Venue("Main Sports Complex & Grounds", "SPORTS-GRD", "Expansive athletic stadium with turf grounds, cricket pitch, and floodlights.", 3000, "Eastern Campus Sports Enclosure", 78.0, 65.0),
                    new Venue("Central Plaza & Food Stall Area", "FOOD-PLAZA", "Vibrant carnival open zone housing 25+ food stalls, beverage counters, and dining seating.", 1500, "Between College Canteen and Hi-Tech Block", 52.0, 58.0)
            );
            venueRepository.saveAll(venues);
            System.out.println(">>> Initialized RVRJC Venues");
        }

        // 5. Seed Events if empty
        if (eventRepository.count() == 0) {
            List<EventCategory> cats = categoryRepository.findAll();
            List<Venue> vens = venueRepository.findAll();

            EventCategory danceCat = cats.stream().filter(c -> c.getSlug().contains("dance")).findFirst().orElse(cats.get(0));
            EventCategory musicCat = cats.stream().filter(c -> c.getSlug().contains("music")).findFirst().orElse(cats.get(1));
            EventCategory litCat = cats.stream().filter(c -> c.getSlug().contains("literary")).findFirst().orElse(cats.get(2));
            EventCategory artCat = cats.stream().filter(c -> c.getSlug().contains("fine-arts")).findFirst().orElse(cats.get(3));
            EventCategory sportsCat = cats.stream().filter(c -> c.getSlug().contains("sports")).findFirst().orElse(cats.get(4));
            EventCategory funCat = cats.stream().filter(c -> c.getSlug().contains("carnival")).findFirst().orElse(cats.get(5));

            Venue oat = vens.stream().filter(v -> v.getCode().equals("OAT")).findFirst().orElse(vens.get(0));
            Venue aud = vens.stream().filter(v -> v.getCode().equals("SJB-AUD")).findFirst().orElse(vens.get(1));
            Venue decStage = vens.stream().filter(v -> v.getCode().equals("DEC-STAGE")).findFirst().orElse(vens.get(2));
            Venue cyberHall = vens.stream().filter(v -> v.getCode().equals("CYB-SH1")).findFirst().orElse(vens.get(3));
            Venue sports = vens.stream().filter(v -> v.getCode().equals("SPORTS-GRD")).findFirst().orElse(vens.get(4));

            Event e1 = new Event();
            e1.setName("Natya Tarang - Mega Group Dance Championship");
            e1.setCategory(danceCat);
            e1.setVenue(oat);
            e1.setDescription("The ultimate inter-collegiate dance spectacle where choreography, synchronization, themes, and explosive energy collide on the grand OAT stage.");
            e1.setRules("1. Team size: 6 to 16 members.\n2. Time limit: 6-8 minutes (empty stage to clear stage).\n3. Any dance genre (Western, Cinematic, Hip-Hop, Fusion) is permitted.\n4. Audio track must be submitted in MP3 format 2 hours prior to the event.\n5. Dangerous props (fire, glass, liquids) are strictly banned.");
            e1.setTeamSize("6-16 Members");
            e1.setPrizes("1st Prize: ₹30,000 | 2nd Prize: ₹18,000 | 3rd Prize: ₹10,000 + Trophies & Certificates");
            e1.setEventDate("2026-03-13");
            e1.setStartTime("05:30 PM");
            e1.setEndTime("09:30 PM");
            e1.setImageUrl("https://images.unsplash.com/photo-1547153760-18fc86324498?auto=format&fit=crop&w=1200&q=80");
            e1.setRegistrationUrl("https://forms.gle/colorido2k26-natya-tarang");
            e1.setIsFeatured(true);
            e1.setStatus("OPEN");
            e1.setCoordinatorName("Prof. K. Rama Krishna & P. Sai Teja");
            e1.setCoordinatorContact("+91 98480 12345");

            Event e2 = new Event();
            e2.setName("Symphony Clash - Battle of the Bands");
            e2.setCategory(musicCat);
            e2.setVenue(oat);
            e2.setDescription("High-voltage battle featuring the best college rock, fusion, and indie bands competing live with blistering solos and original arrangements.");
            e2.setRules("1. Band size: 3 to 8 members.\n2. Time limit: 15 minutes including soundcheck.\n3. Standard drum kit and basic vocal mics provided; bands must bring own guitars, processors, and keyboards.\n4. Original compositions receive bonus weightage from jury.");
            e2.setTeamSize("3-8 Members");
            e2.setPrizes("1st Prize: ₹35,000 | 2nd Prize: ₹20,000 | 3rd Prize: ₹12,000 + Best Instrumentalist Awards");
            e2.setEventDate("2026-03-12");
            e2.setStartTime("06:00 PM");
            e2.setEndTime("10:00 PM");
            e2.setImageUrl("https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=1200&q=80");
            e2.setRegistrationUrl("https://forms.gle/colorido2k26-battle-bands");
            e2.setIsFeatured(true);
            e2.setStatus("OPEN");
            e2.setCoordinatorName("Dr. N. Srinivas Rao & Ch. Akhil");
            e2.setCoordinatorContact("+91 98480 23456");

            Event e3 = new Event();
            e3.setName("Raga Sudha - Solo Vocals (Classical & Western)");
            e3.setCategory(musicCat);
            e3.setVenue(aud);
            e3.setDescription("Showcase your vocal prowess and melodic perfection in front of celebrated playback and carnatic guest judges.");
            e3.setRules("1. Solo performance only.\n2. Time limit: 4 minutes.\n3. Karaoke tracks or single acoustic instrument accompaniment permitted.\n4. Judging on shruti, pitch, clarity, diction, and expression.");
            e3.setTeamSize("Solo (1)");
            e3.setPrizes("1st Prize: ₹10,000 | 2nd Prize: ₹6,000 | 3rd Prize: ₹3,000");
            e3.setEventDate("2026-03-12");
            e3.setStartTime("10:30 AM");
            e3.setEndTime("01:30 PM");
            e3.setImageUrl("https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?auto=format&fit=crop&w=1200&q=80");
            e3.setRegistrationUrl("https://forms.gle/colorido2k26-raga-sudha");
            e3.setIsFeatured(true);
            e3.setStatus("OPEN");
            e3.setCoordinatorName("Dr. S. Radhika & V. Bhavana");
            e3.setCoordinatorContact("+91 98480 34567");

            Event e4 = new Event();
            e4.setName("Step-Up 1v1 Street Dance Faceoff");
            e4.setCategory(danceCat);
            e4.setVenue(decStage);
            e4.setDescription("Raw hip-hop, popping, locking, breaking, and krump faceoffs in a high-octane 1-on-1 cypher battle format with live DJ beats.");
            e4.setRules("1. 1 vs 1 knockout battles.\n2. 45 seconds per round per dancer, best of 3 in finals.\n3. Music dropped spontaneously by DJ - no advance track selection.\n4. Pure crowd interaction and sportsmanship required.");
            e4.setTeamSize("Solo (1)");
            e4.setPrizes("1st Prize: ₹12,000 | 2nd Prize: ₹7,000 | 3rd Prize: ₹4,000");
            e4.setEventDate("2026-03-13");
            e4.setStartTime("02:00 PM");
            e4.setEndTime("05:00 PM");
            e4.setImageUrl("https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?auto=format&fit=crop&w=1200&q=80");
            e4.setRegistrationUrl("https://forms.gle/colorido2k26-street-dance");
            e4.setIsFeatured(true);
            e4.setStatus("OPEN");
            e4.setCoordinatorName("Mr. G. Venkat & M. Karthik");
            e4.setCoordinatorContact("+91 98480 45678");

            Event e5 = new Event();
            e5.setName("Ad-Venture & Rangmanch - Street Play & Mime");
            e5.setCategory(litCat);
            e5.setVenue(decStage);
            e5.setDescription("Social awareness, satire, drama, and silent expression brought to life through thought-provoking street theatre (Nukkad Natak) and mime.");
            e5.setRules("1. Team size: 4 to 12 participants.\n2. Time limit: 10-12 minutes.\n3. Live acoustic instruments (dholak, harmonium, dafli) encouraged; no prerecorded music for street play.\n4. Vulgarity or abusive language leads to immediate disqualification.");
            e5.setTeamSize("4-12 Members");
            e5.setPrizes("1st Prize: ₹16,000 | 2nd Prize: ₹10,000 | 3rd Prize: ₹6,000");
            e5.setEventDate("2026-03-14");
            e5.setStartTime("10:00 AM");
            e5.setEndTime("01:00 PM");
            e5.setImageUrl("https://images.unsplash.com/photo-1469488865564-c2de10f69f96?auto=format&fit=crop&w=1200&q=80");
            e5.setRegistrationUrl("https://forms.gle/colorido2k26-street-play");
            e5.setIsFeatured(true);
            e5.setStatus("OPEN");
            e5.setCoordinatorName("Dr. P. V. Ramana & K. Divya");
            e5.setCoordinatorContact("+91 98480 56789");

            Event e6 = new Event();
            e6.setName("Chitrakaari - Live Canvas Painting & Mandala");
            e6.setCategory(artCat);
            e6.setVenue(cyberHall);
            e6.setDescription("Unleash your artistic imagination on 24x36 canvas around the theme 'Echoes of Euphoria' using oils, acrylics, watercolours, or charcoal.");
            e6.setRules("1. Individual or duo.\n2. Duration: 2.5 hours.\n3. Base canvas provided by organizers; participants must bring personal paints and brushes.\n4. Artwork judged on creativity, technique, adherence to theme, and aesthetics.");
            e6.setTeamSize("1-2 Members");
            e6.setPrizes("1st Prize: ₹8,000 | 2nd Prize: ₹5,000 | 3rd Prize: ₹3,000");
            e6.setEventDate("2026-03-12");
            e6.setStartTime("11:00 AM");
            e6.setEndTime("01:30 PM");
            e6.setImageUrl("https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?auto=format&fit=crop&w=1200&q=80");
            e6.setRegistrationUrl("https://forms.gle/colorido2k26-chitrakaari");
            e6.setIsFeatured(false);
            e6.setStatus("OPEN");
            e6.setCoordinatorName("Mrs. K. Anitha & T. Prasanth");
            e6.setCoordinatorContact("+91 98480 67890");

            Event e7 = new Event();
            e7.setName("RVRJC Gully Cricket Championship");
            e7.setCategory(sportsCat);
            e7.setVenue(sports);
            e7.setDescription("Fast-paced 6-over tennis ball cricket tournament with exciting gully rules, box boundaries, and intense knockout rounds.");
            e7.setRules("1. 7 playing members + 2 substitutes.\n2. 6 overs per innings, max 2 overs per bowler.\n3. Direct wall hit without bounce is out; one tip one hand rule applies in specific zones.\n4. Umpire decisions are final.");
            e7.setTeamSize("7-9 Members");
            e7.setPrizes("Winners: ₹20,000 + COLORIDO Rolling Cup | Runners-Up: ₹10,000 + Medals");
            e7.setEventDate("2026-03-12");
            e7.setStartTime("09:30 AM");
            e7.setEndTime("04:30 PM");
            e7.setImageUrl("https://images.unsplash.com/photo-1540747913346-19e32dc3e97e?auto=format&fit=crop&w=1200&q=80");
            e7.setRegistrationUrl("https://forms.gle/colorido2k26-gully-cricket");
            e7.setIsFeatured(true);
            e7.setStatus("OPEN");
            e7.setCoordinatorName("Physical Director Sri M. Siva & B. Tarun");
            e7.setCoordinatorContact("+91 98480 78901");

            Event e8 = new Event();
            e8.setName("CineSnap - Short Film & Reels Festival");
            e8.setCategory(funCat);
            e8.setVenue(cyberHall);
            e8.setDescription("A premiere showcase for young filmmakers and digital creators. Screen your cinematic short film or creative Instagram fest reel to a packed hall.");
            e8.setRules("1. Short film runtime: 5 to 12 minutes.\n2. Reels runtime: 30 to 90 seconds.\n3. Format: 1080p MP4 submitted via Google Drive link.\n4. All audio/music must either be original or non-copyrighted.");
            e8.setTeamSize("Individual / Team");
            e8.setPrizes("Best Short Film: ₹12,000 | Best Director: ₹5,000 | Best Viral Fest Reel: ₹4,000");
            e8.setEventDate("2026-03-14");
            e8.setStartTime("02:30 PM");
            e8.setEndTime("05:00 PM");
            e8.setImageUrl("https://images.unsplash.com/photo-1485846234645-a62644f84728?auto=format&fit=crop&w=1200&q=80");
            e8.setRegistrationUrl("https://forms.gle/colorido2k26-cinesnap");
            e8.setIsFeatured(false);
            e8.setStatus("OPEN");
            e8.setCoordinatorName("Mr. T. Kishore & S. Harika");
            e8.setCoordinatorContact("+91 98480 89012");

            eventRepository.saveAll(Arrays.asList(e1, e2, e3, e4, e5, e6, e7, e8));
            System.out.println(">>> Initialized COLORIDO Events");
        }

        // 6. Seed Schedule Items if empty
        if (scheduleItemRepository.count() == 0) {
            List<Event> allEvents = eventRepository.findAll();
            List<Venue> allVenues = venueRepository.findAll();

            Venue oat = allVenues.stream().filter(v -> v.getCode().equals("OAT")).findFirst().orElse(allVenues.get(0));
            Venue aud = allVenues.stream().filter(v -> v.getCode().equals("SJB-AUD")).findFirst().orElse(allVenues.get(1));
            Venue decStage = allVenues.stream().filter(v -> v.getCode().equals("DEC-STAGE")).findFirst().orElse(allVenues.get(2));
            Venue cyberHall = allVenues.stream().filter(v -> v.getCode().equals("CYB-SH1")).findFirst().orElse(allVenues.get(3));
            Venue sports = allVenues.stream().filter(v -> v.getCode().equals("SPORTS-GRD")).findFirst().orElse(allVenues.get(4));
            Venue foodPlaza = allVenues.stream().filter(v -> v.getCode().equals("FOOD-PLAZA")).findFirst().orElse(allVenues.get(5));

            Event eBand = allEvents.stream().filter(e -> e.getName().contains("Bands")).findFirst().orElse(null);
            Event eDance = allEvents.stream().filter(e -> e.getName().contains("Natya")).findFirst().orElse(null);
            Event eVocals = allEvents.stream().filter(e -> e.getName().contains("Vocals")).findFirst().orElse(null);
            Event eStreetDance = allEvents.stream().filter(e -> e.getName().contains("Step-Up")).findFirst().orElse(null);
            Event eDrama = allEvents.stream().filter(e -> e.getName().contains("Rangmanch")).findFirst().orElse(null);
            Event eArt = allEvents.stream().filter(e -> e.getName().contains("Chitrakaari")).findFirst().orElse(null);
            Event eCricket = allEvents.stream().filter(e -> e.getName().contains("Cricket")).findFirst().orElse(null);
            Event eCine = allEvents.stream().filter(e -> e.getName().contains("CineSnap")).findFirst().orElse(null);

            List<ScheduleItem> items = Arrays.asList(
                    // Day 1
                    createSchedule("Grand Opening Ceremony & Lamp Lighting", null, aud, 1, "2026-03-12", "09:00 AM", "10:30 AM", "Official inauguration with dignitary addresses, ceremonial lamp lighting, and welcome address.", "CEREMONY"),
                    createSchedule("RVRJC Gully Cricket Knockout Rounds", eCricket, sports, 1, "2026-03-12", "09:30 AM", "04:30 PM", "High-intensity tournament rounds across the campus sports complex.", "EVENT"),
                    createSchedule("Raga Sudha - Solo Vocals (Classical & Western)", eVocals, aud, 1, "2026-03-12", "10:30 AM", "01:30 PM", "Solo vocal performances in the acoustically tuned auditorium.", "EVENT"),
                    createSchedule("Chitrakaari - Live Painting Competition", eArt, cyberHall, 1, "2026-03-12", "11:00 AM", "01:30 PM", "Theme-based live canvas artwork presentation.", "EVENT"),
                    createSchedule("Fest Lunch Break & Food Stalls Carnival", null, foodPlaza, 1, "2026-03-12", "01:30 PM", "03:00 PM", "Explore 25+ food stalls with live acoustic busking music.", "BREAK"),
                    createSchedule("Symphony Clash - Battle of the Bands", eBand, oat, 1, "2026-03-12", "06:00 PM", "10:00 PM", "Electric rock and fusion clash under the stars at Open Air Theatre.", "CULTURAL_NIGHT"),

                    // Day 2
                    createSchedule("Cricket Semi-Finals & Championship Match", eCricket, sports, 2, "2026-03-13", "09:30 AM", "01:00 PM", "The battle for the COLORIDO cricket crown.", "EVENT"),
                    createSchedule("Step-Up 1v1 Street Dance Battle", eStreetDance, decStage, 2, "2026-03-13", "02:00 PM", "05:00 PM", "Knockout cyphers with live DJ sets and audience voting.", "EVENT"),
                    createSchedule("Natya Tarang - Mega Group Dance Spectacle", eDance, oat, 2, "2026-03-13", "05:30 PM", "09:30 PM", "Massive group dance performances and cinematic stage sets.", "CULTURAL_NIGHT"),

                    // Day 3
                    createSchedule("Ad-Venture Street Play & Mime", eDrama, decStage, 3, "2026-03-14", "10:00 AM", "01:00 PM", "Dramatic street plays highlighting contemporary societal issues.", "EVENT"),
                    createSchedule("CineSnap Short Film & Reels Premiere", eCine, cyberHall, 3, "2026-03-14", "02:30 PM", "05:00 PM", "Screening of curated student short films and awards announcement.", "EVENT"),
                    createSchedule("Valedictory Prize Ceremony & Celebrity DJ Night", null, oat, 3, "2026-03-14", "06:00 PM", "10:30 PM", "Grand prize distribution followed by celebrity artist concert and high-energy EDM DJ finale.", "CULTURAL_NIGHT")
            );

            scheduleItemRepository.saveAll(items);
            System.out.println(">>> Initialized COLORIDO Schedule Items");
        }

        // 7. Seed Map Locations if empty (Explicitly including Food Stall Area)
        if (mapLocationRepository.count() == 0) {
            List<Venue> vens = venueRepository.findAll();
            Venue oat = vens.stream().filter(v -> v.getCode().equals("OAT")).findFirst().orElse(null);
            Venue aud = vens.stream().filter(v -> v.getCode().equals("SJB-AUD")).findFirst().orElse(null);
            Venue decStage = vens.stream().filter(v -> v.getCode().equals("DEC-STAGE")).findFirst().orElse(null);
            Venue cyberHall = vens.stream().filter(v -> v.getCode().equals("CYB-SH1")).findFirst().orElse(null);
            Venue sports = vens.stream().filter(v -> v.getCode().equals("SPORTS-GRD")).findFirst().orElse(null);
            Venue foodPlaza = vens.stream().filter(v -> v.getCode().equals("FOOD-PLAZA")).findFirst().orElse(null);

            List<MapLocation> locations = Arrays.asList(
                    new MapLocation("Food Stall Area", "FOOD", "Official festival food zone featuring 25+ food stalls, beverage booths, juices, chaat, and Andhra delicacies.", 52.0, 58.0, "utensils", foodPlaza, true),
                    new MapLocation("Open Air Theatre (OAT)", "VENUE", "Main festival amphitheater stage for Battle of Bands, Mega Group Dance, and DJ Night.", 48.0, 42.0, "stage", oat, true),
                    new MapLocation("Silver Jubilee Auditorium", "VENUE", "Air-conditioned auditorium for Inauguration and solo vocals.", 32.0, 28.0, "stage", aud, true),
                    new MapLocation("Decennial Block Open Stage", "VENUE", "Open-air stage platform for street plays, mime, and 1v1 dance faceoffs.", 65.0, 35.0, "stage", decStage, true),
                    new MapLocation("Cyber Block Cultural Hall", "VENUE", "Computer science block auditorium for fine arts, digital exhibitions, and short films.", 38.0, 62.0, "stage", cyberHall, true),
                    new MapLocation("Athletic & Cricket Sports Arena", "SPORTS", "Campus sports grounds for Gully Cricket and athletic contests.", 78.0, 65.0, "trophy", sports, true),
                    new MapLocation("Main Entrance Arch & Help Desk", "ENTRY", "Official entrance on NH-16 Chowdavaram. Guest verification, passes, festival guide desk.", 18.0, 85.0, "info", null, true),
                    new MapLocation("Campus Health Center & First Aid", "FACILITY", "Emergency medical care, trained paramedical staff, and ambulance on standby.", 26.0, 45.0, "heart-pulse", null, true),
                    new MapLocation("Main Parking Lot", "FACILITY", "Organized two-wheeler and four-wheeler parking zone for students and visitors.", 12.0, 70.0, "car", null, true)
            );

            mapLocationRepository.saveAll(locations);
            System.out.println(">>> Initialized Map Locations (including Food Stall Area)");
        }

        // 8. Seed Gallery Images if empty
        if (galleryImageRepository.count() == 0) {
            List<GalleryImage> images = Arrays.asList(
                    createGallery("Electrifying Pro-Night Crowds", "CROWD", "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?auto=format&fit=crop&w=1200&q=80", "15,000+ students cheering under laser lights at the RVRJC Open Air Theatre.", "2025", true, 1),
                    createGallery("Battle of Bands Guitar Riff", "MUSIC", "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=1200&q=80", "Live solo lead guitar performance by the winning band.", "2025", true, 2),
                    createGallery("Natya Tarang Classical & Fusion", "DANCE", "https://images.unsplash.com/photo-1547153760-18fc86324498?auto=format&fit=crop&w=1200&q=80", "Mesmerizing group choreography in colorful traditional attire.", "2025", true, 3),
                    createGallery("Campus Night Glow & Laser Display", "CAMPUS", "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?auto=format&fit=crop&w=1200&q=80", "The RVRJC campus illuminated in festive colors.", "2024", false, 4),
                    createGallery("Gully Cricket Championship Trophy", "SPORTS", "https://images.unsplash.com/photo-1540747913346-19e32dc3e97e?auto=format&fit=crop&w=1200&q=80", "Thrilling final delivery at the eastern sports ground.", "2025", false, 5),
                    createGallery("Street Play Rangmanch Performance", "CROWD", "https://images.unsplash.com/photo-1469488865564-c2de10f69f96?auto=format&fit=crop&w=1200&q=80", "Dramatic portrayal of social themes at Decennial stage.", "2024", false, 6),
                    createGallery("Live Canvas Art Exhibition", "CAMPUS", "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?auto=format&fit=crop&w=1200&q=80", "Student artists presenting their finished masterpiece canvases.", "2025", false, 7),
                    createGallery("DJ Night Euphoria", "MUSIC", "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?auto=format&fit=crop&w=1200&q=80", "Fest finale confetti blast and euphoria at COLORIDO.", "2025", true, 8)
            );
            galleryImageRepository.saveAll(images);
            System.out.println(">>> Initialized Gallery Images");
        }

        // 9. Seed Contact Info if empty
        if (contactInfoRepository.count() == 0) {
            List<ContactInfo> contacts = Arrays.asList(
                    createContact("Dr. K. Srinivas Rao", "Faculty Convener", "Student Affairs & Cultural Committee", "+91 98480 11001", "colorido.convener@rvrjcce.ac.in", "FACULTY", 1),
                    createContact("Dr. P. Radhika Devi", "Faculty Co-Convener", "Humanities & Sciences", "+91 98480 11002", "radhika.cult@rvrjcce.ac.in", "FACULTY", 2),
                    createContact("Sri M. Siva", "Sports Convener", "Physical Education Department", "+91 98480 11003", "sports@rvrjcce.ac.in", "FACULTY", 3),
                    createContact("P. Sai Teja", "Student President", "Student Council & Logistics", "+91 98480 22001", "president.colorido@rvrjcce.ac.in", "STUDENT", 4),
                    createContact("M. Akhila Reddy", "Cultural Lead Coordinator", "Arts & Performing Wing", "+91 98480 22002", "cultural.lead@rvrjcce.ac.in", "STUDENT", 5),
                    createContact("Ch. Karthik", "Technical & Stage Ops Lead", "Sound, Lights & Media", "+91 98480 22003", "media.colorido@rvrjcce.ac.in", "STUDENT", 6),
                    createContact("COLORIDO General Central Help Desk", "Central Help & Registration Support", "RVRJC Administrative Wing", "+91 863 2288254", "colorido2k26@rvrjcce.ac.in", "HELP_DESK", 7)
            );
            contactInfoRepository.saveAll(contacts);
            System.out.println(">>> Initialized Contact Info");
        }
    }

    private ScheduleItem createSchedule(String title, Event event, Venue venue, Integer day, String date, String start, String end, String desc, String type) {
        ScheduleItem item = new ScheduleItem();
        item.setTitle(title);
        item.setEvent(event);
        item.setVenue(venue);
        item.setDayNumber(day);
        item.setScheduleDate(date);
        item.setStartTime(start);
        item.setEndTime(end);
        item.setDescription(desc);
        item.setType(type);
        return item;
    }

    private GalleryImage createGallery(String title, String category, String url, String desc, String year, boolean feat, int order) {
        GalleryImage img = new GalleryImage();
        img.setTitle(title);
        img.setCategory(category);
        img.setImageUrl(url);
        img.setThumbnailUrl(url);
        img.setDescription(desc);
        img.setYear(year);
        img.setIsFeatured(feat);
        img.setDisplayOrder(order);
        return img;
    }

    private ContactInfo createContact(String name, String role, String dept, String phone, String email, String type, int order) {
        ContactInfo c = new ContactInfo();
        c.setName(name);
        c.setRole(role);
        c.setDepartment(dept);
        c.setPhone(phone);
        c.setEmail(email);
        c.setType(type);
        c.setDisplayOrder(order);
        return c;
    }
}
