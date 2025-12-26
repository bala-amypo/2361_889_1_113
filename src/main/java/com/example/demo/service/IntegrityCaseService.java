package com.example.demo.service;

import com.example.demo.entity.IntegrityCase;
import java.util.List;
import java.util.Optional;

public interface IntegrityCaseService {
    IntegrityCase createCase(IntegrityCase integrityCase);
    IntegrityCase updateCaseStatus(Long caseId, String newStatus);
    List<IntegrityCase> getCasesByStudent(Long studentId);
    Optional<IntegrityCase> getCaseById(Long caseId);
}
