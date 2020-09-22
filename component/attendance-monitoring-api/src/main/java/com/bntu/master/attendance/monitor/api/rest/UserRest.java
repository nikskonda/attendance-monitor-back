package com.bntu.master.attendance.monitor.api.rest;

import com.bntu.master.attendance.monitor.api.model.UserDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RequestMapping("/user")
public interface UserRest {

    @GetMapping("/{id}")
    UserDto find(@PathVariable String qualifier);

    @GetMapping
    List<UserDto> findAll();

    @PostMapping
    UserDto create(@RequestBody UserDto user);

    @PutMapping("/{email}")
    UserDto update(@PathVariable String email,
                   @RequestBody UserDto user);

    @DeleteMapping(("/{email}"))
    void delete(@PathVariable String email);

}
