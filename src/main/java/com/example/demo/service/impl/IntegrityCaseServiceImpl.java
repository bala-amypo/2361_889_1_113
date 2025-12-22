package com.example.demo.service;

import com.example.demo.entity.IntegrityCase;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class IntegrityCaseServiceImpl implements IntegrityCaseService {

    private List<IntegrityCase> list = new ArrayList<>();

    @Override
    public IntegrityCase savedata(IntegrityCase data) {
        list.add(data);
        return data;
    }

    @Override
    public List<IntegrityCase> retdata() {
        return list;
    }

    @Override
    public IntegrityCase id(Long id) {
        for (IntegrityCase data : list) {
            if (data.getId().equals(id)) {
                return data;
            }
        }
        return null;
    }

    @Override
    public void remove(Long id) {
        IntegrityCase toRemove = null;
        for (IntegrityCase data : list) {
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