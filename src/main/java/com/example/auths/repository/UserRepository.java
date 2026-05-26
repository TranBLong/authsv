package com.example.auths.repository;

import com.example.auths.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> findByDeletedAtIsNull();

    Optional<User> findByIdAndDeletedAtIsNull(UUID id);

    Optional<User> findByUsernameAndDeletedAtIsNull(String username);

    List<User> findByIsActiveAndDeletedAtIsNull(Boolean isActive);

    Page<User> findByDeletedAtIsNull(Pageable pageable);
}
