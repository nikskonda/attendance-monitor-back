package com.bntu.master.attendance.monitor.impl.converter;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.entity.Subject;
import org.springframework.stereotype.Component;

@Component
public class SubjectConverter {

    public Subject convertToEntity(ObjectRef dto) {
        Subject entity = new Subject();
        entity.setId(dto.getId());
        entity.setName(dto.getQualifier());
        return entity;
    }

    public ObjectRef convertToDto(Subject entity) {
        ObjectRef dto = new ObjectRef();
        dto.setId(entity.getId());
        dto.setQualifier(entity.getName());
        return dto;
    }
}
