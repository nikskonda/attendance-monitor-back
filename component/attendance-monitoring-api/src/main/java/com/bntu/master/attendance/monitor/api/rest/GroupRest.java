package com.bntu.master.attendance.monitor.api.rest;

import com.bntu.master.attendance.monitor.api.model.GroupDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@RequestMapping("/group")
public interface GroupRest {

    @GetMapping
    GroupDto find(@PathVariable Long id, @PathVariable String qualifier);

    @GetMapping
    List<GroupDto> findAll();

    @PostMapping
    GroupDto create(@RequestBody GroupDto group);

    @PutMapping("/{id}")
    GroupDto update(@PathVariable Long id,
                     @RequestBody GroupDto group);

    @DeleteMapping(("/{id}"))
    void delete(@PathVariable Long id);

}
