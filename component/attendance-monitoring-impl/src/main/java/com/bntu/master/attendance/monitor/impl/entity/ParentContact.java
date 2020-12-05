package com.bntu.master.attendance.monitor.impl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ParentContact implements Base {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "parent_contact_generator")
    private Long id;

    @OneToOne
    @JoinColumn(name = "parentEmail", referencedColumnName = "email")
    private Account parent;

    @OneToOne
    @JoinColumn(name = "studentEmail", referencedColumnName = "email")
    private Person student;

    private boolean isNotify;

    public ParentContact(Account parent, Person student) {
        this.parent = parent;
        this.student = student;
        this.isNotify = true;
    }
}
