package com.bntu.master.attendance.monitor.impl.dataaccess;

import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.StudentGroup;
import com.bntu.master.attendance.monitor.impl.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

public interface StudentGroupRepository extends JpaRepository<StudentGroup, Long> {

    Set<StudentGroup> findAllByGroup(Group group);

    StudentGroup findFirstByStudentAndGroup(Person student, Group group);

    StudentGroup findFirstByStudent(Person student);


}
