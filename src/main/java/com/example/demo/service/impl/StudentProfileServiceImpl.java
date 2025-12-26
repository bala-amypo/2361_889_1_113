package com.example.demo.service.impl;

import com.example.demo.entity.IntegrityCase;
import com.example.demo.entity.RepeatOffenderRecord;
import com.example.demo.entity.StudentProfile;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.IntegrityCaseRepository;
import com.example.demo.repository.RepeatOffenderRecordRepository;
import com.example.demo.repository.StudentProfileRepository;
import com.example.demo.service.RepeatOffenderCalculator;
import com.example.demo.service.StudentProfileService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentProfileServiceImpl implements StudentProfileService {
    private final StudentProfileRepository studentProfileRepository;
    private final IntegrityCaseRepository integrityCaseRepository;
    private final RepeatOffenderRecordRepository repeatOffenderRecordRepository;
    private final RepeatOffenderCalculator repeatOffenderCalculator;

    public StudentProfileServiceImpl(StudentProfileRepository studentProfileRepository,
                                   IntegrityCaseRepository integrityCaseRepository,
                                   RepeatOffenderRecordRepository repeatOffenderRecordRepository,
                                   RepeatOffenderCalculator repeatOffenderCalculator) {
        this.studentProfileRepository = studentProfileRepository;
        this.integrityCaseRepository = integrityCaseRepository;
        this.repeatOffenderRecordRepository = repeatOffenderRecordRepository;
        this.repeatOffenderCalculator = repeatOffenderCalculator;
    }

    @Override
    public StudentProfile createStudent(StudentProfile studentProfile) {
        studentProfile.setRepeatOffender(false);
        return studentProfileRepository.save(studentProfile);
    }

    @Override
    public StudentProfile getStudentById(Long id) {
        if (id == null) {
            throw new ResourceNotFoundException("Student ID cannot be null");
        }
        return studentProfileRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
    }

    @Override
    public List<StudentProfile> getAllStudents() {
        return studentProfileRepository.findAll();
    }

    @Override
    public StudentProfile updateRepeatOffenderStatus(Long studentId) {
        StudentProfile profile = getStudentById(studentId);
        List<IntegrityCase> cases = integrityCaseRepository.findByStudentProfile_Id(studentId);
        
        RepeatOffenderRecord record = repeatOffenderCalculator.computeRepeatOffenderRecord(profile, cases);
        
        profile.setRepeatOffender(record.getTotalCases() >= 2);
        
        repeatOffenderRecordRepository.save(record);
        return studentProfileRepository.save(profile);
    }
}