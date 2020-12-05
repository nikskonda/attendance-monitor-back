package com.bntu.master.attendance.monitor.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class LessonSeries extends LessonDto {

    private Set<Integer> days;
    private LocalDate start;
    private LocalDate finish;
    private Long repeatWeek;

}
