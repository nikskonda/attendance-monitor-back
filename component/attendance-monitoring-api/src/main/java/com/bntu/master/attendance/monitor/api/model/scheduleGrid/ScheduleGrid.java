package com.bntu.master.attendance.monitor.api.model.scheduleGrid;

import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.LessonScheduleDto;
import com.bntu.master.attendance.monitor.api.model.util.LocalTimeSpan;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Data
public class ScheduleGrid {

    private Integer rows;
    private Integer columns;

    private List<LocalDate> header;
    private List<LessonScheduleDto> rowHeader;

    private ScheduleCell[][] cells;


    public ScheduleGrid(Set<LocalDate> header, Set<LessonScheduleDto> rowHeader) {
        setHeader(new ArrayList<>(header));
        setRowHeader(new ArrayList<>(rowHeader));
        cells = new ScheduleCell[rows][columns];
    }

    public void setCell(LessonDto lessonDto){
        int row = 0;
        int col = 0;
        for (LocalDate date : header) {
            if (date.equals(lessonDto.getDate())) {
                break;
            }
            col++;
        }
        for (LessonScheduleDto time : rowHeader) {
            if (time.equals(lessonDto.getTime())) {
                break;
            }
            row++;
        }
        cells[row][col] = new ScheduleCell(lessonDto);
    }

    public void setHeader(List<LocalDate> header) {
        this.header = header;
        this.header.sort(LocalDate::compareTo);
        columns = header.size();
    }

    public void setRowHeader(List<LessonScheduleDto> rowHeader) {
        this.rowHeader = rowHeader;
        this.rowHeader.sort(Comparator.comparing(LessonScheduleDto::getOrder));
        rows = rowHeader.size();
    }

    public List<ScheduleCell> toCellList() {
        List<ScheduleCell> toReturn = new ArrayList<>();
        int rowIndex = 0;
        int colIndex = 0;

        ScheduleCell cell = new ScheduleCell();
        cell.setText("Время (смена)");
        cell.setHeader(true);
        toReturn.add(cell);
        for (LocalDate date : header) {
            toReturn.add(new ScheduleCell(date, colIndex++));
        }

        for (ScheduleCell[] row : cells) {
            rowIndex++;
            colIndex = 1;
            toReturn.add(new ScheduleCell(rowHeader.get(rowIndex-1), rowIndex));
            for (ScheduleCell c : row) {
                if (c != null) {
                    c.setPlace(colIndex++, rowIndex);
                } else {
                    c = ScheduleCell.empty(colIndex++, rowIndex);
                }
                toReturn.add(c);
            }
        }
        return toReturn;
    }


//    public void addRow(String rowHeader, List<Cell> row) {
//        if (cells == null) {
//            cells = new ArrayList<>();
//            this.rowHeader = new ArrayList<>();
//        }
//        while (row.size() < columns - 1){
//            row.add(Cell.empty());
//        }
//        cells.add(row);
//        this.rowHeader.add(rowHeader);
//    }
//
//    public void setRow(int rowIndex, String rowHeader, List<Cell> row) {
//        if (cells == null) {
//            cells = new ArrayList<>();
//            this.rowHeader = new ArrayList<>();
//        }
//        while (row.size() < columns - 1) {
//            row.add(Cell.empty());
//        }
//        cells.set(rowIndex, row);
//        this.rowHeader.set(rowIndex, rowHeader);
//    }


}
