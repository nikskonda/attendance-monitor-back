package com.bntu.master.attendance.monitor.api.rest;

import com.bntu.master.attendance.monitor.api.model.GroupDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@CrossOrigin
@RequestMapping("/group")
public interface GroupRest extends BaseRest<GroupDto> {

    @GetMapping("/volume")
    List<String> getGroupVolumes();

}
