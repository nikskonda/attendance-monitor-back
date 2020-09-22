package com.bntu.master.attendance.monitor.impl.dataaccess;

import com.bntu.master.attendance.monitor.impl.entity.LessonSchedule;
import com.bntu.master.attendance.monitor.impl.entity.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonScheduleRepository extends JpaRepository<LessonSchedule, Long> {

}
