package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "penalty_actions")
public class PenaltyAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "integrity_case_id", nullable = false)
    private IntegrityCase integrityCase;

    @Column(nullable = false)
    private String penaltyType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String details;

    @Column(nullable = false)
    private String issuedBy;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    public PenaltyAction() {
        this.issuedAt = LocalDateTime.now();
    }

    public PenaltyAction(IntegrityCase integrityCase, String penaltyType, String details, String issuedBy) {
        this();
        this.integrityCase = integrityCase;
        this.penaltyType = penaltyType;
        this.details = details;
        this.issuedBy = issuedBy;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public IntegrityCase getIntegrityCase() { return integrityCase; }
    public void setIntegrityCase(IntegrityCase integrityCase) { this.integrityCase = integrityCase; }

    public String getPenaltyType() { return penaltyType; }
    public void setPenaltyType(String penaltyType) { this.penaltyType = penaltyType; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getIssuedBy() { return issuedBy; }
    public void setIssuedBy(String issuedBy) { this.issuedBy = issuedBy; }

    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }
}