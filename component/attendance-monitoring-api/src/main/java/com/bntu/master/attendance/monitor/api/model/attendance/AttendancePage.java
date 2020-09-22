package com.bntu.master.attendance.monitor.api.model.attendance;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class AttendancePage {

    private List<AttendanceByStudentByLesson> studentAttendanceByLesson;


    @JsonIgnore
    public Map<ObjectRef, Map<ObjectRef, AttendanceValue>> getMap() {
        Map<ObjectRef, Map<ObjectRef, AttendanceValue>> map = new HashMap<>();
        for (AttendanceByStudentByLesson lesson : studentAttendanceByLesson) {
            Map<ObjectRef, AttendanceValue> innerMap = new HashMap<>();
            for (AttendanceByStudent stud : lesson.students) {
                innerMap.put(stud.student, stud.value);
            }
            map.put(lesson.lesson, innerMap);
        }
        return map;
    }

    public void setMap(Map<ObjectRef, Map<ObjectRef, AttendanceValue>> map) {
        studentAttendanceByLesson = new ArrayList<>();
        for (ObjectRef lessonRef : map.keySet()) {
            AttendanceByStudentByLesson byLesson = new AttendanceByStudentByLesson();
            byLesson.lesson = lessonRef;
            byLesson.students = new ArrayList<>();
            for (ObjectRef studRef : map.get(lessonRef).keySet()) {
                AttendanceByStudent byStudent = new AttendanceByStudent();
                byStudent.student = studRef;
                byStudent.value = map.get(lessonRef).get(studRef);
                byLesson.students.add(byStudent);
            }
            studentAttendanceByLesson.add(byLesson);
        }
    }

    @Data
    class AttendanceByStudentByLesson {
        private ObjectRef lesson;
        private List<AttendanceByStudent> students;
    }

    @Data
    class AttendanceByStudent {
        private ObjectRef student;
        private AttendanceValue value;
    }
}
