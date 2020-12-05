package com.bntu.master.attendance.monitor.api.rest;

import com.bntu.master.attendance.monitor.api.model.ProfessorDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin
@RequestMapping("/professor")
public interface ProfessorRest extends BaseRest<ProfessorDto> {


}

