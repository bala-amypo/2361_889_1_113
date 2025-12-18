package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.Id;
public class IntegrityCase {
    
    @Id
    private Long id;
    private String CourseCode;
    private String InstructName;
    private String description;
    private String status;
    private LocalDate incidentdate;
    private LocalDateTime createdAt;
    public IntegrityCase(Long id, String courseCode, String instructName, String description, String status,
            LocalDate incidentdate, LocalDateTime createdAt) {
        this.id = id;
        CourseCode = courseCode;
        InstructName = instructName;
        this.description = description;
        this.status = status;
        this.incidentdate = incidentdate;
        this.createdAt = createdAt;
    }
    public IntegrityCase() {
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCourseCode() {
        return CourseCode;
    }
    public void setCourseCode(String courseCode) {
        CourseCode = courseCode;
    }
    public String getInstructName() {
        return InstructName;
    }
    public void setInstructName(String instructName) {
        InstructName = instructName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDate getIncidentdate() {
        return incidentdate;
    }
    public void setIncidentdate(LocalDate incidentdate) {
        this.incidentdate = incidentdate;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
