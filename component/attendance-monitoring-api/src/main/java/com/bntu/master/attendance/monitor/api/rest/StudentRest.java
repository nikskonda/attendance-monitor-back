package com.bntu.master.attendance.monitor.api.rest;

import com.bntu.master.attendance.monitor.api.model.StudentDto;
import com.bntu.master.attendance.monitor.api.model.StudentWithParentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@CrossOrigin
@RequestMapping("/student")
public interface StudentRest extends BaseRest<StudentWithParentDto> {

    @GetMapping("/group/{groupId}/page")
    Page<StudentWithParentDto> findByPage(@PathVariable Long groupId, Pageable pageable);

    @GetMapping("/group/{groupId}")
    List<StudentDto> findByGroup(@PathVariable Long groupId);

    @GetMapping("/byParent")
    List<StudentWithParentDto> findByParent(@RequestParam String parentEmail);
}

