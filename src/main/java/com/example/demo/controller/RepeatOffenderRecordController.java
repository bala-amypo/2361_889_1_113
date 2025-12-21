package com.example.demo.controller;

import com.example.demo.entity.RepeatOffenderRecord;
import com.example.demo.entity.StudentProfile;
import com.example.demo.service.RepeatOffenderRecordService;
import com.example.demo.service.StudentProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repeat-offenders")
public class RepeatOffenderRecordController {

    private final RepeatOffenderRecordService repeatOffenderRecordService;
    private final StudentProfileService studentProfileService;

    public RepeatOffenderRecordController(RepeatOffenderRecordService repeatOffenderRecordService,
                                        StudentProfileService studentProfileService) {
        this.repeatOffenderRecordService = repeatOffenderRecordService;
        this.studentProfileService = studentProfileService;
    }

    @PostMapping("/recalculate/{studentId}")
    public ResponseEntity<RepeatOffenderRecord> recalculateRecord(@PathVariable Long studentId) {
        StudentProfile student = studentProfileService.getStudentById(studentId);
        RepeatOffenderRecord record = repeatOffenderRecordService.recalculateRecord(student);
        return ResponseEntity.ok(record);
    }
}
