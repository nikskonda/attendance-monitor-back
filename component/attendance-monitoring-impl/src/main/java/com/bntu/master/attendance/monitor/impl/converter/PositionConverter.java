package com.bntu.master.attendance.monitor.impl.converter;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.entity.Position;
import org.springframework.stereotype.Component;

@Component
public class PositionConverter extends AbstractListConverter<Position, ObjectRef> {

    public Position convertToEntity(ObjectRef dto) {
        Position entity = new Position();
        entity.setId(dto.getId());
        entity.setName(dto.getQualifier());
        return entity;
    }

    public ObjectRef convertToDto(Position entity) {
        ObjectRef dto = new ObjectRef();
        dto.setId(entity.getId());
        dto.setQualifier(entity.getName());
        return dto;
    }
}
