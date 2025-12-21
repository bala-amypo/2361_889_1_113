package com.example.demo.controller;

import com.example.demo.entity.EvidenceRecord;
import com.example.demo.service.EvidenceRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evidence")
public class EvidenceRecordController {

    private final EvidenceRecordService evidenceRecordService;

    public EvidenceRecordController(EvidenceRecordService evidenceRecordService) {
        this.evidenceRecordService = evidenceRecordService;
    }

    @PostMapping
    public ResponseEntity<EvidenceRecord> submitEvidence(@RequestBody EvidenceRecord evidence) {
        EvidenceRecord submitted = evidenceRecordService.submitEvidence(evidence);
        return ResponseEntity.ok(submitted);
    }
}