package com.bntu.master.attendance.monitor.impl.converter;

import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Lesson;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessonConverter {

    public Lesson convertToEntity(LessonDto dto, Person prof, Group group) {
        Lesson entity = new Lesson();
        entity.setId(dto.getId());
        entity.setDate(dto.getDate());
        entity.setFinishTime(dto.getFinishTime());
        entity.setStartTime(dto.getStartTime());
        entity.setProfessor(prof);
        Subject subject = new Subject(dto.getSubject().getId(), dto.getSubject().getQualifier());
        entity.setSubject(subject);
        entity.setGroup(group);
        return entity;
    }

    public LessonDto convertToDto(Lesson entity) {
        LessonDto dto = new LessonDto();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setFinishTime(entity.getFinishTime());
        dto.setStartTime(entity.getStartTime());
        dto.setProfessor(ObjectRef.toObjectRef(entity.getProfessor().getId(), entity.getProfessor().getEmail()));
        dto.setSubject(ObjectRef.toObjectRef(entity.getSubject().getId(), entity.getSubject().getName()));
        dto.setGroup(ObjectRef.toObjectRef(entity.getGroup().getId(), entity.getGroup().getKey()));
        return dto;
    }
}
