package com.bntu.master.attendance.monitor.impl.dataaccess;

import com.bntu.master.attendance.monitor.impl.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByEmail(String email);

    List<Person> findAllByIdInOrEmailIn(Set<Long> ids, Set<String> emails);

    Page<Person> findAllByIdInOrEmailIn(Set<Long> ids, Set<String> emails, Pageable pageable);


}
