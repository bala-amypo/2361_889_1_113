package com.example.demo.service.impl;

import com.example.demo.entity.StudentProfile;
import com.example.demo.repository.IntegrityCaseRepository;
import com.example.demo.repository.StudentProfileRepository;
import com.example.demo.service.StudentProfileService;
import com.example.demo.util.RepeatOffenderCalculator; // Keep this if you have it
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentProfileServiceImpl implements StudentProfileService {

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private IntegrityCaseRepository integrityCaseRepository;

    public StudentProfileServiceImpl(StudentProfileRepository studentProfileRepository,
                                     IntegrityCaseRepository integrityCaseRepository,
                                     Object repeatOffenderRecordRepository,
                                     Object calculator) { 
        this.studentProfileRepository = studentProfileRepository;
        this.integrityCaseRepository = integrityCaseRepository;
    }

    @Override
    public StudentProfile createStudent(StudentProfile student) {
        return studentProfileRepository.save(student);
    }

    @Override
    public StudentProfile getStudentById(Long id) {
        return studentProfileRepository.findById(id).orElse(null);
    }

    @Override
    public List<StudentProfile> getAllStudents() {
        return studentProfileRepository.findAll();
    }

    @Override
    public StudentProfile updateRepeatOffenderStatus(Long studentId) {
        StudentProfile student = studentProfileRepository.findById(studentId).orElse(null);
        
        if (student != null) {
            long caseCount = integrityCaseRepository.countByStudentProfile_Id(studentId);

            if (caseCount >= 2) {
                student.setRepeatOffender(true);
            } else {
                student.setRepeatOffender(false);
            }
            
            return studentProfileRepository.save(student);
        }
        return null;
    }
}