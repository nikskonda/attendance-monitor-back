package com.bntu.master.attendance.monitor.api.rest;

import com.bntu.master.attendance.monitor.api.model.UserDto;
import com.bntu.master.attendance.monitor.api.model.util.UpdatePassword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RequestMapping("/user")
public interface UserRest {

    @GetMapping("/byEmail")
    UserDto find(@RequestParam String email);

    @PostMapping("/updatePassword")
    UserDto updatePassword(Authentication authentication,
                           @RequestBody UpdatePassword updatePassword);

    @PostMapping("/isUniqueEmail")
    boolean isUniqueEmail(@RequestBody String email);

    @GetMapping
    List<UserDto> findAll();

    @GetMapping("/page")
    Page<UserDto> findPage(Pageable pageable);

    @PostMapping("/resetPassword")
    void resetPassword(@RequestBody String email);

    @PostMapping("/updateRoles")
    UserDto updateRoles(@RequestBody UserDto userDto);

    @PostMapping
    UserDto create(@RequestBody UserDto user);

    @PutMapping("/{email}")
    UserDto update(@PathVariable String email,
                   @RequestBody UserDto user);

    @DeleteMapping(("/{email}"))
    void delete(@PathVariable String email);

}
