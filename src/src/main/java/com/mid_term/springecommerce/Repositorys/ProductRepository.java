package com.mid_term.springecommerce.Repositorys;

import com.mid_term.springecommerce.Models.Entity.Category;
import com.mid_term.springecommerce.Models.Entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.is_deleted = false")
    List<Product> getAllProduct();
    @Query("SELECT p FROM Product p WHERE p.currentQuantity > 0 and p.is_deleted = false")
    List<Product> getAvailableProduct();
    @Query("SELECT p FROM Product p WHERE p.category = :category and p.is_deleted = false")
    List<Product> getProductsByCategory(@Param("category")Category category);
    @Query("SELECT p FROM Product p WHERE p.id = :id and p.is_deleted = false")
    Product getDetailProduct(@Param("id") Long id);

    @Query("SELECT p FROM Product p " +
            "WHERE (LOWER(p.name) LIKE LOWER(concat('%', :keyword, '%')) " +
            "OR LOWER(p.category.name) LIKE LOWER(concat('%', :keyword, '%')) " +
            "OR CAST(p.salePrice AS string) LIKE concat('%', :keyword, '%')) " +
            "OR CAST(p.id AS string) LIKE concat('%', :keyword, '%') " +
            "OR CAST(p.importPrice AS string) LIKE concat('%', :keyword, '%') " +
            "AND p.currentQuantity > 0 " +
            "AND p.is_deleted = false")
    List<Product> searchProducts(@Param("keyword") String keyword);

    @Modifying
    @Query("UPDATE Product p SET p.is_deleted = true  WHERE p.id = :id")
    @Transactional
    void deleteProduct(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Product p SET p.name = :name, p.salePrice = :price, p.description = :description  WHERE p.id = :id")
    @Transactional
    void updateProduct(@Param("id") Long id, @Param("name") String name, @Param("price") int price,@Param("description") String description);
}
