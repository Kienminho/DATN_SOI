package com.mid_term.springecommerce.Repositorys;

import com.mid_term.springecommerce.Models.Entity.Category;
import com.mid_term.springecommerce.Models.ResponseModel.CategoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.name = :categoryName")
    Category findByName(@Param("categoryName") String categoryName);

    @Query("SELECT c FROM Category c WHERE c.is_deleted = false")
    List<Category> getAllCategory();

    @Query("SELECT NEW com.mid_term.springecommerce.Models.ResponseModel.CategoryResponse(c.id, c.name, c.description, COUNT(p.id), c.createdDate) " +
            "FROM Category c " +
            "LEFT JOIN Product p ON p.category.id = c.id " +
            "WHERE c.is_deleted = false " +
            "GROUP BY c.id, c.name, c.description, c.createdDate")
    List<CategoryResponse> getCategoryResponses();

}
