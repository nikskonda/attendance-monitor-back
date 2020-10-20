package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.exception.AttendanceMonitorException;
import com.bntu.master.attendance.monitor.api.exception.EmailBusyException;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.api.model.StudentDto;
import com.bntu.master.attendance.monitor.impl.converter.PersonConverter;
import com.bntu.master.attendance.monitor.impl.dataaccess.ParentContactRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.PersonRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.StudentGroupRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.UserRepository;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.Role;
import com.bntu.master.attendance.monitor.impl.entity.StudentGroup;
import com.bntu.master.attendance.monitor.impl.resolver.GroupResolver;
import com.bntu.master.attendance.monitor.impl.resolver.PersonResolver;
import com.bntu.master.attendance.monitor.impl.resolver.RoleResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
    private UserRepository userRepository;

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

    @Autowired
    private UserService userService;

    public PersonDto find(ObjectRef dto) {
        Person person = resolver.resolvePerson(dto);
        return converter.convertToDto(person);
    }

    public PersonDto create(PersonDto dto) {
        if (!dto.isNullId()) {
            throw new AttendanceMonitorException();
        }
        Person person = converter.convertToEntity(dto);

        person = repository.save(person);

        return converter.convertToDto(person);
    }

    public PersonDto update(Long id, PersonDto dto) {
        dto.setId(id);
        resolver.resolvePerson(dto);
        Person person = converter.convertToEntity(dto);

        person = repository.save(person);

        return converter.convertToDto(person);
    }

    public void delete(PersonDto dto) {
        Person person = resolver.resolvePerson(dto);
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
        Set<Role> roleSet = roleResolver.resolveRefs(roles.stream().map(r -> ObjectRef.toObjectRef(r.getRole())).collect(Collectors.toSet()));
        Set<String> emails = userRepository.findUserEmailByRolesIn(roleSet.stream().map(Role::getId).collect(Collectors.toSet()));
        return repository.findAllByIdInOrEmailIn(Collections.emptySet(), emails).stream()
                .map(person -> converter.convertToDto(person))
                .sorted(Comparator.comparing(PersonDto::getFullName))
                .collect(Collectors.toList());
    }

    public Page<PersonDto> findPageByRoles(List<RoleConstant> roles, Pageable pageable) {
        Set<Role> roleSet = roleResolver.resolveRefs(roles.stream().map(r -> ObjectRef.toObjectRef(r.getRole())).collect(Collectors.toSet()));
        Set<String> emails = userRepository.findUserEmailByRolesIn(roleSet.stream().map(Role::getId).collect(Collectors.toSet()));
        return new PageImpl<>(repository.findAllByIdInOrEmailIn(Collections.emptySet(), emails, pageable).stream()
                .map(person -> converter.convertToDto(person))
                .collect(Collectors.toList()),
                pageable,
                emails.size());
    }

    public List<StudentDto> findStudentsByGroup(ObjectRef group) {
        Group gr = groupResolver.resolve(group);
        Set<StudentGroup> studentGroup = studentGroupRepository.findAllByGroup(gr);
        return studentGroup.stream()
                .map(sg -> converter.convertToDto(sg.getStudent(), gr))
                .sorted(Comparator.comparing(PersonDto::getFullName))
                .collect(Collectors.toList());
    }

    public Page<StudentDto> findStudentsPageByGroup(ObjectRef group, Pageable pageable) {
        Group gr = groupResolver.resolve(group);
        Set<StudentGroup> studentGroup = studentGroupRepository.findAllByGroup(gr);
        return new PageImpl<>(studentGroup.stream()
                .map(sg -> converter.convertToDto(sg.getStudent(), gr))
                .sorted(Comparator.comparing(PersonDto::getFullName))
                .collect(Collectors.toList()),
                pageable,
                repository.count());
    }

    public StudentDto createStudent(StudentDto studentDto) {
        Person stud = create(converter.convertToEntity(studentDto));
        Group group = groupResolver.resolve(studentDto.getGroup());
        studentGroupRepository.save(new StudentGroup(stud, group));
        userService.create(stud.getEmail(), RoleConstant.STUDENT);
        return converter.convertToDto(stud, group);
    }

    private Person create(Person person) {
        if (repository.findByEmail(person.getEmail()).isPresent()) {
            throw new EmailBusyException();
        }
        return repository.save(person);
    }

    public PersonDto createProf(PersonDto profDto) {
        Person prof = create(converter.convertToEntity(profDto));
        userService.create(prof.getEmail(), RoleConstant.PROFESSOR);
        return converter.convertToDto(prof);
    }

    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        Group group = groupResolver.resolve(studentDto.getGroup());
        Person person = resolver.resolvePerson(ObjectRef.toObjectRef(id));
        Person updatedStud = converter.convertToEntity(studentDto);

        if (!person.getEmail().equals(updatedStud.getEmail())) {
            userService.updateEmail(person.getEmail(), updatedStud.getEmail());
        }
        updatedStud.setId(id);
        updatedStud = repository.save(updatedStud);

        StudentGroup studentGroup = studentGroupRepository.findFirstByStudent(person);
        if (!studentGroup.getGroup().getId().equals(group.getId())){
            studentGroupRepository.delete(studentGroup);
            studentGroupRepository.save(new StudentGroup(updatedStud, group));
        }
        return converter.convertToDto(updatedStud, group);
    }

    public PersonDto updateProfessor(Long id, PersonDto personDto) {
        Person person = resolver.resolvePerson(ObjectRef.toObjectRef(id));
        Person updatedProf = converter.convertToEntity(personDto);
        if (!person.getEmail().equals(updatedProf.getEmail())) {
            userService.updateEmail(person.getEmail(), updatedProf.getEmail());
        }
        updatedProf.setId(id);
        updatedProf = repository.save(updatedProf);
        return converter.convertToDto(updatedProf);
    }

    public void deletePerson(Long id) {
        Person person = resolver.resolvePerson(ObjectRef.toObjectRef(id));
        if (resolver.resolveUser(ObjectRef.toObjectRef(person.getEmail())).getRoles().contains(roleResolver.resolve(RoleConstant.STUDENT))) {
            StudentGroup studentGroup = studentGroupRepository.findFirstByStudent(person);
            studentGroupRepository.delete(studentGroup);
        }
        repository.delete(person);
        userService.delete(person.getEmail());
    }
}
