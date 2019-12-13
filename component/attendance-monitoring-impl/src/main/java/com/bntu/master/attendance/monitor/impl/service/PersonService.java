package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.impl.dataaccess.PersonRepository;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.api.exception.Exception;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.impl.resolver.GroupResolver;
import com.bntu.master.attendance.monitor.impl.resolver.PersonResolver;
import com.bntu.master.attendance.monitor.impl.converter.PersonConverter;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.resolver.RoleResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonService {

    @Autowired
    private PersonRepository repository;

    @Autowired
    private PersonConverter converter;

    @Autowired
    private PersonResolver resolver;

    @Autowired
    private RoleResolver roleResolver;

    @Autowired
    private GroupResolver groupResolver;

    public PersonDto find(ObjectRef dto) {
        Person person = resolver.resolve(dto);
        return converter.convertToDto(person);
    }

    public PersonDto create(PersonDto dto) {
        if (!dto.isNullId()) {
            throw new Exception();
        }
        Group group = groupResolver.resolve(dto.getGroup());
        Person person = converter.convertToEntity(dto, group);

        person = repository.save(person);

        return converter.convertToDto(person);
    }

    public PersonDto update(PersonDto dto) {
        Group group = groupResolver.resolve(dto.getGroup());
        Person person = converter.convertToEntity(dto, group);

        person = repository.save(person);

        return converter.convertToDto(person);
    }

    public void delete(PersonDto dto) {
        Person person = resolver.resolve(dto);
        repository.delete(person);
    }

    public List<PersonDto> findByList(List<ObjectRef> persons) {
        Set<Long> ids = new HashSet<>();
        Set<String> emails = new HashSet<>();
        for (ObjectRef personRef : persons) {
            if (!personRef.isNullable()) {
                if (personRef.isNullQualifier()) {
                    ids.add(personRef.getId());
                } else {
                    emails.add(personRef.getQualifier());
                }
            }
        }
        return repository.findAllByIdInOrEmailIn(ids, emails).stream().map(person -> converter.convertToDto(person)).collect(Collectors.toList());
    }

    public List<PersonDto> findAll() {
        return repository.findAll().stream().map(person -> converter.convertToDto(person)).collect(Collectors.toList());
    }

}
