package com.bntu.master.attendance.monitor.impl.service.util;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.impl.converter.ListConverter;
import com.bntu.master.attendance.monitor.impl.entity.Base;
import com.bntu.master.attendance.monitor.impl.resolver.Resolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract class AbstractCrudService<
        DTO extends ObjectRef,
        RESOLVER extends Resolver<ENTITY>,
        CONVERTER extends ListConverter<ENTITY, DTO>,
        REPOSITORY extends JpaRepository<ENTITY, Long>,
        ENTITY extends Base
        > implements CrudService<DTO> {

    @Autowired
    private REPOSITORY repository;

    @Autowired
    private RESOLVER resolver;

    @Autowired
    private CONVERTER converter;


    @Override
    public DTO find(ObjectRef dto) {
        return converter.convertToDto(resolver.resolve(dto));
    }

    @Override
    public DTO create(DTO dto) {
        ENTITY entity = converter.convertToEntity(dto);
        return converter.convertToDto(repository.save(entity));
    }

    @Override
    public abstract DTO update(Long id, DTO dto);

    @Override
    public void delete(ObjectRef dto) {
        resolver.resolve(dto);
        repository.deleteById(dto.getId());
    }

    @Override
    public List<DTO> findAll() {
        return converter.convertToDtos(repository.findAll());
    }

    @Override
    public Page<DTO> findByPage(Pageable pageable) {
        return converter.convertToDtos(repository.findAll(pageable));
    }
}
