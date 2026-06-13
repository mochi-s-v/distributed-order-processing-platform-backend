package com.vicky.product_service.Repository;


import com.vicky.product_service.Entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>{
    boolean existsByName(String name);
    CategoryEntity findByName(String name);
}
