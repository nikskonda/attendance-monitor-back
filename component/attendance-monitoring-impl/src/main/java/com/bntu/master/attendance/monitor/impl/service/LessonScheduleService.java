package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.model.LessonScheduleDto;
import com.bntu.master.attendance.monitor.impl.converter.LessonScheduleConverter;
import com.bntu.master.attendance.monitor.impl.dataaccess.LessonScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class LessonScheduleService {

    @Autowired
    private LessonScheduleConverter converter;

    @Autowired
    private LessonScheduleRepository repository;

    public List<LessonScheduleDto> updateAll(List<LessonScheduleDto> dtos) {
        repository.deleteAll();
//        repository.saveAll(converter.convertToEntityList(dtos));
        return findAll();
    }

    public List<LessonScheduleDto> findAll() {
        List<LessonScheduleDto> result = converter.convertToDtos(repository.findAll());
        result.sort(Comparator.comparingLong(LessonScheduleDto::getOrder));
        return result;
    }

}
