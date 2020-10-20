package com.bntu.master.attendance.monitor.impl.converter;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.api.model.StudentDto;
import com.bntu.master.attendance.monitor.api.model.UserDto;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.Role;
import com.bntu.master.attendance.monitor.impl.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Component
public class UserConverter {

    public UserDto convertToDto(User entity) {
        UserDto userDto = new UserDto();
        userDto.setFullName(entity.getFullName());
        userDto.setPassword(entity.getPassword());
        userDto.setQualifier(entity.getEmail());
        userDto.setMustUpdatePassword(!entity.getPasswordExpireDate().isAfter(LocalDate.now()));
        userDto.setRoles(entity.getRoles().stream().map(role -> ObjectRef.toObjectRef(role.getId(), role.getName())).collect(Collectors.toSet()));
        return userDto;
    }
}
