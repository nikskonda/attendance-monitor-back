package com.bntu.master.attendance.monitor.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class LessonScheduleDto extends ObjectRef {

    private Long order;
    private LocalTime startTime;
    private LocalTime finishTime;

}
