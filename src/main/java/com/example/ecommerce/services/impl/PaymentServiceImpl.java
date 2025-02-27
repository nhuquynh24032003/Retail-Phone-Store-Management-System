package com.example.ecommerce.services.impl;

import com.example.ecommerce.config.VNPAYConfig;
import com.example.ecommerce.models.Order;
import com.example.ecommerce.repositories.OrderRepository;
import com.example.ecommerce.services.OrderService;
import com.example.ecommerce.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public RedirectView create(long amount) {
        // String orderType = req.getParameter("ordertype");
        // long amount = Integer.parseInt(req.getParameter("amount"))*100;
        // String bankCode = req.getParameter("bankCode");

        amount *= 100; // 1_000_000 * 100 == 1 million VND (rule in VNPAY)

        String vnp_TxnRef = VNPAYConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1"; // String vnp_IpAddr = VNPAYConfig.getIpAddress(req);
        String vnp_TmnCode = VNPAYConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPAYConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPAYConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", "billpayment");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VNPAYConfig.vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPAYConfig.hmacSHA512(VNPAYConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPAYConfig.vnp_PayUrl + "?" + queryUrl;

        return new RedirectView(paymentUrl);
    }

    @Override
    public void handlePayment(double amount, byte paymentMethod) {
        orderService.createOrder();
        Order orderFound = orderRepository.getLatestOrder();
        orderFound.setPaymentMethod(paymentMethod); // 0 -> COD, 1 -> vnpay
        if (paymentMethod == 1) {
            orderFound.setAmountPaid(amount / 100); // because we have * 100 above for suit with format of VNPAY
            orderFound.setStatus((byte) 1); // 0 not paid, 1 paid
            orderRepository.save(orderFound);
        } else if (paymentMethod == 0) {
            orderFound.setAmountPaid(amount);
            orderFound.setStatus((byte) 0);
        }
    }
}
