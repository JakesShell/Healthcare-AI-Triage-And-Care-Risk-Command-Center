package com.healthcare.triage.model;

import java.time.LocalDateTime;
import java.util.List;

public class TriageResult {
    private String assessmentId;
    private String possibleConditionCategory;
    private String triageLevel;
    private int riskScore;
    private String confidenceLabel;
    private String recommendedCareType;
    private String carePathway;
    private List<String> reasoning;
    private List<String> redFlags;
    private String googleMapsSearchUrl;
    private String emergencyNotice;
    private String equityNote;
    private LocalDateTime createdAt;

    public String getAssessmentId() { return assessmentId; }
    public void setAssessmentId(String assessmentId) { this.assessmentId = assessmentId; }
    public String getPossibleConditionCategory() { return possibleConditionCategory; }
    public void setPossibleConditionCategory(String possibleConditionCategory) { this.possibleConditionCategory = possibleConditionCategory; }
    public String getTriageLevel() { return triageLevel; }
    public void setTriageLevel(String triageLevel) { this.triageLevel = triageLevel; }
    public int getRiskScore() { return riskScore; }
    public void setRiskScore(int riskScore) { this.riskScore = riskScore; }
    public String getConfidenceLabel() { return confidenceLabel; }
    public void setConfidenceLabel(String confidenceLabel) { this.confidenceLabel = confidenceLabel; }
    public String getRecommendedCareType() { return recommendedCareType; }
    public void setRecommendedCareType(String recommendedCareType) { this.recommendedCareType = recommendedCareType; }
    public String getCarePathway() { return carePathway; }
    public void setCarePathway(String carePathway) { this.carePathway = carePathway; }
    public List<String> getReasoning() { return reasoning; }
    public void setReasoning(List<String> reasoning) { this.reasoning = reasoning; }
    public List<String> getRedFlags() { return redFlags; }
    public void setRedFlags(List<String> redFlags) { this.redFlags = redFlags; }
    public String getGoogleMapsSearchUrl() { return googleMapsSearchUrl; }
    public void setGoogleMapsSearchUrl(String googleMapsSearchUrl) { this.googleMapsSearchUrl = googleMapsSearchUrl; }
    public String getEmergencyNotice() { return emergencyNotice; }
    public void setEmergencyNotice(String emergencyNotice) { this.emergencyNotice = emergencyNotice; }
    public String getEquityNote() { return equityNote; }
    public void setEquityNote(String equityNote) { this.equityNote = equityNote; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
