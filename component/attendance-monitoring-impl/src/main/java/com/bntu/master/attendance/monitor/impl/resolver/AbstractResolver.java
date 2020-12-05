package com.bntu.master.attendance.monitor.impl.resolver;

import com.bntu.master.attendance.monitor.api.exception.NotFoundException;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.entity.Base;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public class AbstractResolver<REPO extends JpaRepository<ENTITY, Long>, ENTITY extends Base> implements Resolver<ENTITY> {

    @Autowired
    protected REPO repository;

    @Override
    public ENTITY resolve(ObjectRef objectRef) {
        if (objectRef.isNullId()) {
            throw new NotFoundException();
        }
        return repository.findById(objectRef.getId()).orElseThrow(NotFoundException::new);
    }
}
