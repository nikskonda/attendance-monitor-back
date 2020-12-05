package com.bntu.master.attendance.monitor.impl.converter;

import com.bntu.master.attendance.monitor.impl.entity.Base;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface ListConverter<E extends Base, DTO> extends Converter<E, DTO> {

    List<DTO> convertToDtos(List<E> entities);

    Set<DTO> convertToDtos(Set<E> entities);

    Page<DTO> convertToDtos(Page<E> page);

}
