package com.bntu.master.attendance.monitor.impl.resolver;

import com.bntu.master.attendance.monitor.api.exception.NotFoundException;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.dataaccess.LessonRepository;
import com.bntu.master.attendance.monitor.impl.entity.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessonResolver {

    @Autowired
    private LessonRepository repository;

    public Lesson resolve(ObjectRef objectRef) {
        if (objectRef.isNullId()) {
            throw new NotFoundException();
        }
        return repository.findById(objectRef.getId()).orElseThrow(NotFoundException::new);
    }

}
