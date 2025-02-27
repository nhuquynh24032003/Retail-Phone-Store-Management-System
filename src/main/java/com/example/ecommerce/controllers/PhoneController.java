package com.example.ecommerce.controllers;

import com.example.ecommerce.models.Brand;
import com.example.ecommerce.models.Phone;
import com.example.ecommerce.models.PhoneRating;
import com.example.ecommerce.models.Role;
import com.example.ecommerce.models.User;
import com.example.ecommerce.services.BrandService;
import com.example.ecommerce.services.PhoneRatingService;
import com.example.ecommerce.services.PhoneService;
import com.example.ecommerce.services.UserService;
import org.apache.commons.io.FilenameUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.io.File;

@Controller
@RequestMapping("phone")
public class PhoneController {
    private final PhoneService phoneService;
    private final PhoneRatingService phoneRatingService;
    private final UserService userService;
    private final ResourceLoader resourceLoader;
    private final BrandService brandService;


    @Value("${upload.directory}")
    private String uploadDirectory;


    @Autowired
    public PhoneController(PhoneService phoneService, PhoneRatingService phoneRatingService,
                           UserService userService, ResourceLoader resourceLoader, BrandService brandService){
        this.phoneService = phoneService;
        this.phoneRatingService = phoneRatingService;
        this.userService = userService;
        this.resourceLoader = resourceLoader;
        this.brandService = brandService;
    }

    @GetMapping("")
    public String loadProductPage(@CookieValue(name="email") String email,
                                  Model model,
                                  HttpServletResponse response) {
        List<Brand> brandList = brandService.getAll();
        Map<Brand, List<Phone>> phoneByBrand = new HashMap<>();
        for (Brand brand: brandList) {
            List<Phone> phone = phoneService.getPhonesByBrand(brand, 0);
            phoneByBrand.put(brand, phone);
        }
        model.addAttribute("phonesByBrand", phoneByBrand);
        List<Phone> phoneList = phoneService.getPhonesByPage(0);
        int total = phoneService.getAll().size();
        int totalPages = (int) Math.ceil((double) total / 20);
        if (total <= 20) {
            totalPages = 1;
        }
        List<Integer> pageNumbers = generatePageNumber(totalPages);
        model.addAttribute("pageNumber", 0);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("phoneList", phoneList);
        try{
            setCookie(email, model, response);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "sale";
    }

    @GetMapping("/page/{pageNumber}")
    public String loadProductPageByPageNumber(Model model, @PathVariable long pageNumber) {
        List<Brand> brandList = brandService.getAll();
        Map<Brand, List<Phone>> phoneByBrand = new HashMap<>();
        for (Brand brand: brandList) {
            List<Phone> phone = phoneService.getPhonesByBrand(brand, 0);
            phoneByBrand.put(brand, phone);
        }
        model.addAttribute("phonesByBrand", phoneByBrand);
        List<Phone> phoneList = phoneService.getPhonesByPage((int)pageNumber);
        int total = phoneService.getAll().size();
        int totalPages = (int)Math.ceil((double) total / 20);
        if (total <= 20) {
            totalPages = 1;
        }
        List<Integer> pageNumbers = generatePageNumber(totalPages);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("phoneList", phoneList);

        return "sale";
    }

    private List<Integer> generatePageNumber(int totalPages) {
        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 0; i < totalPages; i++) {
            pageNumbers.add(i);
        }
        return pageNumbers;
    }

