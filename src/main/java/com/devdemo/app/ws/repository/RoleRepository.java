package com.devdemo.app.ws.repository;

import com.devdemo.app.ws.io.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
    RoleEntity findByName(@NonNull final String name);
}
