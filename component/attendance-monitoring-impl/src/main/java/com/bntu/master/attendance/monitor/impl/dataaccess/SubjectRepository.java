package com.bntu.master.attendance.monitor.impl.dataaccess;

import com.bntu.master.attendance.monitor.impl.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

}
