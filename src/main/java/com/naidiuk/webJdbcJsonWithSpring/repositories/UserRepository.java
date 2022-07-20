package com.naidiuk.webJdbcJsonWithSpring.repositories;

import com.naidiuk.webJdbcJsonWithSpring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT user FROM User user WHERE user.id=(SELECT MAX(user.id) FROM User user)")
    User findWithMaxId();
}
