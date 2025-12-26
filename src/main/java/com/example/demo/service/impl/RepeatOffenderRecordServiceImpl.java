package com.example.demo.service.impl;

import com.example.demo.entity.IntegrityCase;
import com.example.demo.entity.RepeatOffenderRecord;
import com.example.demo.entity.StudentProfile;
import com.example.demo.repository.IntegrityCaseRepository;
import com.example.demo.repository.RepeatOffenderRecordRepository;
import com.example.demo.repository.StudentProfileRepository;
import com.example.demo.service.RepeatOffenderCalculator;
import com.example.demo.service.RepeatOffenderRecordService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RepeatOffenderRecordServiceImpl implements RepeatOffenderRecordService {
    private final StudentProfileRepository studentProfileRepository;
    private final IntegrityCaseRepository integrityCaseRepository;
    private final RepeatOffenderRecordRepository repeatOffenderRecordRepository;
    private final RepeatOffenderCalculator repeatOffenderCalculator;

    public RepeatOffenderRecordServiceImpl(StudentProfileRepository studentProfileRepository,
                                         IntegrityCaseRepository integrityCaseRepository,
                                         RepeatOffenderRecordRepository repeatOffenderRecordRepository,
                                         RepeatOffenderCalculator repeatOffenderCalculator) {
        this.studentProfileRepository = studentProfileRepository;
        this.integrityCaseRepository = integrityCaseRepository;
        this.repeatOffenderRecordRepository = repeatOffenderRecordRepository;
        this.repeatOffenderCalculator = repeatOffenderCalculator;
    }

    @Override
    public RepeatOffenderRecord recalculateRecord(StudentProfile studentProfile) {
        List<IntegrityCase> cases = integrityCaseRepository.findByStudentProfile_Id(studentProfile.getId());
        
        int totalCases = cases.size();
        String severity = repeatOffenderCalculator.calculateSeverity(totalCases);
        boolean isRepeatOffender = repeatOffenderCalculator.isRepeatOffender(cases);
        
        studentProfile.setRepeatOffender(isRepeatOffender);
        studentProfileRepository.save(studentProfile);
        
        RepeatOffenderRecord record = repeatOffenderRecordRepository.findByStudentProfile(studentProfile)
            .orElse(new RepeatOffenderRecord(studentProfile, totalCases, severity));
        
        record.setTotalCases(totalCases);
        record.setFlagSeverity(severity);
        if (!cases.isEmpty()) {
            record.setFirstIncidentDate(cases.get(0).getIncidentDate());
        }
        
        return repeatOffenderRecordRepository.save(record);
    }
}