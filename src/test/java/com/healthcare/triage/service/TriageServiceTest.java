package com.healthcare.triage.service;

import com.healthcare.triage.model.TriageRequest;
import com.healthcare.triage.model.TriageResult;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TriageServiceTest {

    private final TriageService triageService = new TriageService();

    @Test
    void redFlagSymptomsEscalateToHospitalReferral() {
        TriageRequest request = new TriageRequest();
        request.setSymptoms("fever, chest pain, difficulty breathing");
        request.setAge(67);
        request.setFeverC(39.2);
        request.setPainLevel(8);
        request.setMedicationAdherence(70);

        TriageResult result = triageService.assess(request);

        assertThat(result.getTriageLevel()).isEqualTo("Critical Review");
        assertThat(result.getRecommendedCareType()).contains("Hospital");
        assertThat(result.getRedFlags()).isNotEmpty();
    }

    @Test
    void sexualHealthInputsRouteToConfidentialCarePathway() {
        TriageRequest request = new TriageRequest();
        request.setSymptoms("burning urination and pelvic pain");
        request.setAge(28);
        request.setFeverC(37.1);
        request.setPainLevel(4);
        request.setMedicationAdherence(100);
        request.setMultipleRecentPartners(true);
        request.setSexualHealthSymptoms(true);

        TriageResult result = triageService.assess(request);

        assertThat(result.getPossibleConditionCategory()).contains("Sexual Health");
        assertThat(result.getRecommendedCareType()).contains("Sexual Health Clinic");
    }
}
