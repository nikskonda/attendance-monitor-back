package com.bntu.master.attendance.monitor.api.model;

import lombok.Data;

@Data
public class StudentDto extends PersonDto {

    private ObjectRef group;
    private String groupVolume;

}
