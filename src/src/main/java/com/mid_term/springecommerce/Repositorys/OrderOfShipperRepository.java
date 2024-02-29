package com.mid_term.springecommerce.Repositorys;

import com.mid_term.springecommerce.Models.Entity.OrderOfShipper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderOfShipperRepository extends JpaRepository<OrderOfShipper, Long> {

    @Query("SELECT o FROM OrderOfShipper o WHERE o.shipperId = :shipperId")
    List<OrderOfShipper> getOrderByShipperId(@Param("shipperId") Long shipperId);

    @Transactional
    @Modifying
    @Query("UPDATE OrderOfShipper o SET o.status = :status WHERE o.id = :id")
    int updateStatusOrder(@Param("id") Long id, @Param("status") int status);
}
