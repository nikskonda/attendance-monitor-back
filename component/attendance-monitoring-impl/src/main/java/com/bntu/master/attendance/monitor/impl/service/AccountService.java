package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.exception.AttendanceMonitorException;
import com.bntu.master.attendance.monitor.api.exception.EmailBusyException;
import com.bntu.master.attendance.monitor.api.exception.NotFoundException;
import com.bntu.master.attendance.monitor.api.model.AccountDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.impl.converter.AccountConverter;
import com.bntu.master.attendance.monitor.impl.dataaccess.AccountRepository;
import com.bntu.master.attendance.monitor.impl.entity.Account;
import com.bntu.master.attendance.monitor.impl.entity.Role;
import com.bntu.master.attendance.monitor.impl.resolver.PersonResolver;
import com.bntu.master.attendance.monitor.impl.resolver.RoleResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private PersonResolver resolver;

    @Autowired
    private RoleResolver roleResolver;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AccountConverter converter;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return loadUserByEmail(email);
    }

    public AccountDto updatePassword(String email, String oldPassword, String newPassword) {
        Account account = repository.findByEmail(email).orElseThrow(NotFoundException::new);
        if (!bCryptPasswordEncoder.matches(oldPassword, account.getPassword())) {
            throw new RuntimeException();
        }
        account.setPassword(bCryptPasswordEncoder.encode(newPassword));
        account.setPasswordExpireDate(LocalDate.now().plusMonths(3));
        repository.save(account);
        return loadUserByEmail(email);
    }

    public AccountDto loadUserByEmail(String email) throws UsernameNotFoundException {
        Account account = repository.findByEmail(email).orElseThrow(NotFoundException::new);
        return converter.convertToDto(account);
    }

    public <T> Account createOrAddRoleIfExists(String email, T role) {
        return createOrAddRolesIfExists(email, Collections.singleton(role));
    }

    public <T> Account createOrAddRolesIfExists(String email, Set<T> roles) {
        Account account = resolver.resolveUser(ObjectRef.toObjectRef(email));

        Set<ObjectRef> roleRefs = null;
        if (roles.size() > 0 && roles.iterator().next() instanceof RoleConstant) {
            roleRefs = roles.stream().map(role -> {
                RoleConstant roleConstant = (RoleConstant) role;
                return ObjectRef.toObjectRef(roleConstant.getId(), roleConstant.getRole());
            }).collect(Collectors.toSet());
        }
        if (roles.size() > 0 && roles.iterator().next() instanceof ObjectRef) {
            roleRefs = (Set<ObjectRef>) roles;
        }

        Set<Role> newRoles = roleResolver.resolveRefs(roleRefs);

        if (account != null) {
            return addRole(account, newRoles);
        }

        String password = generatePassword();
        Account newAccount = new Account();
        newAccount.setEmail(email);
        newAccount.setPassword(bCryptPasswordEncoder.encode(password));
        newAccount.setPasswordExpireDate(LocalDate.now());
        newAccount.setRoles(newRoles);
        newAccount = repository.save(newAccount);

        try {
            sendEmail(email, password);
        } catch (MailAuthenticationException ex) {
            //ToDo
            password = email.substring(0, email.indexOf("@"));
            newAccount.setPassword(bCryptPasswordEncoder.encode(password));
            newAccount = repository.save(newAccount);
            System.out.println(ex.getMessage());
        }
        return newAccount;
    }

    public boolean removeRoleOrAccount(String email, RoleConstant role) {
        Account account = resolver.resolveUserByRole(ObjectRef.toObjectRef(email), role);
        Role r = roleResolver.resolve(role);
        account.getRoles().remove(r);
        if (account.getRoles().size() == 0) {
            repository.deleteById(account.getEmail());
            return true;
        }
        return false;
    }

    private Account addRole(Account account, Set<Role> newRoles) {
        account.getRoles().addAll(newRoles);
        return repository.save(account);
    }

    private String generatePassword() {
        int length = 10;

        final char[] lowercase = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        final char[] uppercase = "ABCDEFGJKLMNPRSTUVWXYZ".toCharArray();
        final char[] numbers = "0123456789".toCharArray();
        final char[] symbols = "^$?!@#%&".toCharArray();
        final char[] allAllowed = "abcdefghijklmnopqrstuvwxyzABCDEFGJKLMNPRSTUVWXYZ0123456789^$?!@#%&".toCharArray();

        //Use cryptographically secure random number generator
        Random random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length - 4; i++) {
            password.append(allAllowed[random.nextInt(allAllowed.length)]);
        }

        //Ensure password policy is met by inserting required random chars in random positions
        password.insert(random.nextInt(password.length()), lowercase[random.nextInt(lowercase.length)]);
        password.insert(random.nextInt(password.length()), uppercase[random.nextInt(uppercase.length)]);
        password.insert(random.nextInt(password.length()), numbers[random.nextInt(numbers.length)]);
        password.insert(random.nextInt(password.length()), symbols[random.nextInt(symbols.length)]);

        return password.toString();
    }

    public Page<AccountDto> findPage(Pageable pageable) {
        return new PageImpl<>(
                repository.findAll(pageable).stream()
                        .map(user -> {
                            AccountDto accountDto = new AccountDto();
                            accountDto.setFullName(user.getFullName());
                            accountDto.setQualifier(user.getEmail());
                            accountDto.setMustUpdatePassword(!user.getPasswordExpireDate().isAfter(LocalDate.now()));
                            accountDto.setRoles(user.getRoles().stream().map(role -> ObjectRef.toObjectRef(role.getId(), role.getName())).collect(Collectors.toSet()));
                            return accountDto;
                        })
                        .collect(Collectors.toList()),
                pageable,
                repository.count());
    }

    public Page<AccountDto> search(String email, RoleConstant roleDto, Pageable pageable) {
        if (email == null) {
            email = "";
        }
        Set<Role> roles = new HashSet<>();
        if (roleDto == null) {
            roles = roleResolver.resolveRefs(Arrays.stream(RoleConstant.values()).map(r -> new ObjectRef(r.getId())).collect(Collectors.toList()));
        } else {
            roles.add(roleResolver.resolve(roleDto));
        }

        email = String.format("%%%s%%", email);


        return new PageImpl<>(
                repository.findDistinctByEmailLikeAndRolesIn(email, roles, pageable)
                        .stream()
                        .map(user ->
                        {
                            AccountDto accountDto = new AccountDto();
                            accountDto.setFullName(user.getFullName());
                            accountDto.setQualifier(user.getEmail());
                            accountDto.setMustUpdatePassword(!user.getPasswordExpireDate().isAfter(LocalDate.now()));
                            accountDto.setRoles(user.getRoles().stream().map(role -> ObjectRef.toObjectRef(role.getId(), role.getName())).collect(Collectors.toSet()));
                            accountDto.setLock(user.isLock());
                            return accountDto;
                        })
                        .collect(Collectors.toList()),
                pageable,
                repository.count());
    }

    public boolean isUniqueEmail(String email) {
        return !repository.findByEmail(email).isPresent();
    }

    public void resetPassword(String email) {
        Account account = resolver.resolveUser(ObjectRef.toObjectRef(email));
        String password = generatePassword();
        account.setPassword(bCryptPasswordEncoder.encode(password));
        account.setPasswordExpireDate(LocalDate.now());

        repository.save(account);
        sendEmail(email, password);
    }

    public AccountDto updateRoles(AccountDto accountDto) {
        if (StringUtils.isBlank(accountDto.getUsername()) || accountDto.getRoles().isEmpty()) {
            throw new AttendanceMonitorException();
        }

        Account account = resolver.resolveUser(ObjectRef.toObjectRef(accountDto.getUsername()));
        Set<Role> newRoles = roleResolver.resolveRefs(accountDto.getRoles());
        account.setRoles(newRoles);
        return converter.convertToDto(repository.save(account));
    }

    public AccountDto updateEmail(String oldEmail, String newEmail) {
        if (StringUtils.isBlank(newEmail) || StringUtils.isBlank(newEmail)) {
            throw new AttendanceMonitorException();
        }
        if (!isUniqueEmail(newEmail)) {
            throw new EmailBusyException();
        }
        Account account = resolver.resolveUser(ObjectRef.toObjectRef(oldEmail));
        account.setEmail(newEmail);
        return converter.convertToDto(repository.save(account));
    }

    private void sendEmail(String email, String password) {
        emailService.sendEmail(email, "Добро пожаловать в web-приложение для учёта и анализа посещаемости занятий!",
                String.format("Для входа в систему используйте: " +
                                "\n\te-mail: %s " +
                                "\n\tпароль: %s" +
                                "\n\nПожалуйста, поменяйте пароль при первом использовании!",
                        email, password));
    }

    public void delete(String email) {
        repository.delete(resolver.resolveUser(ObjectRef.toObjectRef(email)));
    }

    public void changeLockValue(String email) {
        Account account = repository.findByEmail(email).orElseThrow(NotFoundException::new);
        account.setLock(!account.isLock());
        repository.save(account);
    }

}