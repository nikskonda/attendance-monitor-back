package com.bntu.master.attendance.monitor.api.model.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalTimeSpan {

    private LocalTime startTime;
    private LocalTime endTime;

    public LocalTimeSpan(LocalDateTime start, LocalDateTime end) {
        startTime = start.toLocalTime();
        endTime = end.toLocalTime();
    }

}
