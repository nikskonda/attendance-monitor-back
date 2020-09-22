package com.bntu.master.attendance.monitor.impl.converter;

import com.bntu.master.attendance.monitor.api.model.LessonScheduleDto;
import com.bntu.master.attendance.monitor.impl.entity.LessonSchedule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LessonScheduleConverter {

    public List<LessonSchedule> convertToEntityList(List<LessonScheduleDto> dtoList) {
        List<LessonSchedule> result = new ArrayList<>();
        for (LessonScheduleDto dto : dtoList) {
            LessonSchedule entity = new LessonSchedule();
            entity.setId(dto.getId());
            entity.setOrder(dto.getOrder());
            entity.setStartTime(dto.getStartTime());
            entity.setFinishTime(dto.getFinishTime());
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
            result.add(dto);
        }
        return result;
    }
}
