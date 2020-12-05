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
public class ProfessorPosition implements Base {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "prof_ad_generator")
    private Long id;

    @OneToOne
    private Position position;

    @OneToOne
    @JoinColumn(name = "professorEmail", referencedColumnName = "email")
    private Person professor;

    public ProfessorPosition(Position position, Person professor) {
        this.position = position;
        this.professor = professor;
    }
}
