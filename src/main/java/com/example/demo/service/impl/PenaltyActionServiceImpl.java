package com.example.demo.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.demo.entity.IntegrityCase;
import com.example.demo.entity.PenaltyAction;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.IntegrityCaseRepository;
import com.example.demo.repository.PenaltyActionRepository;
import com.example.demo.service.PenaltyActionService;

@Service
public class PenaltyActionServiceImpl implements PenaltyActionService {

    private final PenaltyActionRepository penaltyActionRepository;
    private final IntegrityCaseRepository integrityCaseRepository;

    public PenaltyActionServiceImpl(PenaltyActionRepository penaltyActionRepository,
                                    IntegrityCaseRepository integrityCaseRepository) {
        this.penaltyActionRepository = penaltyActionRepository;
        this.integrityCaseRepository = integrityCaseRepository;
    }

    @Override
    public PenaltyAction addPenalty(PenaltyAction penaltyAction, Long caseId) {

        IntegrityCase integrityCase = integrityCaseRepository.findById(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Integrity case not found"));

        if (penaltyAction.getIssuedAt() == null) {
            penaltyAction.setIssuedAt(LocalDateTime.now());
        }

        // business rule: OPEN → UNDER_REVIEW
        if ("OPEN".equals(integrityCase.getStatus())) {
            integrityCase.setStatus("UNDER_REVIEW");
            integrityCaseRepository.save(integrityCase);
        }

        return penaltyActionRepository.save(penaltyAction);
    }
}
