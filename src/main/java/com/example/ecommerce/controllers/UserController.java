package com.example.ecommerce.controllers;

import com.example.ecommerce.models.*;
import com.example.ecommerce.services.EmailDetailService;
import com.example.ecommerce.services.OrderService;
import com.example.ecommerce.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/account")
public class UserController {
    private int code;
    private final UserService userService;
    private final EmailDetailService emailDetailService;

    private final OrderService orderService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserController(UserService userService,
                          EmailDetailService emailDetailService,
                          OrderService orderService,
                          PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.emailDetailService = emailDetailService;
        this.orderService = orderService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/see_detail")
    public String seeDetail(@CookieValue String email, Model model){
        try{
            User user = userService.getInforUser(email);
            model.addAttribute("user", user);
            model.addAttribute("orders", orderService.getOrderByUser(user));
            if(user.getRole().equals(Role.ROLE_ADMIN)){
                model.addAttribute("admin", "admin");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "account";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam Map<String, String> account){
        User user = new User(account.get("last-name") + account.get("first-name"),
                passwordEncoder.encode(account.get("password")),
                account.get("email"),
                account.get("phone"),
                Role.ROLE_USER,
                AuthenticationProvider.LOCAL);
        try{
            userService.register(user);
            emailDetailService.sendSimpleMail(new EmailDetail(user.getEmail(),
                    "Chúc mừng bạn đã đăng ký thành công với Spring Mobile",
                    "ĐĂNG KÝ THÀNH CÔNG VỚI SPRING MOBILE"));
            return "login";
        }
        catch(Exception e){
            e.printStackTrace();
            return "register";
        }
    }

    @PostMapping("/update")
    public String updateAccount(@RequestParam Map<String, String> account,
                                @CookieValue(name="email") String email,
                                Model model,
                                HttpServletResponse response){
        User user = userService.getInforUser(email);

        user.setName(account.get("customer[name]"));
        user.setAddress(account.get("customer[address]"));
        user.setPhone(account.get("customer[phone]"));
        try{
            userService.updateUser(user);

            emailDetailService.sendSimpleMail(new EmailDetail(user.getEmail(),
                    "Bạn vừa thay đổi thông tin thành công với chúng tôi",
                    "Thông báo thay đổi thành công"));

            setCookie(user.getEmail(), model, response);

            return "account";
        }
        catch(Exception e){
            return null;
        }
    }

    @GetMapping("/recover")
    public String recoverGet(@RequestParam Map<String, String> account){
        String email = account.get("email");
        User user = userService.getInforUser(email);
        code = (int)(100*(Math.random()));
        emailDetailService.sendSimpleMail(new EmailDetail(user.getEmail(),
                "Mã thay đổi mật khẩu của bạn: "+code,
                "MÃ CODE ĐỔI MẬT KHẨU "));
        return "recover";
    }

    @PostMapping("/recover")
    public String recover(@RequestParam Map<String, String> account){
        if(code == Integer.parseInt(account.get("code"))){
            String email = account.get("email");
            User user = userService.getInforUser(email);
            user.setPassword(passwordEncoder.encode(account.get("password")));
            userService.updateUser(user);
            emailDetailService.sendSimpleMail(new EmailDetail(user.getEmail(),
                    "Thông báo bạn đã đổi mật khẩu vào lúc: " + LocalDate.now(),
                    "ĐỔI MẬT KHẨU THÀNH CÔNG"));
            return "login";
        }
        return "recover";
    }


    private void setCookie(String email,
                           Model model,
                           HttpServletResponse response){
        User newUser = userService.getInforUser(email);

        Cookie cookieEmail = new Cookie("email", newUser.getEmail());
        cookieEmail.setMaxAge(604800);
        cookieEmail.setPath("/");

        response.addCookie(cookieEmail);

        model.addAttribute("user", newUser);
        if(newUser.getRole().equals(Role.ROLE_ADMIN)){
            model.addAttribute("admin", "admin");
        }
    }

}
