package com.example.demo.service.impl;

import com.example.demo.entity.StudentProfile;
import com.example.demo.repository.IntegrityCaseRepository;
import com.example.demo.repository.StudentProfileRepository;
import com.example.demo.service.StudentProfileService;
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
    public void updateRepeatOffenderStatus(Long studentId) {
        Optional<StudentProfile> studentOpt = studentProfileRepository.findById(studentId);
        if (studentOpt.isPresent()) {
            StudentProfile student = studentOpt.get();
            long caseCount = integrityCaseRepository.countByStudentId(studentId);

           
            if (caseCount >= 2) {
                student.setRepeatOffender(true);
            } else {
                student.setRepeatOffender(false);
            }
            
            studentProfileRepository.save(student);
        }
    }
}