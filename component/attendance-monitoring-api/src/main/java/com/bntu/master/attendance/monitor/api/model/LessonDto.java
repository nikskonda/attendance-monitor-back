package com.bntu.master.attendance.monitor.api.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LessonDto extends ObjectRef {

    private LocalDate date;
    private String subject;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private ObjectRef professor;
    private ObjectRef group;

}
