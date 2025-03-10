package com.devdemo.app.ws.repository;

import com.devdemo.app.ws.io.entity.AuthorityEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Long> {
    AuthorityEntity findByName(@NonNull final String name);
}
