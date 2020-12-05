package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.ProfessorDto;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.impl.converter.ProfessorConverter;
import com.bntu.master.attendance.monitor.impl.dataaccess.ProfessorPositionRepository;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.Position;
import com.bntu.master.attendance.monitor.impl.entity.ProfessorPosition;
import com.bntu.master.attendance.monitor.impl.resolver.PersonResolver;
import com.bntu.master.attendance.monitor.impl.resolver.PositionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorPositionRepository repository;

    @Autowired
    private ProfessorConverter converter;

    @Autowired
    private PersonResolver personResolver;

    @Autowired
    private PositionResolver positionResolver;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PersonService personService;

    public ProfessorDto find(Long id, String email) {
        Person prof = personResolver.resolvePerson(ObjectRef.toObjectRef(id, email));
        ProfessorPosition professorPosition = repository.findFirstByProfessor(prof);
        return converter.convertToDto(professorPosition);
    }


    public ProfessorDto create(ProfessorDto professorDto) {
        Position position = positionResolver.resolve(professorDto.getPosition());
        Person prof = personService.create(professorDto, Collections.singleton(RoleConstant.PROFESSOR));
        return converter.convertToDto(repository.save(new ProfessorPosition(position, prof)));
    }

    public ProfessorDto update(Long id, ProfessorDto professorDto) {
        Person person = personResolver.resolvePersonByRole(ObjectRef.toObjectRef(id), RoleConstant.PROFESSOR);
        if (!person.getEmail().equals(professorDto.getEmail())) {
            accountService.updateEmail(person.getEmail(), professorDto.getEmail());
        }
        personService.update(professorDto);

        ProfessorPosition professorPosition = repository.findFirstByProfessor(person);
        Position position = positionResolver.resolve(professorDto.getPosition());
        if (!professorPosition.getPosition().getId().equals(position.getId())) {
            professorPosition.setPosition(position);
            professorPosition = repository.save(professorPosition);
        }
        return converter.convertToDto(professorPosition);
    }

    public void delete(Long id) {
        Person prof = personResolver.resolvePersonByRole(ObjectRef.toObjectRef(id), RoleConstant.PROFESSOR);
        ProfessorPosition professorPosition = repository.findFirstByProfessor(prof);
        repository.delete(professorPosition);

        if (accountService.removeRoleOrAccount(prof.getEmail(), RoleConstant.PROFESSOR)) {
            personService.delete(ObjectRef.toObjectRef(prof.getId()));
        }
    }

    public Page<ProfessorDto> findPage(Pageable pageable) {
        return converter.convertProfessorsToDtos(repository.findPage(pageable));
    }

    public List<ProfessorDto> findAll() {
        List<ProfessorDto> list = converter.convertToDtos(repository.findAll());
        list.sort(Comparator.comparing(ProfessorDto::getFullName));
        return list;
    }
}
