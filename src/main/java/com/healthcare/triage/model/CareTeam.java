package com.healthcare.triage.model;

public class CareTeam {
    private String teamName;
    private int assignedCases;
    private int highRiskCases;
    private int overdueReviews;
    private int capacityPressure;
    private String recommendation;

    public CareTeam(String teamName, int assignedCases, int highRiskCases, int overdueReviews, int capacityPressure, String recommendation) {
        this.teamName = teamName;
        this.assignedCases = assignedCases;
        this.highRiskCases = highRiskCases;
        this.overdueReviews = overdueReviews;
        this.capacityPressure = capacityPressure;
        this.recommendation = recommendation;
    }

    public String getTeamName() { return teamName; }
    public int getAssignedCases() { return assignedCases; }
    public int getHighRiskCases() { return highRiskCases; }
    public int getOverdueReviews() { return overdueReviews; }
    public int getCapacityPressure() { return capacityPressure; }
    public String getRecommendation() { return recommendation; }
}
