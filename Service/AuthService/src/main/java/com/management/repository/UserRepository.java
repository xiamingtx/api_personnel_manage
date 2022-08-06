package com.management.repository;

import com.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 夏明
 * @version 1.0
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUsername(String name);
}
