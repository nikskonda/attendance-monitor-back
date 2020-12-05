package com.bntu.master.attendance.monitor.impl.entity;

import com.bntu.master.attendance.monitor.api.model.SubjectTypeConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Lesson implements Base {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "lesson_generator")
    private Long id;
    private LocalDate date;

    @ManyToOne
    private LessonSchedule time;

    @ManyToOne
    private ProfessorPosition professor;

    @ManyToOne
    private Group group;

    private Long groupVolumeId;

    @ManyToOne
    private Subject subject;

    private SubjectTypeConstant subjectType;

}
