package com.bntu.master.attendance.monitor.impl.converter;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.entity.Base;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractListConverter<ENTITY extends Base, DTO> implements ListConverter<ENTITY, DTO> {

    public abstract ENTITY convertToEntity(DTO dto);

    public abstract DTO convertToDto(ENTITY entity);

    @Override
    public List<DTO> convertToDtos(List<ENTITY> entities) {
        return entities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Set<DTO> convertToDtos(Set<ENTITY> entities) {
        return entities.stream().map(this::convertToDto).collect(Collectors.toSet());
    }

    @Override
    public Page<DTO> convertToDtos(Page<ENTITY> page) {
        return new PageImpl<>(
                page.stream().map(this::convertToDto).collect(Collectors.toList()),
                page.getPageable(),
                page.getTotalElements());
    }

    public ObjectRef toRef(Long id, String name) {
        return ObjectRef.toObjectRef(id, name);
    }
}
