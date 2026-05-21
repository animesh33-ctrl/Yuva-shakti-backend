package com.repository;

import com.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByFullname(String username);

    Optional<UserEntity> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.failedAttempts=u.failedAttempts+1 WHERE u.fullname = :fullname")
    void incrementFailedAttempts(@Param("fullname") String fullname);

    Optional<UserEntity> findByFullnameOrEmail(String username, String email);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.lockedUntil= :lockedUntil WHERE u.fullname = :fullname")
    void lockAccount(@Param("fullname") String username,@Param("lockedUntil") LocalDateTime lockedUntil);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.failedAttempts = 0 ,u.lockedUntil=null WHERE u.fullname = :fullname")
    void resetFailedAttempts(@Param("fullname") String username);
}
