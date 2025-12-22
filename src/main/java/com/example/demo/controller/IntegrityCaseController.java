package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.entity.IntegrityCase;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.IntegrityCaseService;

@RestController
@RequestMapping("/integrity-case") 
public class IntegrityCaseController {

    @Autowired
    IntegrityCaseService src;

    @PostMapping("/post")
    public IntegrityCase postdata(@RequestBody IntegrityCase data) {
        return src.savedata(data);
    }

    @GetMapping("/get")
    public List<IntegrityCase> getdata() {
        return src.retdata();
    } 

    @GetMapping("/getid/{id}")
    public IntegrityCase getIdval(@PathVariable Long id){
        return src.id(id);
    }

    @PutMapping("/put/{id}")
    public IntegrityCase putdata(@PathVariable Long id, @RequestBody IntegrityCase data){
        data.setId(id);
        return src.savedata(data);
    }

    @DeleteMapping("/delete/{id}")
    public String deletedata(@PathVariable Long id){
        src.remove(id);
        return "deleted";
    }
}