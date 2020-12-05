package com.bntu.master.attendance.monitor.impl.converter;

import com.bntu.master.attendance.monitor.api.model.LessonScheduleDto;
import com.bntu.master.attendance.monitor.impl.entity.LessonSchedule;
import org.springframework.stereotype.Component;

@Component
public class LessonScheduleConverter extends AbstractListConverter<LessonSchedule, LessonScheduleDto> {

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
}
