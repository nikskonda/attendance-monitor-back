package com.bntu.master.attendance.monitor.impl.rest;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.api.model.StudentDto;
import com.bntu.master.attendance.monitor.api.rest.PersonRest;
import com.bntu.master.attendance.monitor.impl.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonRestImpl implements PersonRest {

    @Autowired
    private PersonService service;

    @Override
    public PersonDto find(Long id, String qualifier) {
        return service.find(ObjectRef.toObjectRef(id, qualifier));
    }

    @Override
    public List<PersonDto> findAll() {
        return service.findAll();
    }

    @Override
    public List<PersonDto> findAllByRole(List<RoleConstant> roles) {
        return service.findAllByRoles(roles);
    }

    @Override
    public Page<PersonDto> findPageByRole(List<RoleConstant> roles, Pageable pageable) {
        return service.findPageByRoles(roles, pageable);
    }

    @Override
    public PersonDto create(PersonDto person) {
        return service.create(person);
    }

    @Override
    public PersonDto update(Long id, PersonDto person) {
        return service.update(id, person);
    }

    @Override
    public List<StudentDto> findStudentsByGroup(Long groupId) {
        return service.findStudentsByGroup(ObjectRef.toObjectRef(groupId));
    }

    @Override
    public Page<StudentDto> findStudentsPageByGroup(Long groupId, Pageable pageable) {
        return service.findStudentsPageByGroup(ObjectRef.toObjectRef(groupId), pageable);
    }

    @Override
    public StudentDto createStudent(StudentDto studentDto) {
        return service.createStudent(studentDto);
    }

    @Override
    public PersonDto createProfessor(PersonDto personDto) {
        return service.createProf(personDto);
    }

    @Override
    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        return service.updateStudent(id, studentDto);
    }

    @Override
    public PersonDto updateProfessor(Long id, PersonDto personDto) {
        return service.updateProfessor(id, personDto);
    }

    @Override
    public void deletePerson(Long id) {
        service.deletePerson(id);
    }
}
