package com.bntu.master.attendance.monitor.api.rest;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RequestMapping("/report")
public interface ReportRest {

    @GetMapping("/byStudent")
    List<List<String>> findGridForStudentReport(
            @RequestParam Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);

    @GetMapping("/byStudentDetails")
    List<List<String>> findGridForStudentDetailsReport(
            @RequestParam Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);

    @GetMapping("/byStudentAndSubjectDetails")
    List<List<String>> findGridForStudentAndSubjectDetailsReport(
            @RequestParam Long studentId,
            @RequestParam(required = false) Long subjectId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);

    @GetMapping("/byGroupAndSubject")
    List<List<String>> findGridForGroupReport(
            @RequestParam Long groupId,
            @RequestParam Long subjectId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);

    @GetMapping("/students")
    List<List<String>> findGridForStudentReport(@RequestParam Long groupId);

    @GetMapping("/professors")
    List<List<String>> findGridForProfessorReport();


}
