package com.bntu.master.attendance.monitor.impl.dataaccess;

import com.bntu.master.attendance.monitor.impl.entity.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecialityRepository extends JpaRepository<Speciality, Long> {

    Optional<Speciality> findByName(String name);

}
