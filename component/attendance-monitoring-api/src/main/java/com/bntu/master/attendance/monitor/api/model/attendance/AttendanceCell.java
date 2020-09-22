package com.bntu.master.attendance.monitor.api.model.attendance;

import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.api.model.util.LocalTimeSpan;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AttendanceCell {

    private String text;
//    private String color = "#9DF9F3";
    private AttendanceValue value;
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
        text = lessonDto.getSubjectType() + " " + lessonDto.getStartTime() + " - " + lessonDto.getFinishTime();
        isHeader = true;
    }

    public void setPosition(int row, int col){
        this.row = row;
        this.col = col;
    }

    public static AttendanceCell empty(){
        AttendanceCell cell = new AttendanceCell();
        cell.setEmpty(true);
        cell.setText("");
        return cell;
    }

}
