package com.devdemo.app.ws.repository;

import com.devdemo.app.ws.io.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long>, CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(@NonNull final String email);

    UserEntity findByUserId(@NonNull final String userPublicId);

    UserEntity findUserByEmailVerificationToken(@NonNull final String token);

    @Query(value = "select * from Users u where u.EMAIL_VERIFIED = true",
            countQuery = "select count(*) from Users u where u.EMAIL_VERIFIED = true",
            nativeQuery = true)
    Page<UserEntity> findAllUsersWithConfirmedEmail(@NonNull final Pageable pageableRequest);
    @Query(value = "select * from Users u where u.first_name = ?1", nativeQuery = true)
    List<UserEntity> findUserByFirstName(@NonNull final String firstName);
}
