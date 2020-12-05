package com.bntu.master.attendance.monitor.api.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@CrossOrigin
public interface BaseRest<DTO> {

    @GetMapping("/{id}")
    DTO find(@PathVariable Long id);

    @GetMapping
    List<DTO> findAll();

    @GetMapping("/page")
    Page<DTO> findByPage(Pageable pageable);

    @PostMapping
    DTO create(@RequestBody DTO dto);

    @PutMapping("/{id}")
    DTO update(@PathVariable Long id,
               @RequestBody DTO dto);

    @DeleteMapping(("/{id}"))
    void delete(@PathVariable Long id);

}
