package com.bntu.master.attendance.monitor.impl.rest;

import com.bntu.master.attendance.monitor.api.exception.UnsupportedMethodException;
import com.bntu.master.attendance.monitor.api.model.AccountDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.api.model.util.UpdatePassword;
import com.bntu.master.attendance.monitor.api.rest.AccountRest;
import com.bntu.master.attendance.monitor.impl.service.AccountService;
import com.bntu.master.attendance.monitor.impl.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountRestImpl implements AccountRest {

    @Autowired
    private AccountService service;

    @Autowired
    private PersonService personService;

    @Override
    public AccountDto find(String email) {
        return service.loadUserByEmail(email);
    }

    @Override
    public AccountDto updatePassword(Authentication authentication, UpdatePassword updatePassword) {
        if (authentication != null && authentication.getPrincipal() != null &&
                authentication.getPrincipal().equals(updatePassword.getEmail())) {
            return service.updatePassword(updatePassword.getEmail(), updatePassword.getOldPassword(), updatePassword.getNewPassword());
        }
        throw new RuntimeException();
    }

    @Override
    public List<AccountDto> findAll() {
        return null;
    }

    @Override
    public Page<AccountDto> findPage(Pageable pageable) {
        return service.findPage(pageable);
    }

    @Override
    public Page<AccountDto> search(String search, RoleConstant role, Pageable pageable) {
        return service.search(search, role, pageable);
    }

    @Override
    public boolean isUniqueEmail(String email) {
        return service.isUniqueEmail(email);
    }

    @Override
    public void resetPassword(String email) {
        service.resetPassword(email);
    }

    @Override
    public void changeLock(String email) {
        service.changeLockValue(email);
    }

    @Override
    public AccountDto updateRoles(AccountDto accountDto) {
        return service.updateRoles(accountDto);
    }

    @Override
    public PersonDto get(String email) {
        return personService.find(ObjectRef.toObjectRef(email));
    }

    @Override
    public PersonDto create(PersonDto person) {
        return personService.createAndConvertToDto(person);
    }

    @Override
    public PersonDto update(PersonDto person) {
        return personService.update(person);
    }

    @Override
    public void delete(String email) {
        throw new UnsupportedMethodException();
    }
}
