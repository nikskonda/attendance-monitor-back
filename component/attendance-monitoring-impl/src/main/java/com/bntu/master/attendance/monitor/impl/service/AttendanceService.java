package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.model.AttendancePage;
import com.bntu.master.attendance.monitor.api.model.AttendanceValue;
import com.bntu.master.attendance.monitor.api.model.DateSpan;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.impl.dataaccess.AttendanceRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.LessonRepository;
import com.bntu.master.attendance.monitor.impl.entity.Attendance;
import com.bntu.master.attendance.monitor.impl.entity.Lesson;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.resolver.GroupResolver;
import com.bntu.master.attendance.monitor.impl.resolver.LessonResolver;
import com.bntu.master.attendance.monitor.impl.resolver.PersonResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository repository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonResolver lessonResolver;

    @Autowired
    private PersonResolver personResolver;

    @Autowired
    private GroupResolver groupResolver;

    public AttendancePage update(AttendancePage attendancePage) {
        List<Attendance> attendances = new ArrayList<>();
        Set<Lesson> lessons = new HashSet<>();
        Set<Person> students = new HashSet<>();
        for (ObjectRef lessonRef : attendancePage.getMap().keySet()) {
            Lesson lesson = lessonResolver.resolve(lessonRef);
            lessons.add(lesson);
            Map<ObjectRef, AttendanceValue> attendanceByStudents = attendancePage.getMap().get(lessonRef);
            for (ObjectRef studRef : attendanceByStudents.keySet()) {
                Person student = personResolver.resolveByRole(studRef, RoleConstant.STUDENT);
                students.add(student);

                Attendance attendance = new Attendance();
                attendance.setDateTime(LocalDateTime.now());
                attendance.setStudent(student);
                attendance.setProfessor(lesson.getProfessor());
                attendance.setLesson(lesson);
                attendance.setValue(attendanceByStudents.get(studRef));
                attendances.add(attendance);
            }
        }
        repository.deleteByStudentInAndLessonIn(students, lessons);
        attendances = repository.saveAll(attendances);
        return convert(attendances);
    }

    public AttendancePage getAttendanceForGroup(ObjectRef studentGroup, Long personId, DateSpan dateSpan) {
        List<Lesson> lessons = lessonRepository.findAllByDateBetween(dateSpan.getStartDate(), dateSpan.getFinishDate());
        List<Attendance> attendances = repository.findAllByStudentGroupKeyAndLessonIn(groupResolver.resolve(studentGroup).getKey(), lessons);
        return convert(attendances);
    }

    private AttendancePage convert(List<Attendance> list) {
        Map<ObjectRef, Map<ObjectRef, AttendanceValue>> map = new HashMap<>();
        for (Attendance attendance : list) {
            ObjectRef lessonRef = new ObjectRef(attendance.getLesson().getId());
            ObjectRef studentRef = new ObjectRef(attendance.getStudent().getId());
            if (map.containsKey(lessonRef)) {
                map.get(lessonRef).put(studentRef, attendance.getValue());
            } else {
                Map<ObjectRef, AttendanceValue> innerMap = new HashMap<>();
                innerMap.put(studentRef, attendance.getValue());
                map.put(lessonRef, innerMap);
            }
        }
        AttendancePage page = new AttendancePage();
        page.setMap(map);
        return page;
    }

}
