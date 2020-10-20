package com.bntu.master.attendance.monitor.impl.converter;

import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.LessonScheduleDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Lesson;
import com.bntu.master.attendance.monitor.impl.entity.LessonSchedule;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.Subject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LessonConverter {

    public Lesson convertToEntity(LessonDto dto, Person prof, Group group) {
        Lesson entity = new Lesson();
        entity.setId(dto.getId());
        entity.setDate(dto.getDate());
        entity.setTime(convertToEntity(dto.getTime()));
        entity.setProfessor(prof);
        Subject subject = new Subject(dto.getSubject().getId(), dto.getSubject().getQualifier());
        entity.setSubject(subject);
        entity.setSubjectType(dto.getSubjectType());
        entity.setGroup(group);
        return entity;
    }

    public LessonDto convertToDto(Lesson entity) {
        LessonDto dto = new LessonDto();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setTime(convertToDto(entity.getTime()));
        dto.setProfessor(ObjectRef.toObjectRef(entity.getProfessor().getId(), entity.getProfessor().getEmail()));
        dto.setSubject(ObjectRef.toObjectRef(entity.getSubject().getId(), entity.getSubject().getName()));
        dto.setSubjectType(entity.getSubjectType());
        dto.setGroup(ObjectRef.toObjectRef(entity.getGroup().getId(), entity.getGroup().getKey()));
        return dto;
    }

    public LessonSchedule convertToEntity(LessonScheduleDto dto) {
        LessonSchedule entity = new LessonSchedule();
        entity.setId(dto.getId());
        entity.setOrder(dto.getOrder());
        entity.setStartTime(dto.getStartTime());
        entity.setFinishTime(dto.getFinishTime());
        entity.setShift(dto.getShift().toString());
        return entity;
    }

    public LessonScheduleDto convertToDto(LessonSchedule entity) {
        LessonScheduleDto dto = new LessonScheduleDto();
        dto.setId(entity.getId());
        dto.setOrder(entity.getOrder());
        dto.setStartTime(entity.getStartTime());
        dto.setFinishTime(entity.getFinishTime());
        dto.setShift(LessonScheduleDto.Shift.find(entity.getShift()));
        return dto;
    }

    public List<LessonSchedule> convertToEntityList(List<LessonScheduleDto> dtoList) {
        List<LessonSchedule> result = new ArrayList<>();
        for (LessonScheduleDto dto : dtoList) {
            LessonSchedule entity = new LessonSchedule();
            entity.setId(dto.getId());
            entity.setOrder(dto.getOrder());
            entity.setStartTime(dto.getStartTime());
            entity.setFinishTime(dto.getFinishTime());
            entity.setShift(dto.getShift().toString());
            result.add(entity);
        }
        return result;
    }

    public List<LessonScheduleDto> convertToDtoList(List<LessonSchedule> entityList) {
        List<LessonScheduleDto> result = new ArrayList<>();
        for (LessonSchedule entity : entityList) {
            LessonScheduleDto dto = new LessonScheduleDto();
            dto.setId(entity.getId());
            dto.setOrder(entity.getOrder());
            dto.setStartTime(entity.getStartTime());
            dto.setFinishTime(entity.getFinishTime());
            dto.setShift(LessonScheduleDto.Shift.find(entity.getShift()));
            result.add(dto);
        }
        return result;
    }
}
