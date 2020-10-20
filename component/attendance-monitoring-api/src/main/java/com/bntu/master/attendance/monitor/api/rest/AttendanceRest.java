package com.bntu.master.attendance.monitor.api.rest;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.SubjectTypeConstant;
import com.bntu.master.attendance.monitor.api.model.attendance.AttendanceCell;
import com.bntu.master.attendance.monitor.api.model.attendance.AttendanceList;
import com.bntu.master.attendance.monitor.api.model.attendance.AttendancePage;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.Subject;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@CrossOrigin
@RequestMapping("/attendance")
public interface AttendanceRest {

    @GetMapping
    AttendanceList find(@RequestParam Long groupId,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                        @RequestParam Long subjectId,
                        @RequestParam Set<SubjectTypeConstant> subjectType);


    @PostMapping
    List<AttendanceCell> setValues(@RequestBody List<AttendanceCell> cells);

}
