package com.vicky.cart_service.Repository;

import com.vicky.cart_service.Entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItemEntity, Long> {
    Optional<CartItemEntity> findByCartEntity_IdAndProductId(long cartId, long productId);
}
