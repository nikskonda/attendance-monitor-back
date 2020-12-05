package com.bntu.master.attendance.monitor.impl.resolver;

import com.bntu.master.attendance.monitor.api.exception.NotFoundException;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.impl.dataaccess.AccountRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.PersonRepository;
import com.bntu.master.attendance.monitor.impl.entity.Account;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonResolver {

    @Autowired
    private PersonRepository repository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleResolver roleResolver;

    public Person resolvePerson(ObjectRef objectRef) {
        if (objectRef.isNullable()) {
            throw new NotFoundException();
        }
        return objectRef.isNullId() ? repository.findByEmail(objectRef.getQualifier()).orElseThrow(NotFoundException::new)
                : repository.findById(objectRef.getId()).orElseThrow(NotFoundException::new);
    }

    public Account resolveUser(ObjectRef objectRef) {
        if (objectRef.isNullQualifier()) {
            throw new NotFoundException();
        }
        return accountRepository.findFirstByEmail(objectRef.getQualifier()).orElse(null);
    }

    public Person resolvePersonByRole(ObjectRef objectRef, RoleConstant role) {
        Person person = resolvePerson(objectRef);
        resolveUserByRole(ObjectRef.toObjectRef(person.getEmail()), role);
        return person;
    }

    public Account resolveUserByRole(ObjectRef objectRef, RoleConstant role) {
        Account account = resolveUser(objectRef);
        Role roleFromDb = roleResolver.resolve(role.get());
        for (Role r : account.getRoles()) {
            if (roleFromDb.equals(r)) {
                return account;
            }
        }
        throw new NotFoundException();
    }

}
