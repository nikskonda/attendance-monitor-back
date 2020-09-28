package com.bntu.master.attendance.monitor.impl.rest;

import com.bntu.master.attendance.monitor.api.model.SubjectTypeConstant;
import com.bntu.master.attendance.monitor.api.model.attendance.AttendanceCell;
import com.bntu.master.attendance.monitor.api.model.attendance.AttendanceList;
import com.bntu.master.attendance.monitor.api.model.attendance.AttendancePage;
import com.bntu.master.attendance.monitor.api.model.util.DateSpan;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.rest.AttendanceRest;
import com.bntu.master.attendance.monitor.impl.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
public class AttendanceRestImpl implements AttendanceRest {

    @Autowired
    private AttendanceService service;

    @Override
    public AttendanceList find(Long groupId, LocalDate startDate, LocalDate endDate, Long subjectId, Set<SubjectTypeConstant> subjectType) {
        return service.getAttendanceForGroup(
                        ObjectRef.toObjectRef(groupId),
                        new DateSpan(startDate, endDate),
                        ObjectRef.toObjectRef(subjectId),
                        subjectType);
    }

    @Override
    public AttendancePage update(AttendancePage attendancePage) {
        return service.update(attendancePage);
    }

    @Override
    public List<AttendanceCell> setValues(List<AttendanceCell> cells) {
        return service.saveAll(cells);
    }
}