    @GetMapping("/{phoneId}")
    public String loadProductDetailPage(@Nullable @CookieValue(name="email") String email,
                                        Model model,
                                        HttpServletResponse response,
                                        @PathVariable long phoneId) {
        Phone phoneById = phoneService.getPhoneById(phoneId);
        List<PhoneRating> phoneRatingList = phoneRatingService.getRatingByPhone(phoneById);
        model.addAttribute("phoneRatingList", phoneRatingList);
        List<Brand> brandList = brandService.getAll();
        Map<Brand, List<Phone>> phoneByBrand = new HashMap<>();
        for (Brand brand: brandList) {
            List<Phone> phone = phoneService.getPhonesByBrand(brand, 0);
            phoneByBrand.put(brand, phone);
        }
        model.addAttribute("phonesByBrand", phoneByBrand);
        Phone phone = phoneService.getPhoneById(phoneId);
        try{
            setCookie(email, model, response);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        model.addAttribute("phoneDetail", phone);
        List<Phone> phonesByBrandExpId = phoneService.getPhoneByBrandExpId(phone.getBrand(), phoneId);
        if (phonesByBrandExpId.size() > 5) {
            List<Phone> res = new ArrayList<>();
            for (int i = 0; i<5; i++) {
                res.add(phonesByBrandExpId.get(i));
            }
            model.addAttribute("phonesByBrandExpId", res);
        }
        else {
            model.addAttribute("phonesByBrandExpId", phonesByBrandExpId);
        }

        return "product";
    }
    private final Path STORAGE_FOLDER = Paths.get("src/main/resources/static/upload");

    public String storeFile(String nameProduct, MultipartFile file) {
        String filename = "";
        try {
            if(file.isEmpty()){
                throw new Exception("Cannot store empty file");
            }
            if(!FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase().matches("png|jpe?g|gif")){
                throw new Exception("This isn't image file");
            }

            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            filename = nameProduct.replace(" ", "_") + "." + fileExtension;
            System.out.println(filename);

            Path destinationFile = this.STORAGE_FOLDER.resolve(
                            Paths.get(filename.toLowerCase()))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.STORAGE_FOLDER.toAbsolutePath())) {
                // This is a security check
                throw new Exception(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (Exception e) {
            throw new RuntimeException("Cannot store file");
        }
        return filename;
    }

    @PostMapping("/rating")
    public String submitRating(@RequestParam("phoneId") long phoneId,
                               @RequestParam("email") String email,
                               @RequestParam("img") MultipartFile imgFile,
                               @RequestParam("comment") String comment) throws IOException {
        List<User> userList = userService.getUserByEmail(email);
        if (userList.isEmpty()) {
            return "redirect:/phone/";
        } else {
            String img = storeFile(imgFile.getOriginalFilename(), imgFile);


//            String img = UUID.randomUUID().toString() + "-" + imgFile.getOriginalFilename();
//            String uploadDirectory = "classpath:/templates/upload/";
//            Resource uploadResource = resourceLoader.getResource(uploadDirectory);
//            File uploadDirectoryFile = uploadResource.getFile();
//            Path uploadPath = uploadDirectoryFile.toPath();
//            Files.copy(imgFile.getInputStream(), uploadPath.resolve(img), StandardCopyOption.REPLACE_EXISTING);
            User user = userList.get(0);
            Phone phone = phoneService.getPhoneById(phoneId);
            List<PhoneRating> phoneRatingByUserAndPhone = phoneRatingService.getPhoneRatingByUserAndPhone(user, phone);
            if (phoneRatingByUserAndPhone.isEmpty()) {
                phoneRatingService.saveRating(phone, user, img, comment);
                return "redirect:/phone/" + phone.getId();
            }
            else {
                return "redirect:/phone/" + phone.getId();
            }

        }
    }


    @PostMapping("/search")
    public String searchForPhone(@RequestParam("phone-search") String search,
                                 Model model, HttpServletResponse response,
                                 @Nullable @CookieValue(name="email") String email) {
        List<Brand> brandList = brandService.getAll();
        Map<Brand, List<Phone>> phoneByBrand = new HashMap<>();
        for (Brand brand: brandList) {
            List<Phone> phone = phoneService.getPhonesByBrand(brand, 0);
            phoneByBrand.put(brand, phone);
        }
        model.addAttribute("phonesByBrand", phoneByBrand);
        int total = phoneService.getAll().size();
        int totalPages = (int) Math.ceil((double) total / 20);
        if (total <= 20) {
            totalPages = 1;
        }
        try{
            setCookie(email, model, response);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        List<Integer> pageNumbers = generatePageNumber(totalPages);
        model.addAttribute("pageNumber", 0);
        model.addAttribute("pageNumbers", pageNumbers);
        List<Phone> phoneList = phoneService.findPhoneByName(search);
        if (phoneList.isEmpty()) {
            phoneList = phoneService.getAll();
        }
        model.addAttribute("phoneList", phoneList);
        return "search";
    }

    @PostMapping("/filter")
    public String getFilterPhone(Model model, HttpServletResponse response,
                                 @Nullable @CookieValue(name="email") String email,
                                 @RequestParam(value = "brand", required = false) String Brand,
                                 @RequestParam(value = "priceRange", required = false) int price,
                                 @RequestParam(value = "color", required = false) String color){
        List<Brand> brandList = brandService.getAll();
        try{
            setCookie(email, model, response);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Map<Brand, List<Phone>> phoneByBrand = new HashMap<>();
        for (Brand brand: brandList) {
            List<Phone> phone = phoneService.getPhonesByBrand(brand, 0);
            phoneByBrand.put(brand, phone);
        }
        model.addAttribute("phonesByBrand", phoneByBrand);

        List<Phone> phoneList = phoneService.filterPhone(Brand, price, color);
        model.addAttribute("phoneList", phoneList);
        int total = phoneList.size();
        int totalPages = (int) Math.ceil((double) total / 20);
        if (total <= 20) {
            totalPages = 1;
        }
        List<Integer> pageNumbers = generatePageNumber(totalPages);
        model.addAttribute("pageNumber", 0);
        model.addAttribute("pageNumbers", pageNumbers);
        return "filter";
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

