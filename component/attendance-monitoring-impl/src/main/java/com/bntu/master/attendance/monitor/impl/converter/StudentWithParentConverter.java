package com.bntu.master.attendance.monitor.impl.converter;

import com.bntu.master.attendance.monitor.api.model.GroupVolumeConstant;
import com.bntu.master.attendance.monitor.api.model.StudentWithParentDto;
import com.bntu.master.attendance.monitor.impl.entity.Account;
import com.bntu.master.attendance.monitor.impl.entity.ParentContact;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.StudentGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentWithParentConverter extends AbstractListConverter<ParentContact, StudentWithParentDto> {

    @Autowired
    private PersonConverter personConverter;

    @Override
    public ParentContact convertToEntity(StudentWithParentDto studentWithParentDto) {
        ParentContact entity = new ParentContact();
        Person stud = new Person();
        personConverter.fillBase(stud, studentWithParentDto);
        Account parent = new Account();
        parent.setEmail(studentWithParentDto.getParentEmail());
        entity.setParent(parent);
        entity.setStudent(stud);
        return entity;
    }

    @Override
    public StudentWithParentDto convertToDto(ParentContact entity) {
        StudentWithParentDto studentDto = new StudentWithParentDto();
        personConverter.fillBase(studentDto, entity.getStudent());
        if (entity.getParent() != null) {
            studentDto.setParentEmail(entity.getParent().getEmail());
        }
        return studentDto;
    }

    public StudentWithParentDto convertToDto(ParentContact entity, StudentGroup group) {
        StudentWithParentDto studentDto = new StudentWithParentDto();
        personConverter.fillBase(studentDto, entity.getStudent());
        studentDto.setGroup(toRef(group.getGroup().getId(), group.getGroup().getKey()));
        studentDto.setGroupVolume(GroupVolumeConstant.find(group.getGroupVolumeId()).toString());
        if (entity.getParent() != null) {
            studentDto.setParentEmail(entity.getParent().getEmail());
        }
        return studentDto;
    }
}
