package com.example.demo.service;

import com.example.demo.entity.RepeatOffenderRecord;
import com.example.demo.entity.StudentProfile;

public interface RepeatOffenderRecordService {
    RepeatOffenderRecord recalculateRecord(StudentProfile studentProfile);
}
