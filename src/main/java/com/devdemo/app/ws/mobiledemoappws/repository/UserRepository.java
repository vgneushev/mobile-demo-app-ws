package com.devdemo.app.ws.mobiledemoappws.repository;

import com.devdemo.app.ws.mobiledemoappws.io.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
}
