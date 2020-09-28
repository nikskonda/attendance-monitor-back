package com.bntu.master.attendance.monitor.api.model;

import lombok.Data;

import java.util.Set;

@Data
public class StudentDto extends PersonDto {

    private ObjectRef group;
}
