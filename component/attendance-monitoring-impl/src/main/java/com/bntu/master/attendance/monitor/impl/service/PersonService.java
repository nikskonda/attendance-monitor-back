package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.exception.NotFoundException;
import com.bntu.master.attendance.monitor.api.model.AccountDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.impl.converter.PersonConverter;
import com.bntu.master.attendance.monitor.impl.dataaccess.AccountRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.PersonRepository;
import com.bntu.master.attendance.monitor.impl.entity.Account;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.Role;
import com.bntu.master.attendance.monitor.impl.resolver.PersonResolver;
import com.bntu.master.attendance.monitor.impl.resolver.RoleResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonService {

    @Autowired
    private PersonRepository repository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PersonConverter converter;

    @Autowired
    private PersonResolver resolver;

    @Autowired
    private RoleResolver roleResolver;

    @Autowired
    private AccountService accountService;

    public PersonDto find(ObjectRef dto) {
        Optional<Person> person = repository.findByEmail(dto.getQualifier());
        Optional<Account> account = accountRepository.findByEmail(dto.getQualifier());
        if (!account.isPresent() && !person.isPresent()) {
            throw new NotFoundException();
        }
        PersonDto result = new PersonDto();
        if (person.isPresent()) {
            result = converter.convertToDto(person.get());
        }
        if (account.isPresent()) {
            result.setEmail(account.get().getEmail());
            result.setRoles(account.get().getRoles().stream().map(role -> ObjectRef.toObjectRef(role.getId(), role.getName())).collect(Collectors.toSet()));
        }
        return result;
    }

    public PersonDto createAndConvertToDto(PersonDto dto) {
        return converter.convertToDto(create(dto, dto.getRoles()));
    }

    public <T> Person create(PersonDto dto, Set<T> roles) {
        dto.setId(null);
        Person person = converter.convertToEntity(dto);
        Optional<Person> fromDB = repository.findByEmail(dto.getEmail());
        if (fromDB.isPresent()) {
            person.setId(fromDB.get().getId());
        }
        person = repository.save(person);

        accountService.createOrAddRolesIfExists(person.getEmail(), roles);
        return person;
    }

    public PersonDto update(PersonDto dto) {
        dto.setId(null);
        dto.setQualifier(dto.getEmail());
        Optional<Person> optional = repository.findByEmail(dto.getEmail());
        Person toUpdate = converter.convertToEntity(dto);
        if (optional.isPresent()) {
            toUpdate = optional.get();
            dto.setId(toUpdate.getId());
            converter.fillBase(toUpdate, dto);
        }
        repository.save(toUpdate);
        if (!CollectionUtils.isEmpty(dto.getRoles())) {
            AccountDto accountDto = new AccountDto();
            accountDto.setQualifier(dto.getEmail());
            accountDto.setRoles(dto.getRoles());
            accountService.updateRoles(accountDto);
        }
        return find(ObjectRef.toObjectRef(dto.getEmail()));
    }

    public void delete(ObjectRef ref) {
        Person person = resolver.resolvePerson(ref);
        repository.delete(person);
    }

    public List<PersonDto> findAll() {
        return repository.findAll().stream().map(person -> converter.convertToDto(person)).collect(Collectors.toList());
    }

    public List<PersonDto> findAllByRoles(List<RoleConstant> roles) {
        Set<Role> roleSet = roleResolver.resolveRefs(roles.stream().map(r -> ObjectRef.toObjectRef(r.getRole())).collect(Collectors.toSet()));
        Set<String> emails = accountRepository.findUserEmailByRolesIn(roleSet.stream().map(Role::getId).collect(Collectors.toSet()));
        return converter.convertToDtos(repository.findAllByIdInOrEmailIn(Collections.emptySet(), emails));
    }

    public Page<PersonDto> findPageByRoles(List<RoleConstant> roles, Pageable pageable) {
        Set<Role> roleSet = roleResolver.resolveRefs(roles.stream().map(r -> ObjectRef.toObjectRef(r.getRole())).collect(Collectors.toSet()));
        Set<String> emails = accountRepository.findUserEmailByRolesIn(roleSet.stream().map(Role::getId).collect(Collectors.toSet()));
        return converter.convertToDtos(repository.findAllByIdInOrEmailIn(Collections.emptySet(), emails, pageable));
    }
}
