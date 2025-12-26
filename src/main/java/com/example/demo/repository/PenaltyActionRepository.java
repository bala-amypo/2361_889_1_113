package com.example.demo.repository;

import com.example.demo.entity.PenaltyAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PenaltyActionRepository extends JpaRepository<PenaltyAction, Long> {
} 
