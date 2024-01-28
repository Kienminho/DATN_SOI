package com.mid_term.springecommerce.Models.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    private int totalItems;

    private int totalPrice;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
    private List<CartItem> cartItems;

    private boolean isOrder = false;

    public Cart(int totalItems, int totalPrice, User user, boolean isOrder) {
        this.totalItems = totalItems;
        this.totalPrice = totalPrice;
        this.user = user;
        this.isOrder = isOrder;
    }
}
