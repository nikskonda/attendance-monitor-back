package com.bntu.master.attendance.monitor.impl.resolver;

import com.bntu.master.attendance.monitor.api.exception.NotFoundException;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.impl.dataaccess.PersonRepository;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonResolver {

    @Autowired
    private PersonRepository repository;

    @Autowired
    private RoleResolver roleResolver;

    public Person resolve(ObjectRef objectRef) {
        if (objectRef.isNullable()) {
            throw new NotFoundException();
        }
        return objectRef.isNullId() ? repository.findByEmail(objectRef.getQualifier()).orElseThrow(NotFoundException::new)
                : repository.findById(objectRef.getId()).orElseThrow(NotFoundException::new);
    }

    public Person resolveByRole(ObjectRef objectRef, RoleConstant role) {
        Person person = resolve(objectRef);
        Role roleFromDb = roleResolver.resolve(role.get());
        for (Role r : person.getRoles()) {
            if (roleFromDb.equals(r)){
                return person;
            }
        }
        throw new NotFoundException();
    }

}
