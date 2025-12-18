package com.example.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bindannotation.*;
import java.util.*;
import example.demo.entity.Studententity;
import example.demo.service.Studentservice;
@Restcontroller
public class Studentcontroller{
    @Autowired
    Studentservice src;
    @PostMapping("/post");
    public 
}