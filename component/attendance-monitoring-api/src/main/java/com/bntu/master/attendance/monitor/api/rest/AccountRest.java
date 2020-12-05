package com.bntu.master.attendance.monitor.api.rest;

import com.bntu.master.attendance.monitor.api.model.AccountDto;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.api.model.util.UpdatePassword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@CrossOrigin
@RequestMapping("/account")
public interface AccountRest {

    @GetMapping("/byEmail")
    AccountDto find(@RequestParam String email);

    @PostMapping("/updatePassword")
    AccountDto updatePassword(Authentication authentication,
                              @RequestBody UpdatePassword updatePassword);

    @GetMapping("/isUniqueEmail")
    boolean isUniqueEmail(@RequestParam String email);

    @GetMapping
    List<AccountDto> findAll();

    @GetMapping("/page")
    Page<AccountDto> findPage(Pageable pageable);

    @GetMapping("/search")
    Page<AccountDto> search(@RequestParam(required = false) String search, @RequestParam(required = false) RoleConstant role, Pageable pageable);

    @PostMapping("/resetPassword")
    void resetPassword(@RequestBody String email);

    @PostMapping("/changeLock")
    void changeLock(@RequestBody String email);

    @PostMapping("/updateRoles")
    AccountDto updateRoles(@RequestBody AccountDto accountDto);

    @GetMapping("/{email}")
    PersonDto get(@PathVariable String email);

    @PostMapping("/create")
    PersonDto create(@RequestBody PersonDto person);

    @PutMapping("/update")
    PersonDto update(@RequestBody PersonDto person);

    @DeleteMapping("/{email}")
    void delete(@PathVariable String email);

}
