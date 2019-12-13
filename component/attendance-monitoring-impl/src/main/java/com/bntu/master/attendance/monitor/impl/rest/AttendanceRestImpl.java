package com.bntu.master.attendance.monitor.impl.rest;

import com.bntu.master.attendance.monitor.api.model.AttendancePage;
import com.bntu.master.attendance.monitor.api.model.DateSpan;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.rest.AttendanceRest;
import com.bntu.master.attendance.monitor.impl.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class AttendanceRestImpl implements AttendanceRest {

    @Autowired
    private AttendanceService service;

    @Override
    public AttendancePage find(Long groupId, LocalDate startDate, LocalDate endDate) {
        return service.getAttendanceForGroup(ObjectRef.toObjectRef(groupId), groupId, new DateSpan(startDate, endDate));
    }

    @Override
    public AttendancePage update(AttendancePage attendancePage) {
        return service.update(attendancePage);
    }
}
