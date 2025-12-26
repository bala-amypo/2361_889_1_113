package com.example.demo.util;

import com.example.demo.entity.IntegrityCase;
import com.example.demo.entity.RepeatOffenderRecord;
import com.example.demo.entity.StudentProfile;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class RepeatOffenderCalculator {
    
    public RepeatOffenderRecord computeRepeatOffenderRecord(StudentProfile student, List<IntegrityCase> cases) {
        int totalCases = cases.size();
        String severity = calculateSeverity(totalCases);
        
        RepeatOffenderRecord record = new RepeatOffenderRecord();
        record.setStudentProfile(student);
        record.setTotalCases(totalCases);
        record.setFlagSeverity(severity);
        
        return record;
    }
    
    public String calculateSeverity(int totalCases) {
        if (totalCases == 1) return "LOW";
        if (totalCases == 2) return "MEDIUM";
        if (totalCases >= 4) return "HIGH";
        return "MEDIUM";
    }
}