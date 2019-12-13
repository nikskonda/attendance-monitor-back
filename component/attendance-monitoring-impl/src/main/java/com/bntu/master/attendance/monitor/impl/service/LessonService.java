package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Lesson;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.api.exception.Exception;
import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.impl.resolver.GroupResolver;
import com.bntu.master.attendance.monitor.impl.resolver.PersonResolver;
import com.bntu.master.attendance.monitor.impl.converter.LessonConverter;
import com.bntu.master.attendance.monitor.impl.dataaccess.LessonRepository;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.impl.resolver.LessonResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    private GroupResolver groupResolver;

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

    public LessonDto update(LessonDto lessonDto) {
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
        return repository.findAll().stream().map(lesson -> converter.convertToDto(lesson)).collect(Collectors.toList());
    }


}
