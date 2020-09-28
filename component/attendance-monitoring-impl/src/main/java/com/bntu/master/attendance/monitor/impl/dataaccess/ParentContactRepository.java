package com.bntu.master.attendance.monitor.impl.dataaccess;

import com.bntu.master.attendance.monitor.impl.entity.ParentContact;
import com.bntu.master.attendance.monitor.impl.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentContactRepository extends JpaRepository<ParentContact, Long> {

}
