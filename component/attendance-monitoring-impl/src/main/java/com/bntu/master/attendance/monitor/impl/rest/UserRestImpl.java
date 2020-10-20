package com.bntu.master.attendance.monitor.impl.rest;

import com.bntu.master.attendance.monitor.api.model.UserDto;
import com.bntu.master.attendance.monitor.api.model.util.UpdatePassword;
import com.bntu.master.attendance.monitor.api.rest.UserRest;
import com.bntu.master.attendance.monitor.impl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserRestImpl implements UserRest {

    @Autowired
    private UserService service;

    @Override
    public UserDto find(String email) {
        return service.loadUserByEmail(email);
    }

    @Override
    public UserDto updatePassword(Authentication authentication, UpdatePassword updatePassword) {
        if (authentication != null && authentication.getPrincipal() != null &&
                authentication.getPrincipal().equals(updatePassword.getEmail())) {
            return service.updatePassword(updatePassword.getEmail(), updatePassword.getOldPassword(), updatePassword.getNewPassword());
        }
        throw new RuntimeException();
    }

    @Override
    public List<UserDto> findAll() {
        return null;
    }

    @Override
    public Page<UserDto> findPage(Pageable pageable) {
        return service.findPage(pageable);
    }

    @Override
    public boolean isUniqueEmail(String email) {
        return service.isUniqueEmail(email);
    }

    @Override
    public void resetPassword(String email) {
        service.resetPassword(email);
    }

    @Override
    public UserDto updateRoles(UserDto userDto) {
        return service.updateRoles(userDto);
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
