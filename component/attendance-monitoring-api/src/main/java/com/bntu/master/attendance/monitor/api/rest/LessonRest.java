package com.bntu.master.attendance.monitor.api.rest;

import com.bntu.master.attendance.monitor.api.model.scheduleGrid.ScheduleCell;
import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.LessonScheduleDto;
import com.bntu.master.attendance.monitor.api.model.scheduleGrid.ScheduleGrid;
import com.bntu.master.attendance.monitor.api.model.scheduleGrid.ScheduleList;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RequestMapping("/lesson")
public interface LessonRest {

    @GetMapping("/{id}")
    LessonDto find(@PathVariable Long id);

    @GetMapping
    List<LessonDto> findAll();

    @GetMapping("/findByDateRange")
    List<LessonDto> findByDateRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate finalDate);

    @GetMapping("/findGridByDateRange")
    ScheduleList findGridByDateRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate finalDate,
                                     @RequestParam(required = false) Long personId);

    @PostMapping
    LessonDto create(@RequestBody LessonDto lesson);

    @PutMapping("/{id}")
    LessonDto update(@PathVariable Long id,
                     @RequestBody LessonDto lesson);

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id);

    @GetMapping("/schedule")
    List<LessonScheduleDto> getLessonScheduling();

    @PutMapping("/schedule")
    List<LessonScheduleDto> updateLessonScheduling(@RequestBody List<LessonScheduleDto> dtoList);

}
