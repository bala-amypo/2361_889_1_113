package com.example.demo.service.impl;

import com.example.demo.entity.StudentProfile;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.IntegrityCaseRepository;
import com.example.demo.repository.RepeatOffenderRecordRepository;
import com.example.demo.repository.StudentProfileRepository;
import com.example.demo.service.StudentProfileService;
import com.example.demo.util.RepeatOffenderCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final IntegrityCaseRepository integrityCaseRepository;
    private final RepeatOffenderRecordRepository repeatOffenderRecordRepository;
    private final RepeatOffenderCalculator repeatOffenderCalculator;

    @Autowired
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
    public StudentProfile createStudent(StudentProfile student) {
        System.out.println("DEBUG: Creating student. Input RepeatOffender status: " + student.getRepeatOffender());
        
        if (student.getRepeatOffender() == null) {
            System.out.println("DEBUG: Status is null. Setting to FALSE.");
            student.setRepeatOffender(false);
        }
        return studentProfileRepository.save(student);
    }

    @Override
    public StudentProfile getStudentById(Long id) {
        if (id == null) throw new ResourceNotFoundException("ID cannot be null");
        return studentProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    @Override
    public List<StudentProfile> getAllStudents() {
        return studentProfileRepository.findAll();
    }

    @Override
    public StudentProfile updateRepeatOffenderStatus(Long studentId) {
        System.out.println("DEBUG: Updating status for Student ID: " + studentId);
        
        StudentProfile student = studentProfileRepository.findById(studentId).orElse(null);
        if (student != null) {
            // Using the correct repository method
            long caseCount = integrityCaseRepository.countByStudentProfile_Id(studentId);
            
            System.out.println("DEBUG: Case Count found: " + caseCount);

            if (caseCount >= 2) {
                System.out.println("DEBUG: Setting RepeatOffender = TRUE");
                student.setRepeatOffender(true);
            } else {
                System.out.println("DEBUG: Setting RepeatOffender = FALSE");
                student.setRepeatOffender(false);
            }
            return studentProfileRepository.save(student);
        }
        System.out.println("DEBUG: Student not found!");
        return null;
    }
}