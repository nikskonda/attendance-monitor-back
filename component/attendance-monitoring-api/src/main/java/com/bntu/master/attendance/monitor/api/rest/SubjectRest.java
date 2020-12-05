package com.bntu.master.attendance.monitor.api.rest;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.SubjectTypeConstant;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@CrossOrigin
@RequestMapping("/subject")
public interface SubjectRest extends BaseRest<ObjectRef> {

    @GetMapping("/types")
    SubjectTypeConstant[] getSubjectTypes();

    @GetMapping("/byGroup")
    List<ObjectRef> findByGroup(@RequestParam Long groupId);

}
