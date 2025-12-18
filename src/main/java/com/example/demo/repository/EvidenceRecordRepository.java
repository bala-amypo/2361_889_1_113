package com.example.demo.repository;
import com.example.demo.entity.EvidenceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EvidenceRecordRepository  extends JpaRepository<EvidenceRecord, Long> {
    
}
