package com.bntu.master.attendance.monitor.impl.converter;

import com.bntu.master.attendance.monitor.api.exception.UnsupportedMethodException;
import com.bntu.master.attendance.monitor.api.model.GroupVolumeConstant;
import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Lesson;
import com.bntu.master.attendance.monitor.impl.entity.ProfessorPosition;
import com.bntu.master.attendance.monitor.impl.entity.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessonConverter extends AbstractListConverter<Lesson, LessonDto> {

    @Autowired
    private LessonScheduleConverter timeConverter;

    public Lesson convertToEntity(LessonDto dto, ProfessorPosition prof, Group group) {
        Lesson entity = new Lesson();
        entity.setId(dto.getId());
        entity.setDate(dto.getDate());
        entity.setTime(timeConverter.convertToEntity(dto.getTime()));
        entity.setProfessor(prof);
        Subject subject = new Subject(dto.getSubject().getId(), dto.getSubject().getQualifier());
        entity.setSubject(subject);
        entity.setSubjectType(dto.getSubjectType());
        entity.setGroup(group);
        GroupVolumeConstant
                .find(dto.getGroupVolume())
                .ifPresent(v -> entity.setGroupVolumeId(v.getId()));
        return entity;
    }

    public LessonDto convertToDto(Lesson entity) {
        LessonDto dto = new LessonDto();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setTime(timeConverter.convertToDto(entity.getTime()));
        dto.setProfessor(ObjectRef.toObjectRef(entity.getProfessor().getProfessor().getId(), entity.getProfessor().getProfessor().getEmail()));
        dto.setSubject(ObjectRef.toObjectRef(entity.getSubject().getId(), entity.getSubject().getName()));
        dto.setSubjectType(entity.getSubjectType());
        dto.setGroup(ObjectRef.toObjectRef(entity.getGroup().getId(), entity.getGroup().getKey()));
        GroupVolumeConstant
                .find(entity.getGroupVolumeId())
                .ifPresent(v -> dto.setGroupVolume(v.getName()));
        return dto;
    }

    @Override
    public Lesson convertToEntity(LessonDto lessonDto) {
        throw new UnsupportedMethodException();
    }
}
