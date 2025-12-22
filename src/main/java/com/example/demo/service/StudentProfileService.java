package com.example.demo.service;

import com.example.demo.entity.StudentProfile;
import java.util.List;

public interface StudentProfileService {
    StudentProfile savedata(StudentProfile data);
    List<StudentProfile> retdata();
    StudentProfile id(Long id);
    void remove(Long id);
}