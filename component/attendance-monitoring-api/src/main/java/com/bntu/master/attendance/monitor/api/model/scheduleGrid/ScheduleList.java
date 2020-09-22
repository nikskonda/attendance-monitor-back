package com.bntu.master.attendance.monitor.api.model.scheduleGrid;

import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.api.model.attendance.AttendanceCell;
import com.bntu.master.attendance.monitor.api.model.util.LocalTimeSpan;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Data
public class ScheduleList {

    private int rows;
    private int cols;
    private PersonDto professor;
    private List<ScheduleCell> cells;


    public ScheduleList(ScheduleGrid grid, PersonDto professor) {
        this.rows = grid.getRows()+1;
        this.cols = grid.getColumns()+1;
        this.professor = professor;
        cells = grid.toCellList();
    }
}
