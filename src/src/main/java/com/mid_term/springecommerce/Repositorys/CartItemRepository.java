package com.mid_term.springecommerce.Repositorys;

import com.mid_term.springecommerce.Models.Entity.Cart;
import com.mid_term.springecommerce.Models.Entity.CartItem;
import com.mid_term.springecommerce.Models.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT item FROM CartItem  AS item WHERE item.product = :product AND item.cart = :cart")
    CartItem getCartItem(@Param("product") Product product, @Param("cart") Cart cart);

    @Query("SELECT item FROM CartItem  AS item WHERE item.cart = :cart")
    List<CartItem> getAll(@Param("cart") Cart cart);

    @Modifying
    @Query("UPDATE CartItem c SET c.quantity = :quantity, c.totalPrice = :totalPrice WHERE c.product = :product AND c.cart = :cart")
    @Transactional
    void updateCartItem(@Param("product") Product product, @Param("quantity") int quantity,
                        @Param("totalPrice") int totalPrice, @Param("cart") Cart cart);

    @Query("SELECT SUM(c.quantity) as totalQuantity, SUM(c.totalPrice) as totalPrice FROM CartItem c WHERE c.cart = :cart")
    Map<String, Long> calculateTotalQuantityAndPrice(@Param("cart") Cart cart);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.product = :product")
    @Transactional
    void deleteCartItemByProduct(@Param("product") Product product);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.cart = :cart")
    @Transactional
    void deleteCartItemByCart(@Param("cart") Cart cart);
}
