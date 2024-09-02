package com.devdemo.app.ws.repository;

import com.devdemo.app.ws.io.entity.AddressEntity;
import com.devdemo.app.ws.io.entity.UserEntity;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    Collection<AddressEntity> findAllByUserDetails(@NonNull final UserEntity userEntity);
}
