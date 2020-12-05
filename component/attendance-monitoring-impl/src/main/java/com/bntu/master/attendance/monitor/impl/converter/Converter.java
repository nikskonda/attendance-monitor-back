package com.bntu.master.attendance.monitor.impl.converter;

import com.bntu.master.attendance.monitor.impl.entity.Base;

public interface Converter<E extends Base, DTO> {

    E convertToEntity(DTO dto);

    DTO convertToDto(E entity);

}
