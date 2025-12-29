package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "repeat_offender_records")
public class RepeatOffenderRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_profile_id", nullable = false)
    private StudentProfile studentProfile;

    @Column(nullable = false)
    private Integer totalCases;

    private LocalDate firstIncidentDate;

    @Column(nullable = false)
    private String flagSeverity;

    public RepeatOffenderRecord() {}

    public RepeatOffenderRecord(StudentProfile studentProfile, Integer totalCases, String flagSeverity) {
        this.studentProfile = studentProfile;
        this.totalCases = totalCases;
        this.flagSeverity = flagSeverity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StudentProfile getStudentProfile() { return studentProfile; }
    public void setStudentProfile(StudentProfile studentProfile) { this.studentProfile = studentProfile; }

    public Integer getTotalCases() { return totalCases; }
    public void setTotalCases(Integer totalCases) { this.totalCases = totalCases; }

    public LocalDate getFirstIncidentDate() { return firstIncidentDate; }
    public void setFirstIncidentDate(LocalDate firstIncidentDate) { this.firstIncidentDate = firstIncidentDate; }

    public String getFlagSeverity() { return flagSeverity; }
    public void setFlagSeverity(String flagSeverity) { this.flagSeverity = flagSeverity; }
}