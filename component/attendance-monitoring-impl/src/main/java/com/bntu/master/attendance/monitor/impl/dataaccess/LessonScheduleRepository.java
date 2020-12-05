package com.bntu.master.attendance.monitor.impl.dataaccess;

import com.bntu.master.attendance.monitor.impl.entity.LessonSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonScheduleRepository extends JpaRepository<LessonSchedule, Long> {

}
