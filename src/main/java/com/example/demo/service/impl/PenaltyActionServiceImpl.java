package com.example.demo.service.impl;

import com.example.demo.entity.IntegrityCase;
import com.example.demo.entity.PenaltyAction;
import com.example.demo.repository.IntegrityCaseRepository;
import com.example.demo.repository.PenaltyActionRepository;
import com.example.demo.service.PenaltyActionService;
import org.springframework.stereotype.Service;

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
    public PenaltyAction addPenalty(PenaltyAction penaltyAction) {
        IntegrityCase integrityCase = penaltyAction.getIntegrityCase();
        if (integrityCase == null) {
            throw new IllegalArgumentException("IntegrityCase must be provided");
        }

        // Save penalty first
        PenaltyAction savedPenalty = penaltyActionRepository.save(penaltyAction);
        
        // Update case status from OPEN to UNDER_REVIEW
        if ("OPEN".equals(integrityCase.getStatus())) {
            integrityCase.setStatus("UNDER_REVIEW");
            integrityCaseRepository.save(integrityCase);
        }
        
        return savedPenalty;
    }
}