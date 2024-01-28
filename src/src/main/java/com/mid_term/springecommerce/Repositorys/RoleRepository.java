package com.mid_term.springecommerce.Repositorys;

import com.mid_term.springecommerce.Models.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT u FROM Role u WHERE u.id = :id")
    Role getRoleById(@Param("id") Long id);
}
