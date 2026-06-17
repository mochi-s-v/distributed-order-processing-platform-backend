package com.vicky.product_service.Repository;

import com.vicky.product_service.Entity.ProductEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE ProductEntity p SET p.active = false WHERE p.categoryEntity.id = :categoryId")
    void softDeleteAllByCategoryId(@Param("categoryId") long categoryId);

    List<ProductEntity> getByCategoryEntity_Id(long id);

    boolean existsBySku(String sku);

    @Modifying
    @Transactional
    @Query("UPDATE ProductEntity p SET p.quantity = :qty, p.updatedAt = CURRENT_TIMESTAMP WHERE p.sku = :sku")
    void restockBySku(@Param("sku") String sku, @Param("qty") int qty);

    Optional<ProductEntity> findBySku(String sku);
}
