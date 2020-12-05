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
        LocalDate current = LocalDate.now();
        color = current.equals(date) ? "#A0FF95" : "#9DF9F3";
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
        color = isToday(lessonDto) ? isBetweenCurrentTime(lessonDto) ? "lightpink" : "#A0FF95" : "#9DF9F3";
        lesson = lessonDto;
    }

    private boolean isToday(LessonDto lessonDto) {
        LocalDateTime current = LocalDateTime.now();
        return current.toLocalDate().equals(lessonDto.getDate());
    }

    private boolean isBetweenCurrentTime(LessonDto lessonDto) {
        LessonScheduleDto time = lessonDto.getTime();
        LocalTime current = LocalTime.now();
        return isToday(lessonDto) && current.isAfter(time.getStartTime()) && current.isBefore(time.getFinishTime());
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

    public static ScheduleCell empty(int col, int row) {
        ScheduleCell cell = new ScheduleCell();
        cell.setPlace(col, row);
        cell.setText("");
        cell.setEmpty(true);
        return cell;
    }
}
