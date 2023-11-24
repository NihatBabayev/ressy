package com.example.ressy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ressy.entity.User;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.email = :userEmail")
    void updatePasswordByUserEmail(String userEmail, String newPassword);
}
