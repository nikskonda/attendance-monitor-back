package com.bntu.master.attendance.monitor.impl.converter;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.ProfessorDto;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.ProfessorPosition;
import com.bntu.master.attendance.monitor.impl.entity.util.Professor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProfessorConverter extends AbstractListConverter<ProfessorPosition, ProfessorDto> {

    @Autowired
    private PersonConverter personConverter;

    @Autowired
    private PositionConverter positionConverter;

    @Override
    public ProfessorPosition convertToEntity(ProfessorDto dto) {
        ProfessorPosition entity = new ProfessorPosition();
        Person prof = new Person();
        personConverter.fillBase(prof, dto);
        entity.setProfessor(prof);
        entity.setPosition(positionConverter.convertToEntity(dto.getPosition()));
        return entity;
    }

    @Override
    public ProfessorDto convertToDto(ProfessorPosition entity) {
        ProfessorDto dto = new ProfessorDto();
        personConverter.fillBase(dto, entity.getProfessor());
        dto.setPosition(positionConverter.convertToDto(entity.getPosition()));
        return dto;
    }

    public ProfessorDto convertToDto(Professor entity) {
        ProfessorDto dto = new ProfessorDto();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPatronymic(entity.getPatronymic());
        dto.setPhone(entity.getPhone());
        dto.setPosition(ObjectRef.toObjectRef(entity.getPositionId(), entity.getPositionName()));
        return dto;
    }

    public Page<ProfessorDto> convertProfessorsToDtos(Page<Professor> page) {
        return new PageImpl<>(
                page.stream().map(this::convertToDto).collect(Collectors.toList()),
                page.getPageable(),
                page.getTotalElements());
    }
}
