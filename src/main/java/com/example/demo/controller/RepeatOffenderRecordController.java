package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.RepeatOffenderRecord;
import com.example.demo.service.RepeatOffenderRecordService;

@RestController
@RequestMapping("/repeat-offenders")
public class RepeatOffenderRecordController {

    private final RepeatOffenderRecordService repeatOffenderRecordService;

    public RepeatOffenderRecordController(
            RepeatOffenderRecordService repeatOffenderRecordService) {
        this.repeatOffenderRecordService = repeatOffenderRecordService;
    }

    @PostMapping
    public ResponseEntity<RepeatOffenderRecord> saveRecord(
            @RequestBody RepeatOffenderRecord record) {

        return new ResponseEntity<>(repeatOffenderRecordService.saveRepeatOffenderRecord(record),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepeatOffenderRecord> getRecordById(@PathVariable Long id) {

        return ResponseEntity.ok(
                repeatOffenderRecordService.getRepeatOffenderRecordById(id)
        );
    }
}
