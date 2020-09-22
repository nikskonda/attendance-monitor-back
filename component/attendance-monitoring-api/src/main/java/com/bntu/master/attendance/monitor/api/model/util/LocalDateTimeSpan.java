package com.bntu.master.attendance.monitor.api.model.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalDateTimeSpan {

    private LocalDateTime startDateTime;
    private LocalDateTime finishDateTime;

}
