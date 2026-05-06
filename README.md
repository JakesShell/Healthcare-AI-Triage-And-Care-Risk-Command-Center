# Healthcare AI Triage And Care Risk Command Center

A Spring Boot healthcare operations dashboard that simulates AI-assisted symptom intake, triage interpretation, patient risk queueing, care escalation, nearby healthcare referral, business impact analysis, and governance audit tracking.

> This is a portfolio and educational simulation. It does not provide medical diagnosis, treatment, or medical advice.

## Project Objective

The original project was a small healthcare risk assessment demo. This upgraded version turns it into a more realistic internal healthcare operations system that helps a care team answer:

- Which cases need review first?
- What symptoms or operational risk factors increased urgency?
- What care pathway should be recommended?
- Should the user be routed to a clinic, urgent care, hospital, or confidential sexual health service?
- What is the operational cost of missed follow-ups and delayed reviews?
- What decision-support events should be visible in an audit trail?

## Key Features

- Symptom Intake Form With Selectable Symptom Chips
- Age, Fever, Duration, Pain Level, Smoking Status, Medical History, Medication Adherence, And Missed Follow-Up Inputs
- Sexual Health Screening Pathway With Respectful Care Routing
- Optional Race / Ethnicity Field For Equity Audit Context Only
- Simulated AI-Style Triage Scoring Engine
- Possible Condition Category Output Instead Of Final Diagnosis Claims
- Red-Flag Escalation Detection
- Recommended Care Pathway
- Browser Location Capture With Consent
- Google Maps Nearby Care Referral Link
- Patient Risk Queue
- Care Team Capacity Monitor
- Business Impact Simulator
- Governance Audit Trail
- Polished Dark Medical Command-Center UI
- Unit Tests For Triage Escalation Logic

## Tech Stack

- Java 17
- Spring Boot
- Spring MVC
- Thymeleaf
- Vanilla JavaScript
- CSS
- Maven
- JUnit 5

## Run Locally

```powershell
mvn clean test
mvn spring-boot:run
```

Open:

```text
http://localhost:8080
```

## Suggested Screenshots

Save screenshots in the `screenshots` folder:

```text
screenshots/executive-care-risk-overview.png
screenshots/symptom-intake-triage-form.png
screenshots/triage-result-care-referral.png
screenshots/patient-risk-queue.png
screenshots/care-team-capacity-monitor.png
screenshots/business-impact-simulator.png
screenshots/governance-audit-trail.png
```

## Safety Boundary

This application intentionally uses language such as "possible condition category," "triage interpretation," and "recommended care pathway." It does not claim to diagnose patients. The project demonstrates responsible AI product design by adding safety notices, red-flag escalation, clinician-review language, and equity-audit boundaries.

## Real-World Relevance

Healthcare teams often need to prioritize large queues of cases, identify patients who may require urgent review, route people to the right type of care, and maintain a record of AI-assisted decision-support events. This project models those workflows in a portfolio-safe, fictional-data environment.

## Future Improvements

- Add Authentication And Role-Based Views
- Store Triage Events In PostgreSQL
- Add Provider Directory API Integration
- Add PDF Export For Case Review Summaries
- Add Admin-Controlled Triage Thresholds
- Add Human Reviewer Approval Workflow
- Add Deployment As A Dockerized Healthcare Operations Service
