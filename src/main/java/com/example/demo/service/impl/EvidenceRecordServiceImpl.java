package com.example.demo.service;

import com.example.demo.entity.EvidenceRecord;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class EvidenceRecordServiceImpl implements EvidenceRecordService {

    private List<EvidenceRecord> list = new ArrayList<>();

    @Override
    public EvidenceRecord savedata(EvidenceRecord data) {
        list.add(data);
        return data;
    }

    @Override
    public List<EvidenceRecord> retdata() {
        return list;
    }

    @Override
    public EvidenceRecord id(Long id) {
        for (EvidenceRecord data : list) {
            if (data.getId().equals(id)) {
                return data;
            }
        }
        return null;
    }

    @Override
    public void remove(Long id) {
        EvidenceRecord toRemove = null;
        for (EvidenceRecord data : list) {
            if (data.getId().equals(id)) {
                toRemove = data;
                break;
            }
        }
        if (toRemove != null) {
            list.remove(toRemove);
        }
    }
}