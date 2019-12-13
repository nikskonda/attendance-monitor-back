package com.bntu.master.attendance.monitor.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateSpan {

    private LocalDate startDate;
    private LocalDate finishDate;

}
