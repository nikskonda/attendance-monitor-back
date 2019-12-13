package com.bntu.master.attendance.monitor.api.rest;

import com.bntu.master.attendance.monitor.api.model.AttendancePage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;


@RequestMapping("/attendance")
@RestController
public interface AttendanceRest {

    @GetMapping
    AttendancePage find(@PathVariable Long groupId, @PathVariable LocalDate startDate, @PathVariable LocalDate endDate);


    @PutMapping
    AttendancePage update(@RequestBody AttendancePage attendancePage);

}
