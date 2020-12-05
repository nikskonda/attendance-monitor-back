package com.bntu.master.attendance.monitor.api.model.attendance;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceList {

    private int rows;
    private int cols;
    private ObjectRef subject;
    private ObjectRef group;
    private List<AttendanceCell> headers;
    private List<AttendanceCell> cells;


    public AttendanceList(AttendanceGrid grid, ObjectRef subject, ObjectRef group) {
        this.rows = grid.getRows() + 1;
        this.cols = grid.getColumns() + 1;
        this.cells = grid.toCellList();
        this.headers = grid.toHeaderList();
        this.subject = subject;
        this.group = group;
    }
}
