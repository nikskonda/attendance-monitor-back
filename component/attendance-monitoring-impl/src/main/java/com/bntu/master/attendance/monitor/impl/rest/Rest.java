package com.bntu.master.attendance.monitor.impl.rest;

import com.bntu.master.attendance.monitor.api.model.attendance.AttendancePage;
import com.bntu.master.attendance.monitor.api.model.util.DateSpan;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.service.AttendanceService;
import com.bntu.master.attendance.monitor.impl.service.GenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/rest/for/test")
public class Rest {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private GenerateService generateService;

    @GetMapping
    public AttendancePage test() {
        DateSpan dateSpan = new DateSpan();
        dateSpan.setStartDate(LocalDate.now().minusDays(1));
        dateSpan.setFinishDate(LocalDate.now().plusDays(6));
        return attendanceService.getAttendanceForGroup(new ObjectRef("group4_spec2"), 123L, dateSpan);
    }

    @GetMapping("/generate")
    public void generate() {
        generateService.create();
    }

}