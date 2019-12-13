package com.bntu.master.attendance.monitor.impl.rest;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.rest.SpecialityRest;
import com.bntu.master.attendance.monitor.impl.service.SpecialityService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SpecialityRestImpl implements SpecialityRest {

    @Autowired
    private SpecialityService service;

    @Override
    public ObjectRef find(Long id, String qualifier) {
        return service.find(ObjectRef.toObjectRef(id, qualifier));
    }

    @Override
    public List<ObjectRef> findAll() {
        return service.findAll();
    }

    @Override
    public ObjectRef create(ObjectRef speciality) {
        return service.create(speciality);
    }

    @Override
    public ObjectRef update(Long id, ObjectRef speciality) {
        return service.update(id, speciality);
    }

    @Override
    public void delete(Long id) {
        service.delete(ObjectRef.toObjectRef(id));
    }
}
