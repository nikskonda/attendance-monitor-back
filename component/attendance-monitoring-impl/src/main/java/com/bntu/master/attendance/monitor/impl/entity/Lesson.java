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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "lesson_generator")
    private Long id;
    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;

    @ManyToOne
    private Person professor;

    @ManyToOne
    private Group group;

    @ManyToOne
    private Subject subject;

    private SubjectTypeConstant subjectType;

}
