package com.bntu.master.attendance.monitor.impl.converter;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.api.model.StudentDto;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.Role;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class PersonConverter {

    public Person convertToEntity(PersonDto dto) {
        Person entity = new Person();
        entity.setId(dto.getId());
        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPatronymic(dto.getPatronymic());
        entity.setRoles(dto.getRoles().stream().map(ref -> new Role(ref.getId(), ref.getQualifier())).collect(Collectors.toSet()));
        return entity;
    }

    public PersonDto convertToDto(Person entity) {
        PersonDto dto = new PersonDto();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPatronymic(entity.getPatronymic());
        dto.setRoles(entity.getRoles().stream().map(role -> ObjectRef.toObjectRef(role.getId(), role.getName())).collect(Collectors.toSet()));
        return dto;
    }

    private void convertToDto(PersonDto dto, Person entity) {
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPatronymic(entity.getPatronymic());
        dto.setRoles(entity.getRoles().stream().map(role -> ObjectRef.toObjectRef(role.getId(), role.getName())).collect(Collectors.toSet()));
    }

    public StudentDto convertToDto(Person entity, Group group) {
        StudentDto studentDto = new StudentDto();
        convertToDto(studentDto, entity);
        studentDto.setGroup(ObjectRef.toObjectRef(group.getId(), group.getKey()));
        return studentDto;
    }
}
