package com.bntu.master.attendance.monitor.impl.converter;

import com.bntu.master.attendance.monitor.api.model.GroupDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Speciality;
import org.springframework.stereotype.Component;

@Component
public class GroupConverter {

    public Group convertToEntity(GroupDto dto, Speciality speciality) {
        Group entity = new Group();
        entity.setId(dto.getId());
        entity.setKey(dto.getQualifier());
        entity.setSpeciality(speciality);
        return entity;
    }

    public GroupDto convertToDto(Group entity) {
        GroupDto dto = new GroupDto();
        dto.setId(entity.getId());
        dto.setQualifier(entity.getKey());
        dto.setSpeciality(ObjectRef.toObjectRef(entity.getSpeciality().getId(), entity.getSpeciality().getName()));
        return dto;
    }
}
