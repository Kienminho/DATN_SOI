package com.mid_term.springecommerce.Repositorys;

import com.mid_term.springecommerce.Models.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.email LIKE %:keyword% OR o.name LIKE %:keyword% order by o.orderDateTime desc")
    List<Order> getAll(@Param("keyword") String keyword);

    @Query("SELECT o FROM Order o WHERE o.id =:id")
    Order getDetailById(@Param("id") Long id);
}
