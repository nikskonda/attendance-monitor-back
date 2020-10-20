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
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.Set;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@Entity
@Table(name = "user_data")
public class User {

    @Id
    private String email;

    private String password;

    private LocalDate passwordExpireDate;

    @Formula("(select concat(p.last_name, ' ', p.first_name, ' ', p.patronymic) from person p where p.email = email)")
    private String fullName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "person_roles",
            joinColumns = @JoinColumn(name = "email"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, LocalDate passwordExpireDate) {
        this.email = email;
        this.password = password;
        this.passwordExpireDate = passwordExpireDate;
    }

    public User(String email, String password, LocalDate passwordExpireDate, Set<Role> roles) {
        this.email = email;
        this.password = password;
        this.passwordExpireDate = passwordExpireDate;
        this.roles = roles;
    }


    public String getFullName() {
        return StringUtils.normalizeSpace(fullName);
    }
}
