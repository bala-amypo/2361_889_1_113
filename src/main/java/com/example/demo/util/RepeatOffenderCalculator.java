package com.example.demo.util;

import com.example.demo.entity.StudentProfile;
import com.example.demo.entity.RepeatOffenderRecord;
import com.example.demo.entity.IntegrityCase;

import java.util.List;

public class RepeatOffenderCalculator {

    // Compute RepeatOffenderRecord from student and their cases
    public RepeatOffenderRecord computeRepeatOffenderRecord(StudentProfile student, List<IntegrityCase> cases) {
        int totalCases = cases.size();
        String severity = calculateSeverity(totalCases);

        RepeatOffenderRecord record = new RepeatOffenderRecord();
        record.setStudentProfile(student);
        record.setTotalCases(totalCases);
        record.setFlagSeverity(severity);

        return record;
    }

    // Determine if student is repeat offender
    public boolean isRepeatOffender(long caseCount) {
        return caseCount >= 2; // two or more cases is considered repeat offender
    }

    // Calculate severity based on number of cases
    public String calculateSeverity(long caseCount) {
        if (caseCount >= 4) return "HIGH";      // 4+ cases => HIGH
        if (caseCount >= 2) return "MEDIUM";    // 2-3 cases => MEDIUM
        if (caseCount == 1) return "LOW";       // 1 case => LOW
        return "NONE";                           // 0 cases => NONE
    }
}
