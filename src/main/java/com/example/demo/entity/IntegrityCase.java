package com.example.demoentity;

import java.time.LocalDateTime;

public class IntegrityCase {

    private Long id;
    private String caseNumber;
    private String title;
    private String description;
    private String severity;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime resolvedDate;

    public IntegrityCase() {
        this.createdDate = LocalDateTime.now();
        this.status = "OPEN";
    }

    public IntegrityCase(Long id, String caseNumber, String title, String severity) {
        this.id = id;
        this.caseNumber = caseNumber;
        this.title = title;
        this.severity = severity;
        this.status = "OPEN";
        this.createdDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(LocalDateTime resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

    @Override
    public String toString() {
        return "IntegrityCase{" +"id=" + id +", caseNumber='" + caseNumber + '\'' +", title='" + title + '\'' +", severity='" + severity + '\'' +", status='" + status + '\'' +'}';
    }
}