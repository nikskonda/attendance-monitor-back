package com.bntu.master.attendance.monitor.impl.dataaccess;

import com.bntu.master.attendance.monitor.impl.entity.Account;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.ParentContact;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.util.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParentContactRepository extends JpaRepository<ParentContact, Long> {

    ParentContact findFirstByStudent(Person student);

    List<ParentContact> findAllByParent(Account parent);

    @Query(nativeQuery = true,
            value = "select p.id, p.email, p.first_name firstName, p.last_name lastName, p.patronymic, pc.parent_email parentEmail, sg.group_volume_id groupVolumeId " +
                    "from person p " +
                    "join students_by_group sg on sg.student_email = p.email " +
                    "left join parent_contact pc on p.email = pc.student_email " +
                    "where sg.group_id = ?1",
            countQuery = "select count(*) " +
                    "from person p " +
                    "join students_by_group sg on sg.student_email = p.email " +
                    "left join parent_contact pc on p.email = pc.student_email " +
                    "where sg.group_id = ?1")
    Page<Student> findStudentsByGroupPage(Group group, Pageable pageable);

    @Query(value = "select p.id, p.email, p.first_name firstName, p.last_name lastName, p.patronymic, pc.parent_email parentEmail, sg.group_volume_id groupVolumeId " +
            "from person p " +
            "join students_by_group sg on sg.student_email = p.email " +
            "left join parent_contact pc on p.email = pc.student_email " +
            "where sg.group_id = ?1", nativeQuery = true)
    List<Student> findStudentsByGroup(Group group);

}
