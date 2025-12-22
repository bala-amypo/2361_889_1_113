package com.example.demo.service;

import com.example.demo.entity.RepeatOffenderRecord;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class RepeatOffenderRecordServiceImpl implements RepeatOffenderRecordService {

    private List<RepeatOffenderRecord> list = new ArrayList<>();

    @Override
    public RepeatOffenderRecord savedata(RepeatOffenderRecord data) {
        list.add(data);
        return data;
    }

    @Override
    public List<RepeatOffenderRecord> retdata() {
        return list;
    }

    @Override
    public RepeatOffenderRecord id(Long id) {
        for (RepeatOffenderRecord data : list) {
            if (data.getId().equals(id)) {
                return data;
            }
        }
        return null;
    }

    @Override
    public void remove(Long id) {
        RepeatOffenderRecord toRemove = null;
        for (RepeatOffenderRecord data : list) {
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