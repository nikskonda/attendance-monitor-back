package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.exception.AttendanceMonitorException;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.converter.SubjectConverter;
import com.bntu.master.attendance.monitor.impl.dataaccess.LessonRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.SubjectRepository;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Lesson;
import com.bntu.master.attendance.monitor.impl.entity.Subject;
import com.bntu.master.attendance.monitor.impl.resolver.GroupResolver;
import com.bntu.master.attendance.monitor.impl.resolver.SubjectResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository repository;

    @Autowired
    private SubjectResolver resolver;


    @Autowired
    private SubjectConverter converter;

    @Autowired
    private GroupResolver groupResolver;

    @Autowired
    private LessonRepository lessonRepository;


    public ObjectRef find(ObjectRef dto) {
        Subject subject = resolver.resolve(dto);
        return converter.convertToDto(subject);
    }

    public ObjectRef create(ObjectRef dto) {
        if (!dto.isNullId() && dto.isNullQualifier()) {
            throw new AttendanceMonitorException();
        }
        Subject subject = converter.convertToEntity(dto);

        return converter.convertToDto(repository.save(subject));
    }

    public ObjectRef update(Long id, ObjectRef dto) {
        dto.setId(id);
        resolver.resolve(dto);
        Subject subject = converter.convertToEntity(dto);

        return converter.convertToDto(repository.save(subject));
    }

    public void delete(ObjectRef dto) {
        repository.delete(resolver.resolve(dto));
    }

    public List<ObjectRef> findAll() {
        return repository.findAll().stream()
                .map(subject -> converter.convertToDto(subject))
                .sorted(Comparator.comparing(ObjectRef::getQualifier))
                .collect(Collectors.toList());
    }

    public Page<ObjectRef> findByPage(Pageable pageable) {
        return converter.convertToDtos(repository.findAll(pageable));
    }

    public List<ObjectRef> findByGroup(ObjectRef groupRef) {
        Group group = groupResolver.resolve(groupRef);
        List<Lesson> lesson = lessonRepository.findAllByGroup(group);
        Set<Subject> subjects = lesson.stream().map(Lesson::getSubject).collect(Collectors.toSet());
        return subjects.stream()
                .map(s -> converter.convertToDto(s))
                .sorted(Comparator.comparing(ObjectRef::getQualifier))
                .collect(Collectors.toList());
    }

}
