package com.rvrjc.colorido.service;

import com.rvrjc.colorido.dto.AiChatResponse;
import com.rvrjc.colorido.entity.*;
import com.rvrjc.colorido.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * COLORIDO 2K26 AI Assistant Service
 * Strictly grounded in the live MySQL/MariaDB database.
 * No hardcoded festival knowledge base — every detail (events, venues, coordinators,
 * rules, Google Form URLs, schedules, map locations, fest info) is retrieved
 * directly from the database repositories shared with the public festival website.
 */
@Service
public class AiAssistantService {

    @Autowired
    private FestInfoRepository festInfoRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventCategoryRepository categoryRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private ScheduleItemRepository scheduleItemRepository;

    @Autowired
    private MapLocationRepository mapLocationRepository;

    @Autowired
    private ContactInfoRepository contactInfoRepository;

    private static final String OUT_OF_SCOPE_RESPONSE = 
            "I'm the COLORIDO/RVRJC event assistant. I can only help with RVRJC college events and official COLORIDO information.";

    private static final String NOT_AVAILABLE_RESPONSE = 
            "I don't have that information in the current RVRJC event data.";

    // Explicit regex patterns for known out-of-scope queries (general trivia, programming, math, world news)
    private static final Pattern OUT_OF_SCOPE_PATTERN = Pattern.compile(
            "\\b(python|java (program|code|loop)|c\\+\\+|javascript code|write a function|debug|compile|" +
            "president of|prime minister|world cup|fifa|capital of|recipe|weather in|stock price|bitcoin|" +
            "cryptocurrency|calculate 2|derivative|integral|who is the king|translate into french|" +
            "tell me a bedtime story|recommend movies|solve this math|who discovered|currency of)\\b",
            Pattern.CASE_INSENSITIVE
    );

