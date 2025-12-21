package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.IntegrityCase;

public interface IntegrityCaseService {

    IntegrityCase createCase(IntegrityCase integrityCase);

    IntegrityCase updateCaseStatus(Long caseId, String newStatus);

    List<IntegrityCase> getCasesByStudent(Long studentId);

    Optional<IntegrityCase> getCaseById(Long caseId);
}
