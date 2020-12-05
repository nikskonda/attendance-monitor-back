package com.bntu.master.attendance.monitor.impl.service.util;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CrudService<DTO> {

    DTO find(ObjectRef dto);

    DTO create(DTO dto);

    DTO update(Long id, DTO dto);

    void delete(ObjectRef dto);

    List<DTO> findAll();

    Page<DTO> findByPage(Pageable pageable);

}
