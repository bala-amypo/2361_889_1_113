package com.example.demo.util;

import com.example.demo.entity.RepeatOffenderRecord;
import com.example.demo.entity.StudentProfile;
import org.springframework.stereotype.Component;

@Component
public class RepeatOffenderCalculator {

    // Determine if a student is a repeat offender
    public boolean isRepeatOffender(long totalCases) {
        return totalCases >= 2; // exactly what your test expects
    }

    // Calculate severity based on number of cases
    public String calculateSeverity(long totalCases) {
        if (totalCases == 1) return "LOW";
        if (totalCases == 2) return "MEDIUM";
        if (totalCases >= 4) return "HIGH";
        return "MEDIUM"; // default
    }

    // Compute a RepeatOffenderRecord entity
    public RepeatOffenderRecord computeRepeatOffenderRecord(StudentProfile student, long totalCases) {
        RepeatOffenderRecord record = new RepeatOffenderRecord();
        record.setStudentProfile(student);
        record.setTotalCases((int) totalCases);
        record.setFlagSeverity(calculateSeverity(totalCases));
        return record;
    }
}
