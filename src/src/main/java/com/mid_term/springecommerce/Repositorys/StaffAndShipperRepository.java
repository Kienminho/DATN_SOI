package com.mid_term.springecommerce.Repositorys;

import com.mid_term.springecommerce.DTO.StaffAndShipperDTO;
import com.mid_term.springecommerce.Models.Entity.StaffAndShipper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StaffAndShipperRepository extends JpaRepository<StaffAndShipper, Long> {
    @Query("SELECT u FROM StaffAndShipper u JOIN u.roles r WHERE r.roleName = 'ROLE_EMPLOYEE'")
    List<StaffAndShipper> getAllUser();

    @Query("SELECT u FROM StaffAndShipper u JOIN u.roles r WHERE r.roleName = 'ROLE_SHIPPER'")
    List<StaffAndShipper> getAllShipper();

    @Query("SELECT u FROM StaffAndShipper u JOIN u.roles r WHERE r.roleName = 'ROLE_SHIPPER' and u.isDeleted = false and u.isActivated = true")
    List<StaffAndShipper> getAllShipperActive();

    @Query("SELECT u FROM StaffAndShipper u where u.email =:email")
    StaffAndShipper getUserByEmail(@Param("email") String email);
    @Query("SELECT new com.mid_term.springecommerce.DTO.StaffAndShipperDTO(u.Id, u.userName,u.password,u.fullName," +
            "u.email,u.address,u.phoneNumber,u.avatar,r.roleName, u.activationToken, u.isActivated,u.firstLogin,u.isDeleted) FROM StaffAndShipper u JOIN u.roles r WHERE u.userName =:username")
    StaffAndShipperDTO getUserByName(@Param("username") String username);
    @Query("SELECT u FROM StaffAndShipper u where u.Id =:id")
    StaffAndShipper getUserById(@Param("id") Long id);
    @Query("SELECT new com.mid_term.springecommerce.DTO.StaffAndShipperDTO(u.Id, u.userName,u.fullName," +
            "u.email,u.address,u.phoneNumber,u.avatar,r.roleName) FROM StaffAndShipper u JOIN u.roles r WHERE u.Id =:id")
    StaffAndShipperDTO getInfoMine(@Param("id") Long id);
    @Query("SELECT u FROM StaffAndShipper u where u.activationToken =:token and u.isDeleted = false and u.isActivated = false ")
    StaffAndShipper getUserByToken(@Param("token") String token);


}
