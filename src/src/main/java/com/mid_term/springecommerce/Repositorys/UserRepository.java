package com.mid_term.springecommerce.Repositorys;

import com.mid_term.springecommerce.Models.Entity.User;
import com.mid_term.springecommerce.Models.ResponseModel.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    //get list user if username == null
    @Query("SELECT u FROM User u WHERE u.username is null")
    List<User> getAllUser();

    @Query("SELECT u FROM User u JOIN u.roles r WHERE u.username = :name and r.id != 1")
    User getUserByName(@Param("name") String name);

    @Query("SELECT u FROM User u WHERE u.phone = :phone")
    User getUserByPhone(@Param("phone") String phone);

    @Query("SELECT NEW com.mid_term.springecommerce.Models.ResponseModel.UserResponse(" +
            "u.username,u.fullName,u.email, u.phone,u.address,u.gender) " +
            "FROM User u WHERE u.username = :username")
    UserResponse getInfoMine(@Param("username")String username);

    @Query("SELECT NEW com.mid_term.springecommerce.Models.ResponseModel.UserResponse(" +
            "u.username,u.fullName,u.email, u.phone,u.address,u.gender) " +
            "FROM User u WHERE u.phone = :phoneNumber")
    UserResponse getUserByPhoneNumber(@Param("phoneNumber")String phoneNumber);

}
