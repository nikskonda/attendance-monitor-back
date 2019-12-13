package com.bntu.master.attendance.monitor.api.rest;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@RequestMapping("/speciality")
public interface SpecialityRest {

    @GetMapping
    ObjectRef find(@PathVariable Long id, @PathVariable String qualifier);

    @GetMapping
    List<ObjectRef> findAll();

    @PostMapping
    ObjectRef create(@RequestBody ObjectRef speciality);

    @PutMapping("/{id}")
    ObjectRef update(@PathVariable Long id,
                     @RequestBody ObjectRef speciality);

    @DeleteMapping(("/{id}"))
    void delete(@PathVariable Long id);

}
