package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.IntegrityCase;
import com.example.demo.service.IntegrityCaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cases")
public class IntegrityCaseController {
    private final IntegrityCaseService integrityCaseService;

    public IntegrityCaseController(IntegrityCaseService integrityCaseService) {
        this.integrityCaseService = integrityCaseService;
    }

    @PostMapping
    public ResponseEntity<IntegrityCase> createCase(@RequestBody IntegrityCase integrityCase) {
        IntegrityCase created = integrityCaseService.createCase(integrityCase);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{caseId}/status")
    public ResponseEntity<ApiResponse> updateCaseStatus(@PathVariable Long caseId, @RequestParam String status) {
        integrityCaseService.updateCaseStatus(caseId, status);
        return ResponseEntity.ok(new ApiResponse(true, "Case status updated"));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<IntegrityCase>> getCasesByStudent(@PathVariable Long studentId) {
        List<IntegrityCase> cases = integrityCaseService.getCasesByStudent(studentId);
        return ResponseEntity.ok(cases);
    }

    @GetMapping("/{caseId}")
    public ResponseEntity<IntegrityCase> getCaseById(@PathVariable Long caseId) {
        Optional<IntegrityCase> caseOpt = integrityCaseService.getCaseById(caseId);
        return caseOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}