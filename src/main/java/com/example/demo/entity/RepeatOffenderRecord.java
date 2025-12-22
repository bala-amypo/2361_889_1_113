package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "repeat_offender_records")
public class RepeatOffenderRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalCases;
    private String lastincidentDate; 
    private String flagSeverity;

    public RepeatOffenderRecord() {
    }

    public RepeatOffenderRecord(Long id, int totalCases, String lastincidentDate, String flagSeverity) {
        this.id = id;
        this.totalCases = totalCases;
        this.lastincidentDate = lastincidentDate;
        this.flagSeverity = flagSeverity;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getTotalCases() {
        return totalCases;
    }
    public void setTotalCases(int totalCases) {
        this.totalCases = totalCases;
    }
    public String getLastincidentDate() {
        return lastincidentDate;
    }
    public void setLastincidentDate(String lastincidentDate) {
        this.lastincidentDate = lastincidentDate;
    }
    public String getFlagSeverity() {
        return flagSeverity;
    }
    public void setFlagSeverity(String flagSeverity) {
        this.flagSeverity = flagSeverity;
    }
}