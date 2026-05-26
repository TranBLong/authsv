package com.example.auths.repository;

import com.example.auths.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID>, JpaSpecificationExecutor<Role> {

    List<Role> findByIsActiveAndDeletedAtIsNull(Boolean isActive);

    List<Role> findByIsSystemTrueAndDeletedAtIsNull();

    List<Role> findByDeletedAtIsNull();

    List<Role> findByNameContainingIgnoreCaseAndCodeContainingIgnoreCaseAndDeletedAtIsNull(String name, String code);

    Optional<Role> findByIdAndDeletedAtIsNull(UUID id);

    Page<Role> findByDeletedAtIsNull(Pageable pageable);

}
