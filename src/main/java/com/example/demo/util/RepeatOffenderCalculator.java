import com.example.demo.entity.IntegrityCase;
import com.example.demo.entity.RepeatOffenderRecord;
import com.example.demo.entity.StudentProfile;
import java.util.List;

public class RepeatOffenderCalculator {

    public boolean isRepeatOffender(List<IntegrityCase> cases) {
        return cases.size() >= 2; // or your rule
    }

    public String calculateSeverity(List<IntegrityCase> cases) {
        int count = cases.size();
        if (count >= 4) return "HIGH";
        if (count >= 2) return "MEDIUM";
        return "LOW";
    }

    public RepeatOffenderRecord computeRepeatOffenderRecord(StudentProfile student, List<IntegrityCase> cases) {
        RepeatOffenderRecord record = new RepeatOffenderRecord();
        record.setStudentProfile(student);
        record.setTotalCases(cases.size());
        record.setFlagSeverity(calculateSeverity(cases));
        return record;
    }
}
