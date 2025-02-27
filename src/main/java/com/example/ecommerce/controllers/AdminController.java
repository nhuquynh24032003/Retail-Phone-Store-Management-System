package com.example.ecommerce.controllers;


import com.example.ecommerce.models.*;
import com.example.ecommerce.models.pk.PhoneDetailKey;
import com.example.ecommerce.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CapacityService capacityService;
    @Autowired
    private ColorService colorService;

    @Autowired
    private PhoneService phoneService;
    @Autowired SpecificationService specificationService;
    @Autowired PhoneDetailService phoneDetailService;
    @Autowired OrderService orderService;
    @Autowired OrderDetailService orderDetailService;
    @GetMapping("/Admin")
    public String index() { return "Admin/indexAdmin"; }

    @GetMapping("/UserManage")
    public String userManage(Model model, @Param("keyword") String keyword) {

        model.addAttribute("listUsers", userService.getUsers(keyword));
        model.addAttribute("keyword", keyword);
        return "Admin/UserManage";
    }

    @GetMapping("/UserBlockManage")
    public String userBlockManage(Model model, @Param("keyword") String keyword) {
        model.addAttribute("listUsers", userService.getUsersBlock(keyword));
        return "Admin/UserBlockManage";
    }
    @GetMapping("/UserManage/deleteUser/{id}")
    public String deleteUser(@PathVariable(value="id") long id) {
        userService.deleteUserByID(id);
        return "redirect:/UserManage";
    }

    @GetMapping("/UserManage/blockUser/{id}")
    public String blockUser(@PathVariable(value="id") long id) {
        // save employee to database
        userService.blockUserByID(id);
        return "redirect:/UserManage";
    }

    @GetMapping("/UserBlockManage/unlockUser/{id}")
    public String unlockUser(@PathVariable(value="id") long id) {
        // save employee to database
        userService.unlockUserByID(id);
        return "redirect:/UserManage";
    }

    @GetMapping("/ProductManage")
    public String ProductManage() {

        return "Admin/ProductManage";
    }

    @GetMapping("/BrandManage")
    public String BrandManage(Model model) {
        model.addAttribute("listBrands", brandService.getAll());
        return "Admin/BrandManage";
    }

    @GetMapping("/BrandManage/deleteBrand/{id}")
    public String deleteBrand(@PathVariable(value="id") long id) {
        brandService.deleteBrandByID(id);
        return "redirect:/BrandManage";
    }

    @PostMapping("/saveBrand")
    public String saveBrand(@ModelAttribute("brand") Brand brand) {
        // save employee to database
        brandService.saveBrand(brand);
        return "redirect:/BrandManage";
    }

    @GetMapping("/updateBrand/{id}")
    public String updateBrand(@PathVariable(value="id") long id, Model model) {
        model.addAttribute("brand", brandService.getBrandById(id));
        return "Admin/formBrandManage";
    }

    @GetMapping("/addBrand")
    public String addBrand(Model model) {
        Brand brand = new Brand();
        model.addAttribute("brand", brand);
        return "Admin/formBrandManage";
    }

    @GetMapping("/CapacityManage")
    public String CapacityManage(Model model) {

        model.addAttribute("listCapacity", capacityService.getAllCapaCiTy());
        return "Admin/CapacityManage";
    }

    @GetMapping("/CapacityManage/deleteCapacity/{id}")
    public String deleteCapacity(@PathVariable(value="id") long id) {
        capacityService.deleteCapacity(id);
        return "redirect:/CapacityManage";
    }

    @PostMapping("/saveCapacity")
    public String saveCapacity(@ModelAttribute("capacity") Capacity capacity) {
        // save employee to database
        capacityService.saveCapcity(capacity);
        return "redirect:/CapacityManage";
    }

    @GetMapping("/addCapacity")
    public String capacityAdd(Model model) {

        Capacity capacity = new Capacity();
        model.addAttribute("capacity", capacity);
        return "Admin/formCapacityManage";
    }

    @GetMapping("/updateCapacity/{id}")
    public String updateCapacity(@PathVariable(value="id") long id, Model model) {

        model.addAttribute("capacity", capacityService.getCapacityById(id));

        return "Admin/formCapacityManage";
    }

    @GetMapping("/ColorManage")
    public String viewColor(Model model) {
        model.addAttribute("listColors", colorService.getAllColors());
        return "Admin/ColorManage";
    }
    @GetMapping("/ColorManage/deleteColor/{id}")
    public String deleteColor(@PathVariable(value="id") long id) {
        colorService.deleteColor(id);
        return "redirect:/ColorManage";
    }

    @PostMapping("/saveColor")
    public String saveColor(@ModelAttribute("color") Color color) {
        // save employee to database
        colorService.saveColor(color);
        return "redirect:/ColorManage";
    }

    @GetMapping("/updateColor/{id}")
    public String updateColor(@PathVariable(value="id") long id, Model model) {

        model.addAttribute("color", colorService.getColorById(id));

        return "Admin/formColorManage";
    }

    @GetMapping("/addColor")
    public String addColor(Model model) {

        Color color = new Color();
        model.addAttribute("color", color);
        return "Admin/formColorManage";
    }

    @GetMapping("/PhoneManage")
    public String viewPhone(Model model, @Param("keyword") String keyword) {
        model.addAttribute("listPhoneDetail", phoneDetailService.getAllPhoneDetail(keyword));
        model.addAttribute("keyword", keyword);
        return "Admin/PhoneManage";
    }
    @GetMapping("/PhoneManage/deletePhoneDetail")
    public String deletePhoneDetail(@RequestParam("phoneId") long phoneId, @RequestParam("colorId") long colorId, @RequestParam("capacityId") long capacityId, Model model) {

        phoneDetailService.deletePhoneDetail(phoneId, colorId, capacityId);
        return "redirect:/PhoneManage";

    }

    @PostMapping("/savePhone")
    public String savePhone(@ModelAttribute("phone") Phone phone, @ModelAttribute("specification") Specification specification, Model model, @RequestParam Map<String, Object> params) {
        // get params
        double price = Double.parseDouble(params.get("price").toString());
        long brandId = Long.parseLong(params.get("brandId").toString());
        Brand brand = brandService.getBrandById(brandId);
        long capacityId = Long.parseLong(params.get("capacityId").toString());
        long colorId = Long.parseLong(params.get("colorId").toString());
        int quantity = Integer.parseInt(params.get("quantity").toString());
        String img = params.get("img").toString();

        // save specification because not set cascade all
        specificationService.saveSpecification(specification);
        Specification addedSpecification = specificationService.getLatestSpecification();

        phone.setBrand(brand);
        phone.setSpec(addedSpecification);
        phoneService.savePhone(phone);

        Phone addedPhone = phoneService.getLatestPhone();

        // save phone first then save phone detail to get the phoneId
        PhoneDetailKey phoneDetailKey = new PhoneDetailKey(addedPhone.getId(), colorId, capacityId);
        PhoneDetail phoneDetail = new PhoneDetail(phoneDetailKey, img, price, quantity);
        List<PhoneDetail> phoneDetailList = new ArrayList<>();
        phoneDetailList.add(phoneDetail);
        addedPhone.setPhoneDetails(phoneDetailList);
        phoneService.savePhone(addedPhone);
        return "redirect:/PhoneManage";
    }

    @PostMapping("/updatePhone")
    public String updatePhone(@ModelAttribute("phone") Phone phone, @ModelAttribute("specification") Specification specification, Model model, @RequestParam Map<String, Object> params) {
        // get params
        double price = Double.parseDouble(params.get("price").toString());
        long brandId = Long.parseLong(params.get("brandId").toString());
        Brand brand = brandService.getBrandById(brandId);
        long capacityId = Long.parseLong(params.get("capacityId").toString());
        long colorId = Long.parseLong(params.get("colorId").toString());
        int quantity = Integer.parseInt(params.get("quantity").toString());
        String img = params.get("img").toString();
        long phoneId = Long.parseLong(params.get("phoneId").toString());

        // save specification because not set cascade all
        specificationService.saveSpecification(specification);

        phone.setId(phoneId);
        phone.setBrand(brand);
        phone.setSpec(specification);

        PhoneDetailKey phoneDetailKey = new PhoneDetailKey(phone.getId(), colorId, capacityId);
        PhoneDetail phoneDetail = new PhoneDetail(phoneDetailKey, img, price, quantity);
        List<PhoneDetail> phoneDetailList = new ArrayList<>();
        phoneDetailList.add(phoneDetail);
        phone.setPhoneDetails(phoneDetailList);
        phoneService.savePhone(phone);
        return "redirect:/PhoneManage";
    }

    @GetMapping("/addPhone")
    public String phoneAdd(Model model) {
        Phone phone = new Phone();
        model.addAttribute("phone", phone);
        Specification specification = new Specification();
        model.addAttribute("specification", specification);
        model.addAttribute("brands", brandService.getAll());
        model.addAttribute("colors", colorService.getAllColors());
        model.addAttribute("capacities", capacityService.getAllCapaCiTy());
        model.addAttribute("specifications", specificationService.getAllSpecification());

        return "Admin/addPhoneManage";
    }
    @GetMapping("/updatePhoneDetail")
    public String phoneUpdate(@RequestParam("phoneId") long phoneId, @RequestParam("colorId") long colorId, @RequestParam("capacityId") long capacityId, Model model) {

        model.addAttribute("phone", phoneDetailService.getPhoneDetail(phoneId, colorId, capacityId));
        model.addAttribute("p", phoneService.getPhoneById(phoneId));
        model.addAttribute("specification", specificationService.getSpecification(phoneService.getPhoneById(phoneId).getSpec().getId()));

        model.addAttribute("brands", brandService.getAll());
        model.addAttribute("colors", colorService.getAllColors());
        model.addAttribute("capacities", capacityService.getAllCapaCiTy());
        model.addAttribute("specifications", specificationService.getAllSpecification());


        model.addAttribute("selectedBrand", brandService.getBrandById(phoneService.getPhoneById(phoneId).getBrand().getId()));
        model.addAttribute("selectedColor", colorService.getColorById(colorId));
        model.addAttribute("selectedCapacity", capacityService.getCapacityById(capacityId));
        return "Admin/editPhoneManage";
    }

    @GetMapping("/OrderManage")
    public String viewOder(Model model) {
        model.addAttribute("listOrders", orderService.getAllOrders());


        return "Admin/OrderManage";
    }
    @PostMapping("/OrderManage/updateDeliveryStatus/{orderId}")
    public String updateDeliveryStatus(@PathVariable("orderId") long id, Model model) {
        Order order = orderService.getById(id);

        if (order.getDeliveryStatus() == (byte) 0) {
            order.setDeliveryStatus((byte) 1);
        } else if (order.getDeliveryStatus() == (byte) 1) {
            order.setDeliveryStatus((byte) 2);
        } else {
            order.setDeliveryStatus((byte) 0);
        }

        orderService.saveOrder(order);
        model.addAttribute("orderId", id);
        System.out.println(id);
        return "redirect:/OrderManage";
    }
    @PostMapping("/OrderManage/updateStatus/{orderId}")
    public String updateStatus(@PathVariable("orderId") long id, Model model) {
        Order order = orderService.getById(id);

        if (order.getStatus() == (byte) 0) {
            order.setStatus((byte) 1);
        } else {
            order.setStatus((byte) 0);
        }

        orderService.saveOrder(order);
        model.addAttribute("orderId", id);
        System.out.println(id);
        return "redirect:/OrderManage";
    }

    @GetMapping("/OrderManage/OrderDetailManage/{orderId}")
    public String viewOrderDetail(@PathVariable("orderId") long id, Model model) {
        model.addAttribute("listOrderDetails", orderDetailService.getOderDetail(id));
        return "Admin/OrderDetailManage";
    }
}
