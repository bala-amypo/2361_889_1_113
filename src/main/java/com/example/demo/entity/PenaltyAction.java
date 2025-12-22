package com.example.demo.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "penalty_actions")
public class PenaltyAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String penaltyType;
    private String details;
    private String issuedBy;
    private LocalDateTime issuedAt;

    public PenaltyAction() {
    }

    public PenaltyAction(Long id, String penaltyType, String details, String issuedBy, LocalDateTime issuedAt) {
        this.id = id;
        this.penaltyType = penaltyType;
        this.details = details;
        this.issuedBy = issuedBy;
        this.issuedAt = issuedAt;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPenaltyType() {
        return penaltyType;
    }
    public void setPenaltyType(String penaltyType) {
        this.penaltyType = penaltyType;
    }
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }
    public String getIssuedBy() {
        return issuedBy;
    }
    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }
    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }
    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }
}