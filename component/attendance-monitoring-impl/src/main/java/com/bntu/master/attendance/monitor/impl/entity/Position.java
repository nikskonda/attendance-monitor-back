package com.bntu.master.attendance.monitor.impl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Position implements Base {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "position_generator")
    private Long id;

    @Column(unique = true)
    private String name;

}
