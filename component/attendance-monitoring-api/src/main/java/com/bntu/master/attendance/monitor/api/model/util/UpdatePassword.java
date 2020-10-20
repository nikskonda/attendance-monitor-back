package com.bntu.master.attendance.monitor.api.model.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePassword {

    private String email;
    private String oldPassword;
    private String newPassword;

}
