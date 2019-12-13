package com.bntu.master.attendance.monitor.impl.rest;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.api.rest.PersonRest;
import com.bntu.master.attendance.monitor.impl.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
    public PersonDto create(PersonDto person) {
        return service.create(person);
    }

    @Override
    public PersonDto update(Long id, PersonDto person) {
        return service.update(id, person);
    }

    @Override
    public void delete(Long id) {

    }
}
