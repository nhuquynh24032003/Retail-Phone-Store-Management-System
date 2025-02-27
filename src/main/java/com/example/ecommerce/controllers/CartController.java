package com.example.ecommerce.controllers;

import com.example.ecommerce.DTO.MyResponseMessage;
import com.example.ecommerce.models.Role;
import com.example.ecommerce.models.User;
import com.example.ecommerce.services.CartService;
import com.example.ecommerce.services.OrderService;
import com.example.ecommerce.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {
    CartService cartService;

    OrderService orderService;

    UserService userService;

    @Autowired
    public CartController(CartService cartService, OrderService orderService, UserService userService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("")
    public String showCartPage(@Nullable @CookieValue(name="email") String email,
                               Model model,
                               HttpServletResponse response) {
        try{
            setCookie(email, model, response);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "cart";
    }

    @PostMapping("/add")
    public ResponseEntity<MyResponseMessage<String>> add(@RequestBody Map<String, Object> params) {
        System.out.println(params);
        long phoneId = Long.parseLong((String) params.get("phoneId"));
        long colorId = Long.parseLong((String) params.get("colorId"));
        long capacityId = Long.parseLong((String) params.get("capacityId"));
        int quantity = Integer.parseInt((String) params.get("quantity"));
        cartService.addToCart(phoneId, colorId, capacityId);
        MyResponseMessage<String> myResponseMessage = new MyResponseMessage<>(Response.SC_OK, "Add cart item success");
        return ResponseEntity.ok(myResponseMessage);
    }

    @PostMapping(value = "/remove")
    public ResponseEntity<MyResponseMessage<String>> remove(@RequestBody Map<String, Object> params) {
        long phoneId = ((Number) params.get("productId")).longValue();
        long colorId = ((Number) params.get("colorId")).longValue();
        long capacityId = ((Number) params.get("capacityId")).longValue();
        cartService.remove(phoneId, colorId, capacityId);
        MyResponseMessage<String> myResponseMessage = new MyResponseMessage<>(Response.SC_OK, "Remove cart item success");
        return ResponseEntity.ok(myResponseMessage);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<MyResponseMessage<String>> update(@RequestBody Map<String, Object> params) {
        long phoneId = ((Number) params.get("productId")).longValue();
        long colorId = ((Number) params.get("colorId")).longValue();
        long capacityId = ((Number) params.get("capacityId")).longValue();
        int quantity = Integer.parseInt((String)params.get("quantity"));
        cartService.update(phoneId, colorId, capacityId, quantity);
        MyResponseMessage<String> myResponseMessage = new MyResponseMessage<>(Response.SC_OK, "Update quantity for cart item success");
        return ResponseEntity.ok(myResponseMessage);
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
