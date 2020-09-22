package com.bntu.master.attendance.monitor.impl.resolver;

import com.bntu.master.attendance.monitor.api.exception.NotFoundException;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.dataaccess.GroupRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.SubjectRepository;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubjectResolver {

    @Autowired
    private SubjectRepository repository;

    public Subject resolve(ObjectRef objectRef) {
        if (objectRef.isNullId()) {
            throw new NotFoundException();
        }
        return repository.findById(objectRef.getId()).orElseThrow(NotFoundException::new);
    }

}
