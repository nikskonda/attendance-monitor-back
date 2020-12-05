package com.bntu.master.attendance.monitor.api.model.scheduleGrid;

import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.LessonScheduleDto;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Data
public class ScheduleGrid {

    private Integer rows;
    private Integer columns;

    private List<LocalDate> dateHeader;
    private List<LessonScheduleDto> timeHeader;

    private ScheduleCell[][] cells;


    public ScheduleGrid(Set<LocalDate> dateHeader, Set<LessonScheduleDto> timeHeader) {
        setDateHeader(new ArrayList<>(dateHeader));
        setTimeHeader(new ArrayList<>(timeHeader));
        cells = new ScheduleCell[rows][columns];
    }

    public void setCell(LessonDto lessonDto) {
        int row = 0;
        int col = 0;
        for (LocalDate date : dateHeader) {
            if (date.equals(lessonDto.getDate())) {
                break;
            }
            col++;
        }
        for (LessonScheduleDto time : timeHeader) {
            if (time.equals(lessonDto.getTime())) {
                break;
            }
            row++;
        }
        cells[row][col] = new ScheduleCell(lessonDto);
    }

    public void setDateHeader(List<LocalDate> dateHeader) {
        this.dateHeader = dateHeader;
        this.dateHeader.sort(LocalDate::compareTo);
        columns = dateHeader.size();
    }

    public void setTimeHeader(List<LessonScheduleDto> timeHeader) {
        this.timeHeader = timeHeader;
        this.timeHeader.sort(Comparator.comparing(LessonScheduleDto::getOrder));
        rows = timeHeader.size();
    }

    public Pair<List<ScheduleCell>, List<ScheduleCell>> toCellList(boolean topDateHeader) {
        List<ScheduleCell> header = new ArrayList<>();
        ScheduleCell cell = new ScheduleCell();
        cell.setText("");
        cell.setHeader(true);
        header.add(cell);
        addTopHeader(header, topDateHeader);

        List<ScheduleCell> toReturnBody = new ArrayList<>();
        if (topDateHeader) {
            for (int i = 0; i < timeHeader.size(); i++) {
                toReturnBody.add(new ScheduleCell(timeHeader.get(i), i + 1, 0));
                for (int j = 0; j < dateHeader.size(); j++) {
                    ScheduleCell c = cells[i][j];
                    if (c == null) {
                        toReturnBody.add(ScheduleCell.empty(j + 1, i + 1));
                    } else {
                        c.setPlace(j + 1, i + 1);
                        toReturnBody.add(c);
                    }

                }
            }
        } else {
            for (int j = 0; j < dateHeader.size(); j++) {
                toReturnBody.add(new ScheduleCell(dateHeader.get(j), j + 1, 0));
                for (int i = 0; i < timeHeader.size(); i++) {
                    ScheduleCell c = cells[i][j];
                    if (c == null) {
                        toReturnBody.add(ScheduleCell.empty(i + 1, j + 1));
                    } else {
                        c.setPlace(i + 1, j + 1);
                        toReturnBody.add(c);
                    }
                }
            }
        }
        return Pair.of(header, toReturnBody);
    }


    private void addTopHeader(List<ScheduleCell> toReturn, boolean topDateHeader) {
        int colIndex = 1;
        if (topDateHeader) {
            for (LocalDate date : dateHeader) {
                toReturn.add(new ScheduleCell(date, 0, colIndex++));
            }
        } else {
            for (LessonScheduleDto date : timeHeader) {
                toReturn.add(new ScheduleCell(date, 0, colIndex++));
            }
        }
    }

}
