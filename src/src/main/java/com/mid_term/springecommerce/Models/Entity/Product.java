package com.mid_term.springecommerce.Models.Entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "imageUrl" }))
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(columnDefinition = "nvarchar(255)")
    private String name;

    @Column(columnDefinition = "nvarchar(1000)")
    private String description;

    @Column(columnDefinition = "int default 0")
    private int importPrice;

    @Column(columnDefinition = "int default 0")
    private int salePrice;

    @Column(columnDefinition = "int default 0")
    private int currentQuantity;

    @Column(columnDefinition = "int default 0")
    private int saleNumber;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    private boolean is_activated;

    private boolean is_deleted;

    @Nullable
    @Column(columnDefinition = "datetime")
    private Date createdDate;
    @Nullable
    @Column(columnDefinition = "datetime")
    private Date updatedDate;

    public Product(String name, int salePrice, Category c, String description) {
        this.name = name;
        this.salePrice = salePrice;
        this.category = c;
        this.description = description;
    }

    public Product(String name, String description, String imageUrl, int importPrice, int salePrice, int currentQuantity, Category category) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.importPrice = importPrice;
        this.salePrice = salePrice;
        this.currentQuantity = currentQuantity;
        this.category = category;
        is_activated = true;
        is_deleted = false;
        createdDate = new Date();
    }

    public Product(String name, String description, String imageUrl, int importPrice, int salePrice, int currentQuantity, Category category, Date createdDate) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.importPrice = importPrice;
        this.salePrice = salePrice;
        this.currentQuantity = currentQuantity;
        this.category = category;
        is_activated = true;
        is_deleted = false;
        this.createdDate = createdDate;
    }
}
