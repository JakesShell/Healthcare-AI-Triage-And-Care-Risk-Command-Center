package com.healthcare.triage.model;

public class AuditEvent {
    private String eventId;
    private String caseId;
    private String action;
    private String reviewer;
    private String decision;
    private String timestamp;

    public AuditEvent(String eventId, String caseId, String action, String reviewer, String decision, String timestamp) {
        this.eventId = eventId;
        this.caseId = caseId;
        this.action = action;
        this.reviewer = reviewer;
        this.decision = decision;
        this.timestamp = timestamp;
    }

    public String getEventId() { return eventId; }
    public String getCaseId() { return caseId; }
    public String getAction() { return action; }
    public String getReviewer() { return reviewer; }
    public String getDecision() { return decision; }
    public String getTimestamp() { return timestamp; }
}
