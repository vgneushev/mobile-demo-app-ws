package com.devdemo.app.ws.repository;

import com.devdemo.app.ws.io.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Query(value = "select * from Users u where u.last_name = :lastName", nativeQuery = true)
    List<UserEntity> findUserByLastName(@NonNull @Param("lastName") final String lastName);

    @Query(value = "select * from Users u where u.last_name like %:keyword% or u.last_name like %:keyword%",
            nativeQuery = true)
    List<UserEntity> findUserByKeyword(@NonNull @Param("keyword") final String keyword);

    @Query(value = "select u.first_name, u.last_name from Users u " +
            "where u.last_name like %:keyword% or u.last_name like %:keyword%", nativeQuery = true)
    List<Object[]> getUserFirstLastNamesByKeyword(@NonNull @Param("keyword") final String keyword);

    @Transactional
    @Modifying
    @Query(value = "update Users u set u.email_verified = :status " +
            "where u.user_id = :id", nativeQuery = true)
    void updateUserEmailVerificationStatus(
            @NonNull @Param("status") final boolean status,
            @NonNull @Param("id") final String userId);

    @Query(value = "select user from Users user where user.userId =:userId", nativeQuery = true)
    UserEntity findUserEntityByUserId(@Param("userId") String userId);
}
