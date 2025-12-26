package com.example.demo.repository;

import com.example.demo.entity.IntegrityCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface IntegrityCaseRepository extends JpaRepository<IntegrityCase, Long> {
    List<IntegrityCase> findByStudentProfile_Id(Long studentId);
    
    @Query("SELECT ic FROM IntegrityCase ic WHERE ic.studentProfile.studentId = :studentIdentifier")
    List<IntegrityCase> findByStudentIdentifier(@Param("studentIdentifier") String studentIdentifier);
    
    @Query("SELECT ic FROM IntegrityCase ic WHERE ic.status = :status AND ic.incidentDate >= :sinceDate")
    List<IntegrityCase> findRecentCasesByStatus(@Param("status") String status, @Param("sinceDate") LocalDate sinceDate);
    
    List<IntegrityCase> findByIncidentDateBetween(LocalDate start, LocalDate end);
    List<IntegrityCase> findByStatus(String status);
}