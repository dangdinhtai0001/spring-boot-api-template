package com.phoenix.infrastructure.repositories.primary;


import com.phoenix.domain.persistence.primary.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "PermissionRepository")
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
}
