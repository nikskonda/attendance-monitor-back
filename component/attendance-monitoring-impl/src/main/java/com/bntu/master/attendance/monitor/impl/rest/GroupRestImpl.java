package com.bntu.master.attendance.monitor.impl.rest;

import com.bntu.master.attendance.monitor.api.model.GroupDto;
import com.bntu.master.attendance.monitor.api.model.GroupVolumeConstant;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.rest.GroupRest;
import com.bntu.master.attendance.monitor.impl.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GroupRestImpl implements GroupRest {

    @Autowired
    private GroupService service;

    @Override
    public GroupDto find(Long id) {
        return service.find(ObjectRef.toObjectRef(id));
    }

    @Override
    public List<GroupDto> findAll() {
        return service.findAll();
    }

    @Override
    public List<String> getGroupVolumes() {
        return Arrays.stream(GroupVolumeConstant.values())
                .sorted(Comparator.comparing(GroupVolumeConstant::getSortOrder))
                .map(GroupVolumeConstant::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Page<GroupDto> findByPage(Pageable pageable) {
        return service.findByPage(pageable);
    }

    @Override
    public GroupDto create(GroupDto dto) {
        return service.create(dto);
    }

    @Override
    public GroupDto update(Long id, GroupDto dto) {
        return service.update(id, dto);
    }

    @Override
    public void delete(Long id) {
        service.delete(ObjectRef.toObjectRef(id));
    }
}
