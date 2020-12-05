package com.bntu.master.attendance.monitor.impl.rest;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.rest.ReportRest;
import com.bntu.master.attendance.monitor.impl.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ReportRestImpl implements ReportRest {

    @Autowired
    private ReportService service;

    @Override
    public List<List<String>> findGridForStudentReport(Long studentId, LocalDate startDate, LocalDate endDate) {
        return service.findGridForStudentReport(startDate, endDate, ObjectRef.toObjectRef(studentId));
    }

    @Override
    public List<List<String>> findGridForStudentDetailsReport(Long studentId, LocalDate startDate, LocalDate endDate) {
        return service.findGridForStudentDetailsReport(startDate, endDate, ObjectRef.toObjectRef(studentId));
    }

    @Override
    public List<List<String>> findGridForStudentAndSubjectDetailsReport(Long studentId, Long subjectId, LocalDate startDate, LocalDate endDate) {
        return service.findGridForStudentAndSubjectDetailsReport(startDate, endDate, ObjectRef.toObjectRef(studentId), ObjectRef.toObjectRef(subjectId));
    }

    @Override
    public List<List<String>> findGridForGroupReport(Long groupId, Long subjectId, LocalDate startDate, LocalDate endDate) {
        return service.findGridForGroupReport(ObjectRef.toObjectRef(groupId), ObjectRef.toObjectRef(subjectId), startDate, endDate);
    }

    @Override
    public List<List<String>> findGridForStudentReport(Long groupId) {
        return service.findGridForStudentReport(groupId);
    }

    @Override
    public List<List<String>> findGridForProfessorReport() {
        return service.findGridForProfessorReport();
    }
}