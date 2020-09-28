package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.exception.Exception;
import com.bntu.master.attendance.monitor.api.model.LessonScheduleDto;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.api.model.SubjectTypeConstant;
import com.bntu.master.attendance.monitor.api.model.scheduleGrid.ScheduleGrid;
import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.api.model.scheduleGrid.ScheduleList;
import com.bntu.master.attendance.monitor.api.model.util.LocalTimeSpan;
import com.bntu.master.attendance.monitor.impl.converter.LessonConverter;
import com.bntu.master.attendance.monitor.impl.converter.PersonConverter;
import com.bntu.master.attendance.monitor.impl.dataaccess.LessonRepository;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Lesson;
import com.bntu.master.attendance.monitor.impl.entity.LessonSchedule;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.Subject;
import com.bntu.master.attendance.monitor.impl.resolver.GroupResolver;
import com.bntu.master.attendance.monitor.impl.resolver.LessonResolver;
import com.bntu.master.attendance.monitor.impl.resolver.PersonResolver;
import com.bntu.master.attendance.monitor.impl.resolver.SubjectResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LessonService {

    @Autowired
    private LessonRepository repository;

    @Autowired
    private LessonResolver resolver;

    @Autowired
    private PersonResolver personResolver;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonConverter personConverter;

    @Autowired
    private GroupResolver groupResolver;

    @Autowired
    private SubjectResolver subjectResolver;

    @Autowired
    private LessonConverter converter;

    public LessonDto find(ObjectRef objectRef) {
        return converter.convertToDto(resolver.resolve(objectRef));
    }

    public LessonDto create(LessonDto lessonDto) {
        if (!lessonDto.isNullId()) {
            throw new Exception();
        }
        Group group = groupResolver.resolve(lessonDto.getGroup());
        Person professor = personResolver.resolveByRole(lessonDto.getProfessor(), RoleConstant.PROFESSOR);
        Lesson lesson = converter.convertToEntity(lessonDto, professor, group);

        lesson = repository.save(lesson);

        return converter.convertToDto(lesson);
    }

    public LessonDto update(Long id, LessonDto lessonDto) {
        lessonDto.setId(id);
        resolver.resolve(lessonDto);
        Group group = groupResolver.resolve(lessonDto.getGroup());
        Person professor = personResolver.resolveByRole(lessonDto.getProfessor(), RoleConstant.PROFESSOR);
        Lesson lesson = converter.convertToEntity(lessonDto, professor, group);

        lesson = repository.save(lesson);

        return converter.convertToDto(lesson);
    }

    public void delete(ObjectRef objectRef) {
        repository.delete(resolver.resolve(objectRef));
    }

    public List<LessonDto> findAll() {
        return repository.findAll()
                .stream()
                .map(lesson -> converter.convertToDto(lesson))
                .collect(Collectors.toList());
    }

    public List<LessonDto> findByDateRange(LocalDate startDate, LocalDate finalDate) {
        return repository.findAllByDateBetween(startDate, finalDate)
                .stream()
                .map(lesson -> converter.convertToDto(lesson))
                .collect(Collectors.toList());
    }

    public List<LessonDto> findByDateRangeAndProfessor(LocalDate startDate, LocalDate finalDate, Person prof) {
        return repository.findAllByDateBetweenAndProfessor(startDate, finalDate, prof)
                .stream()
                .map(lesson -> converter.convertToDto(lesson))
                .collect(Collectors.toList());
    }


    public Set<LessonDto> findAllByDateBetweenAndSubjectAndSubjectTypeIn(LocalDate startDate, LocalDate finalDate, Subject subject, Set<SubjectTypeConstant> subjectType) {
        return repository.findAllByDateBetweenAndSubjectAndSubjectTypeIn(startDate, finalDate, subject, subjectType)
                .stream()
                .map(lesson -> converter.convertToDto(lesson))
                .collect(Collectors.toSet());
    }

    public ScheduleList findGridByDateRange(LocalDate startDate, LocalDate finalDate, Long personId) {
        Person prof = personService.resolve(ObjectRef.toObjectRef(personId));

        List<LessonDto> lessons = findByDateRangeAndProfessor(startDate, finalDate, prof);
        Set<LocalDate> header = new HashSet<>();
        lessons.forEach(l -> header.add(l.getDate()));
        header.stream().sorted();

        Set<LessonScheduleDto> headerRow = new HashSet<>();
        lessons.forEach(l -> headerRow.add(l.getTime()));
        headerRow.stream().sorted(Comparator.comparing(LessonScheduleDto::getOrder));

        ScheduleGrid grid = new ScheduleGrid(header, headerRow);
        lessons.forEach(l -> grid.setCell(l));

        return new ScheduleList(grid, personConverter.convertToDto(prof));
    }
}
