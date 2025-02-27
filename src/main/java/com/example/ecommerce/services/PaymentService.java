package com.example.ecommerce.services;

import org.springframework.web.servlet.view.RedirectView;

public interface PaymentService {
    RedirectView create(long amount);

    void handlePayment(double amount, byte paymentMethod);
}
