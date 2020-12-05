package com.bntu.master.attendance.monitor.impl.dataaccess;

import com.bntu.master.attendance.monitor.impl.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByKey(String key);

}
