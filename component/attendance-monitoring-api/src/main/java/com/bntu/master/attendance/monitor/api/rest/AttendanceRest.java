package com.bntu.master.attendance.monitor.api.rest;

import com.bntu.master.attendance.monitor.api.model.AttendancePage;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;


@RequestMapping("/attendance")
public interface AttendanceRest {

    @GetMapping
    AttendancePage find(@PathVariable Long groupId, @PathVariable LocalDate startDate, @PathVariable LocalDate endDate);


    @PutMapping
    PersonDto update(@RequestBody AttendancePage attendancePage);

}
