package com.bntu.master.attendance.monitor.api.rest;

import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.LessonScheduleDto;
import com.bntu.master.attendance.monitor.api.model.LessonSeries;
import com.bntu.master.attendance.monitor.api.model.scheduleGrid.ScheduleList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RequestMapping("/lesson")
public interface LessonRest extends BaseRest<LessonDto> {

    @GetMapping("/findByDateRange")
    List<LessonDto> findByDateRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate finalDate);

    @GetMapping("/findPageByDateRange")
    Page<LessonDto> findPageByDateRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate finalDate, Pageable pageable);


    @GetMapping("/findGridByDateRange")
    ScheduleList findGridByDateRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate finalDate,
                                     @RequestParam(required = false) Long personId,
                                     @RequestParam(required = false) boolean topDateHeader,
                                     Authentication authentication);


    @PostMapping("/series/create")
    List<LessonDto> createSeries(@RequestBody LessonSeries lessonSeries);

    @PostMapping("/series/delete")
    void deleteSeries(@RequestBody LessonSeries lessonSeries);

    @GetMapping("/schedule")
    List<LessonScheduleDto> getLessonScheduling();

    @PutMapping("/schedule")
    List<LessonScheduleDto> updateLessonScheduling(@RequestBody List<LessonScheduleDto> dtoList);

}
