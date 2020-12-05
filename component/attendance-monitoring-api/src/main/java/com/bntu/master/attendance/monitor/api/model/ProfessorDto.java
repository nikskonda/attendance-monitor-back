package com.bntu.master.attendance.monitor.api.model;

import lombok.Data;

@Data
public class ProfessorDto extends PersonDto {

    private ObjectRef position;

}
