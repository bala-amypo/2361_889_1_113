package com.example.demo.service.impl;

import com.example.demo.entity.StudentProfile;
import com.example.demo.entity.RepeatOffenderRecord;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.IntegrityCaseRepository;
import com.example.demo.repository.RepeatOffenderRecordRepository;
import com.example.demo.repository.StudentProfileRepository;
import com.example.demo.service.StudentProfileService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final IntegrityCaseRepository integrityCaseRepository;
    private final RepeatOffenderRecordRepository repeatOffenderRecordRepository;

    public StudentProfileServiceImpl(StudentProfileRepository studentProfileRepository,
                                     IntegrityCaseRepository integrityCaseRepository,
                                     RepeatOffenderRecordRepository repeatOffenderRecordRepository) {
        this.studentProfileRepository = studentProfileRepository;
        this.integrityCaseRepository = integrityCaseRepository;
        this.repeatOffenderRecordRepository = repeatOffenderRecordRepository;
    }

    @Override
    public StudentProfile createStudent(StudentProfile student) {
        student.setRepeatOffender(false);
        return studentProfileRepository.save(student);
    }

    @Override
    public StudentProfile getStudentById(Long id) {
        if (id == null) {
            throw new ResourceNotFoundException("ID cannot be null");
        }
        return studentProfileRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found with id: " + id));
    }

    @Override
    public List<StudentProfile> getAllStudents() {
        return studentProfileRepository.findAll();
    }

    @Override
    public StudentProfile updateRepeatOffenderStatus(Long studentId) {
        StudentProfile student = studentProfileRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found with id: " + studentId));

        long caseCount = integrityCaseRepository.countByStudentProfile_Id(studentId);

        boolean isRepeat = caseCount >= 2;
        student.setRepeatOffender(isRepeat);

        if (isRepeat) {
            repeatOffenderRecordRepository.findByStudentProfile(student)
                    .orElseGet(() ->
                            repeatOffenderRecordRepository.save(
                                    new RepeatOffenderRecord(
                                            null,
                                            (int) caseCount,
                                            null,
                                            "MEDIUM"
                                    )
                            )
                    );
        }

        return studentProfileRepository.save(student);
    }
}
