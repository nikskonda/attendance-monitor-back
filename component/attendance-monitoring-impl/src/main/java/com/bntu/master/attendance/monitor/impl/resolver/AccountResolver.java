package com.bntu.master.attendance.monitor.impl.resolver;

import com.bntu.master.attendance.monitor.api.exception.NotFoundException;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.dataaccess.AccountRepository;
import com.bntu.master.attendance.monitor.impl.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountResolver {

    @Autowired
    protected AccountRepository repository;

    public Account resolve(ObjectRef objectRef) {
        if (objectRef.isNullQualifier()) {
            throw new NotFoundException();
        }
        return repository.findById(objectRef.getQualifier()).orElseThrow(NotFoundException::new);
    }

}
