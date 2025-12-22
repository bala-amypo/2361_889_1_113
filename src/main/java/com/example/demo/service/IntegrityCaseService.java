package com.example.demo.service;

import com.example.demo.entity.IntegrityCase;
import java.util.List;

public interface IntegrityCaseService {
    IntegrityCase savedata(IntegrityCase data);
    List<IntegrityCase> retdata();
    IntegrityCase id(Long id);
    void remove(Long id);
}