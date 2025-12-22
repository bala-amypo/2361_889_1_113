package com.example.demo.service;

import com.example.demo.entity.StudentProfile;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentProfileServiceImpl implements StudentProfileService {

    private List<StudentProfile> list = new ArrayList<>();

    @Override
    public StudentProfile savedata(StudentProfile data) {
        list.add(data);
        return data;
    }

    @Override
    public List<StudentProfile> retdata() {
        return list;
    }

    @Override
    public StudentProfile id(Long id) {
        for (StudentProfile data : list) {
            if (data.getId().equals(id)) {
                return data;
            }
        }
        return null;
    }

    @Override
    public void remove(Long id) {
        StudentProfile toRemove = null;
        for (StudentProfile data : list) {
            if (data.getId().equals(id)) {
                toRemove = data;
                break;
            }
        }
        if (toRemove != null) {
            list.remove(toRemove);
        }
    }
}