package com.healthcare.triage.model;

public class PatientRiskCase {
    private String caseId;
    private String patientAlias;
    private int age;
    private String conditionCategory;
    private int readmissionProbability;
    private int riskScore;
    private String triageLevel;
    private String assignedCareTeam;
    private String escalationStatus;
    private int missedFollowUps;
    private int medicationAdherence;

    public PatientRiskCase(String caseId, String patientAlias, int age, String conditionCategory, int readmissionProbability, int riskScore, String triageLevel, String assignedCareTeam, String escalationStatus, int missedFollowUps, int medicationAdherence) {
        this.caseId = caseId;
        this.patientAlias = patientAlias;
        this.age = age;
        this.conditionCategory = conditionCategory;
        this.readmissionProbability = readmissionProbability;
        this.riskScore = riskScore;
        this.triageLevel = triageLevel;
        this.assignedCareTeam = assignedCareTeam;
        this.escalationStatus = escalationStatus;
        this.missedFollowUps = missedFollowUps;
        this.medicationAdherence = medicationAdherence;
    }

    public String getCaseId() { return caseId; }
    public String getPatientAlias() { return patientAlias; }
    public int getAge() { return age; }
    public String getConditionCategory() { return conditionCategory; }
    public int getReadmissionProbability() { return readmissionProbability; }
    public int getRiskScore() { return riskScore; }
    public String getTriageLevel() { return triageLevel; }
    public String getAssignedCareTeam() { return assignedCareTeam; }
    public String getEscalationStatus() { return escalationStatus; }
    public int getMissedFollowUps() { return missedFollowUps; }
    public int getMedicationAdherence() { return medicationAdherence; }
}
