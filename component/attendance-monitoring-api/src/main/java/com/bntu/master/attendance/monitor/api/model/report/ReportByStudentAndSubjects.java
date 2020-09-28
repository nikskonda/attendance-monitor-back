package com.bntu.master.attendance.monitor.api.model.report;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportByStudentAndSubjects {

    private PersonDto student;
    private ObjectRef subject;
    private Integer attendanceHours = 0;

    public void addHours(Integer hours) {
        attendanceHours += hours;
    }

}
