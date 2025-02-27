package com.example.ecommerce.controllers;

import com.example.ecommerce.models.Brand;
import com.example.ecommerce.models.Phone;
import com.example.ecommerce.models.Role;
import com.example.ecommerce.models.User;
import com.example.ecommerce.services.BrandService;
import com.example.ecommerce.services.PhoneService;
import com.example.ecommerce.services.UserService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private BrandService brandService;
    private PhoneService phoneService;
    private UserService userService;

    @Autowired
    public HomeController(UserService userService,
                          PhoneService phoneService, BrandService brandService){
        this.userService = userService;
        this.phoneService = phoneService;
        this.brandService = brandService;
    }

    @GetMapping("/")
    public String loadHomePage(@Nullable @CookieValue String email, Model model, HttpSession session){
        List<Brand> brands =brandService.getAll();
        Map<Brand, List<Phone>> phonesByBrand = new HashMap<>();
        for (Brand brand: brands) {
            List<Phone> phoneList = phoneService.getPhonesByBrand(brand, 0);
            phonesByBrand.put(brand, phoneList);
        }
        model.addAttribute("phonesByBrand", phonesByBrand);
        if(email != null){
            try{
                User user = userService.getInforUser(email);
                session.setAttribute("user", user);
                model.addAttribute("user", user);
                if(user.getRole().equals(Role.ROLE_ADMIN)){
                    model.addAttribute("admin", "admin");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        return "index";
    }
}
