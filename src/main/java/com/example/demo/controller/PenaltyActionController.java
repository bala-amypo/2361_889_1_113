package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping; 
import com.example.demo.entity.PenaltyAction;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.PenaltyActionService;

@RestController
@RequestMapping("/penalty-action")
public class PenaltyActionController {

    @Autowired
    PenaltyActionService src;

    @PostMapping("/post")
    public PenaltyAction postdata(@RequestBody PenaltyAction data) {
        return src.savedata(data);
    }

    @GetMapping("/get")
    public List<PenaltyAction> getdata() {
        return src.retdata();
    } 

    @GetMapping("/getid/{id}")
    public PenaltyAction getIdval(@PathVariable Long id){
        return src.id(id);
    }

    @PutMapping("/put/{id}")
    public PenaltyAction putdata(@PathVariable Long id, @RequestBody PenaltyAction data){
        data.setId(id);
        return src.savedata(data);
    }

    @DeleteMapping("/delete/{id}")
    public String deletedata(@PathVariable Long id){
        src.remove(id);
        return "deleted";
    }
}