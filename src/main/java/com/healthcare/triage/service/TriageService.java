package com.healthcare.triage.service;

import com.healthcare.triage.model.TriageRequest;
import com.healthcare.triage.model.TriageResult;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class TriageService {

    public TriageResult assess(TriageRequest request) {
        List<String> reasoning = new ArrayList<>();
        List<String> redFlags = detectRedFlags(request.getSymptoms());
        String normalizedSymptoms = normalize(request.getSymptoms() + " " + nullSafe(request.getNotes()));

        int score = 10;

        if (request.getAge() > 0 && (request.getAge() < 5 || request.getAge() >= 65)) {
            score += 12;
            reasoning.add("Age is in a higher-priority review group for triage routing.");
        } else if (request.getAge() >= 45) {
            score += 5;
            reasoning.add("Age adds a small triage priority increase.");
        }

        if (request.getFeverC() >= 39.0) {
            score += 18;
            reasoning.add("High fever increased the triage priority.");
        } else if (request.getFeverC() >= 38.0) {
            score += 10;
            reasoning.add("Fever contributed to the possible infection category.");
        }

        if (request.getPainLevel() >= 8) {
            score += 15;
            reasoning.add("Severe pain level increased the escalation score.");
        } else if (request.getPainLevel() >= 5) {
            score += 7;
            reasoning.add("Moderate pain contributed to the care priority.");
        }

        if (request.getSymptomDurationDays() >= 7) {
            score += 8;
            reasoning.add("Symptoms lasting a week or more increased follow-up priority.");
        } else if (request.getSymptomDurationDays() >= 3) {
            score += 4;
            reasoning.add("Several days of symptoms increased monitoring priority.");
        }

        if ("current smoker".equalsIgnoreCase(nullSafe(request.getSmokingStatus()))) {
            score += 7;
            reasoning.add("Current smoking status increased risk for respiratory and recovery concerns.");
        }

        int chronicCount = countListedItems(request.getChronicConditions());
        if (chronicCount > 0) {
            int chronicScore = Math.min(chronicCount * 6, 18);
            score += chronicScore;
            reasoning.add("Medical history added " + chronicScore + " points to the operational risk score.");
        }

        if (request.getMedicationAdherence() > 0 && request.getMedicationAdherence() < 50) {
            score += 12;
            reasoning.add("Low medication adherence increased follow-up risk.");
        } else if (request.getMedicationAdherence() > 0 && request.getMedicationAdherence() < 75) {
            score += 6;
            reasoning.add("Medication adherence is below the preferred monitoring threshold.");
        }

        if (request.getMissedFollowUps() > 0) {
            int missedScore = Math.min(request.getMissedFollowUps() * 4, 16);
            score += missedScore;
            reasoning.add("Missed follow-ups increased care coordination priority.");
        }

        if (request.isPregnantOrPostpartum()) {
            score += 10;
            reasoning.add("Pregnancy or postpartum status triggered a cautious care routing increase.");
        }

        if (request.isRecentTravel()) {
            score += 6;
            reasoning.add("Recent travel added a screening consideration for care routing.");
        }

        if (request.isMultipleRecentPartners()) {
            score += 4;
            reasoning.add("Sexual health risk factors suggest testing or clinician review may be appropriate.");
        }

        if (request.isSexualHealthSymptoms() || containsAny(normalizedSymptoms, "discharge", "burning urination", "pelvic pain", "sti", "std")) {
            score += 12;
            reasoning.add("Sexual health symptoms triggered a confidential sexual health screening pathway.");
        }

        if (!redFlags.isEmpty()) {
            score += 25;
            reasoning.add("Red-flag symptoms were detected and escalated the recommended care pathway.");
        }

        score = Math.min(score, 100);

        String category = classifyCategory(normalizedSymptoms, request);
        String triageLevel = triageLevel(score, redFlags);
        String careType = recommendedCareType(score, redFlags, category);
        String carePathway = carePathway(score, redFlags, category);
        String mapQuery = mapQueryFor(careType, category);

        TriageResult result = new TriageResult();
        result.setAssessmentId("TRI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT));
        result.setPossibleConditionCategory(category);
        result.setTriageLevel(triageLevel);
        result.setRiskScore(score);
        result.setConfidenceLabel(confidenceLabel(score, request));
        result.setRecommendedCareType(careType);
        result.setCarePathway(carePathway);
        result.setReasoning(reasoning.isEmpty() ? List.of("Symptoms were reviewed using the simulated triage rule engine.") : reasoning);
        result.setRedFlags(redFlags);
        result.setGoogleMapsSearchUrl(buildMapsUrl(mapQuery, request.getLatitude(), request.getLongitude()));
        result.setEmergencyNotice(emergencyNotice(redFlags));
        result.setEquityNote("Race and ethnicity are treated as optional equity-audit metadata only and are not used to directly increase the triage score.");
        result.setCreatedAt(LocalDateTime.now());
        return result;
    }

    private List<String> detectRedFlags(String symptoms) {
        String normalized = normalize(symptoms);
        List<String> flags = new ArrayList<>();
        if (containsAny(normalized, "difficulty breathing", "shortness of breath", "can't breathe", "cannot breathe")) {
            flags.add("Breathing difficulty reported");
        }
        if (containsAny(normalized, "chest pain", "pressure in chest")) {
            flags.add("Chest pain or chest pressure reported");
        }
        if (containsAny(normalized, "confusion", "fainting", "passed out")) {
            flags.add("Confusion, fainting, or altered awareness reported");
        }
        if (containsAny(normalized, "stiff neck", "severe headache")) {
            flags.add("Severe headache or stiff neck reported");
        }
        if (containsAny(normalized, "blood in stool", "vomiting blood", "coughing blood")) {
            flags.add("Bleeding symptom reported");
        }
        if (containsAny(normalized, "severe abdominal pain", "severe stomach pain")) {
            flags.add("Severe abdominal pain reported");
        }
        return flags;
    }

    private String classifyCategory(String symptoms, TriageRequest request) {
        if (request.isSexualHealthSymptoms() || request.isMultipleRecentPartners() || containsAny(symptoms, "discharge", "burning urination", "pelvic pain", "sti", "std")) {
            return "Sexual Health / Confidential Testing Concern";
        }
        if (containsAny(symptoms, "chest pain", "pressure in chest", "palpitations")) {
            return "Cardiac / Urgent Review Concern";
        }
        if (containsAny(symptoms, "cough", "sore throat", "shortness of breath", "difficulty breathing", "wheezing", "congestion")) {
            return "Respiratory / Infection Concern";
        }
        if (containsAny(symptoms, "vomiting", "diarrhea", "abdominal", "stomach", "nausea")) {
            return "Gastrointestinal / Hydration Concern";
        }
        if (request.getFeverC() >= 38.0 || containsAny(symptoms, "fever", "chills", "body aches")) {
            return "General Infection / Fever Concern";
        }
        if (containsAny(symptoms, "rash", "itching", "swelling")) {
            return "Skin / Allergy Review Concern";
        }
        return "General Care Review";
    }

    private String triageLevel(int score, List<String> redFlags) {
        if (!redFlags.isEmpty() || score >= 80) return "Critical Review";
        if (score >= 60) return "High Priority";
        if (score >= 35) return "Moderate Priority";
        return "Routine Review";
    }

    private String recommendedCareType(int score, List<String> redFlags, String category) {
        if (!redFlags.isEmpty() || score >= 80) return "Hospital / Emergency Department";
        if (category.startsWith("Sexual Health")) return "Sexual Health Clinic / General Practitioner";
        if (score >= 60) return "Urgent Care / Same-Day Clinic";
        if (score >= 35) return "General Practitioner / Clinic";
        return "Routine Clinic / Telehealth Review";
    }

    private String carePathway(int score, List<String> redFlags, String category) {
        if (!redFlags.isEmpty() || score >= 80) {
            return "Seek urgent medical review. The system is routing this case toward emergency or hospital-based care options.";
        }
        if (category.startsWith("Sexual Health")) {
            return "Arrange confidential testing or clinician review. A sexual health clinic or general practitioner is recommended.";
        }
        if (score >= 60) {
            return "Same-day clinical review is recommended based on the simulated triage score.";
        }
        if (score >= 35) {
            return "Book a clinic or GP review and monitor symptoms carefully.";
        }
        return "Routine follow-up or telehealth review may be appropriate if symptoms continue or worsen.";
    }

    private String mapQueryFor(String careType, String category) {
        if (careType.contains("Emergency") || careType.contains("Hospital")) return "hospital near me";
        if (category.startsWith("Sexual Health")) return "sexual health clinic near me";
        if (careType.contains("Urgent")) return "urgent care clinic near me";
        if (careType.contains("General Practitioner")) return "general practitioner clinic near me";
        return "clinic near me";
    }

    private String buildMapsUrl(String query, Double latitude, Double longitude) {
        String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
        if (latitude != null && longitude != null) {
            return "https://www.google.com/maps/search/" + encoded + "/@" + latitude + "," + longitude + ",14z";
        }
        return "https://www.google.com/maps/search/" + encoded;
    }

    private String confidenceLabel(int score, TriageRequest request) {
        int dataPoints = 0;
        if (request.getSymptoms() != null && !request.getSymptoms().isBlank()) dataPoints++;
        if (request.getAge() > 0) dataPoints++;
        if (request.getFeverC() > 0) dataPoints++;
        if (request.getSymptomDurationDays() > 0) dataPoints++;
        if (request.getPainLevel() > 0) dataPoints++;
        if (request.getSmokingStatus() != null && !request.getSmokingStatus().isBlank()) dataPoints++;
        if (request.getChronicConditions() != null && !request.getChronicConditions().isBlank()) dataPoints++;
        if (dataPoints >= 6) return "High Data Completeness";
        if (dataPoints >= 4) return "Moderate Data Completeness";
        return "Limited Data Completeness";
    }

    private String emergencyNotice(List<String> redFlags) {
        if (redFlags.isEmpty()) {
            return "This is a simulated triage result, not a medical diagnosis. Contact a qualified healthcare professional for medical advice.";
        }
        return "Red-flag symptoms were detected. Consider urgent medical help immediately. This tool cannot diagnose or replace emergency care.";
    }

    private boolean containsAny(String text, String... terms) {
        for (String term : terms) {
            if (text.contains(term)) return true;
        }
        return false;
    }

    private String normalize(String value) {
        return nullSafe(value).toLowerCase(Locale.ROOT);
    }

    private String nullSafe(String value) {
        return value == null ? "" : value;
    }

    private int countListedItems(String value) {
        if (value == null || value.isBlank()) return 0;
        String[] items = value.split("[,;]");
        int count = 0;
        for (String item : items) {
            if (!item.trim().isBlank()) count++;
        }
        return count;
    }
}
