package com.bntu.master.attendance.monitor.impl.dataaccess;

import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.ProfessorPosition;
import com.bntu.master.attendance.monitor.impl.entity.util.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProfessorPositionRepository extends JpaRepository<ProfessorPosition, Long> {

    ProfessorPosition findFirstByProfessor(Person prof);

    @Query(nativeQuery = true,
            value = "select p.id, p.email, p.first_name firstName, p.last_name lastName, p.patronymic, p.phone, " +
                    "pp.position_id positionId, pos.name positionName " +
                    "from person p " +
                    "join professor_position pp on pp.professor_email = p.email " +
                    "join position pos on pos.id = pp.position_id ",
            countQuery = "select count(*) " +
                    "from person p " +
                    "left join parent_contact pc on p.email = pc.student_email " +
                    "left join students_by_group sg on sg.student_email = p.email")
    Page<Professor> findPage(Pageable pageable);

}
