package com.bntu.master.attendance.monitor.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto extends ObjectRef {

    private ObjectRef speciality;

}
