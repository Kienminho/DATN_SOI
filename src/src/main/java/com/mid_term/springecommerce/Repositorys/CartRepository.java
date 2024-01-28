package com.mid_term.springecommerce.Repositorys;

import com.mid_term.springecommerce.Models.Entity.Cart;
import com.mid_term.springecommerce.Models.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT C FROM Cart AS C WHERE C.user = :user AND C.isOrder = false ")
    public Cart getCartByUser(@Param("user") User user);

    @Modifying
    @Query("UPDATE Cart c SET c.totalPrice = :totalPrice, c.totalItems = :totalItems WHERE c.id = :id")
    @Transactional
    void updateCart(@Param("id") Long id, @Param("totalPrice") int totalPrice, @Param("totalItems") int totalItems);

    @Modifying
    @Query("UPDATE Cart c SET c.totalItems = 0, c.totalPrice = 0 WHERE c.id = :id")
    @Transactional
    void updateStatusCart(@Param("id") Long id);

}
