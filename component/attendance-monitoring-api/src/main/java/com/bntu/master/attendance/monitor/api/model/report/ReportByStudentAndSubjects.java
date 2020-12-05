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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ReportByStudentAndSubjects {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Cell {
        private int goodHours = 0;
        private int attHours = 0;
        private int totalHours = 0;

        private void addGood(int hours) {
            goodHours += hours;
            addAtt(hours);
        }

        private void addAtt(int hours) {
            attHours += hours;
        }

        private void addTotal() {
            totalHours += 2;
        }

        private void add(Cell cell) {
            goodHours += cell.getGoodHours();
            attHours += (cell.getAttHours());
            totalHours += cell.getTotalHours();
        }

        @Override
        public String toString() {
            return String.format("%d/%d/%d", attHours, goodHours, totalHours);
        }
    }

    private PersonDto student;
    private LocalDate startDate;
    private LocalDate finishDate;

    private List<SubjectTypeConstant> subjectTypes;
    private List<ObjectRef> subjects;
    private Cell[][] cells;

    public ReportByStudentAndSubjects(Set<SubjectTypeConstant> subjectTypes, Set<ObjectRef> subjects) {
        setSubjectTypes(subjectTypes);
        setSubjects(subjects);
        cells = new Cell[subjects.size()][subjectTypes.size()];
    }

    public void setSubjectTypes(Set<SubjectTypeConstant> subjectTypes) {
        this.subjectTypes = subjectTypes.stream()
                .sorted(Comparator.comparing(SubjectTypeConstant::getSortOrder))
                .collect(Collectors.toList());
    }

    public void setSubjects(Set<ObjectRef> subjects) {
        this.subjects = subjects.stream().sorted(Comparator.comparing(ObjectRef::getQualifier)).collect(Collectors.toList());
    }

    public Pair<Integer, Integer> getIndex(ObjectRef subject, SubjectTypeConstant type) {
        int i;
        int j;
        for (i = 0; i < subjects.size(); i++) {
            if (subject.equals(subjects.get(i))) {
                break;
            }
        }
        for (j = 0; i < subjectTypes.size(); j++) {
            if (type.equals(subjectTypes.get(j))) {
                break;
            }
        }
        return Pair.of(i, j);
    }

    public void setHours(ObjectRef subject, SubjectTypeConstant type, Integer goodHours, Integer attHours, Integer totalHours) {
        Pair<Integer, Integer> index = getIndex(subject, type);
        cells[index.getLeft()][index.getRight()] = new Cell(goodHours, attHours, totalHours);
    }

    public void addAttHours(ObjectRef subject, SubjectTypeConstant type, Integer hours, boolean isGoodReason) {
        Pair<Integer, Integer> index = getIndex(subject, type);
        Cell cell = cells[index.getLeft()][index.getRight()];
        if (cell == null) {
            cells[index.getLeft()][index.getRight()] = new Cell();
            cell = cells[index.getLeft()][index.getRight()];
        }

        if (isGoodReason) {
            cell.addGood(hours);
        } else {
            cell.addAtt(hours);
        }
    }

    public void addTotalHours(ObjectRef subject, SubjectTypeConstant type) {
        Pair<Integer, Integer> index = getIndex(subject, type);
        Cell cell = cells[index.getLeft()][index.getRight()];
        if (cell == null) {
            cells[index.getLeft()][index.getRight()] = new Cell(0, 0, 2);
        } else {
            cell.addTotal();
        }
    }

    public List<List<String>> toStringGrid() {
        List<List<String>> result = new ArrayList<>();
        List<String> row = new ArrayList<>();
        row.add("Дисциплина");
        for (SubjectTypeConstant type : subjectTypes) {
            row.add(type.getText());
        }
        row.add("Сумма");
        result.add(row);
        Cell sum = new Cell();

        int rowIndex = 0;
        for (ObjectRef subject : subjects) {
            row = new ArrayList<>();
            row.add(subject.getQualifier());

            Cell total = new Cell();
            for (int i = 0; i < subjectTypes.size(); i++) {
                Cell cell = cells[rowIndex][i];
                if (cell != null) {
                    row.add(cell.toString());
                    total.add(cell);
                } else {
                    row.add("");
                }
            }
            sum.add(total);
            row.add(total.toString());
            result.add(row);
            rowIndex++;
        }
        row = new ArrayList<>();
        for (int i = 0; i < subjectTypes.size() + 1; i++) {
            row.add("");
        }
        row.add(sum.toString());
        result.add(row);

        row = new ArrayList<>();
        for (int i = 0; i < subjectTypes.size() + 1; i++) {
            row.add("");
        }
        row.add(String.format("%.2f%%/%.2f%%", (float) (100 * sum.getAttHours() / sum.getTotalHours()), (float) (100 * sum.getGoodHours() / sum.getTotalHours())));
        result.add(row);
        return result;
    }
}
