package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entity.EvidenceRecord;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.demo.service.EvidenceRecordService;

@RestController
public class EvidenceRecordController {

    @Autowired
    EvidenceRecordService src;

    @PostMapping("/post")
    public EvidenceRecord postdata(@RequestBody EvidenceRecord evidence) {
        return src.savedata(evidence);
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
    public EvidenceRecord putdata(@PathVariable Long id, @RequestBody EvidenceRecord st){
        return src.savedata(st);
    }

    @DeleteMapping("/delete/{id}")
    public String deletedata(@PathVariable Long id){
        src.remove(id);
        return "deleted";
    }
}