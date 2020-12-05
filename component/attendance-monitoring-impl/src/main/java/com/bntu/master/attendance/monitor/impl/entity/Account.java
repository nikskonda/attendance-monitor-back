package com.bntu.master.attendance.monitor.impl.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Set;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account implements Base {

    @Id
    private String email;

    private String password;

    private LocalDate passwordExpireDate;

    @Formula("(select concat(p.last_name, ' ', p.first_name, ' ', p.patronymic) from person p where p.email = email)")
    private String fullName;

    private boolean isLock;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "person_roles",
            joinColumns = @JoinColumn(name = "email"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
        this.isLock = false;
    }

    public Account(String email, String password, LocalDate passwordExpireDate) {
        this.email = email;
        this.password = password;
        this.passwordExpireDate = passwordExpireDate;
        this.isLock = false;
    }

    public Account(String email, String password, LocalDate passwordExpireDate, Set<Role> roles) {
        this.email = email;
        this.password = password;
        this.passwordExpireDate = passwordExpireDate;
        this.roles = roles;
        this.isLock = false;
    }


    public String getFullName() {
        return StringUtils.normalizeSpace(fullName);
    }
}
