package com.bntu.master.attendance.monitor.impl.dataaccess;

import com.bntu.master.attendance.monitor.api.model.SubjectTypeConstant;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Lesson;
import com.bntu.master.attendance.monitor.impl.entity.LessonSchedule;
import com.bntu.master.attendance.monitor.impl.entity.ProfessorPosition;
import com.bntu.master.attendance.monitor.impl.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    Optional<Lesson> findFirstByDateAndGroupAndTimeAndProfessorIdAndGroupVolumeIdAndSubjectAndSubjectType(LocalDate date, Group group, LessonSchedule time, Long professor, Long groupVolumeId, Subject subject, SubjectTypeConstant subjectType);

    List<Lesson> findAllByDateBetweenAndGroupAndGroupVolumeIdIn(LocalDate starDate, LocalDate finishDate, Group group, List<Long> volumes);

    List<Lesson> findAllByGroup(Group group);

    List<Lesson> findAllByDateBetweenAndGroupAndSubjectAndGroupVolumeIdIn(LocalDate starDate, LocalDate finishDate, Group group, Subject subject, List<Long> volumes);

    List<Lesson> findAllByDateBetweenAndGroupAndSubject(LocalDate starDate, LocalDate finishDate, Group group, Subject subject);

    List<Lesson> findAllByDateBetween(LocalDate starDate, LocalDate finishDate);

    Page<Lesson> findAllByDateBetween(LocalDate starDate, LocalDate finishDate, Pageable pageable);

    Set<Lesson> findAllByDateBetweenAndProfessor(LocalDate starDate, LocalDate finishDate, ProfessorPosition prof);

    Set<Lesson> findAllByDateBetweenAndSubjectAndSubjectTypeIn(LocalDate starDate, LocalDate finishDate, Subject subject, Set<SubjectTypeConstant> subjectType);

    Set<Lesson> findAllByDateBetweenAndGroupAndSubjectAndSubjectTypeInAndGroupVolumeId(LocalDate starDate, LocalDate finishDate, Group groupId, Subject subject, Set<SubjectTypeConstant> subjectType, Long groupVolumeId);

    Set<Lesson> findAllByDateBetweenAndGroupAndSubjectAndSubjectTypeIn(LocalDate starDate, LocalDate finishDate, Group groupId, Subject subject, Set<SubjectTypeConstant> subjectType);

}
