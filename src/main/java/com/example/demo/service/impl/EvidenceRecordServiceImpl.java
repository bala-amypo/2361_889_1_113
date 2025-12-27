package com.example.demo.service.impl;

import com.example.demo.entity.EvidenceRecord;
import com.example.demo.repository.EvidenceRecordRepository;
import com.example.demo.service.EvidenceRecordService;
import org.springframework.stereotype.Service;

@Service
public class EvidenceRecordServiceImpl implements EvidenceRecordService {

    private final EvidenceRecordRepository evidenceRecordRepository;

    public EvidenceRecordServiceImpl(EvidenceRecordRepository evidenceRecordRepository) {
        this.evidenceRecordRepository = evidenceRecordRepository;
    }

    @Override
    public EvidenceRecord submitEvidence(EvidenceRecord evidenceRecord) {
        if (evidenceRecord == null
                || evidenceRecord.getIntegrityCase() == null
                || evidenceRecord.getIntegrityCase().getId() == null) {
            throw new IllegalArgumentException("Valid IntegrityCase must be provided");
        }

        return evidenceRecordRepository.save(evidenceRecord);
    }
}
