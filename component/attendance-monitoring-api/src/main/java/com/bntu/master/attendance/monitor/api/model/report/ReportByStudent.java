package com.bntu.master.attendance.monitor.api.model.report;

import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.api.model.StudentDto;
import com.bntu.master.attendance.monitor.api.model.SubjectTypeConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
            this.addAtt(hours.getAttHours());
            this.addGood(hours.getGoodHours());
        }

        public String toStringAttendance() {
            return String.format("%d/%d", attHours, goodHours);
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
        result.add(Arrays.asList("Предмет", "Тип", "Дата", "Время", "Пропущено/по уважительной"));
        Collections.sort(list, Comparator.comparing(row -> LocalDateTime.of(row.getDate(), row.getTime())));
        for (Row item : list) {
            List<String> row = new ArrayList<>();
            row.add(item.getSubject().getQualifier());
            row.add(item.getType().name());
            row.add(item.getDate().toString());
            row.add(item.getTime().toString());
            row.add(item.toStringAttendance());
            result.add(row);
        }
        return result;
    }
}
