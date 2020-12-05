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
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Mark implements Base {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "mark_generator")
    private Long id;

    @ManyToOne
    @JoinColumn
    private Lesson lesson;

    @ManyToOne
    @JoinColumn
    private Person student;

    @ManyToOne
    @JoinColumn
    private Person professor;

    private LocalDateTime dateTime;
    private Short mark;

}
