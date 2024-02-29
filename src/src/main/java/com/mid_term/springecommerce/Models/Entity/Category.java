package com.mid_term.springecommerce.Models.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    @Column(columnDefinition = "nvarchar(50)")
    private String name;
    @Column(columnDefinition = "nvarchar(1000)")
    private String description;
    private boolean is_activated = true;
    private boolean is_deleted = false;
    @Column(columnDefinition = "datetime")
    private Date createdDate = new Date();
    @Column(columnDefinition = "datetime")
    private Date updatedDate;
}
