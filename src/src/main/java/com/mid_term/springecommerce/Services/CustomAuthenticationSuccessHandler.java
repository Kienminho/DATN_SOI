package com.mid_term.springecommerce.Services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        System.out.println("onAuthenticationSuccess");
        // Lưu thông tin vào cookie
        Cookie cookie = new Cookie("username", authentication.getName());
        cookie.setMaxAge(3600); // thời gian sống của cookie (giây)
        cookie.setPath("/");
        response.addCookie(cookie);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
