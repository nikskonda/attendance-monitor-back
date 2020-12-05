package com.bntu.master.attendance.monitor.impl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Person implements Base {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "person_generator")
    private Long id;

    private String firstName;
    private String lastName;
    private String patronymic;

    @NaturalId(mutable = true)
    private String email;

    private String phone;
}
