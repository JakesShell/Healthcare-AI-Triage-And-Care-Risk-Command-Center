package com.healthcare.triage.controller;

import com.healthcare.triage.model.TriageRequest;
import com.healthcare.triage.model.TriageResult;
import com.healthcare.triage.service.SampleDataService;
import com.healthcare.triage.service.TriageService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class RiskApiController {
    private final TriageService triageService;
    private final SampleDataService sampleDataService;

    public RiskApiController(TriageService triageService, SampleDataService sampleDataService) {
        this.triageService = triageService;
        this.sampleDataService = sampleDataService;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return sampleDataService.getDashboardData();
    }

    @PostMapping("/triage")
    public TriageResult assess(@Valid @RequestBody TriageRequest request) {
        return triageService.assess(request);
    }
}
