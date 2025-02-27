package com.example.ecommerce.config;

import com.example.ecommerce.models.AuthenticationProvider;
import com.example.ecommerce.models.Role;
import com.example.ecommerce.models.User;
import com.example.ecommerce.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String authorizedClientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();

        String email = customOAuth2User.getEmail();
        Cookie emailCookie = new Cookie("email", email);
        emailCookie.setMaxAge(604800);
        emailCookie.setPath("/");

        response.addCookie(emailCookie);
        User user = null;
        try{
            user = userService.getInforUser(email);
        }
        catch(Exception e){
            AuthenticationProvider authProvider = null;
            if("facebook".equals(authorizedClientRegistrationId)){
                authProvider = AuthenticationProvider.FACEBOOK;
            }
            else if("google".equals(authorizedClientRegistrationId)){
                authProvider = AuthenticationProvider.GOOGLE;
            }
            user = userService.registerOAuth2User(email, customOAuth2User.getName(), authProvider);
        }


        if(user != null && user.getRole().equals(Role.ROLE_ADMIN)){
            response.sendRedirect("/Admin");
        }else{
            response.sendRedirect("/");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }


}
