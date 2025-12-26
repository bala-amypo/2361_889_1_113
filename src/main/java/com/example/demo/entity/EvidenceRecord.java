package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evidence_records")
public class EvidenceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "integrity_case_id", nullable = false)
    private IntegrityCase integrityCase;

    @Column(nullable = false)
    private String evidenceType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String submittedBy;

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    public EvidenceRecord() {
        this.submittedAt = LocalDateTime.now();
    }

    public EvidenceRecord(IntegrityCase integrityCase, String evidenceType, String content, String submittedBy) {
        this();
        this.integrityCase = integrityCase;
        this.evidenceType = evidenceType;
        this.content = content;
        this.submittedBy = submittedBy;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public IntegrityCase getIntegrityCase() { return integrityCase; }
    public void setIntegrityCase(IntegrityCase integrityCase) { this.integrityCase = integrityCase; }

    public String getEvidenceType() { return evidenceType; }
    public void setEvidenceType(String evidenceType) { this.evidenceType = evidenceType; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSubmittedBy() { return submittedBy; }
    public void setSubmittedBy(String submittedBy) { this.submittedBy = submittedBy; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}