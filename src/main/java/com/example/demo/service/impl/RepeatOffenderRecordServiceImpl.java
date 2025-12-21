package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.entity.RepeatOffenderRecord;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.RepeatOffenderRecordRepository;
import com.example.demo.service.RepeatOffenderRecordService;

@Service
public class RepeatOffenderRecordServiceImpl implements RepeatOffenderRecordService {

    private final RepeatOffenderRecordRepository repeatOffenderRecordRepository;

    public RepeatOffenderRecordServiceImpl(
            RepeatOffenderRecordRepository repeatOffenderRecordRepository) {
        this.repeatOffenderRecordRepository = repeatOffenderRecordRepository;
    }

    @Override
    public RepeatOffenderRecord saveRepeatOffenderRecord(RepeatOffenderRecord record) {
        return repeatOffenderRecordRepository.save(record);
    }

    @Override
    public RepeatOffenderRecord getRepeatOffenderRecordById(Long id) {
        return repeatOffenderRecordRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Repeat offender record not found"));
    }
}
