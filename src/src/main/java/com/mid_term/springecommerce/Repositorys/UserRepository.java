package com.mid_term.springecommerce.Repositorys;

import com.mid_term.springecommerce.Models.Entity.User;
import com.mid_term.springecommerce.Models.ResponseModel.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :name")
    User getUserByName(@Param("name") String name);

    @Query("SELECT NEW com.mid_term.springecommerce.Models.ResponseModel.UserResponse(" +
            "u.username,u.fullName,u.email,u.phone,u.address,u.gender) " +
            "FROM User u WHERE u.username = :username")
    UserResponse getInfoMine(@Param("username")String username);
}
