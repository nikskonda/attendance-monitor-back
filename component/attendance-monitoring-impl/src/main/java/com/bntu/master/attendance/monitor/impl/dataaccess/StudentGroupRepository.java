package com.bntu.master.attendance.monitor.impl.dataaccess;

import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.StudentGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentGroupRepository extends JpaRepository<StudentGroup, Long> {

    List<StudentGroup> findAllByGroupAndGroupVolumeId(Group group, Long groupVolume);

    List<StudentGroup> findAllByGroup(Group group);

    Page<StudentGroup> findAllByGroup(Group group, Pageable pageable);

    StudentGroup findFirstByStudentAndGroup(Person student, Group group);

    StudentGroup findFirstByStudent(Person student);


}
