package com.bntu.master.attendance.monitor.api.rest;

import com.bntu.master.attendance.monitor.api.model.LessonDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@RequestMapping("/lesson")
public interface LessonRest {

    @GetMapping
    LessonDto find(@PathVariable Long id);

    @GetMapping
    List<LessonDto> findAll();

    @PostMapping
    LessonDto create(@RequestBody LessonDto lesson);

    @PutMapping("/{id}")
    LessonDto update(@PathVariable Long id,
                     @RequestBody LessonDto lesson);

    @DeleteMapping(("/{id}"))
    void delete(@PathVariable Long id);

}
