package com.bntu.master.attendance.monitor.impl.rest;

import com.bntu.master.attendance.monitor.api.exception.AccessDeniedException;
import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.LessonScheduleDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.scheduleGrid.ScheduleList;
import com.bntu.master.attendance.monitor.api.rest.LessonRest;
import com.bntu.master.attendance.monitor.impl.service.LessonScheduleService;
import com.bntu.master.attendance.monitor.impl.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class LessonRestImpl implements LessonRest {

    @Autowired
    private LessonService service;

    @Autowired
    private LessonScheduleService lessonScheduleService;

    @Override
    public LessonDto find(Long id) {
        return service.find(ObjectRef.toObjectRef(id));
    }

    @Override
    public List<LessonDto> findAll() {
        return service.findAll();
    }

    @Override
    public List<LessonDto> findByDateRange(LocalDate startDate, LocalDate finalDate) {
        return service.findByDateRange(startDate, finalDate);
    }

    @Override
    public Page<LessonDto> findPageByDateRange(LocalDate startDate, LocalDate finalDate, Pageable pageable) {
        return service.findPageByDateRange(startDate, finalDate, pageable);
    }

    @Override
    public ScheduleList findGridByDateRange(LocalDate startDate, LocalDate finalDate, Long personId, Authentication authentication) {
        if (authentication != null) {
            if (personId != null &&
                    authentication.getAuthorities()
                            .stream()
                            .findFirst()
                            .filter(a -> "ADMIN".equals(a.getAuthority()))
                            .isPresent()) {
                return service.findGridByDateRange(startDate, finalDate, personId);
            } else {
                return service.findGridByDateRange(startDate, finalDate, authentication.getName());
            }
        }
        throw new AccessDeniedException();
    }

    @Override
    public List<LessonDto> createSeries(Long inWeek, Long count, LessonDto lesson) {
        return service.createSeries(lesson, inWeek, count);
    }

    @Override
    public LessonDto create(LessonDto lesson) {
        return service.create(lesson);
    }

    @Override
    public LessonDto update(Long id, LessonDto lesson) {
        return service.update(id, lesson);
    }

    @Override
    public void delete(Long id) {
        service.delete(ObjectRef.toObjectRef(id));
    }

    @Override
    public List<LessonScheduleDto> getLessonScheduling() {
        return lessonScheduleService.findAll();
    }

    @Override
    public List<LessonScheduleDto> updateLessonScheduling(List<LessonScheduleDto> dtoList) {
        return lessonScheduleService.updateAll(dtoList);
    }
}
