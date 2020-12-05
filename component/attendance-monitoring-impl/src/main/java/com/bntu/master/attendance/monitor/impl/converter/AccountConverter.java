package com.bntu.master.attendance.monitor.impl.converter;

import com.bntu.master.attendance.monitor.api.exception.UnsupportedMethodException;
import com.bntu.master.attendance.monitor.api.model.AccountDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.entity.Account;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Component
public class AccountConverter extends AbstractListConverter<Account, AccountDto> {

    @Override
    public Account convertToEntity(AccountDto accountDto) {
        throw new UnsupportedMethodException();
    }

    public AccountDto convertToDto(Account entity) {
        AccountDto accountDto = new AccountDto();
        accountDto.setFullName(entity.getFullName());
        accountDto.setPassword(entity.getPassword());
        accountDto.setQualifier(entity.getEmail());
        accountDto.setMustUpdatePassword(!entity.getPasswordExpireDate().isAfter(LocalDate.now()));
        accountDto.setRoles(entity.getRoles().stream().map(role -> ObjectRef.toObjectRef(role.getId(), role.getName())).collect(Collectors.toSet()));
        accountDto.setLock(entity.isLock());
        return accountDto;
    }
}
