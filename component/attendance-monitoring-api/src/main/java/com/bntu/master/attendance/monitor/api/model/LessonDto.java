package com.bntu.master.attendance.monitor.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class LessonDto extends ObjectRef {

    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private ObjectRef subject;
    private SubjectTypeConstant subjectType;
    private ObjectRef professor;
    private ObjectRef group;

}
