package com.example.demo.service.impl;

import com.example.demo.entity.IntegrityCase;
import com.example.demo.entity.StudentProfile;
import com.example.demo.repository.IntegrityCaseRepository;
import com.example.demo.repository.StudentProfileRepository;
import com.example.demo.service.IntegrityCaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IntegrityCaseServiceImpl implements IntegrityCaseService {

    private final IntegrityCaseRepository integrityCaseRepository;
    private final StudentProfileRepository studentProfileRepository;

    public IntegrityCaseServiceImpl(IntegrityCaseRepository integrityCaseRepository,
                                    StudentProfileRepository studentProfileRepository) {
        this.integrityCaseRepository = integrityCaseRepository;
        this.studentProfileRepository = studentProfileRepository;
    }

    @Override
    public IntegrityCase createCase(IntegrityCase integrityCase) {
        if (integrityCase == null || integrityCase.getStudentProfile() == null
                || integrityCase.getStudentProfile().getId() == null) {
            throw new IllegalArgumentException("StudentProfile must be provided");
        }

        StudentProfile student = studentProfileRepository.findById(
                integrityCase.getStudentProfile().getId()
        ).orElseThrow(() -> new IllegalArgumentException("Student not found"));

        integrityCase.setStudentProfile(student);

        return integrityCaseRepository.save(integrityCase);
    }

    @Override
    public IntegrityCase updateCaseStatus(Long caseId, String newStatus) {
        if (caseId == null) {
            throw new IllegalArgumentException("Case ID cannot be null");
        }

        IntegrityCase integrityCase = integrityCaseRepository.findById(caseId)
                .orElseThrow(() -> new IllegalArgumentException("Case not found"));

        integrityCase.setStatus(newStatus);

        return integrityCaseRepository.save(integrityCase);
    }

    @Override
    public List<IntegrityCase> getCasesByStudent(Long studentId) {
        return integrityCaseRepository.findByStudentProfile_Id(studentId);
    }

    @Override
    public Optional<IntegrityCase> getCaseById(Long caseId) {
        if (caseId == null) {
            return Optional.empty();
        }
        return integrityCaseRepository.findById(caseId);
    }
}
