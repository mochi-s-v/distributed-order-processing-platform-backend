package com.vicky.cart_service.Repository;


import com.vicky.cart_service.Entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long>{
    Optional<CartEntity> findByUsername(String username);
}
