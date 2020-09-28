package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.exception.Exception;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.api.model.StudentDto;
import com.bntu.master.attendance.monitor.impl.converter.PersonConverter;
import com.bntu.master.attendance.monitor.impl.dataaccess.ParentContactRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.PersonRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.StudentGroupRepository;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.ParentContact;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.Role;
import com.bntu.master.attendance.monitor.impl.entity.StudentGroup;
import com.bntu.master.attendance.monitor.impl.resolver.GroupResolver;
import com.bntu.master.attendance.monitor.impl.resolver.PersonResolver;
import com.bntu.master.attendance.monitor.impl.resolver.RoleResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonService {

    @Autowired
    private PersonRepository repository;

    @Autowired
    private ParentContactRepository parentContactRepository;

    @Autowired
    private StudentGroupRepository studentGroupRepository;

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

    public Person resolve(ObjectRef dto) {
        return resolver.resolve(dto);
    }

    public PersonDto create(PersonDto dto) {
        if (!dto.isNullId()) {
            throw new Exception();
        }
        Person person = converter.convertToEntity(dto);

        person = repository.save(person);

        return converter.convertToDto(person);
    }

    public PersonDto update(Long id, PersonDto dto) {
        dto.setId(id);
        resolver.resolve(dto);
        Person person = converter.convertToEntity(dto);

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

    public List<PersonDto> findAllByRoles(List<RoleConstant> roles) {
        List<Role> roleList = roles.stream().map(r -> roleResolver.resolve(ObjectRef.toObjectRef(r.getRole()))).collect(Collectors.toList());
        return repository.findAllByRolesIn(roleList).stream()
                .map(person -> converter.convertToDto(person))
                .sorted(Comparator.comparing(PersonDto::getFullName))
                .collect(Collectors.toList());
    }

    public List<StudentDto> findStudentsByGroup(ObjectRef group) {
        Group gr = groupResolver.resolve(group);
        Set<StudentGroup> studentGroup = studentGroupRepository.findAllByGroup(gr);
        return studentGroup.stream()
                .map(sg -> converter.convertToDto(sg.getStudent(), gr))
                .sorted(Comparator.comparing(PersonDto::getFullName))
                .collect(Collectors.toList());
    }

    public StudentDto createStudent(StudentDto studentDto) {
        Group group = groupResolver.resolve(studentDto.getGroup());
        Person stud = converter.convertToEntity(studentDto);
        Set<Role> roles = roleResolver.resolve(stud.getRoles());
        stud.setRoles(roles);
        stud = repository.save(stud);
        studentGroupRepository.save(new StudentGroup(stud, group));
        return converter.convertToDto(stud, group);
    }

    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        Group group = groupResolver.resolve(studentDto.getGroup());
        Person person = resolver.resolve(ObjectRef.toObjectRef(id));

        Person updatedStud = converter.convertToEntity(studentDto);
        updatedStud.setId(id);
        updatedStud = repository.save(updatedStud);

        StudentGroup studentGroup = studentGroupRepository.findFirstByStudent(person);
        if (!studentGroup.getGroup().getId().equals(group.getId())){
            studentGroupRepository.delete(studentGroup);
            studentGroupRepository.save(new StudentGroup(updatedStud, group));
        }
        return converter.convertToDto(updatedStud, group);
    }

    public void deleteStudent(Long id) {
        Person stud = resolver.resolve(ObjectRef.toObjectRef(id));
        StudentGroup studentGroup = studentGroupRepository.findFirstByStudent(stud);
        studentGroupRepository.delete(studentGroup);
        repository.delete(stud);
    }



}
