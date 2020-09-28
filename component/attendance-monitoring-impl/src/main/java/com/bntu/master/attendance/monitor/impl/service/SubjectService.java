package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.exception.Exception;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.converter.SubjectConverter;
import com.bntu.master.attendance.monitor.impl.dataaccess.SubjectRepository;
import com.bntu.master.attendance.monitor.impl.entity.Subject;
import com.bntu.master.attendance.monitor.impl.resolver.SubjectResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository repository;

    @Autowired
    private SubjectResolver resolver;


    @Autowired
    private SubjectConverter converter;


    public ObjectRef find(ObjectRef dto) {
        Subject subject = resolver.resolve(dto);
        return converter.convertToDto(subject);
    }

    public ObjectRef create(ObjectRef dto) {
        if (!dto.isNullId() && dto.isNullQualifier()) {
            throw new Exception();
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

}
