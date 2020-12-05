package com.bntu.master.attendance.monitor.impl.dataaccess;

import com.bntu.master.attendance.monitor.impl.entity.Account;
import com.bntu.master.attendance.monitor.impl.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findByEmail(String email);

    Optional<Account> findFirstByEmail(String email);

    @Query(value = "select email from person_roles pr where pr.role_id In (?1)", nativeQuery = true)
    Set<String> findUserEmailByRolesIn(Set<Long> roles);

    //    @Query(value = "select * from person_roles pr " +
//            "join account a on a.email = pr.email " +
//            "join role r on r.id = pr.role_id " +
//            "where pr.email like ?1 and  pr.role_id In (?2) " +
//            "group by a.email", nativeQuery = true,
//            countQuery = "select count() from person_roles pr " +
//            "join account a on a.email = pr.email " +
//            "join role r on r.id = pr.role_id " +
//            "where pr.email like ?1 and  pr.role_id In (?2) " +
//            "group by pr.email")
    Page<Account> findByEmailLikeAndRolesIn(String emailSearch, Set<Role> roles, Pageable pageable);

}
