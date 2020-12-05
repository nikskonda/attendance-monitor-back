package com.bntu.master.attendance.monitor.impl.dataaccess;

import com.bntu.master.attendance.monitor.impl.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {

}
