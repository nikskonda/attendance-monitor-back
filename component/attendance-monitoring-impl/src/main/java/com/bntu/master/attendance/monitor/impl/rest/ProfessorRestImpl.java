package com.bntu.master.attendance.monitor.impl.rest;

import com.bntu.master.attendance.monitor.api.model.ProfessorDto;
import com.bntu.master.attendance.monitor.api.rest.ProfessorRest;
import com.bntu.master.attendance.monitor.impl.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProfessorRestImpl implements ProfessorRest {

    @Autowired
    private ProfessorService service;


    @Override
    public ProfessorDto find(Long id) {
        return service.find(id, null);
    }

    @Override
    public List<ProfessorDto> findAll() {
        return service.findAll();
    }

    @Override
    public Page<ProfessorDto> findByPage(Pageable pageable) {
        return service.findPage(pageable);
    }


    @Override
    public ProfessorDto create(ProfessorDto dto) {
        return service.create(dto);
    }

    @Override
    public ProfessorDto update(Long id, ProfessorDto dto) {
        return service.update(id, dto);
    }

    @Override
    public void delete(Long id) {
        service.delete(id);
    }
}
