package com.example.demo.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "evidence_records")
public class EvidenceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String evidenceType;
    private String content;
    private String submittedBy;
    private LocalDateTime submittedAt;
    
    public EvidenceRecord() {
    }

    public EvidenceRecord(Long id, String evidenceType, String content, String submittedBy,
            LocalDateTime submittedAt) {
        this.id = id;
        this.evidenceType = evidenceType;
        this.content = content;
        this.submittedBy = submittedBy;
        this.submittedAt = submittedAt;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEvidenceType() {
        return evidenceType;
    }
    public void setEvidenceType(String evidenceType) {
        this.evidenceType = evidenceType;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getSubmittedBy() {
        return submittedBy;
    }
    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}