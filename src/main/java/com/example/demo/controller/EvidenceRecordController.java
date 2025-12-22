package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.entity.EvidenceRecord;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.EvidenceRecordService;

@RestController
@RequestMapping("/evidence-record") 
public class EvidenceRecordController {

    @Autowired
    EvidenceRecordService src;

    @PostMapping("/post")
    public EvidenceRecord postdata(@RequestBody EvidenceRecord data) {
        return src.savedata(data);
    }

    @GetMapping("/get")
    public List<EvidenceRecord> getdata() {
        return src.retdata();
    } 

    @GetMapping("/getid/{id}")
    public EvidenceRecord getIdval(@PathVariable Long id){
        return src.id(id);
    }

    @PutMapping("/put/{id}")
    public EvidenceRecord putdata(@PathVariable Long id, @RequestBody EvidenceRecord data){
        data.setId(id);
        return src.savedata(data);
    }

    @DeleteMapping("/delete/{id}")
    public String deletedata(@PathVariable Long id){
        src.remove(id);
        return "deleted";
    }
}