package com.bntu.master.attendance.monitor.impl.dataaccess;

import com.bntu.master.attendance.monitor.api.model.SubjectTypeConstant;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Lesson;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findAllByDateBetweenAndGroup(LocalDate starDate, LocalDate finishDate, Group group);

    List<Lesson> findAllByGroup(Group group);

    List<Lesson> findAllByDateBetweenAndGroupAndSubject(LocalDate starDate, LocalDate finishDate, Group group, Subject subject);

    List<Lesson> findAllByDateBetween(LocalDate starDate, LocalDate finishDate);

    Page<Lesson> findAllByDateBetween(LocalDate starDate, LocalDate finishDate, Pageable pageable);

    Set<Lesson> findAllByDateBetweenAndProfessor(LocalDate starDate, LocalDate finishDate, Person prof);

    Set<Lesson> findAllByDateBetweenAndSubjectAndSubjectTypeIn(LocalDate starDate, LocalDate finishDate, Subject subject, Set<SubjectTypeConstant> subjectType);

    Set<Lesson> findAllByDateBetweenAndGroupAndSubjectAndSubjectTypeIn(LocalDate starDate, LocalDate finishDate, Group groupId, Subject subject, Set<SubjectTypeConstant> subjectType);

}
