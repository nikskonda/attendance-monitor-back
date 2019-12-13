package com.bntu.master.attendance.monitor.impl.rest;

import com.bntu.master.attendance.monitor.api.model.GroupDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.rest.GroupRest;
import com.bntu.master.attendance.monitor.impl.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GroupRestImpl implements GroupRest {

    @Autowired
    private GroupService service;

    @Override
    public GroupDto find(Long id, String qualifier) {
        return service.find(ObjectRef.toObjectRef(id, qualifier));
    }

    @Override
    public List<GroupDto> findAll() {
        return service.findAll();
    }

    @Override
    public GroupDto create(GroupDto group) {
        return service.create(group);
    }

    @Override
    public GroupDto update(Long id, GroupDto group) {
        return service.update(id, group);
    }

    @Override
    public void delete(Long id) {
        service.delete(ObjectRef.toObjectRef(id));
    }
}
