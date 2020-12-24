package com.bntu.master.attendance.monitor.api.model.scheduleGrid;

import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.LessonScheduleDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class ScheduleCell {

    private String text;
    private String color = "#9DF9F3";
    private LessonDto lesson;
    private int rows;
    private int cols;
    private boolean isEmpty = false;
    private boolean isHeader = false;

    public ScheduleCell(LocalDate date, int row, int col) {
        text = date.toString();
        setCol(date, null);
        setPlace(col, row);
        setHeader(true);
    }

    public ScheduleCell(LessonScheduleDto time, int row, int col) {
        text = String.format("%s - %s (%s)", time.getStartTime().toString(), time.getFinishTime().toString(), time.getShift().getValue());
        setPlace(col, row);
        setHeader(true);
    }

    public ScheduleCell(LessonDto lessonDto) {
        text = lessonDto.getSubject() + "\n" + lessonDto.getGroup().getQualifier();
        setCol(lessonDto.getDate(), lessonDto.getTime());
        lesson = lessonDto;
    }

    private boolean isToday(LocalDate date) {
        LocalDateTime current = LocalDateTime.now();
        return current.toLocalDate().equals(date);
    }

    public void setCol(LocalDate date, LessonScheduleDto time) {
        color = isToday(date) ?
                time != null && isBetweenCurrentTime(date, time.getStartTime(), time.getFinishTime())
                        ? "NOW"
                        : "TODAY"
                : null;
    }


    private boolean isBetweenCurrentTime(LocalDate date, LocalTime start, LocalTime finish) {
        LocalTime current = LocalTime.now();
        return isToday(date) && current.isAfter(start) && current.isBefore(finish);
    }

    private void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public void setHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }

    public void setPlace(int col, int row) {
        this.rows = row;
        this.cols = col;
    }

    public static ScheduleCell empty(int col, int row, LocalDate date, LessonScheduleDto time) {
        ScheduleCell cell = new ScheduleCell();
        cell.setPlace(col, row);
        cell.setText("");
        cell.setEmpty(true);
        LocalDate current = LocalDate.now();
        cell.setCol(date, time);
        return cell;
    }
}
