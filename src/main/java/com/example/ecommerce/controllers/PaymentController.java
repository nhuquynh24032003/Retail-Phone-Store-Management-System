package com.example.ecommerce.controllers;

import com.example.ecommerce.models.Role;
import com.example.ecommerce.models.User;
import com.example.ecommerce.services.PaymentService;
import com.example.ecommerce.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    // 9704198526191432198
    // NGUYEN VAN A
    // 0715
    // 123456

    @Autowired
    PaymentService paymentService;

    @Autowired
    UserService userService;

    @GetMapping("/checkout")
    public String checkoutPage(@Nullable @CookieValue(name="email") String email,
                               Model model,
                               HttpServletResponse response) {
        try{
            setCookie(email, model, response);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return "checkout";
    }

    @GetMapping("/method")
    public String paymentMethodPage(@Nullable @CookieValue(name="email") String email,
                                    Model model,
                                    HttpServletResponse response) {
        try{
            setCookie(email, model, response);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return "payment/method";
    }

    @GetMapping("/create")
    public RedirectView create(@RequestParam("amount") String amountStr) {
        BigDecimal amountDecimal = new BigDecimal(amountStr);
        long amount = amountDecimal.longValue();
        return paymentService.create(amount); // just for test
    }

    @GetMapping("/handle-vnpay")
    public RedirectView handleVNPAYPaymentSuccess(@RequestParam(value = "vnp_Amount") String amount) {
        byte paymentMethod = 1; // vnpoy
        paymentService.handlePayment(Double.parseDouble(amount), paymentMethod);
        return new RedirectView("/payment/success");
    }

    @GetMapping("/handle-cod")
    public RedirectView handleCODPaymentSuccess(@RequestParam(value = "amount") String amount) {
        byte paymentMethod = 0; // cod
        paymentService.handlePayment(Double.parseDouble(amount), paymentMethod);
        return new RedirectView("/payment/success");
    }

    @GetMapping("/success")
    public String paymentSuccessPage() {
        return "payment/success";
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
