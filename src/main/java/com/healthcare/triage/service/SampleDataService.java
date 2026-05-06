package com.healthcare.triage.service;

import com.healthcare.triage.model.AuditEvent;
import com.healthcare.triage.model.CareTeam;
import com.healthcare.triage.model.PatientRiskCase;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SampleDataService {
    public Map<String, Object> getDashboardData() {
        List<PatientRiskCase> cases = List.of(
                new PatientRiskCase("CASE-1048", "Patient Alpha", 68, "Cardiac Follow-Up", 78, 86, "Critical Review", "Cardiac Care Team", "Doctor Review", 3, 54),
                new PatientRiskCase("CASE-1052", "Patient Bravo", 43, "Respiratory Review", 52, 71, "High Priority", "Respiratory Team", "Same-Day Clinic", 1, 69),
                new PatientRiskCase("CASE-1061", "Patient Cedar", 29, "Sexual Health Screening", 24, 58, "Moderate Priority", "Community Care", "Confidential Testing", 0, 92),
                new PatientRiskCase("CASE-1075", "Patient Delta", 74, "Diabetes Management", 66, 79, "High Priority", "Chronic Care Team", "Care Coordinator", 4, 43),
                new PatientRiskCase("CASE-1089", "Patient Ember", 36, "General Infection", 31, 46, "Moderate Priority", "Primary Care", "GP Review", 1, 84),
                new PatientRiskCase("CASE-1093", "Patient Flint", 59, "Post-Discharge Review", 69, 81, "Critical Review", "Discharge Review Team", "Doctor Review", 2, 61)
        );

        List<CareTeam> teams = List.of(
                new CareTeam("Cardiac Care Team", 42, 12, 5, 88, "Redistribute 4 cases and prioritize same-day review."),
                new CareTeam("Chronic Care Team", 57, 16, 9, 94, "Capacity pressure is critical. Add coordinator support."),
                new CareTeam("Respiratory Team", 33, 7, 2, 69, "Monitor high-priority fever cases."),
                new CareTeam("Community Care", 28, 4, 1, 54, "Stable capacity. Accept redirected follow-ups."),
                new CareTeam("Primary Care", 39, 6, 3, 72, "Review moderate-risk queue within 48 hours.")
        );

        List<AuditEvent> audit = List.of(
                new AuditEvent("AUD-8812", "CASE-1048", "Risk score recalculated", "System", "Escalated to doctor review", "2026-05-06 09:15"),
                new AuditEvent("AUD-8819", "CASE-1061", "Sexual health pathway triggered", "System", "Confidential testing recommended", "2026-05-06 09:31"),
                new AuditEvent("AUD-8830", "CASE-1075", "Care team capacity review", "Care Ops Lead", "Coordinator outreach assigned", "2026-05-06 10:12"),
                new AuditEvent("AUD-8841", "CASE-1093", "Readmission risk review", "Clinical Reviewer", "Post-discharge review required", "2026-05-06 10:47")
        );

        int highRiskCases = (int) cases.stream().filter(c -> c.getRiskScore() >= 70).count();
        int overdueReviews = cases.stream().mapToInt(PatientRiskCase::getMissedFollowUps).sum();
        int avgRisk = (int) Math.round(cases.stream().mapToInt(PatientRiskCase::getRiskScore).average().orElse(0));
        int monthlyRiskExposure = highRiskCases * 16200 + overdueReviews * 2100;

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalCases", cases.size());
        summary.put("highRiskCases", highRiskCases);
        summary.put("overdueReviews", overdueReviews);
        summary.put("averageRiskScore", avgRisk);
        summary.put("estimatedMonthlyRiskExposure", monthlyRiskExposure);
        summary.put("executiveSummary", "The highest operational pressure is concentrated in chronic care and cardiac follow-up. Critical cases require doctor review, while the community care team has capacity to absorb lower-risk follow-ups.");

        Map<String, Object> businessImpact = new LinkedHashMap<>();
        businessImpact.put("avoidableReadmissionExposure", "$" + monthlyRiskExposure);
        businessImpact.put("careTeamsOverCapacity", 2);
        businessImpact.put("casesNeeding48HourReview", highRiskCases + 2);
        businessImpact.put("estimatedAdminHours", overdueReviews * 3);

        return Map.of(
                "summary", summary,
                "cases", cases,
                "teams", teams,
                "audit", audit,
                "businessImpact", businessImpact
        );
    }
}
