package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.exception.AttendanceMonitorException;
import com.bntu.master.attendance.monitor.api.exception.EmailBusyException;
import com.bntu.master.attendance.monitor.api.exception.NotFoundException;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.api.model.UserDto;
import com.bntu.master.attendance.monitor.impl.converter.UserConverter;
import com.bntu.master.attendance.monitor.impl.dataaccess.UserRepository;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.Role;
import com.bntu.master.attendance.monitor.impl.entity.User;
import com.bntu.master.attendance.monitor.impl.resolver.PersonResolver;
import com.bntu.master.attendance.monitor.impl.resolver.RoleResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PersonResolver resolver;

    @Autowired
    private RoleResolver roleResolver;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserConverter converter;



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return loadUserByEmail(email);
    }

    public UserDto updatePassword(String email, String oldPassword, String newPassword){
        User user = repository.findByEmail(email).orElseThrow(NotFoundException::new);
        if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException();
        }
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        user.setPasswordExpireDate(LocalDate.now().plusMonths(3));
        repository.save(user);
        return loadUserByEmail(email);
    }

    public UserDto loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email).orElseThrow(NotFoundException::new);
        return converter.convertToDto(user);
    }

    public UserDto create(String email, RoleConstant role) {
        return create(email, Collections.singleton(ObjectRef.toObjectRef(role.getId(), role.getRole())));
    }

    public UserDto create(String email, Set<ObjectRef> roles) {
        User user = resolver.resolveUser(ObjectRef.toObjectRef(email));
        if (user != null) {
            throw new RuntimeException("User already exist");
        }

        Set<Role> newRoles = roleResolver.resolveRefs(roles);

        String password = generatePassword();
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(bCryptPasswordEncoder.encode(password));
        newUser.setPasswordExpireDate(LocalDate.now());
        newUser.setRoles(newRoles);

        newUser = repository.save(newUser);

        sendEmail(email, password);
        return converter.convertToDto(newUser);
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

    public Page<UserDto> findPage(Pageable pageable) {
        return new PageImpl<>(repository.findAll(pageable).stream()
                .map(user -> {
                    UserDto userDto = new UserDto();
                    userDto.setFullName(user.getFullName());
                    userDto.setQualifier(user.getEmail());
                    userDto.setMustUpdatePassword(!user.getPasswordExpireDate().isAfter(LocalDate.now()));
                    userDto.setRoles(user.getRoles().stream().map(role -> ObjectRef.toObjectRef(role.getId(), role.getName())).collect(Collectors.toSet()));
                    return userDto;
                })
                .collect(Collectors.toList()),
                pageable,
                repository.count());
    }

    public boolean isUniqueEmail(String email) {
        return !repository.findByEmail(email).isPresent();
    }

    public void resetPassword(String email) {
        User user = resolver.resolveUser(ObjectRef.toObjectRef(email));
        String password = generatePassword();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setPasswordExpireDate(LocalDate.now());

        repository.save(user);
        sendEmail(email, password);
    }

    public UserDto updateRoles(UserDto userDto) {
        if (StringUtils.isBlank(userDto.getUsername()) || userDto.getRoles().isEmpty()) {
            throw new AttendanceMonitorException();
        }

        User user = resolver.resolveUser(ObjectRef.toObjectRef(userDto.getUsername()));
        Set<Role> newRoles = roleResolver.resolveRefs(userDto.getRoles());
        user.setRoles(newRoles);
        return converter.convertToDto(repository.save(user));
    }

    public UserDto updateEmail(String oldEmail, String newEmail) {
        if (StringUtils.isBlank(newEmail) || StringUtils.isBlank(newEmail)) {
            throw new AttendanceMonitorException();
        }
        if (!isUniqueEmail(newEmail)) {
            throw new EmailBusyException();
        }
        User user = resolver.resolveUser(ObjectRef.toObjectRef(oldEmail));
        user.setEmail(newEmail);
        return converter.convertToDto(repository.save(user));
    }

    private void sendEmail(String email, String password) {
        emailService.sendEmail(email, "Добро пожаловать в систему Attendance-monitor App",
                String.format("Для входа в систему используйте: " +
                                "\n\te-mail: %s " +
                                "\n\tпароль: %s" +
                                "\n\nПожалуйста, поменяйте пароль при первом использовании!",
                        email, password));
    }

    public void delete(String email) {
        repository.delete(resolver.resolveUser(ObjectRef.toObjectRef(email)));
    }

}