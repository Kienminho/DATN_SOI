package com.mid_term.springecommerce.Repositorys;

import com.mid_term.springecommerce.Models.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
