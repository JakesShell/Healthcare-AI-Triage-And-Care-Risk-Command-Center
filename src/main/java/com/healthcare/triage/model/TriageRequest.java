package com.healthcare.triage.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class TriageRequest {
    @NotBlank(message = "Symptoms are required for the triage simulation.")
    private String symptoms;

    @Min(0)
    @Max(120)
    private int age;

    private String raceEthnicity;

    @Min(0)
    @Max(45)
    private double feverC;

    @Min(0)
    @Max(365)
    private int symptomDurationDays;

    @Min(0)
    @Max(10)
    private int painLevel;

    private String smokingStatus;
    private boolean multipleRecentPartners;
    private boolean sexualHealthSymptoms;
    private boolean pregnantOrPostpartum;
    private String chronicConditions;

    @Min(0)
    @Max(100)
    private int medicationAdherence;

    @Min(0)
    @Max(30)
    private int missedFollowUps;

    private boolean recentTravel;
    private String notes;
    private Double latitude;
    private Double longitude;

    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getRaceEthnicity() { return raceEthnicity; }
    public void setRaceEthnicity(String raceEthnicity) { this.raceEthnicity = raceEthnicity; }
    public double getFeverC() { return feverC; }
    public void setFeverC(double feverC) { this.feverC = feverC; }
    public int getSymptomDurationDays() { return symptomDurationDays; }
    public void setSymptomDurationDays(int symptomDurationDays) { this.symptomDurationDays = symptomDurationDays; }
    public int getPainLevel() { return painLevel; }
    public void setPainLevel(int painLevel) { this.painLevel = painLevel; }
    public String getSmokingStatus() { return smokingStatus; }
    public void setSmokingStatus(String smokingStatus) { this.smokingStatus = smokingStatus; }
    public boolean isMultipleRecentPartners() { return multipleRecentPartners; }
    public void setMultipleRecentPartners(boolean multipleRecentPartners) { this.multipleRecentPartners = multipleRecentPartners; }
    public boolean isSexualHealthSymptoms() { return sexualHealthSymptoms; }
    public void setSexualHealthSymptoms(boolean sexualHealthSymptoms) { this.sexualHealthSymptoms = sexualHealthSymptoms; }
    public boolean isPregnantOrPostpartum() { return pregnantOrPostpartum; }
    public void setPregnantOrPostpartum(boolean pregnantOrPostpartum) { this.pregnantOrPostpartum = pregnantOrPostpartum; }
    public String getChronicConditions() { return chronicConditions; }
    public void setChronicConditions(String chronicConditions) { this.chronicConditions = chronicConditions; }
    public int getMedicationAdherence() { return medicationAdherence; }
    public void setMedicationAdherence(int medicationAdherence) { this.medicationAdherence = medicationAdherence; }
    public int getMissedFollowUps() { return missedFollowUps; }
    public void setMissedFollowUps(int missedFollowUps) { this.missedFollowUps = missedFollowUps; }
    public boolean isRecentTravel() { return recentTravel; }
    public void setRecentTravel(boolean recentTravel) { this.recentTravel = recentTravel; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}
