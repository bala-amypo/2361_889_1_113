package com.example.demo.service;

import com.example.demo.entity.PenaltyAction;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class PenaltyActionServiceImpl implements PenaltyActionService {

    private List<PenaltyAction> list = new ArrayList<>();

    @Override
    public PenaltyAction savedata(PenaltyAction data) {
        list.add(data);
        return data;
    }

    @Override
    public List<PenaltyAction> retdata() {
        return list;
    }

    @Override
    public PenaltyAction id(Long id) {
        for (PenaltyAction data : list) {
            if (data.getId().equals(id)) {
                return data;
            }
        }
        return null;
    }

    @Override
    public void remove(Long id) {
        PenaltyAction toRemove = null;
        for (PenaltyAction data : list) {
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