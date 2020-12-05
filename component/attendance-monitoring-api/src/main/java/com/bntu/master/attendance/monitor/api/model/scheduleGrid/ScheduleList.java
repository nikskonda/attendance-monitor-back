package com.bntu.master.attendance.monitor.api.model.scheduleGrid;

import com.bntu.master.attendance.monitor.api.model.PersonDto;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@Data
public class ScheduleList {

    private int rows;
    private int cols;
    private PersonDto professor;
    private List<ScheduleCell> headers;
    private List<ScheduleCell> cells;


    public ScheduleList(ScheduleGrid grid, PersonDto professor, boolean topDateHeader) {
        if (topDateHeader) {
            this.rows = grid.getRows() + 1;
            this.cols = grid.getColumns() + 1;
        } else {
            this.rows = grid.getColumns() + 1;
            this.cols = grid.getRows() + 1;
        }
        this.professor = professor;
        Pair<List<ScheduleCell>, List<ScheduleCell>> table = grid.toCellList(topDateHeader);
        headers = table.getKey();
        cells = table.getValue();
    }
}
