package com.bntu.master.attendance.monitor.impl.entity;

import com.bntu.master.attendance.monitor.api.model.attendance.AttendanceValue;
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
@Entity
public class Attendance implements Base {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attendance_generator")
    private Long id;
    @ManyToOne
    @JoinColumn
    private Lesson lesson;
    @ManyToOne
    @JoinColumn
    private Person student;
    private AttendanceValue value;
    @ManyToOne
    @JoinColumn
    private ProfessorPosition professor;
    private LocalDateTime dateTime;
    private boolean isGoodReason = false;

    public Attendance(Long id, Lesson lesson, Person student, AttendanceValue value, ProfessorPosition professor, LocalDateTime dateTime, boolean isGoodReason) {
        this.id = id;
        this.lesson = lesson;
        this.student = student;
        this.value = value;
        this.professor = professor;
        this.dateTime = dateTime;
        this.isGoodReason = !AttendanceValue.COME.equals(value) && isGoodReason;
    }

    public void setValue(AttendanceValue value) {
        this.value = value;
        if (AttendanceValue.COME.equals(value)) {
            setGoodReason(false);
        }
    }

    public void setGoodReason(boolean goodReason) {
        if (AttendanceValue.COME.equals(value)) {
            isGoodReason = false;
        } else {
            isGoodReason = goodReason;
        }
    }

    public boolean isGoodReason() {
        return isGoodReason;
    }
}
