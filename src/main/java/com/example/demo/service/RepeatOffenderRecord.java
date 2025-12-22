package com.example.demo.service;

import com.example.demo.entity.RepeatOffenderRecord;
import java.util.List;

public interface RepeatOffenderRecordService {
    RepeatOffenderRecord savedata(RepeatOffenderRecord data);
    List<RepeatOffenderRecord> retdata();
    RepeatOffenderRecord id(Long id);
    void remove(Long id);
}