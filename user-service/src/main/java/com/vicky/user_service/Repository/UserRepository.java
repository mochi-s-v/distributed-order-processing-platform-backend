package com.vicky.user_service.Repository;

import com.vicky.user_service.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
    Optional<UserEntity> findByUsername(String username);
    boolean existsByRole(String roleAdmin);
    boolean existsByEmail(String mail);
}
