package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping; 
import com.example.demo.entity.StudentProfile;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.StudentProfileService;

@RestController
@RequestMapping("/student-profile")
public class StudentProfileController {

    @Autowired
    StudentProfileService src;

    @PostMapping("/post")
    public StudentProfile postdata(@RequestBody StudentProfile data) {
        return src.savedata(data);
    }

    @GetMapping("/get")
    public List<StudentProfile> getdata() {
        return src.retdata();
    } 

    @GetMapping("/getid/{id}")
    public StudentProfile getIdval(@PathVariable Long id){
        return src.id(id);
    }

    @PutMapping("/put/{id}")
    public StudentProfile putdata(@PathVariable Long id, @RequestBody StudentProfile data){
        data.setId(id);
        return src.savedata(data);
    }

    @DeleteMapping("/delete/{id}")
    public String deletedata(@PathVariable Long id){
        src.remove(id);
        return "deleted";
    }
}