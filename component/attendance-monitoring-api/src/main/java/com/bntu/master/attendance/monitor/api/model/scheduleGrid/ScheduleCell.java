package com.bntu.master.attendance.monitor.api.model.scheduleGrid;

import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.util.LocalTimeSpan;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public ScheduleCell(LocalDate date, int cols){
        text = date.toString();
        LocalDate current = LocalDate.now();
        color = current.equals(date) ? "#A0FF95" : "#9DF9F3";
        setPlace(cols, 0);
        setHeader(true);
    }

    public ScheduleCell(LocalTimeSpan timeSpan, int row){
        text = timeSpan.getStartTime().toString() + " - " + timeSpan.getEndTime().toString();
        setPlace(0, row);
        setHeader(true);
    }

    public ScheduleCell(LessonDto lessonDto) {
        text = lessonDto.getSubject() + "\n" + lessonDto.getGroup().getQualifier();
        LocalDateTime current = LocalDateTime.now();
        color = current.toLocalDate().equals(lessonDto.getDate()) ? current.isAfter(lessonDto.getStartTime()) && current.isBefore(lessonDto.getFinishTime()) ? "lightpink" : "#A0FF95" : "#9DF9F3";
        lesson = lessonDto;
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

    public static ScheduleCell empty(int col, int row){
        ScheduleCell cell = new ScheduleCell();
        cell.setPlace(col, row);
        cell.setText("");
        cell.setEmpty(true);
        return cell;
    }
}
