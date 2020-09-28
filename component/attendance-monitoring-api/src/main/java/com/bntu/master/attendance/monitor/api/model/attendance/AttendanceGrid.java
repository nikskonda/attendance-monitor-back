package com.bntu.master.attendance.monitor.api.model.attendance;

import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class AttendanceGrid {

    private Integer rows;
    private Integer columns;

    private List<LessonDto> lessonHeader;
    private Map<Long, Integer> lessonIndexMap; //key: lessonId  value:lessonIndex
    private List<PersonDto> personHeader;
    private Map<Long, Integer> personIndexMap; //key: personId  value:personIndex


    private AttendanceCell[][] cells;

    public AttendanceGrid(Set<LessonDto> lessonHeader, List<PersonDto> personHeader) {
        setLessonHeader(lessonHeader);
        setPersonHeader(personHeader);
        cells = new AttendanceCell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells[i][j] = AttendanceCell.empty(this.lessonHeader.get(j), this.personHeader.get(i));
            }
        }
    }

    private void setLessonHeader(Set<LessonDto> lessonHeader) {
        this.lessonHeader = new ArrayList<>(lessonHeader);
        this.lessonHeader.sort(Comparator.comparing(l -> l.getTime().getStartTime()));
        columns = this.lessonHeader.size();
        lessonIndexMap = new HashMap<>();
        for (int i = 0; i < this.lessonHeader.size(); i++) {
            lessonIndexMap.put(this.lessonHeader.get(i).getId(), i);
        }
    }

    private void setPersonHeader(List<PersonDto> personHeader) {
        this.personHeader = new ArrayList<>(personHeader);
        this.personHeader.sort(Comparator.comparing(PersonDto::getLastName));
        rows = this.personHeader.size();
        personIndexMap = new HashMap<>();
        for (int i = 0; i < this.personHeader.size(); i++) {
            personIndexMap.put(this.personHeader.get(i).getId(), i);
        }
    }

    public void setCell(ObjectRef person, ObjectRef lesson, AttendanceValue value) {
        int rowIndex = personIndexMap.get(person.getId());
        int colIndex = lessonIndexMap.get(lesson.getId());
        AttendanceCell cell = new AttendanceCell();
        cell.setLesson(lesson);
        cell.setPerson(person);
        cell.setValue(value);
        cell.setText(cell.getValue().getText());
        cell.setPosition(rowIndex, colIndex);
        cells[rowIndex][colIndex] = cell;
    }

    public List<AttendanceCell> toCellList() {
        List<AttendanceCell> toReturn = new ArrayList<>();
        int rowIndex = 0;
        int colIndex = 0;

        toReturn.add(AttendanceCell.empty());
        for (LessonDto lesson : lessonHeader) {
            toReturn.add(new AttendanceCell(lesson));
        }

        for (AttendanceCell[] row : cells) {
            rowIndex++;
            ObjectRef person = personHeader.get(rowIndex - 1);
            toReturn.add(new AttendanceCell(personHeader.get(rowIndex - 1)));

            for (AttendanceCell c : row) {
                if (c != null) {

                } else {
                    c = AttendanceCell.empty();
                }
                toReturn.add(c);
            }
        }
        return toReturn;
    }
}
