package com.mid_term.springecommerce.APIController;

import com.mid_term.springecommerce.DTO.AvatarDTO;
import com.mid_term.springecommerce.DTO.StaffAndShipperDTO;
import com.mid_term.springecommerce.DTO.UserRequest;
import com.mid_term.springecommerce.Models.Entity.Cart;
import com.mid_term.springecommerce.Models.Entity.Role;
import com.mid_term.springecommerce.Models.Entity.StaffAndShipper;
import com.mid_term.springecommerce.Models.Entity.User;
import com.mid_term.springecommerce.Models.ResponseModel.UserResponse;
import com.mid_term.springecommerce.Repositorys.CartRepository;
import com.mid_term.springecommerce.Repositorys.RoleRepository;
import com.mid_term.springecommerce.Repositorys.StaffAndShipperRepository;
import com.mid_term.springecommerce.Repositorys.UserRepository;
import com.mid_term.springecommerce.Services.MailService;
import com.mid_term.springecommerce.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/users/")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StaffAndShipperRepository staffAndShipperRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CartRepository carRepository;

    @Autowired
    private MailService mailService;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @PostMapping("login")
    public Object handleLogin(@RequestBody UserRequest req) {
        User user = userRepository.getUserByName(req.getUsername());
        Cart c = carRepository.getCartByUser(user);
        if(user != null) {
            if(Utils.verifyPassword(req.getPassword(), user.getPassword())) {
                Utils.idUserLogin = user.getId();
                Utils.userLogin = user;
                Utils.userNameLogin = user.getUsername();
                Utils.cart = c;
                return Response.createSuccessResponseModel(0, true);
            }
            else {
                return Response.createErrorResponseModel("Mật khẩu không đúng vui lòng thử lại!", false);
            }
        }
        return Response.createErrorResponseModel("", false);
    }

    @GetMapping("check-login")
    public Object getUserInfo() {
        try {
            UserResponse userOptional = userRepository.getInfoMine(Utils.userNameLogin);
            if(userOptional == null) {
                return Response.createErrorResponseModel("Vui lòng đăng nhập!", false);
            }
            return Response.createSuccessResponseModel(0, userOptional);
        } catch (Exception e) {
            return Response.createErrorResponseModel(e.getMessage(), false);
        }
    }


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
            StaffAndShipperDTO s = staffAndShipperRepository.getInfoMine(Utils.idStaffAndShipperLogin);
            return Response.createSuccessResponseModel(0, s);
        } catch (Exception e) {
            return Response.createErrorResponseModel(e.getMessage(), false);
        }
    }

    @PostMapping(value = "upload-avatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Object UploadAvatar(@ModelAttribute AvatarDTO req, @RequestParam("avatar") MultipartFile image) {
        try {
            if(!image.isEmpty()) {
                String avatarLink = saveImage("avatar", image);
                StaffAndShipper u = staffAndShipperRepository.getUserById(req.getId());
                u.setAvatar(avatarLink);
                staffAndShipperRepository.save(u);
                return Response.createSuccessResponseModel(0,true);
            }
            return Response.createErrorResponseModel("Vui lòng chọn ảnh.", false);
        }
        catch (Exception ex) {
            return Response.createErrorResponseModel("Vui lòng thử lại.", ex.getMessage());
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

    private String saveImage(String name, MultipartFile image) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedDate = dateFormat.format(new Date());
        String imageName = name + '_' + formattedDate + ".jpg";
        String imagePath = uploadPath + imageName;
        File f = new File(uploadPath);
        if (!f.exists()) {
            f.mkdir();
        }
        // Save image to the specified path
        Files.copy(image.getInputStream(), Paths.get(imagePath));
        return "/images/" + imageName;
    }
}
