package com.bntu.master.attendance.monitor.impl.dataaccess;

import com.bntu.master.attendance.monitor.api.model.attendance.AttendanceValue;
import com.bntu.master.attendance.monitor.impl.entity.Attendance;
import com.bntu.master.attendance.monitor.impl.entity.Lesson;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

//    List<Attendance> findAllByStudentGroupKeyAndLessonIn(String groupKey, List<Lesson> lessons);

    List<Attendance> findAllByStudentInAndLessonIn(Set<Person> students, Set<Lesson> lessons);

    List<Attendance> findAllByStudentInAndLessonInAndValueIn(Set<Person> students, Set<Lesson> lessons, Set<AttendanceValue> values);

    List<Attendance> findAllByStudentAndLessonIn(Person student, List<Lesson> lessons);

    void deleteByStudentInAndLessonIn(Set<Person> students, Set<Lesson> lessons);

    Attendance findFirstByStudentAndLesson(Person student, Lesson lesson);

}
