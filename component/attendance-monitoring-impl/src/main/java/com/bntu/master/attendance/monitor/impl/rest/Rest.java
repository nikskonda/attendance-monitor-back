package com.bntu.master.attendance.monitor.impl.rest;

import com.bntu.master.attendance.monitor.impl.service.AttendanceService;
import com.bntu.master.attendance.monitor.impl.service.GenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping(value = "/rest/for/test")
public class Rest {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private GenerateService generateService;

    @PostConstruct
    public void baseConstruct() {
        generateBase();
    }

    @GetMapping("/generate")
    public void generate() {
        generateService.create();
    }

    @GetMapping("/generateBase")
    public void generateBase() {
        generateService.createBase();
    }

}