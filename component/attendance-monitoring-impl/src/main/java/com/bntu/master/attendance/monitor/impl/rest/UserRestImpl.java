package com.bntu.master.attendance.monitor.impl.rest;

import com.bntu.master.attendance.monitor.api.model.UserDto;
import com.bntu.master.attendance.monitor.api.rest.UserRest;
import com.bntu.master.attendance.monitor.impl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserRestImpl implements UserRest {

    @Autowired
    private UserService service;

    @Override
    public UserDto find(String email) {
        return (UserDto) service.loadUserByUsername(email);
    }

    @Override
    public List<UserDto> findAll() {
        return null;
    }

    @Override
    public UserDto create(UserDto user) {
        return null;
    }

    @Override
    public UserDto update(String email, UserDto user) {
        return null;
    }

    @Override
    public void delete(String email) {

    }
}
