package com.example.demo.entity;

import java.time.LocalDateTime;

public class IntegrityCase {
    @Id
    private Long id;
    private String CourseCode;
    private String InstructName;
    private String description;
    private String status;
    private LocalDate incidentdate;
    private LocalDateTime createdAt;
}