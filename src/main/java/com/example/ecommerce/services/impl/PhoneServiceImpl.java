package com.example.ecommerce.services.impl;

import com.example.ecommerce.models.Brand;
import com.example.ecommerce.models.Color;
import com.example.ecommerce.models.Phone;
import com.example.ecommerce.models.PhoneDetail;
import com.example.ecommerce.repositories.*;
import com.example.ecommerce.models.Specification;
import com.example.ecommerce.repositories.PhoneDetailRepository;
import com.example.ecommerce.repositories.PhonePagingAndSortingRepository;
import com.example.ecommerce.repositories.PhoneRepository;
import com.example.ecommerce.services.BrandService;
import com.example.ecommerce.services.PhoneService;
import com.example.ecommerce.services.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PhoneServiceImpl implements PhoneService {
    private final PhoneRepository phoneRepository;
    private final PhoneDetailRepository phoneDetailRepository;
    private SpecificationService specificationService;
    private BrandService brandService;
    private final PhonePagingAndSortingRepository phonePageAndSort;

    private final BrandRepository brandRepository;

    private final ColorRepository colorRepository;

    @Autowired
    public PhoneServiceImpl(PhoneRepository phoneRepository,
                            PhoneDetailRepository phoneDetailRepository,
                            PhonePagingAndSortingRepository phonePageAndSort,
                            BrandRepository brandRepository,
                            ColorRepository colorRepository) {
        this.phoneRepository = phoneRepository;
        this.phoneDetailRepository = phoneDetailRepository;
        this.phonePageAndSort = phonePageAndSort;
        this.brandRepository = brandRepository;
        this.colorRepository = colorRepository;
    }

    @Override
    public Phone getPhoneById(long id) {
        return phoneRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Phone> getPhonesByPage(int numberPage) {
        Pageable page = PageRequest.of(numberPage, 20);
        return phoneRepository.findAll(page).getContent();
    }

//    public Phone getPhoneBy

    @Override
    public List<Phone> getPhonesByBrand(Brand brand, int numberPage) {
        Pageable page = PageRequest.of(numberPage, 10);
        return phonePageAndSort.findAllByBrand(brand, page);
    }

    @Override
    public List<Phone> getPhonesByPhoneDetails(int page, int number) {
        Pageable pageable = PageRequest.of(page, number, Sort.by("phoneDetails.createdAt").ascending());
        return phonePageAndSort.findAll(pageable).getContent();
    }

    @Override
    public List<Phone> getPhonesBySelling(int page, int number) {
        Pageable pageable = PageRequest.of(page, number, Sort.by("orderDetails.quantity"));
        return phonePageAndSort.findAll(pageable).getContent();
    }

    @Override
    public void addPhone(Phone phone) {
        phoneRepository.save(phone);
    }

    @Override
    public List<Phone> getPhoneByBrandExpId(Brand brand, long id) {
        return phoneRepository.findByBrandAndIdNot(brand, id);
    }

    @Override
    public List<Phone> getAll() {
        return phoneRepository.findAll();
    }

    @Override
    public List<Phone> findPhoneByName(String name) {
        return phoneRepository.findPhoneByNameContainsIgnoreCase(name);
    }

    @Override
    public List<Phone> filterPhone(String brand, int price, String color) {
        List<Brand> findBrand = brandRepository.findBrandByName(brand);
        if (brand.equals("") && price == 0 && color.equals("")) {
            return phoneRepository.findAll();
        }
        if (findBrand.isEmpty()) {
            List<Color> findColor = colorRepository.findColorByName(color);
            if (price == 0) {

                if (findColor.isEmpty()) {
                    return Collections.emptyList();
                }
                else {
                    List<PhoneDetail> findDetail = phoneDetailRepository.findByColor(findColor.get(0));
                    List<Phone> phoneList = new ArrayList<>();
                    if (findDetail.isEmpty()) {
                        return Collections.emptyList();
                    }
                    else {
                        for (PhoneDetail detail: findDetail) {
                            List<Phone> temp = phoneRepository.findPhoneByPhoneDetails(detail);
                            for (Phone phone: temp) {
                                if (!phoneList.contains(phone)) {
                                    phoneList.add(phone);
                                }
                            }
                        }
                        return phoneList;
                    }

                }
            }
            else {
                if (price == 1) {
                    List<PhoneDetail> findDetail = phoneDetailRepository.findByColorAndPriceLessThan(findColor.get(0)
                            , 1000000);
                    List<Phone> phoneList = new ArrayList<>();
                    if (findDetail.isEmpty()) {
                        return Collections.emptyList();
                    }
                    else {
                        for (PhoneDetail detail: findDetail) {
                            List<Phone> temp = phoneRepository.findPhoneByPhoneDetails(detail);
                            for (Phone phone: temp) {
                                if (!phoneList.contains(phone)) {
                                    phoneList.add(phone);
                                }
                            }
                        }
                        return phoneList;
                    }

                }
                else if (price == 2) {
                    List<PhoneDetail> findDetail = phoneDetailRepository.findByColorAndPriceBetween(findColor.get(0)
                            , 1000000, 10000000);
                    List<Phone> phoneList = new ArrayList<>();
                    if (findDetail.isEmpty()) {
                        return Collections.emptyList();
                    }
                    else {
                        for (PhoneDetail detail: findDetail) {
                            List<Phone> temp = phoneRepository.findPhoneByPhoneDetails(detail);
                            for (Phone phone: temp) {
                                if (!phoneList.contains(phone)) {
                                    phoneList.add(phone);
                                }
                            }
                        }
                        return phoneList;
                    }

                }
                else if (price == 3) {
                    List<PhoneDetail> findDetail = phoneDetailRepository.findByColorAndPriceGreaterThan(findColor.get(0)
                            , 10000000);
                    List<Phone> phoneList = new ArrayList<>();
                    if (findDetail.isEmpty()) {
                        return Collections.emptyList();
                    }
                    else {
                        for (PhoneDetail detail: findDetail) {
                            List<Phone> temp = phoneRepository.findPhoneByPhoneDetails(detail);
                            for (Phone phone: temp) {
                                if (!phoneList.contains(phone)) {
                                    phoneList.add(phone);
                                }
                            }
                        }
                        return phoneList;
                    }

                }
                else {
                    return Collections.emptyList();
                }
            }
        }
        else {
            List<Color> findColor = colorRepository.findColorByName(color);
            List<Phone> result = new ArrayList<>();
            if (price == 0) {

                if (findColor.isEmpty()) {
                    return Collections.emptyList();
                }
                else {
                    List<PhoneDetail> findDetail = phoneDetailRepository.findByColor(findColor.get(0));
                    List<Phone> phoneList = new ArrayList<>();
                    if (findDetail.isEmpty()) {
                        return Collections.emptyList();
                    }
                    else {
                        for (PhoneDetail detail: findDetail) {
                            List<Phone> temp = phoneRepository.findPhoneByPhoneDetails(detail);
                            for (Phone phone: temp) {
                                if (!phoneList.contains(phone)) {
                                    phoneList.add(phone);
                                }
                            }
                        }
                        for (Phone phone: phoneList) {
                            if (phone.getBrand().getName().equals(brand)) {
                                result.add(phone);
                            }
                        }
                        return result;
                    }
                }
            }
            else {
                if (findColor.isEmpty()) {
                    if (price == 1) {
                        List<PhoneDetail> findDetail = phoneDetailRepository.findByPriceLessThan(1000000);
                        List<Phone> phoneList = new ArrayList<>();
                        for (PhoneDetail detail: findDetail) {
                            List<Phone> temp = phoneRepository.findPhoneByPhoneDetails(detail);
                            for (Phone phone: temp) {
                                if (!phoneList.contains(phone)) {
                                    phoneList.add(phone);
                                }
                            }
                        }
                        for (Phone phone: phoneList) {
                            if (phone.getBrand().getName().equals(brand)) {
                                result.add(phone);
                            }
                        }
                        return result;
                    }
                    else if (price == 2) {
                        List<PhoneDetail> findDetail = phoneDetailRepository.findByPriceBetween(1000000, 10000000);
                        List<Phone> phoneList = new ArrayList<>();
                        for (PhoneDetail detail: findDetail) {
                            List<Phone> temp = phoneRepository.findPhoneByPhoneDetails(detail);
                            for (Phone phone: temp) {
                                if (!phoneList.contains(phone)) {
                                    phoneList.add(phone);
                                }
                            }
                        }
                        for (Phone phone: phoneList) {
                            if (phone.getBrand().getName().equals(brand)) {
                                result.add(phone);
                            }
                        }
                        return result;
                    }
                    else if (price == 3) {
                        List<PhoneDetail> findDetail = phoneDetailRepository.findByPriceGreaterThan(10000000);
                        List<Phone> phoneList = new ArrayList<>();
                        for (PhoneDetail detail: findDetail) {
                            List<Phone> temp = phoneRepository.findPhoneByPhoneDetails(detail);
                            for (Phone phone: temp) {
                                if (!phoneList.contains(phone)) {
                                    phoneList.add(phone);
                                }
                            }
                        }
                        for (Phone phone: phoneList) {
                            if (phone.getBrand().getName().equals(brand)) {
                                result.add(phone);
                            }
                        }
                        return result;
                    }
                    else {
                        return phoneRepository.findAll();
                    }
                }
                else {
                    if (price == 1) {
                        List<PhoneDetail> findDetail = phoneDetailRepository.findByColorAndPriceLessThan(findColor.get(0)
                                , 1000000);
                        List<Phone> phoneList = new ArrayList<>();
                        if (findDetail.isEmpty()) {
                            return Collections.emptyList();
                        }
                        else {
                            for (PhoneDetail detail: findDetail) {
                                List<Phone> temp = phoneRepository.findPhoneByPhoneDetails(detail);
                                for (Phone phone: temp) {
                                    if (!phoneList.contains(phone)) {
                                        phoneList.add(phone);
                                    }
                                }
                            }
                            for (Phone phone: phoneList) {
                                if (phone.getBrand().getName().equals(brand)) {
                                    result.add(phone);
                                }
                            }
                            return result;
                        }
                    }
                    else if (price == 2) {
                        List<PhoneDetail> findDetail = phoneDetailRepository.findByColorAndPriceBetween(findColor.get(0)
                                , 1000000, 10000000);
                        List<Phone> phoneList = new ArrayList<>();
                        if (findDetail.isEmpty()) {
                            return Collections.emptyList();
                        }
                        else {
                            for (PhoneDetail detail: findDetail) {
                                List<Phone> temp = phoneRepository.findPhoneByPhoneDetails(detail);
                                for (Phone phone: temp) {
                                    if (!phoneList.contains(phone)) {
                                        phoneList.add(phone);
                                    }
                                }
                            }
                            for (Phone phone: phoneList) {
                                if (phone.getBrand().getName().equals(brand)) {
                                    result.add(phone);
                                }
                            }
                            return result;
                        }

                    }
                    else if (price == 3) {
                        List<PhoneDetail> findDetail = phoneDetailRepository.findByColorAndPriceGreaterThan(findColor.get(0)
                                , 10000000);
                        List<Phone> phoneList = new ArrayList<>();
                        if (findDetail.isEmpty()) {
                            return Collections.emptyList();
                        }
                        else {
                            for (PhoneDetail detail: findDetail) {
                                List<Phone> temp = phoneRepository.findPhoneByPhoneDetails(detail);
                                if (!temp.isEmpty()) {
                                    for (Phone phone: temp) {
                                        if (!phoneList.contains(phone)) {
                                            phoneList.add(phone);
                                        }
                                    }
                                }
                            }
                            for (Phone phone: phoneList) {
                                if (phone.getBrand().getName().equals(brand)) {
                                    result.add(phone);
                                }
                            }
                            return result;
                        }

                    }
                    else {
                        return Collections.emptyList();
                    }
                }

            }
        }
    }
    @Override
    public void deletePhone(long id) {

        phoneRepository.deleteById(id);
    }
    @Override
    public void savePhone(Phone phone) {

        phoneRepository.save(phone);
    }

    @Override
    public Phone getLatestPhone() {
        return phoneRepository.getLatestPhone();
    }
}