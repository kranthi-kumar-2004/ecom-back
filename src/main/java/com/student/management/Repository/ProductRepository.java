package com.student.management.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.student.management.enitity.Products;

import java.util.List;

public interface ProductRepository extends JpaRepository<Products, Long> {
    List<Products> findByName(String name);
    List<Products> findByNameContainingIgnoreCase(String name);
    List<Products> findByCategoryContainingIgnoreCase(String category);
}
