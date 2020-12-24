package com.bntu.master.attendance.monitor.api.model.report;

import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.StudentDto;
import com.bntu.master.attendance.monitor.api.model.SubjectTypeConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
public class ReportByStudent {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Row {

        private ObjectRef subject;
        private SubjectTypeConstant type;
        private LocalDate date;
        private LocalTime time;

        private int attHours = 0;
        private int goodHours = 0;

        private void addAtt(int hours) {
            this.attHours += hours;
        }

        private void addGood(int hours) {
            this.goodHours += hours;
        }

        private void addHours(Row hours) {
            attHours += (hours.getAttHours());
            goodHours += (hours.getGoodHours());
        }

        public String toStringAttendance() {
            return String.format("%d/%d", attHours+goodHours, goodHours);
        }
    }

    private StudentDto student;
    private LocalDate startDate;
    private LocalDate finishDate;

    private List<Row> list;

    public ReportByStudent(StudentDto student, LocalDate startDate, LocalDate finishDate) {
        this.student = student;
        this.startDate = startDate;
        this.finishDate = finishDate;
        list = new ArrayList<>();
    }

    public void add(LessonDto lessonDto, int attHours, boolean isGoodHours) {
        Row row = new Row();
        row.setDate(lessonDto.getDate());
        row.setSubject(lessonDto.getSubject());
        row.setType(lessonDto.getSubjectType());
        row.setTime(lessonDto.getTime().getStartTime());
        if (isGoodHours) {
            row.setGoodHours(attHours);
        } else {
            row.setAttHours(attHours);
        }
        list.add(row);
    }

    public List<List<String>> toStringGrid() {
        List<List<String>> result = new ArrayList<>();
        result.add(Arrays.asList("Дисциплина", "Вид", "Дата", "Время", "Пропущено (ч)"));
        Collections.sort(list, Comparator.comparing(row -> LocalDateTime.of(row.getDate(), row.getTime())));
        Row sum = new Row();
        DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY");
        for (Row item : list) {
            List<String> row = new ArrayList<>();
            row.add(item.getSubject().getQualifier());
            row.add(item.getType().getText());
            row.add(item.getDate().format(formatter));
            row.add(item.getTime().toString());
            row.add(item.toStringAttendance());
            sum.addHours(item);
            result.add(row);
        }
        result.add(Arrays.asList("", "", "", "Сумма", sum.toStringAttendance()));
        return result;
    }
}
