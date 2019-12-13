package com.bntu.master.attendance.monitor.api.rest;

import com.bntu.master.attendance.monitor.api.model.PersonDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/person")
public interface PersonRest {

    @GetMapping
    PersonDto find(@PathVariable Long id, @PathVariable String qualifier);

    @GetMapping
    List<PersonDto> findAll();

    @PostMapping
    PersonDto create(@RequestBody PersonDto person);

    @PutMapping("/{id}")
    PersonDto update(@PathVariable Long id,
                     @RequestBody PersonDto person);

    @DeleteMapping(("/{id}"))
    void delete(@PathVariable Long id);

}
