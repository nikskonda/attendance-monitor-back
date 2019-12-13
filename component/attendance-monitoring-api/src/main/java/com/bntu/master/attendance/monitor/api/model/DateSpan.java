package com.bntu.master.attendance.monitor.api.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DateSpan {

    private LocalDate startDate;
    private LocalDate finishDate;

}
