package com.mid_term.springecommerce.Repositorys;

import com.mid_term.springecommerce.Models.Entity.Guarantee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GuaranteeRepository extends JpaRepository<Guarantee, Long> {

    @Query("SELECT g FROM Guarantee g WHERE g.userName = :userName")
    List<Guarantee> getGuaranteeByUser(@Param("userName") String userName);

    @Query("SELECT g FROM Guarantee g WHERE g.codeGuarantee = :codeGuarantee")
    Guarantee getGuaranteeByCode(@Param("codeGuarantee") String codeGuarantee);
}
