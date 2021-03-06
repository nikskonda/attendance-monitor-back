package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.exception.AttendanceMonitorException;
import com.bntu.master.attendance.monitor.api.exception.UsedInSystemException;
import com.bntu.master.attendance.monitor.api.model.GroupDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.converter.GroupConverter;
import com.bntu.master.attendance.monitor.impl.dataaccess.GroupRepository;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Speciality;
import com.bntu.master.attendance.monitor.impl.resolver.GroupResolver;
import com.bntu.master.attendance.monitor.impl.resolver.SpecialityResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired
    private GroupRepository repository;

    @Autowired
    private GroupResolver resolver;

    @Autowired
    private SpecialityResolver specialityResolver;

    @Autowired
    private GroupConverter converter;


    public GroupDto find(ObjectRef dto) {
        if (dto.isNullId()) {
            throw new AttendanceMonitorException();
        }
        Group group = resolver.resolve(dto);
        return converter.convertToDto(group);
    }

    public GroupDto create(GroupDto dto) {
        if (!dto.isNullId() && dto.isNullQualifier()) {
            throw new AttendanceMonitorException();
        }

        Speciality speciality = specialityResolver.resolve(dto.getSpeciality());
        Group group = converter.convertToEntity(dto, speciality);

        return converter.convertToDto(repository.save(group));
    }

    public GroupDto update(Long id, GroupDto dto) {
        dto.setId(id);
        resolver.resolve(dto);
        Speciality speciality = specialityResolver.resolve(dto.getSpeciality());
        Group group = converter.convertToEntity(dto, speciality);

        return converter.convertToDto(repository.save(group));
    }

    public void delete(ObjectRef dto) {
        try{
            repository.delete(resolver.resolve(dto));
        } catch (DataIntegrityViolationException ex) {
            throw new UsedInSystemException();
        }
    }

    public List<GroupDto> findAll() {
        return repository.findAll().stream().map(group -> converter.convertToDto(group)).collect(Collectors.toList());
    }

    public Page<GroupDto> findByPage(Pageable pageable) {
        return converter.convertToDtos(repository.findAll(pageable));
    }

}
