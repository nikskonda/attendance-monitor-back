package com.bntu.master.attendance.monitor.impl.rest;

import com.bntu.master.attendance.monitor.api.exception.UnsupportedMethodException;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.StudentDto;
import com.bntu.master.attendance.monitor.api.model.StudentWithParentDto;
import com.bntu.master.attendance.monitor.api.rest.StudentRest;
import com.bntu.master.attendance.monitor.impl.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudentRestImpl implements StudentRest {

    @Autowired
    private StudentService service;


    @Override
    public StudentWithParentDto find(Long id) {
        return service.find(id, null);
    }

    @Override
    public List<StudentWithParentDto> findAll() {
        throw new UnsupportedMethodException();
    }

    @Override
    public Page<StudentWithParentDto> findByPage(Pageable pageable) {
        throw new UnsupportedMethodException();
    }

    @Override
    public Page<StudentWithParentDto> findByPage(Long groupId, Pageable pageable) {
        return service.findStudentsPageByGroup(ObjectRef.toObjectRef(groupId), pageable);
    }

    @Override
    public List<StudentDto> findByGroup(Long groupId) {
        return service.findStudentsGroup(ObjectRef.toObjectRef(groupId));
    }

    @Override
    public List<StudentWithParentDto> findByParent(String parentEmail) {
        return service.findByParent(parentEmail);
    }

    @Override
    public StudentWithParentDto create(StudentWithParentDto dto) {
        return service.create(dto);
    }

    @Override
    public StudentWithParentDto update(Long id, StudentWithParentDto dto) {
        return service.update(id, dto);
    }

    @Override
    public void delete(Long id) {
        service.delete(id);
    }
}
