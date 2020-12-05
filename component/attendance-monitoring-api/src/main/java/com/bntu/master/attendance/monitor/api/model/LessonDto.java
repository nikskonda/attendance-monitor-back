package com.bntu.master.attendance.monitor.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class LessonDto extends ObjectRef {

    private LocalDate date;
    private LessonScheduleDto time;
    private ObjectRef subject;
    private SubjectTypeConstant subjectType;
    private ObjectRef professor;
    private ObjectRef group;
    private String groupVolume;

}
