package com.example.demo.service;

import com.example.demo.entity.EvidenceRecord;
import java.util.List;

public interface EvidenceRecordService {
    EvidenceRecord savedata(EvidenceRecord data);
    List<EvidenceRecord> retdata();
    EvidenceRecord id(Long id);
    void remove(Long id);
}