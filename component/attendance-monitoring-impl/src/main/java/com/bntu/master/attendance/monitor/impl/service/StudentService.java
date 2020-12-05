package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.model.GroupVolumeConstant;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.api.model.StudentDto;
import com.bntu.master.attendance.monitor.api.model.StudentWithParentDto;
import com.bntu.master.attendance.monitor.impl.converter.PersonConverter;
import com.bntu.master.attendance.monitor.impl.converter.StudentConverter;
import com.bntu.master.attendance.monitor.impl.converter.StudentWithParentConverter;
import com.bntu.master.attendance.monitor.impl.dataaccess.ParentContactRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.StudentGroupRepository;
import com.bntu.master.attendance.monitor.impl.entity.Account;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.ParentContact;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.StudentGroup;
import com.bntu.master.attendance.monitor.impl.entity.util.Student;
import com.bntu.master.attendance.monitor.impl.resolver.AccountResolver;
import com.bntu.master.attendance.monitor.impl.resolver.GroupResolver;
import com.bntu.master.attendance.monitor.impl.resolver.PersonResolver;
import com.bntu.master.attendance.monitor.impl.resolver.RoleResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentGroupRepository repository;

    @Autowired
    private ParentContactRepository parentRepository;

    @Autowired
    private StudentWithParentConverter converter;

    @Autowired
    private StudentConverter studentConverter;

    @Autowired
    private PersonConverter personConverter;

    @Autowired
    private GroupResolver groupResolver;

    @Autowired
    private PersonResolver personResolver;

    @Autowired
    private AccountResolver accountResolver;

    @Autowired
    private RoleResolver roleResolver;

    @Autowired
    private PersonService personService;

    @Autowired
    private AccountService accountService;

    public StudentWithParentDto find(Long id, String email) {
        Person stud = personResolver.resolvePersonByRole(ObjectRef.toObjectRef(id, email), RoleConstant.STUDENT);
        StudentGroup studentGroup = repository.findFirstByStudent(stud);
        ParentContact parentContact = parentRepository.findFirstByStudent(stud);
        return converter.convertToDto(parentContact, studentGroup);
    }

    public StudentWithParentDto create(StudentWithParentDto studentWithParentDto) {
        Group group = groupResolver.resolve(studentWithParentDto.getGroup());

        Person stud = personService.create(studentWithParentDto, Collections.singleton(RoleConstant.STUDENT));
        repository.save(new StudentGroup(group, stud, GroupVolumeConstant.find(studentWithParentDto.getGroupVolume()).get().getId()));

        String parentEmail = studentWithParentDto.getParentEmail();
        ParentContact result = new ParentContact();
        result.setStudent(stud);

        if (StringUtils.isNotBlank(parentEmail)) {
            Account parent = accountService.createOrAddRoleIfExists(studentWithParentDto.getParentEmail(), RoleConstant.PARENT);
            result = parentRepository.save(new ParentContact(parent, stud));
        }
        return converter.convertToDto(result);
    }

    public StudentWithParentDto update(Long id, StudentWithParentDto studentDto) {
        Group group = groupResolver.resolve(studentDto.getGroup());
        Person stud = personResolver.resolvePersonByRole(ObjectRef.toObjectRef(id), RoleConstant.STUDENT);

        if (!stud.getEmail().equals(studentDto.getEmail())) {
            accountService.updateEmail(stud.getEmail(), studentDto.getEmail());
        }
        personService.update(studentDto);

        stud = personResolver.resolvePersonByRole(ObjectRef.toObjectRef(id), RoleConstant.STUDENT);

        StudentGroup studentGroup = repository.findFirstByStudent(stud);
        if (!studentGroup.getGroup().getId().equals(group.getId())) {
            studentGroup.setGroup(group);
        }
        GroupVolumeConstant volume = GroupVolumeConstant.find(studentDto.getGroupVolume()).get();
        if (!studentGroup.getGroupVolumeId().equals(volume.getId())) {
            studentGroup.setGroupVolumeId(volume.getId());
        }
        studentGroup = repository.save(studentGroup);


        if (StringUtils.isNotBlank(studentDto.getParentEmail())) {
            ParentContact parentContact = parentRepository.findFirstByStudent(stud);
            if (parentContact == null) {
                Account parent = accountService.createOrAddRoleIfExists(studentDto.getParentEmail(), RoleConstant.PARENT);
                parentContact = parentRepository.save(new ParentContact(parent, stud));
            } else {
                if (!studentDto.getParentEmail().equals(parentContact.getParent().getEmail())) {
                    Account parent = accountService.createOrAddRoleIfExists(studentDto.getParentEmail(), RoleConstant.PARENT);
                    parentContact.setParent(parent);
                    parentRepository.save(parentContact);
                }
            }
        }

        return converter.convertToDto(parentRepository.findFirstByStudent(stud), studentGroup);
    }

    public void delete(Long id) {
        Person stud = personResolver.resolvePersonByRole(ObjectRef.toObjectRef(id), RoleConstant.STUDENT);

        StudentGroup studentGroup = repository.findFirstByStudent(stud);
        repository.delete(studentGroup);

        ParentContact parentContact = parentRepository.findFirstByStudent(stud);
        if (parentContact != null) {
            parentRepository.delete(parentContact);
        }

        if (accountService.removeRoleOrAccount(stud.getEmail(), RoleConstant.STUDENT)) {
            personService.delete(ObjectRef.toObjectRef(stud.getId()));
        }
    }


    public List<StudentDto> findStudentsByGroup(ObjectRef group, GroupVolumeConstant groupVolume) {
        Group gr = groupResolver.resolve(group);
        List<StudentDto> list = studentConverter.convertToDtos(repository.findAllByGroupAndGroupVolumeId(gr, groupVolume.getId()));
        list.sort(Comparator.comparing(StudentDto::getFullName));
        return list;
    }

    public List<StudentDto> findStudentsGroup(ObjectRef group) {
        Group gr = groupResolver.resolve(group);
        List<StudentDto> list = studentConverter.convertToDtos(repository.findAllByGroup(gr));
        list.sort(Comparator.comparing(StudentDto::getFullName));
        return list;
    }

    public List<StudentWithParentDto> findByParent(String email) {
        Account parent = personResolver.resolveUserByRole(ObjectRef.toObjectRef(email), RoleConstant.PARENT);
        List<ParentContact> parentContacts = parentRepository.findAllByParent(parent);
        List<StudentWithParentDto> result = new ArrayList<>();
        for (ParentContact parentContact : parentContacts) {
            result.add(find(parentContact.getStudent().getId(), parentContact.getStudent().getEmail()));
        }
        result.sort(Comparator.comparing(StudentWithParentDto::getFullName));
        return result;
    }

    public Page<StudentWithParentDto> findStudentsPageByGroup(ObjectRef group, Pageable pageable) {
        Group gr = groupResolver.resolve(group);
        Page<Student> students = parentRepository.findStudentsByGroupPage(gr, pageable);
        return new PageImpl<>(students.stream().map(s -> convertToDto(s, gr)).collect(Collectors.toList()), students.getPageable(), students.getTotalElements());
    }

    public List<StudentWithParentDto> findStudentsByGroup(ObjectRef group) {
        Group gr = groupResolver.resolve(group);
        return parentRepository.findStudentsByGroup(gr).stream().map(s -> convertToDto(s, gr)).sorted(Comparator.comparing(StudentWithParentDto::getFullName)).collect(Collectors.toList());
    }

    private StudentWithParentDto convertToDto(Student student, Group group) {
        StudentWithParentDto dto = new StudentWithParentDto();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setParentEmail(student.getParentEmail());
        dto.setPatronymic(student.getPatronymic());
        dto.setGroupVolume(GroupVolumeConstant.find(student.getGroupVolumeId()).get().toString());
        dto.setGroup(ObjectRef.toObjectRef(group.getId(), group.getKey()));
        dto.setEmail(student.getEmail());
        return dto;
    }
}
