package com.mid_term.springecommerce.Models.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;

    private String password;

    @Column(columnDefinition = "nvarchar(80)")
    private String fullName;

    private String email;

    private String phone;

    @Column(columnDefinition = "nvarchar(100)")
    private String address;

    @Column(columnDefinition = "nvarchar(10)")
    private String gender;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    private Collection<Role> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;

    public User(String username, String encodedPassword, String email, Role role) {
        this.username = username;
        this.password = encodedPassword;
        this.email = email;

        this.roles = new HashSet<>(Collections.singletonList(role));
    }
}
