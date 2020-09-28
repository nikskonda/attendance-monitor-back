package com.bntu.master.attendance.monitor.api.rest;

import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.api.model.StudentDto;
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
@RequestMapping("/person")
public interface PersonRest {

    @GetMapping("/{id}")
    PersonDto find(@PathVariable Long id, @PathVariable String qualifier);

    @GetMapping
    List<PersonDto> findAll();

    @GetMapping("/byRoles")
    List<PersonDto> findAllByRole(@RequestParam List<RoleConstant> roles);



    @PostMapping
    PersonDto create(@RequestBody PersonDto person);

    @PutMapping("/{id}")
    PersonDto update(@PathVariable Long id,
                     @RequestBody PersonDto person);

    @DeleteMapping(("/{id}"))
    void delete(@PathVariable Long id);


    @GetMapping("/students")
    List<StudentDto> findStudentsByGroup(@RequestParam Long groupId);

    @PostMapping("/student")
    StudentDto createStudent(@RequestBody StudentDto studentDto);

    @PutMapping("/student/{id}")
    StudentDto updateStudent(@PathVariable Long id, @RequestBody StudentDto studentDto);

    @DeleteMapping("/student/{id}")
    void deleteStudent(@PathVariable Long id);

}
