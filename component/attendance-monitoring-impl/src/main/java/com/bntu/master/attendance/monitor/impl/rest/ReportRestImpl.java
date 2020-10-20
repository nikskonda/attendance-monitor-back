package com.bntu.master.attendance.monitor.impl.rest;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.report.ReportByStudentAndSubjects;
import com.bntu.master.attendance.monitor.api.rest.ReportRest;
import com.bntu.master.attendance.monitor.impl.service.AttendanceService;
import com.bntu.master.attendance.monitor.impl.service.GenerateService;
import com.bntu.master.attendance.monitor.impl.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ReportRestImpl implements ReportRest {

    @Autowired
    private ReportService service;

    @Override
    public List<List<String>> findGridByStudentForDateRange(Long studentId, LocalDate startDate, LocalDate endDate) {
        return service.getDataByStudentInDateRange(startDate, endDate, ObjectRef.toObjectRef(studentId));
    }

    @Override
    public List<List<String>> findGridByStudentDetailsForDateRange(Long studentId, LocalDate startDate, LocalDate endDate) {
        return service.getDataByStudentDetailInDateRange(startDate, endDate, ObjectRef.toObjectRef(studentId));
    }

    @Override
    public List<List<String>> findGridByGroupForDateRange(Long groupId, Long subjectId, LocalDate startDate, LocalDate endDate) {
        return service.findGridByGroupForDateRange(ObjectRef.toObjectRef(groupId), ObjectRef.toObjectRef(subjectId), startDate, endDate);
    }
}