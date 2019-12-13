package com.bntu.master.attendance.monitor.impl.converter;

public interface Converter<E, DTO> {

    E convertToEntity(DTO dto);

    DTO convertToDto(E entity);

}
