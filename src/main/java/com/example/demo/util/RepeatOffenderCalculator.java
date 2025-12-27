package com.example.demo.util;

import com.example.demo.entity.StudentProfile;
import com.example.demo.entity.RepeatOffenderRecord;
import com.example.demo.entity.IntegrityCase;

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

    public boolean isRepeatOffender(long caseCount) {
        return caseCount >= 2; 
    }

    public String calculateSeverity(long caseCount) {
        if (caseCount >= 4) return "HIGH";      
        if (caseCount >= 2) return "MEDIUM";   
        if (caseCount == 1) return "LOW";       
        return "NONE";                           
    }
}
