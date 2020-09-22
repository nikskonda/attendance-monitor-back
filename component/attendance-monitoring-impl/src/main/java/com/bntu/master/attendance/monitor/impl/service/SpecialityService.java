package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.exception.Exception;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.dataaccess.SpecialityRepository;
import com.bntu.master.attendance.monitor.impl.entity.Speciality;
import com.bntu.master.attendance.monitor.impl.resolver.SpecialityResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialityService {

    @Autowired
    private SpecialityRepository repository;

    @Autowired
    private SpecialityResolver resolver;

    public ObjectRef find(ObjectRef dto) {
        if (dto.isNullable()) {
            throw new Exception();
        }
        Speciality speciality = resolver.resolve(dto);
        return ObjectRef.toObjectRef(speciality.getId(), speciality.getName());
    }

    public ObjectRef create(ObjectRef dto) {
        if (dto.isNullable() || (!dto.isNullId() && dto.isNullQualifier())) {
            throw new Exception();
        }

        Speciality speciality = new Speciality();
        speciality.setName(dto.getQualifier());
        speciality = repository.save(speciality);

        return ObjectRef.toObjectRef(speciality.getId(), speciality.getName());
    }

    public ObjectRef update(Long id, ObjectRef dto) {
        dto.setId(id);
        if (dto.isNullAnyField()) {
            throw new Exception();
        }
        Speciality speciality = resolver.resolve(dto);
        speciality.setName(dto.getQualifier());
        speciality = repository.save(speciality);

        return ObjectRef.toObjectRef(speciality.getId(), speciality.getName());
    }

    public void delete(ObjectRef dto) {
        Speciality speciality = resolver.resolve(dto);
        repository.delete(speciality);
    }

    public List<ObjectRef> findAll() {
        return repository.findAll().stream().map(spec -> ObjectRef.toObjectRef(spec.getId(), spec.getName())).collect(Collectors.toList());
    }

}
