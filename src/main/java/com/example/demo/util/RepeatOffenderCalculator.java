package com.example.demo.util;

import com.example.demo.entity.IntegrityCase;
import com.example.demo.entity.RepeatOffenderRecord;
import com.example.demo.entity.StudentProfile;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class RepeatOffenderCalculator {

    // New method to check if a student is a repeat offender
    public boolean isRepeatOffender(int totalCases) {
        return totalCases >= 2; // repeat offender if 2 or more cases
    }

    // Existing method to calculate severity
    public String calculateSeverity(int totalCases) {
        if (totalCases == 1) return "LOW";
        if (totalCases == 2) return "MEDIUM";
        if (totalCases >= 4) return "HIGH";
        return "MEDIUM";
    }

    // Optional: keep this if you want to generate RepeatOffenderRecord
    public RepeatOffenderRecord computeRepeatOffenderRecord(StudentProfile student, List<IntegrityCase> cases) {
        int totalCases = cases.size();
        String severity = calculateSeverity(totalCases);

        RepeatOffenderRecord record = new RepeatOffenderRecord();
        record.setStudentProfile(student);
        record.setTotalCases(totalCases);
        record.setFlagSeverity(severity);

        return record;
    }
}