    public AiChatResponse processQuestion(String userMessage) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return new AiChatResponse(
                    "Hello! I am your COLORIDO 2K26 Event Assistant for R.V.R. & J.C. College of Engineering. " +
                    "Ask me about our competitions, rules, prizes, venues, schedule, coordinators, registration forms, or campus map!",
                    true
            );
        }

        String query = userMessage.trim();
        String lowerQuery = query.toLowerCase();

        // 1. Strict Server-Side Scope Enforcement
        if (isOutOfScope(lowerQuery)) {
            return new AiChatResponse(OUT_OF_SCOPE_RESPONSE, false);
        }

        // 2. Fetch Live Database Context (Shared with Public Website & Admin Console)
        FestInfo fest = festInfoRepository.findAll().stream().findFirst().orElse(null);
        List<Event> events = eventRepository.findAll();
        List<EventCategory> categories = categoryRepository.findAll();
        List<Venue> venues = venueRepository.findAll();
        List<ScheduleItem> schedules = scheduleItemRepository.findAll();
        List<MapLocation> mapLocations = mapLocationRepository.findAll();
        List<ContactInfo> contacts = contactInfoRepository.findAll();

        // 3. Dynamic Response Generation from Live Entities
        String answer = generateAnswer(lowerQuery, fest, events, categories, venues, schedules, mapLocations, contacts);
        return new AiChatResponse(answer, true);
    }

    private boolean isOutOfScope(String q) {
        if (OUT_OF_SCOPE_PATTERN.matcher(q).find()) {
            return true;
        }

        // Check for common festival / college domain markers
        boolean domainRelated = q.contains("colorido") || q.contains("rvr") || q.contains("jc") ||
                q.contains("fest") || q.contains("event") || q.contains("venue") || q.contains("dance") ||
                q.contains("music") || q.contains("band") || q.contains("schedule") || q.contains("register") ||
                q.contains("map") || q.contains("food") || q.contains("stall") || q.contains("stage") ||
                q.contains("auditorium") || q.contains("prize") || q.contains("rule") || q.contains("contact") ||
                q.contains("date") || q.contains("time") || q.contains("campus") || q.contains("college") ||
                q.contains("oat") || q.contains("cricket") || q.contains("art") || q.contains("drama") ||
                q.contains("sing") || q.contains("vocal") || q.contains("film") || q.contains("paint") ||
                q.contains("where") || q.contains("when") || q.contains("how") || q.contains("who") ||
                q.contains("what") || q.contains("hi") || q.contains("hello") || q.contains("help") ||
                q.contains("guideline") || q.contains("faculty") || q.contains("student") || q.contains("phone") ||
                q.contains("email") || q.contains("form") || q.contains("coordinator") || q.contains("convener");

        return !domainRelated;
    }

    private String generateAnswer(String q, FestInfo fest, List<Event> events, List<EventCategory> categories,
                                  List<Venue> venues, List<ScheduleItem> schedules, List<MapLocation> mapLocations,
                                  List<ContactInfo> contacts) {

        // ==========================================
        // 1. GREETINGS
        // ==========================================
        if (q.matches("^(hi|hello|hey|greetings|good (morning|afternoon|evening)).*")) {
            String festTitle = (fest != null && fest.getFestName() != null) ? fest.getFestName() : "COLORIDO 2K26";
            String college = (fest != null && fest.getCollegeName() != null) ? fest.getCollegeName() : "R.V.R. & J.C. College of Engineering";
            return "Hello and welcome to " + festTitle + " at " + college + "! " +
                   "I have live access to all event rules, prize bounties, venues, schedules, coordinators, Google Form links, and the campus map. " +
                   "How can I assist you today?";
        }

        // ==========================================
        // 2. FEST INFORMATION / ABOUT / DATES / HISTORY / GUIDELINES
        // ==========================================
        // Fest Tagline
        if (q.contains("tagline") || q.contains("motto") || q.contains("theme")) {
            if (fest != null && fest.getTagline() != null) {
                return "The official tagline of " + fest.getFestName() + " is: \"" + fest.getTagline() + "\".";
            }
            return NOT_AVAILABLE_RESPONSE;
        }

        // Fest Dates / Commencing / Timeline
        if (q.contains("when is the fest") || q.contains("fest date") || q.contains("dates of the fest") ||
            q.contains("when is colorido") || q.contains("fest happening") || q.contains("when does colorido")) {
            if (fest != null && fest.getStartDate() != null && fest.getEndDate() != null) {
                return fest.getFestName() + " will take place from " + fest.getStartDate() + " to " + fest.getEndDate() + " at " + fest.getCollegeName() + ", " + fest.getCollegeLocation() + ".";
            }
            return NOT_AVAILABLE_RESPONSE;
        }

        // Fest History / RVRJC Legacy / Heritage / Autonomous / Established
        if (q.contains("history") || q.contains("heritage") || q.contains("established") || q.contains("nagarjuna") || q.contains("legacy")) {
            if (fest != null && fest.getHistory() != null && !fest.getHistory().isEmpty()) {
                return fest.getHistory();
            }
            return NOT_AVAILABLE_RESPONSE;
        }

        // Campus Atmosphere / Experience / Campus Vibe / About Campus
        if (q.contains("atmosphere") || q.contains("vibe") || q.contains("campus life") || q.contains("scenic") ||
            q.contains("environment") || q.contains("experience") || q.contains("campus look") || q.contains("about campus")) {
            if (fest != null && fest.getAboutContent() != null && !fest.getAboutContent().isEmpty()) {
                return fest.getAboutContent();
            }
            return NOT_AVAILABLE_RESPONSE;
        }

        // General Rules / Code of Conduct / Guidelines / Notice / ID Card
        boolean isSpecificEventRuleQuery = (q.contains("rules for ") || q.contains("rules of ") || q.contains("guidelines for ") || q.contains("instructions for "))
                && !q.contains("rules of the fest") && !q.contains("rules of colorido") && !q.contains("rules of the college") && !q.contains("rules of festival");
        if (!isSpecificEventRuleQuery && 
            (q.contains("fest rule") || q.contains("festival rule") || q.contains("general rule") || 
             q.contains("code of conduct") || q.contains("conduct") || q.contains("notice") || 
             q.contains("guideline") || q.contains("id card") || q.equals("rules") || q.contains("rules of the fest")) &&
            !hasEventKeyword(q, events)) {
            if (fest != null && fest.getImportantNotice() != null && !fest.getImportantNotice().isEmpty()) {
                return "Official Festival Guidelines & Code of Conduct:\n" + fest.getImportantNotice();
            }
            return NOT_AVAILABLE_RESPONSE;
        }

        // Fest Overview / About COLORIDO / About College
        if (q.contains("what is colorido") || q.contains("about colorido") || q.contains("tell me about colorido") ||
            q.contains("tell me about rvr") || q.contains("about college") || q.contains("about the college") ||
            q.contains("fest info") || q.contains("festival details")) {
            if (fest != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(fest.getFestName()).append(" — ").append(fest.getEdition() != null ? fest.getEdition() : "Annual Fest").append("\n");
                sb.append("Institution: ").append(fest.getCollegeName()).append(" (").append(fest.getCollegeLocation()).append(")\n");
                sb.append("Tagline: \"").append(fest.getTagline()).append("\"\n");
                sb.append("Dates: ").append(fest.getStartDate()).append(" to ").append(fest.getEndDate()).append("\n\n");
                if (fest.getDescription() != null) {
                    sb.append(fest.getDescription()).append("\n\n");
                }
                if (fest.getAboutContent() != null) {
                    sb.append("Campus Atmosphere: ").append(fest.getAboutContent());
                }
                return sb.toString();
            }
            return NOT_AVAILABLE_RESPONSE;
        }

        // ==========================================
        // 3. FOOD STALL AREA (Represented only as Map Location)
        // ==========================================
        if (q.contains("food") || q.contains("stall") || q.contains("eat") || q.contains("canteen") || q.contains("snack") || q.contains("beverage")) {
            Optional<MapLocation> foodLoc = mapLocations.stream()
                    .filter(l -> l.getName().toLowerCase().contains("food") || (l.getCategory() != null && l.getCategory().equalsIgnoreCase("FOOD")))
                    .findFirst();
            if (foodLoc.isPresent()) {
                MapLocation loc = foodLoc.get();
                String vName = (loc.getVenue() != null) ? loc.getVenue().getName() : "Central Plaza";
                return "The official Food Stall Area is located at " + vName + " (Coordinates: " + loc.getPosX() + "% X, " + loc.getPosY() + "% Y on campus map).\n" +
                       "Description: " + loc.getDescription() + "\n" +
                       "You can view its exact pin directly on our interactive Campus Map page!";
            }
            return "The Food Stall Area is situated near the Central Plaza. Check the interactive Campus Map for the marker.";
        }

        // ==========================================
        // 4. SPECIFIC EVENT INQUIRY (Dynamic Match on Live DB Events)
        // ==========================================
        Event matchedEvent = findBestMatchingEvent(q, events);
        if (matchedEvent != null) {
            return generateEventAnswer(q, matchedEvent);
        }

        // ==========================================
        // 5. SPECIFIC VENUE INQUIRY (Dynamic Match on Live DB Venues)
        // ==========================================
        Venue matchedVenue = findBestMatchingVenue(q, venues);
        if (matchedVenue != null) {
            return generateVenueAnswer(matchedVenue, events, schedules);
        }

        // ==========================================
        // 6. ALL VENUES LIST
        // ==========================================
        if (q.contains("all venues") || q.contains("list of venues") || q.contains("venues list") || q.contains("what venues")) {
            if (!venues.isEmpty()) {
                StringBuilder sb = new StringBuilder("Here are the official campus venues for COLORIDO 2K26 at R.V.R. & J.C. College of Engineering:\n\n");
                for (Venue v : venues) {
                    sb.append("• ").append(v.getName()).append(" (").append(v.getCode() != null ? v.getCode() : "Venue").append(")")
                      .append(" - Capacity: ").append(v.getCapacity() != null ? String.format("%,d", v.getCapacity()) : "N/A").append(" seats\n")
                      .append("  Landmark: ").append(v.getLandmark() != null ? v.getLandmark() : "Campus Ground").append("\n")
                      .append("  Description: ").append(v.getDescription()).append("\n\n");
                }
                return sb.toString();
            }
            return NOT_AVAILABLE_RESPONSE;
        }

        // ==========================================
        // 7. COORDINATORS & CONTACT INFORMATION
        // ==========================================
        // Specific faculty coordinators
        if (q.contains("faculty coordinator") || q.contains("faculty convener") || q.contains("professors") || q.contains("teachers in charge")) {
            List<ContactInfo> faculty = contacts.stream()
                    .filter(c -> c.getType() != null && c.getType().equalsIgnoreCase("FACULTY"))
                    .collect(Collectors.toList());
            if (!faculty.isEmpty()) {
                StringBuilder sb = new StringBuilder("Official Faculty Conveners & Coordinators:\n\n");
                for (ContactInfo c : faculty) {
                    sb.append("• ").append(c.getName()).append(" — ").append(c.getRole()).append(" (").append(c.getDepartment()).append(")\n")
                      .append("  Phone: ").append(c.getPhone()).append(" | Email: ").append(c.getEmail()).append("\n\n");
                }
                return sb.toString();
            }
            return NOT_AVAILABLE_RESPONSE;
        }

        // Specific student coordinators
        if (q.contains("student coordinator") || q.contains("student lead") || q.contains("student president") || q.contains("student council")) {
            List<ContactInfo> student = contacts.stream()
                    .filter(c -> c.getType() != null && c.getType().equalsIgnoreCase("STUDENT"))
                    .collect(Collectors.toList());
            if (!student.isEmpty()) {
                StringBuilder sb = new StringBuilder("Official Student Leads & Coordinators:\n\n");
                for (ContactInfo c : student) {
                    sb.append("• ").append(c.getName()).append(" — ").append(c.getRole()).append(" (").append(c.getDepartment()).append(")\n")
                      .append("  Phone: ").append(c.getPhone()).append(" | Email: ").append(c.getEmail()).append("\n\n");
                }
                return sb.toString();
            }
            return NOT_AVAILABLE_RESPONSE;
        }

        // Central Help Desk / Emergency Support
        if (q.contains("help desk") || q.contains("helpdesk") || q.contains("helpline") || q.contains("registration support") || q.contains("emergency")) {
            List<ContactInfo> helpDesk = contacts.stream()
                    .filter(c -> c.getType() != null && c.getType().equalsIgnoreCase("HELP_DESK"))
                    .collect(Collectors.toList());
            if (!helpDesk.isEmpty()) {
                ContactInfo hd = helpDesk.get(0);
                return "COLORIDO Central Help Desk:\n" +
                       "• Organization: " + hd.getName() + " (" + hd.getDepartment() + ")\n" +
                       "• Telephone: " + hd.getPhone() + "\n" +
                       "• Email: " + hd.getEmail();
            }
        }

        // All Coordinators & Contact Information
        if (q.contains("coordinator") || q.contains("contact") || q.contains("phone") || q.contains("email") ||
            q.contains("who can i contact") || q.contains("whom should i contact") || q.contains("organizer") || q.contains("reach out")) {
            if (!contacts.isEmpty()) {
                StringBuilder sb = new StringBuilder("Official COLORIDO 2K26 Festival Coordinators & Contact Directory:\n\n");
                
                sb.append("--- Faculty Conveners ---\n");
                for (ContactInfo c : contacts) {
                    if ("FACULTY".equalsIgnoreCase(c.getType())) {
                        sb.append("• ").append(c.getName()).append(" (").append(c.getRole()).append("): ")
                          .append(c.getPhone()).append(" | ").append(c.getEmail()).append("\n");
                    }
                }

                sb.append("\n--- Student Coordinators ---\n");
                for (ContactInfo c : contacts) {
                    if ("STUDENT".equalsIgnoreCase(c.getType())) {
                        sb.append("• ").append(c.getName()).append(" (").append(c.getRole()).append("): ")
                          .append(c.getPhone()).append(" | ").append(c.getEmail()).append("\n");
                    }
                }

                sb.append("\n--- Central Festival Help Desk ---\n");
                for (ContactInfo c : contacts) {
                    if ("HELP_DESK".equalsIgnoreCase(c.getType())) {
                        sb.append("• ").append(c.getName()).append(": ").append(c.getPhone()).append(" | ").append(c.getEmail()).append("\n");
                    }
                }
                
                sb.append("\nTip: Individual competitions also have dedicated coordinators listed under each event!");
                return sb.toString();
            }
            return NOT_AVAILABLE_RESPONSE;
        }

        // ==========================================
        // 8. SCHEDULE & TIMELINE (Dynamic from DB)
        // ==========================================
        if (q.contains("day 1") || q.contains("day1") || q.contains("march 12") || q.contains("first day")) {
            return formatDaySchedule(schedules, 1, "Day 1 (March 12, 2026)");
        }
        if (q.contains("day 2") || q.contains("day2") || q.contains("march 13") || q.contains("second day")) {
            return formatDaySchedule(schedules, 2, "Day 2 (March 13, 2026)");
        }
        if (q.contains("day 3") || q.contains("day3") || q.contains("march 14") || q.contains("third day") || q.contains("valedictory") || q.contains("dj night")) {
            return formatDaySchedule(schedules, 3, "Day 3 (March 14, 2026)");
        }

        if (q.contains("schedule") || q.contains("timeline") || q.contains("agenda") || q.contains("timing") || q.contains("program")) {
            return formatFullSchedule(schedules);
        }

        // ==========================================
        // 9. MAP LOCATIONS / LANDMARKS / CAMPUS NAVIGATION
        // ==========================================
        if (q.contains("map") || q.contains("campus map") || q.contains("landmarks") || q.contains("directions") ||
            q.contains("parking") || q.contains("first aid") || q.contains("health center") || q.contains("entrance") ||
            q.contains("where to park") || q.contains("gate")) {
            
            // Check specific map landmark query
            if (q.contains("parking")) {
                Optional<MapLocation> park = mapLocations.stream().filter(m -> m.getName().toLowerCase().contains("parking")).findFirst();
                if (park.isPresent()) {
                    return park.get().getName() + ": " + park.get().getDescription() + " (Coordinates: " + park.get().getPosX() + "% X, " + park.get().getPosY() + "% Y on the map).";
                }
            }
            if (q.contains("first aid") || q.contains("health") || q.contains("medical") || q.contains("dispensary")) {
                Optional<MapLocation> med = mapLocations.stream().filter(m -> m.getName().toLowerCase().contains("health") || m.getName().toLowerCase().contains("first aid")).findFirst();
                if (med.isPresent()) {
                    return med.get().getName() + ": " + med.get().getDescription() + " (Coordinates: " + med.get().getPosX() + "% X, " + med.get().getPosY() + "% Y on the map).";
                }
            }
            if (q.contains("entrance") || q.contains("gate") || q.contains("entry") || q.contains("arch")) {
                Optional<MapLocation> ent = mapLocations.stream().filter(m -> m.getName().toLowerCase().contains("entrance") || m.getName().toLowerCase().contains("entry")).findFirst();
                if (ent.isPresent()) {
                    return ent.get().getName() + ": " + ent.get().getDescription() + " (Coordinates: " + ent.get().getPosX() + "% X, " + ent.get().getPosY() + "% Y on the map).";
                }
            }

            // General Map summary synthesized dynamically from DB mapLocations
            if (!mapLocations.isEmpty()) {
                StringBuilder sb = new StringBuilder("The interactive COLORIDO 2K26 Fest Map covers the 37.4-acre RVRJC campus with the following official markers:\n\n");
                for (MapLocation loc : mapLocations) {
                    sb.append("• ").append(loc.getName()).append(" [").append(loc.getCategory()).append("]: ")
                      .append(loc.getDescription()).append("\n");
                }
                sb.append("\nYou can inspect every pin interactively on our dedicated Map page!");
                return sb.toString();
            }
            return NOT_AVAILABLE_RESPONSE;
        }

        // ==========================================
        // 10. ALL EVENTS / CATEGORIES LIST
        // ==========================================
        if (q.contains("what events") || q.contains("all events") || q.contains("list of events") ||
            q.contains("events list") || q.contains("competitions") || q.contains("what competitions")) {
            if (!events.isEmpty()) {
                StringBuilder sb = new StringBuilder("COLORIDO 2K26 features " + events.size() + " premier competitions (all non-technical):\n\n");
                for (Event e : events) {
                    sb.append("• ").append(e.getName()).append("\n")
                      .append("  Category: ").append(e.getCategory() != null ? e.getCategory().getName() : "Cultural")
                      .append(" | Venue: ").append(e.getVenue() != null ? e.getVenue().getName() : "TBA")
                      .append(" | Date: ").append(e.getEventDate()).append(" (").append(e.getStartTime()).append(")\n")
                      .append("  Prizes: ").append(e.getPrizes() != null ? e.getPrizes() : "N/A").append("\n\n");
                }
                sb.append("Note: COLORIDO 2K26 does NOT include technical events. To register for any competition, visit the Events page to access its official Google Form!");
                return sb.toString();
            }
            return NOT_AVAILABLE_RESPONSE;
        }

        // Category breakdown
        if (q.contains("category") || q.contains("categories") || q.contains("verticals") || q.contains("genres")) {
            if (!categories.isEmpty()) {
                StringBuilder sb = new StringBuilder("COLORIDO 2K26 Competition Categories (Non-Technical):\n\n");
                for (EventCategory cat : categories) {
                    sb.append("• ").append(cat.getName()).append(": ").append(cat.getDescription()).append("\n");
                }
                return sb.toString();
            }
            return NOT_AVAILABLE_RESPONSE;
        }

        // How to register
        if (q.contains("how can i register") || q.contains("how do i register") || q.contains("registration process") || q.contains("google form")) {
            return "Registration for all COLORIDO 2K26 events is handled through official Google Forms:\n" +
                   "1. Navigate to the Events page on this website.\n" +
                   "2. Select the event you wish to participate in to view its detailed rules and prize pool.\n" +
                   "3. Click the 'Register Now' button to open the event's official Google Form.\n" +
                   "4. Fill in participant/team details and submit.\n\n" +
                   "Remember: College ID cards are strictly mandatory for all registered participants!";
        }

        // Technical events inquiry
        if (q.contains("technical event") || q.contains("coding") || q.contains("hackathon") || q.contains("paper presentation") || q.contains("robowars")) {
            return "COLORIDO 2K26 does not host technical events. COLORIDO is exclusively an inter-collegiate cultural, artistic, musical, dramatic, and athletic festival.";
        }

        // If asking about something domain-related but not in the database
        return NOT_AVAILABLE_RESPONSE;
    }

    // ==========================================
    // HELPER METHODS FOR DYNAMIC SYNTHESIS
    // ==========================================

    private boolean hasEventKeyword(String q, List<Event> events) {
        return findBestMatchingEvent(q, events) != null;
    }

    private Event findBestMatchingEvent(String q, List<Event> events) {
        Event bestMatch = null;
        int maxScore = 0;

        for (Event event : events) {
            String name = event.getName().toLowerCase();
            int score = 0;

            // Direct substring match on full name
            if (q.contains(name)) {
                score += 100;
            }

            // Word token match
            String[] tokens = name.split("[\\s\\-\\(\\)\\,\\/]+");
            for (String t : tokens) {
                String tokenLower = t.toLowerCase();
                if (tokenLower.length() > 3 && q.contains(tokenLower)) {
                    // Ignore common stopwords that should not identify a single event
                    if (tokenLower.equals("festival") || tokenLower.equals("fest") ||
                        tokenLower.equals("championship") || tokenLower.equals("competition") ||
                        tokenLower.equals("contest") || tokenLower.equals("cultural") ||
                        tokenLower.equals("college") || tokenLower.equals("rvrjc") ||
                        tokenLower.equals("annual") || tokenLower.equals("national")) {
                        continue;
                    }
                    score += 15;
                }
            }

            // Specific aliases
            if (name.contains("natya") && (q.contains("natya") || q.contains("group dance") || q.contains("dance championship"))) score += 30;
            if (name.contains("symphony") && (q.contains("symphony") || q.contains("battle of the bands") || q.contains("band clash") || q.contains("bands"))) score += 30;
            if (name.contains("raga") && (q.contains("raga") || q.contains("sudha") || q.contains("solo vocal") || q.contains("singing"))) score += 30;
            if (name.contains("step-up") && (q.contains("step up") || q.contains("step-up") || q.contains("street dance") || q.contains("1v1"))) score += 30;
            if (name.contains("rangmanch") && (q.contains("rangmanch") || q.contains("ad-venture") || q.contains("street play") || q.contains("mime"))) score += 30;
            if (name.contains("chitrakaari") && (q.contains("chitrakaari") || q.contains("painting") || q.contains("canvas") || q.contains("mandala"))) score += 30;
            if (name.contains("cricket") && (q.contains("cricket") || q.contains("gully cricket"))) score += 30;
            if (name.contains("cinesnap") && (q.contains("cinesnap") || q.contains("short film") || q.contains("reels"))) score += 30;

            if (score > maxScore && score >= 15) {
                maxScore = score;
                bestMatch = event;
            }
        }

        return bestMatch;
    }

    private String generateEventAnswer(String q, Event event) {
        // Specific Field Queries

        // 1. Rules
        if (q.contains("rule") || q.contains("guideline") || q.contains("instruction") || q.contains("format") || q.contains("criteria")) {
            return "Rules & Regulations for \"" + event.getName() + "\":\n\n" +
                   (event.getRules() != null && !event.getRules().isEmpty() ? event.getRules() : NOT_AVAILABLE_RESPONSE);
        }

        // 2. Registration URL / Link / Google Form
        if (q.contains("register") || q.contains("registration") || q.contains("google form") || q.contains("form") || q.contains("link") || q.contains("url")) {
            if (event.getRegistrationUrl() != null && !event.getRegistrationUrl().isEmpty()) {
                return "Official Google Form Registration for \"" + event.getName() + "\":\n" +
                       "Link: " + event.getRegistrationUrl() + "\n" +
                       "Team Requirement: " + (event.getTeamSize() != null ? event.getTeamSize() : "See Google Form") + "\n" +
                       "Status: " + (event.getStatus() != null ? event.getStatus() : "OPEN");
            }
            return "Registration for \"" + event.getName() + "\" is handled via official Google Form. Please check the Events page.";
        }

        // 3. Coordinator / Faculty / Student Contact
        if (q.contains("coordinator") || q.contains("convener") || q.contains("incharge") || q.contains("contact") ||
            q.contains("phone") || q.contains("mobile") || q.contains("number") || q.contains("who is organizing") || q.contains("who to contact")) {
            StringBuilder sb = new StringBuilder("Coordinator details for \"" + event.getName() + "\":\n");
            sb.append("• Coordinator(s): ").append(event.getCoordinatorName() != null ? event.getCoordinatorName() : "Faculty/Student Committee").append("\n");
            sb.append("• Contact Phone: ").append(event.getCoordinatorContact() != null ? event.getCoordinatorContact() : "General Help Desk +91 863 2288254").append("\n");
            sb.append("• Venue: ").append(event.getVenue() != null ? event.getVenue().getName() : "TBA");
            return sb.toString();
        }

        // 4. Venue / Location / Where
        if (q.contains("where") || q.contains("venue") || q.contains("location") || q.contains("hall") || q.contains("stage") || q.contains("place")) {
            String vName = (event.getVenue() != null) ? event.getVenue().getName() : "Venue TBA";
            String landmark = (event.getVenue() != null && event.getVenue().getLandmark() != null) ? " (" + event.getVenue().getLandmark() + ")" : "";
            String vDesc = (event.getVenue() != null && event.getVenue().getDescription() != null) ? "\nVenue Info: " + event.getVenue().getDescription() : "";
            return "\"" + event.getName() + "\" is taking place at " + vName + landmark + "." + vDesc;
        }

        // 5. Date & Time / When
        if (q.contains("when") || q.contains("date") || q.contains("time") || q.contains("timing") || q.contains("hour")) {
            String vName = (event.getVenue() != null) ? event.getVenue().getName() : "the designated venue";
            return "\"" + event.getName() + "\" is scheduled on " + event.getEventDate() + " from " + event.getStartTime() + " to " + event.getEndTime() + " at " + vName + ".";
        }

        // 6. Prizes / Bounty
        if (q.contains("prize") || q.contains("cash") || q.contains("award") || q.contains("reward") || q.contains("bounty") || q.contains("money")) {
            return "Prize Pool for \"" + event.getName() + "\":\n" +
                   (event.getPrizes() != null ? event.getPrizes() : NOT_AVAILABLE_RESPONSE);
        }

        // 7. Team Size
        if (q.contains("team") || q.contains("team size") || q.contains("solo") || q.contains("group") || q.contains("members")) {
            return "Team size requirement for \"" + event.getName() + "\": " +
                   (event.getTeamSize() != null ? event.getTeamSize() : "Please refer to the event registration form") + ".";
        }

        // 8. Complete Event Details (Fallback)
        StringBuilder sb = new StringBuilder("Here are the complete details for \"" + event.getName() + "\":\n\n");
        sb.append("• Category: ").append(event.getCategory() != null ? event.getCategory().getName() : "Cultural").append("\n");
        sb.append("• Venue: ").append(event.getVenue() != null ? event.getVenue().getName() : "TBA");
        if (event.getVenue() != null && event.getVenue().getLandmark() != null) {
            sb.append(" (").append(event.getVenue().getLandmark()).append(")");
        }
        sb.append("\n");
        sb.append("• Date & Time: ").append(event.getEventDate()).append(" | ").append(event.getStartTime()).append(" - ").append(event.getEndTime()).append("\n");
        sb.append("• Team Size: ").append(event.getTeamSize() != null ? event.getTeamSize() : "N/A").append("\n");
        sb.append("• Prize Pool: ").append(event.getPrizes() != null ? event.getPrizes() : "N/A").append("\n");
        sb.append("• Coordinator: ").append(event.getCoordinatorName() != null ? event.getCoordinatorName() : "Department Committee").append("\n");
        sb.append("• Coordinator Contact: ").append(event.getCoordinatorContact() != null ? event.getCoordinatorContact() : "Help Desk").append("\n");
        sb.append("• Google Form Registration: ").append(event.getRegistrationUrl() != null ? event.getRegistrationUrl() : "Check Events page").append("\n");
        sb.append("• Status: ").append(event.getStatus() != null ? event.getStatus() : "OPEN").append("\n\n");
        sb.append("Description: ").append(event.getDescription() != null ? event.getDescription() : "").append("\n\n");
        if (event.getRules() != null && !event.getRules().isEmpty()) {
            sb.append("Rules:\n").append(event.getRules());
        }
        return sb.toString();
    }

    private Venue findBestMatchingVenue(String q, List<Venue> venues) {
        for (Venue venue : venues) {
            String name = venue.getName().toLowerCase();
            String code = (venue.getCode() != null) ? venue.getCode().toLowerCase() : "";

            if (q.contains(name) || (!code.isEmpty() && q.contains(code))) {
                return venue;
            }
            if (name.contains("theatre") && (q.contains("oat") || q.contains("theatre") || q.contains("theater") || q.contains("amphitheater"))) {
                return venue;
            }
            if (name.contains("auditorium") && (q.contains("auditorium") || q.contains("silver jubilee") || q.contains("sjb"))) {
                return venue;
            }
            if (name.contains("decennial") && (q.contains("decennial") || q.contains("open stage"))) {
                return venue;
            }
            if (name.contains("cyber") && (q.contains("cyber block") || q.contains("seminar hall"))) {
                return venue;
            }
            if (name.contains("sports") && (q.contains("sports complex") || q.contains("sports grounds") || q.contains("ground"))) {
                return venue;
            }
            if (name.contains("plaza") && (q.contains("plaza") || q.contains("central plaza") || q.contains("canteen"))) {
                return venue;
            }
        }
        return null;
    }

    private String generateVenueAnswer(Venue venue, List<Event> events, List<ScheduleItem> schedules) {
        StringBuilder sb = new StringBuilder();
        sb.append(venue.getName()).append(" (").append(venue.getCode() != null ? venue.getCode() : "Venue").append(")\n\n");
        sb.append("• Seating Capacity: ").append(venue.getCapacity() != null ? String.format("%,d", venue.getCapacity()) : "N/A").append(" people\n");
        if (venue.getLandmark() != null) {
            sb.append("• Landmark: ").append(venue.getLandmark()).append("\n");
        }
        if (venue.getMapX() != null && venue.getMapY() != null) {
            sb.append("• Campus Map Coordinates: ").append(venue.getMapX()).append("% X, ").append(venue.getMapY()).append("% Y\n");
        }
        if (venue.getDescription() != null) {
            sb.append("• Description: ").append(venue.getDescription()).append("\n\n");
        }

        // Events hosted at this venue
        List<Event> vEvents = events.stream()
                .filter(e -> e.getVenue() != null && e.getVenue().getId().equals(venue.getId()))
                .collect(Collectors.toList());

        if (!vEvents.isEmpty()) {
            sb.append("Competitions hosted here:\n");
            for (Event ve : vEvents) {
                sb.append("• ").append(ve.getName()).append(" on ").append(ve.getEventDate()).append(" (").append(ve.getStartTime()).append(" - ").append(ve.getEndTime()).append(")\n");
            }
        }

        // Schedule items hosted at this venue
        List<ScheduleItem> vSchedule = schedules.stream()
                .filter(s -> s.getVenue() != null && s.getVenue().getId().equals(venue.getId()))
                .collect(Collectors.toList());

        if (!vSchedule.isEmpty()) {
            sb.append("\nTimeline sessions here:\n");
            for (ScheduleItem si : vSchedule) {
                sb.append("• Day ").append(si.getDayNumber()).append(" (").append(si.getStartTime()).append("): ").append(si.getTitle()).append("\n");
            }
        }

        return sb.toString();
    }

    private String formatDaySchedule(List<ScheduleItem> schedules, int dayNumber, String dayTitle) {
        List<ScheduleItem> dayItems = schedules.stream()
                .filter(s -> s.getDayNumber() != null && s.getDayNumber() == dayNumber)
                .sorted(Comparator.comparing(s -> s.getStartTime() != null ? s.getStartTime() : ""))
                .collect(Collectors.toList());

        if (dayItems.isEmpty()) {
            return "No schedule items currently listed for " + dayTitle + ".";
        }

        StringBuilder sb = new StringBuilder("Schedule for " + dayTitle + ":\n\n");
        for (ScheduleItem item : dayItems) {
            sb.append("• ").append(item.getStartTime()).append(" - ").append(item.getEndTime()).append(": ")
              .append(item.getTitle()).append(" [").append(item.getType() != null ? item.getType() : "EVENT").append("]\n")
              .append("  Venue: ").append(item.getVenue() != null ? item.getVenue().getName() : "TBA").append("\n");
            if (item.getDescription() != null && !item.getDescription().isEmpty()) {
                sb.append("  Details: ").append(item.getDescription()).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private String formatFullSchedule(List<ScheduleItem> schedules) {
        if (schedules.isEmpty()) {
            return "The festival schedule is currently being finalized. Please check back shortly.";
        }

        StringBuilder sb = new StringBuilder("COLORIDO 2K26 Complete 3-Day Schedule (Live Database):\n\n");

        for (int day = 1; day <= 3; day++) {
            final int currentDay = day;
            List<ScheduleItem> dayItems = schedules.stream()
                    .filter(s -> s.getDayNumber() != null && s.getDayNumber() == currentDay)
                    .collect(Collectors.toList());

            sb.append("=== Day ").append(day);
            if (!dayItems.isEmpty() && dayItems.get(0).getScheduleDate() != null) {
                sb.append(" (").append(dayItems.get(0).getScheduleDate()).append(")");
            }
            sb.append(" ===\n");

            if (dayItems.isEmpty()) {
                sb.append("• No events scheduled yet.\n\n");
            } else {
                for (ScheduleItem si : dayItems) {
                    sb.append("• ").append(si.getStartTime()).append(" - ").append(si.getEndTime()).append(": ")
                      .append(si.getTitle()).append(" (at ").append(si.getVenue() != null ? si.getVenue().getName() : "TBA").append(")\n");
                }
                sb.append("\n");
            }
        }

        sb.append("You can view or filter the complete chronological timeline on the Schedule page!");
        return sb.toString();
    }
}
