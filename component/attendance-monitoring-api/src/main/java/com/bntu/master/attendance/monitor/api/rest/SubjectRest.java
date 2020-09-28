package com.bntu.master.attendance.monitor.api.rest;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.SubjectTypeConstant;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@CrossOrigin
@RequestMapping("/subject")
public interface SubjectRest {

    @GetMapping("/{id}")
    ObjectRef find(@PathVariable Long id, @PathVariable String qualifier);

    @GetMapping
    List<ObjectRef> findAll();

    @PostMapping
    ObjectRef create(@RequestBody ObjectRef speciality);

    @PutMapping("/{id}")
    ObjectRef update(@PathVariable Long id,
                     @RequestBody ObjectRef subject);

    @DeleteMapping(("/{id}"))
    void delete(@PathVariable Long id);

    @GetMapping("/types")
    SubjectTypeConstant[] getSubjectTypes();

}
