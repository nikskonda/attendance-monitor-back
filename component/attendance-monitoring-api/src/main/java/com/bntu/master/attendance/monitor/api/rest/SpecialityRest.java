package com.bntu.master.attendance.monitor.api.rest;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/speciality")
public interface SpecialityRest {

    @GetMapping("/{id}")
    ObjectRef find(@PathVariable Long id, @PathVariable String qualifier);

    @GetMapping
    List<ObjectRef> findAll();

    @GetMapping("/page")
    Page<ObjectRef> findByPage(Pageable pageable);

    @PostMapping
    ObjectRef create(@RequestBody ObjectRef speciality);

    @PutMapping("/{id}")
    ObjectRef update(@PathVariable Long id,
                     @RequestBody ObjectRef speciality);

    @DeleteMapping(("/{id}"))
    void delete(@PathVariable Long id);

}
