package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.exception.AttendanceMonitorException;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.converter.PositionConverter;
import com.bntu.master.attendance.monitor.impl.dataaccess.LessonRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.PositionRepository;
import com.bntu.master.attendance.monitor.impl.entity.Position;
import com.bntu.master.attendance.monitor.impl.resolver.GroupResolver;
import com.bntu.master.attendance.monitor.impl.resolver.PositionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PositionService {

    @Autowired
    private PositionRepository repository;

    @Autowired
    private PositionResolver resolver;


    @Autowired
    private PositionConverter converter;

    @Autowired
    private GroupResolver groupResolver;

    @Autowired
    private LessonRepository lessonRepository;


    public ObjectRef find(ObjectRef dto) {
        Position subject = resolver.resolve(dto);
        return converter.convertToDto(subject);
    }

    public ObjectRef create(ObjectRef dto) {
        if (!dto.isNullId() && dto.isNullQualifier()) {
            throw new AttendanceMonitorException();
        }
        Position subject = converter.convertToEntity(dto);

        return converter.convertToDto(repository.save(subject));
    }

    public ObjectRef update(Long id, ObjectRef dto) {
        dto.setId(id);
        resolver.resolve(dto);
        Position subject = converter.convertToEntity(dto);

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

}
