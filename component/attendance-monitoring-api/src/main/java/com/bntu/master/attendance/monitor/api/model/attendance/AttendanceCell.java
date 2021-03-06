package com.bntu.master.attendance.monitor.api.model.attendance;

import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttendanceCell {

    private String text;
    //    private String color = "#9DF9F3";
    private AttendanceValue value;
    private boolean isGoodReason;
    private ObjectRef lesson;
    private ObjectRef person;
    private int row;
    private int col;
    private boolean isEmpty = false;
    private boolean isHeader = false;

    public AttendanceCell(PersonDto person) {
        this.person = ObjectRef.toObjectRef(person.getId(), person.getQualifier());
        text = person.getFullName();
        isHeader = true;
    }

    public AttendanceCell(LessonDto lessonDto) {
        this.lesson = lessonDto;
        text = String.format("%s\n%s - %s (%s)", lessonDto.getDate(), lessonDto.getTime().getStartTime(), lessonDto.getTime().getFinishTime(), lessonDto.getSubjectType());
        isHeader = true;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static AttendanceCell empty() {
        return empty("");
    }

    public static AttendanceCell empty(String text) {
        AttendanceCell cell = new AttendanceCell();
        cell.setEmpty(true);
        cell.setText(text);
        return cell;
    }

    public static AttendanceCell empty(LessonDto lessonDto, PersonDto person) {
        AttendanceCell cell = new AttendanceCell();
        cell.setEmpty(true);
        cell.setText("");
        cell.setLesson(lessonDto);
        cell.setPerson(person);
        return cell;
    }

}
