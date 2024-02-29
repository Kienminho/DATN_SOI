package com.mid_term.springecommerce.Repositorys;

import com.mid_term.springecommerce.Models.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT o FROM OrderItem o WHERE o.order.id =:id")
    List<OrderItem> getDetailByOrderId(@Param("id") Long id);
}
