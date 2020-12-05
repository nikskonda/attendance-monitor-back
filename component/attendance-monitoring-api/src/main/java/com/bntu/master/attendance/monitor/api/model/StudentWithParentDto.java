package com.bntu.master.attendance.monitor.api.model;

import lombok.Data;

@Data
public class StudentWithParentDto extends StudentDto {

    private String parentEmail;

}
