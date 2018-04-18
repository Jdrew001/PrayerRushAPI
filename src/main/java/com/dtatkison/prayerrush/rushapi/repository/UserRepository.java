package com.dtatkison.prayerrush.rushapi.repository;

import com.dtatkison.prayerrush.rushapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.firstname = :firstname, u.lastname = :lastname, u.username = :username, u.email = :email where u.id = :user_id")
    int updateUser(@Param("firstname") String firstname, @Param("lastname") String lastname, @Param("username") String username, @Param("email") String email, @Param("user_id") Integer userId);
}
