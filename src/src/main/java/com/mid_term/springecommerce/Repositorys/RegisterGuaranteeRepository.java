package com.mid_term.springecommerce.Repositorys;

import com.mid_term.springecommerce.Models.Entity.RegisterGuarantee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegisterGuaranteeRepository extends JpaRepository<RegisterGuarantee, Long> {

    @Query("SELECT rg FROM RegisterGuarantee rg WHERE rg.idUser = :idUser")
    List<RegisterGuarantee> getRegisterGuaranteeByIdUser(@Param("idUser") Long idUser);
}
