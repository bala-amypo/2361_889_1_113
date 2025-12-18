package com.example.demo.entity;

public class StudentProfile{
    @Id;
    private Long id;
    private String studentId;
    private String name;
    private String email;
    private String program;
    private int yearLevel;
    private boolean isRepeatOffender;
    private LocalDateTime createdAt;
}