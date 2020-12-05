package com.bntu.master.attendance.monitor.impl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_group")
public class Group implements Base {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "group_generator")
    private Long id;

    @Column(unique = true)
    private String key;

    @ManyToOne
    @JoinColumn
    private Speciality speciality;

}
