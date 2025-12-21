package com.example.demo.controller;

import com.example.demo.entity.PenaltyAction;
import com.example.demo.service.PenaltyActionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/penalties")
public class PenaltyActionController {

    private final PenaltyActionService penaltyActionService;

    public PenaltyActionController(PenaltyActionService penaltyActionService) {
        this.penaltyActionService = penaltyActionService;
    }

    @PostMapping
    public ResponseEntity<PenaltyAction> addPenalty(@RequestBody PenaltyAction penalty) {
        PenaltyAction added = penaltyActionService.addPenalty(penalty);
        return ResponseEntity.ok(added);
    }
}
