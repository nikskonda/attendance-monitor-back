package com.bntu.master.attendance.monitor.impl.converter;

import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class PersonConverter extends AbstractListConverter<Person, PersonDto> {

    public void fillBase(Person entity, PersonDto dto) {
        entity.setId(dto.getId());
        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPatronymic(dto.getPatronymic());
        String phone = dto.getPhone();
        if (StringUtils.isNotBlank(phone)) {
            phone = phone.replaceAll("\\D", "");
            entity.setPhone(String.format("(%s) %s-%s-%s", phone.substring(0, 2), phone.substring(2, 5), phone.substring(5, 7), phone.substring(7)));
        } else {
            entity.setPhone(null);
        }
    }

    public void fillBase(PersonDto dto, Person entity) {
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPatronymic(entity.getPatronymic());
        dto.setPhone(entity.getPhone());
    }

    public Person convertToEntity(PersonDto dto) {
        Person entity = new Person();
        fillBase(entity, dto);
        return entity;
    }

    public PersonDto convertToDto(Person entity) {
        PersonDto dto = new PersonDto();
        fillBase(dto, entity);
        return dto;
    }
}
