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
import javax.persistence.Table;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students_by_group")
public class StudentGroup implements Base {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "stud_gr_generator")
    private Long id;

    @OneToOne
    private Group group;

    @OneToOne
    @JoinColumn(name = "studentEmail", referencedColumnName = "email")
    private Person student;

    private Long groupVolumeId;

    public StudentGroup(Person student, Group group) {
        this.group = group;
        this.student = student;
    }

    public StudentGroup(Group group, Person student, Long groupVolumeId) {
        this.group = group;
        this.student = student;
        this.groupVolumeId = groupVolumeId;
    }
}
