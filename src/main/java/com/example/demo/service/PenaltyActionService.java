package com.example.demo.service;

import com.example.demo.entity.PenaltyAction;
import java.util.List;

public interface PenaltyActionService {
    PenaltyAction savedata(PenaltyAction data);
    List<PenaltyAction> retdata();
    PenaltyAction id(Long id);
    void remove(Long id);
}