package com.example.demo.repository;

import com.example.demo.entity.RepeatOffenderRecord;
import com.example.demo.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RepeatOffenderRecordRepository extends JpaRepository<RepeatOffenderRecord, Long> {
    Optional<RepeatOffenderRecord> findByStudentProfile(StudentProfile studentProfile);
}
