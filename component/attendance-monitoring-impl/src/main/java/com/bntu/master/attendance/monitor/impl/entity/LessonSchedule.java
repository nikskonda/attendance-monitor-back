package com.bntu.master.attendance.monitor.impl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalTime;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LessonSchedule implements Base {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lesson_schedule_generator")
    private Long id;

    @Column(name = "sort_order")
    private Long order;

    private LocalTime startTime;
    private LocalTime finishTime;
    private String shift;

}
