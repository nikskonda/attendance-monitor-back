package com.bntu.master.attendance.monitor.impl.rest;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.SubjectTypeConstant;
import com.bntu.master.attendance.monitor.api.rest.SubjectRest;
import com.bntu.master.attendance.monitor.impl.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SubjectRestImpl implements SubjectRest {

    @Autowired
    private SubjectService service;

    @Override
    public ObjectRef find(Long id) {
        return service.find(ObjectRef.toObjectRef(id));
    }

    @Override
    public List<ObjectRef> findAll() {
        return service.findAll();
    }

    @Override
    public ObjectRef create(ObjectRef dto) {
        return service.create(dto);
    }

    @Override
    public ObjectRef update(Long id, ObjectRef dto) {
        return service.update(id, dto);
    }

    @Override
    public void delete(Long id) {
        service.delete(ObjectRef.toObjectRef(id));
    }

    @Override
    public SubjectTypeConstant[] getSubjectTypes() {
        return SubjectTypeConstant.values();
    }

    @Override
    public List<ObjectRef> findByGroup(Long groupId) {
        return service.findByGroup(ObjectRef.toObjectRef(groupId));
    }

    @Override
    public Page<ObjectRef> findByPage(Pageable pageable) {
        return service.findByPage(pageable);
    }
}
