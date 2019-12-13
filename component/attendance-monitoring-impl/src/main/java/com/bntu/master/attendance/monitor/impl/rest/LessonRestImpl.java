package com.bntu.master.attendance.monitor.impl.rest;

import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.rest.LessonRest;
import com.bntu.master.attendance.monitor.impl.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class LessonRestImpl implements LessonRest {

    @Autowired
    private LessonService service;

    @Override
    public LessonDto find(Long id) {
        return service.find(ObjectRef.toObjectRef(id));
    }

    @Override
    public List<LessonDto> findAll() {
        return service.findAll();
    }

    @Override
    public LessonDto create(LessonDto lesson) {
        return service.create(lesson);
    }

    @Override
    public LessonDto update(Long id, LessonDto lesson) {
        return service.update(id, lesson);
    }

    @Override
    public void delete(Long id) {
        service.delete(ObjectRef.toObjectRef(id));
    }
}
