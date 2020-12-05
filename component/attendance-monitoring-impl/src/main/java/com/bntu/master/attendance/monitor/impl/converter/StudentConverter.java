package com.bntu.master.attendance.monitor.impl.converter;

import com.bntu.master.attendance.monitor.api.model.GroupVolumeConstant;
import com.bntu.master.attendance.monitor.api.model.StudentDto;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.StudentGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentConverter extends AbstractListConverter<StudentGroup, StudentDto> {

    @Autowired
    private PersonConverter personConverter;

    @Override
    public StudentGroup convertToEntity(StudentDto dto) {
        StudentGroup entity = new StudentGroup();
        Person stud = new Person();
        personConverter.fillBase(stud, dto);
        entity.setStudent(stud);
        Group group = new Group();
        group.setId(dto.getGroup().getId());
        group.setKey(dto.getGroup().getQualifier());
        entity.setGroup(group);
        entity.setGroupVolumeId(GroupVolumeConstant.find(dto.getGroupVolume()).get().getId());
        return entity;
    }

    @Override
    public StudentDto convertToDto(StudentGroup entity) {
        StudentDto studentDto = new StudentDto();
        personConverter.fillBase(studentDto, entity.getStudent());
        studentDto.setGroup(toRef(entity.getGroup().getId(), entity.getGroup().getKey()));
        studentDto.setGroupVolume(GroupVolumeConstant.find(entity.getGroupVolumeId()).toString());
        return studentDto;
    }
}
