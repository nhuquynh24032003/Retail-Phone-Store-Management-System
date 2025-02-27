package com.example.ecommerce.services;

import com.example.ecommerce.models.Brand;
import com.example.ecommerce.models.Phone;
import com.example.ecommerce.models.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PhoneService {
    Phone getPhoneById(long id);

    List<Phone> getPhonesByPage(int numberPage);

    List<Phone> getPhonesByBrand(Brand brand, int numberPage);

    List<Phone> getPhonesByPhoneDetails(int page, int number);

    List<Phone> getPhonesBySelling(int page, int number);

    void addPhone(Phone phone);

    List<Phone> getPhoneByBrandExpId(Brand brand, long id);


    List<Phone> getAll();
    void deletePhone(long id);
    void savePhone(Phone phone);


    Phone getLatestPhone();


    List<Phone> findPhoneByName(String name);

    List<Phone> filterPhone(String brand, int price, String color);

}
