package com.devdemo.app.ws.repository;

import com.devdemo.app.ws.io.entity.PasswordResetTokenEntity;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long> {
    PasswordResetTokenEntity findByToken(@NonNull final String token);
}
