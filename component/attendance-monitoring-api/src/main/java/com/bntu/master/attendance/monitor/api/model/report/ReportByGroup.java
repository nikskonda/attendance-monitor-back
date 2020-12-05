package com.bntu.master.attendance.monitor.api.model.report;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.api.model.SubjectTypeConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ReportByGroup {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Cell {
        private int attHours = 0;
        private int goodHours = 0;

        private void addAtt(int hours) {
            this.attHours += hours;
        }

        private void addGood(int hours) {
            this.goodHours += hours;
            addAtt(hours);
        }

        private void addHours(Cell hours) {
            if (hours == null) return;
            this.attHours += (hours.getAttHours());
            this.goodHours += (hours.getGoodHours());
        }

        @Override
        public String toString() {
            return String.format("%d/%d", attHours, goodHours);
        }
    }

    private ObjectRef subject;
    private ObjectRef group;
    private LocalDate startDate;
    private LocalDate finishDate;

    private List<SubjectTypeConstant> subjectTypes;
    private Map<SubjectTypeConstant, Integer> lessonTotal;
    private Map<SubjectTypeConstant, Cell> attTotal;
    private List<PersonDto> students;
    private Cell[][] cells;

    public ReportByGroup(Set<SubjectTypeConstant> subjectTypes, Set<PersonDto> students) {
        setSubjectTypes(subjectTypes);
        setStudents(students);
        cells = new Cell[students.size()][subjectTypes.size()];
        lessonTotal = new HashMap<>();
        attTotal = new HashMap<>();
        for (SubjectTypeConstant type : subjectTypes) {
            lessonTotal.put(type, 0);
            attTotal.put(type, new Cell());
        }
    }

    public void addAttTotal(SubjectTypeConstant type, int hour) {
        Cell cell = attTotal.get(type);
        cell.addAtt(hour);
    }

    public void addLessonTotal(SubjectTypeConstant type) {
        lessonTotal.put(type, lessonTotal.get(type) + 2);
    }

    public void setSubjectTypes(Set<SubjectTypeConstant> subjectTypes) {
        this.subjectTypes = subjectTypes.stream()
                .sorted(Comparator.comparing(SubjectTypeConstant::getSortOrder))
                .collect(Collectors.toList());
    }

    public void setStudents(Set<PersonDto> students) {
        this.students = students.stream().sorted(Comparator.comparing(PersonDto::getFullName)).collect(Collectors.toList());
    }

    public Pair<Integer, Integer> getIndex(PersonDto student, SubjectTypeConstant type) {
        int i;
        int j;
        for (i = 0; i < students.size(); i++) {
            if (student.equals(students.get(i))) {
                break;
            }
        }
        for (j = 0; j < subjectTypes.size(); j++) {
            if (type.equals(subjectTypes.get(j))) {
                break;
            }
        }
        return Pair.of(i, j);
    }

    public void addHours(PersonDto sudent, SubjectTypeConstant type, Integer attHours, boolean isGoodReason) {
        Pair<Integer, Integer> index = getIndex(sudent, type);
        Cell cell = cells[index.getLeft()][index.getRight()];
        if (cell == null) {
            cells[index.getLeft()][index.getRight()] = new Cell();
            cell = cells[index.getLeft()][index.getRight()];
        }
        if (isGoodReason) {
            cell.addGood(attHours);
        } else {
            cell.addAtt(attHours);
        }
    }

    public Map<SubjectTypeConstant, Cell> recalcAttTotal() {
        attTotal = new HashMap<>();
        for (int i = 0; i < subjectTypes.size(); i++) {
            Cell total = new Cell();
            for (int j = 0; j < students.size(); j++) {
                total.addHours(cells[j][i]);
            }
            attTotal.put(subjectTypes.get(i), total);
        }
        return attTotal;
    }

    public List<List<String>> toStringGrid() {
        List<List<String>> result = new ArrayList<>();
        List<String> row = new ArrayList<>();
        row.add("");
        for (SubjectTypeConstant type : subjectTypes) {
            row.add(type.getText());
        }
        row.add("Всего (ч)");
        row.add("Всего (%)");
        result.add(row);

        int sumLessons = 0;
        List<String> lessonCountRow = new ArrayList<>();
        lessonCountRow.add("Всего занятий (ч)");
        for (SubjectTypeConstant type : subjectTypes) {
            sumLessons += lessonTotal.get(type);
            lessonCountRow.add(lessonTotal.get(type).toString());
        }
        lessonCountRow.add(String.format("%d", sumLessons));

        int rowIndex = 0;
        for (PersonDto student : students) {
            row = new ArrayList<>();
            row.add(student.getFullName());

            Cell attSum = new Cell();
            for (int i = 0; i < subjectTypes.size(); i++) {
                Cell cell = cells[rowIndex][i];
                if (cell != null) {
                    attSum.addHours(cell);
                    row.add(cell.toString());
                } else {
                    row.add("");
                }
            }
            row.add(attSum.toString());
            row.add(String.format("%.2f%% / %.2f%%",
                    sumLessons == 0 ? 0 : (float) (100 * attSum.getAttHours() / sumLessons),
                    sumLessons == 0 ? 0 : (float) (100 * attSum.getGoodHours() / sumLessons)));
            result.add(row);
            rowIndex++;
        }
        row = new ArrayList<>();
        Cell sum = new Cell();
        row.add("Посещаемость группы (ч)");
        recalcAttTotal();
        for (SubjectTypeConstant type : subjectTypes) {
            sum.addHours(attTotal.get(type));
            row.add(attTotal.get(type).toString());
        }
        row.add(sum.toString());
        row.add("");
        result.add(row);

        row = new ArrayList<>();
        row.add("Посещаемость группы (%)");
        for (SubjectTypeConstant type : subjectTypes) {
            row.add(String.format("%.2f%% / %.2f%%",
                    (100F * attTotal.get(type).getAttHours() / (lessonTotal.get(type) * students.size())),
                    (100F * attTotal.get(type).getGoodHours() / (lessonTotal.get(type) * students.size()))
            ));
        }
        row.add(String.format("%.2f%% / %.2f%%",
                (100F * sum.getAttHours() / sumLessons * students.size()),
                (100F * sum.getGoodHours() / sumLessons * students.size())));
        row.add("");
        result.add(row);

        lessonCountRow.add("");
        result.add(lessonCountRow);

        return result;
    }
}
