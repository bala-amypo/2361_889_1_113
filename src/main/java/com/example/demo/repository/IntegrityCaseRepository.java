package com.example.demo.repository;
import com.example.demo.entity.IntegrityCase;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IntegrityCaseRepository extends JpaRepository<IntegrityCase, Long> {
    
}
