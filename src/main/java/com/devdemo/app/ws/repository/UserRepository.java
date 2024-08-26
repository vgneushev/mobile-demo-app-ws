package com.devdemo.app.ws.repository;

import com.devdemo.app.ws.io.entity.UserEntity;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(@NonNull final String email);

    UserEntity findByUserId(@NonNull final String userPublicId);

}