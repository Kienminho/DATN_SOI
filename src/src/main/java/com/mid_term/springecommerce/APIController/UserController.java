package com.mid_term.springecommerce.APIController;

import com.mid_term.springecommerce.DTO.UserRequest;
import com.mid_term.springecommerce.Models.Entity.Cart;
import com.mid_term.springecommerce.Models.Entity.Role;
import com.mid_term.springecommerce.Models.Entity.User;
import com.mid_term.springecommerce.Models.ResponseModel.UserResponse;
import com.mid_term.springecommerce.Repositorys.CartRepository;
import com.mid_term.springecommerce.Repositorys.RoleRepository;
import com.mid_term.springecommerce.Repositorys.UserRepository;
import com.mid_term.springecommerce.Services.MailService;
import com.mid_term.springecommerce.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CartRepository carRepository;

    @Autowired
    private MailService mailService;

    @PostMapping("register")
    public Object handleRegister(@RequestBody UserRequest req) {
        System.out.println(req.getPassword() + req.getUsername());
        User userExist  = userRepository.getUserByName(req.getUsername());
        if(userExist == null) {
            String encodedPassword = passwordEncoder.encode(req.getPassword());
            Role role =  roleRepository.getRoleById(2L);
            User user = new User(req.getUsername(), encodedPassword, req.getEmail(),role);
            userRepository.save(user);
            Cart cart = new Cart();
            cart.setUser(user);
            carRepository.save(cart);

            mailService.sendMailRegister(req.getEmail(), req.getUsername());
            return Response.createSuccessResponseModel(0, true);
        }
        return Response.createSuccessResponseModel(0, false);
    }

    //get info mine
    @GetMapping("get-info-mine")
    public Object getInfoMine() {
        try {
            UserResponse user = userRepository.getInfoMine(Utils.userLogin.getUsername());
            return Response.createSuccessResponseModel(0, user);
        } catch (Exception e) {
            return Response.createErrorResponseModel(e.getMessage(), false);
        }
    }

    @GetMapping("get-info-by-phone/{phone}")
    public Object getInfoByPhone(@PathVariable String phone) {
        try {
            UserResponse user = userRepository.getUserByPhoneNumber(phone);
            return Response.createSuccessResponseModel(0, user);
        } catch (Exception e) {
            return Response.createErrorResponseModel(e.getMessage(), false);
        }
    }

    @PutMapping("update-profile")
    public Object UpdateProfile(@RequestBody UserResponse req) {
        try {
            User userExist = userRepository.getUserByName(Utils.userLogin.getUsername());
            if(userExist != null) {
                userExist.setEmail(req.getEmail());
                userExist.setFullName(req.getFullName());
                userExist.setPhone(req.getPhone());
                userExist.setAddress(req.getAddress());
                userExist.setGender(req.getGender());
                userRepository.save(userExist);
            }

            return Response.createSuccessResponseModel(0, true);
        } catch (Exception e) {
            return Response.createSuccessResponseModel(0, e.getMessage());
        }
    }
}
