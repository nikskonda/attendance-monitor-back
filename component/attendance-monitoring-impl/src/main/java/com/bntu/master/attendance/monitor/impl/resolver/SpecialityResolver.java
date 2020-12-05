package com.bntu.master.attendance.monitor.impl.resolver;

import com.bntu.master.attendance.monitor.api.exception.NotFoundException;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.dataaccess.SpecialityRepository;
import com.bntu.master.attendance.monitor.impl.entity.Speciality;
import org.springframework.stereotype.Component;

@Component
public class SpecialityResolver extends AbstractResolver<SpecialityRepository, Speciality> {

    public Speciality resolve(ObjectRef objectRef) {
        if (objectRef.isNullable()) {
            throw new NotFoundException();
        }
        return objectRef.isNullId() ? repository.findByName(objectRef.getQualifier()).orElseThrow(NotFoundException::new)
                : repository.findById(objectRef.getId()).orElseThrow(NotFoundException::new);
    }

}
