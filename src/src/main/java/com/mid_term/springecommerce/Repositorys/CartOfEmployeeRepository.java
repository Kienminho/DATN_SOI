package com.mid_term.springecommerce.Repositorys;

import com.mid_term.springecommerce.Models.Entity.CartOfEmployee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartOfEmployeeRepository extends JpaRepository<CartOfEmployee, Long> {
    @Query("SELECT c FROM CartOfEmployee c WHERE c.Id =:id")
    CartOfEmployee getCartItem(@Param("id")Long id);
    @Query("SELECT c FROM CartOfEmployee c WHERE c.idSalePeople = :id")
    List<CartOfEmployee> getCartsByIdSalePeople(Long id);
    @Modifying
    @Transactional
    @Query("DELETE FROM CartOfEmployee c WHERE c.idSalePeople =:id")
    void deleteCartById(@Param("id") Long id);
}
