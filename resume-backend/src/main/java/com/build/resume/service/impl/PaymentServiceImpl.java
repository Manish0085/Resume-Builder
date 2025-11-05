package com.build.resume.service.impl;

import com.build.resume.dto.AuthResponse;
import com.build.resume.entity.Payment;
import com.build.resume.entity.User;
import com.build.resume.repository.PaymentRepository;
import com.build.resume.repository.UserRepository;
import com.build.resume.service.AuthService;
import com.build.resume.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.build.resume.util.AppConstants.PREMIUM;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepo;
    private final AuthService authService;
    private final UserRepository userRepo;

    @Value("${razorpay.key-id}")
    private String  razorpayKeyId;
    @Value("${razorpay.key-secret}")
    private String razorpayKeySecrete;
    @Value("${razorpay.currency}")
    private String currency;


    private final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    public PaymentServiceImpl(PaymentRepository paymentRepo, AuthService authService, UserRepository userRepo) {
        this.paymentRepo = paymentRepo;
        this.authService = authService;
        this.userRepo = userRepo;
    }

    @Override
    public Payment createdOrder(Object principal, String planType) throws RazorpayException {

        AuthResponse response = authService.getProfile(principal);

        RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecrete);

        int amount = 99900; // Amount should be in paisa
        String receipt = PREMIUM + "_" + UUID.randomUUID().toString().substring(0, 8);

        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", amount);
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", receipt);

        Order razorpayOrder = razorpayClient.orders.create(orderRequest);

        Payment payment = Payment.builder()
                .userId(response.getId())
                .razorpayOrderId(razorpayOrder.get("id"))
                .amount(amount)
                .currency(currency)
                .planType(planType)
                .status("created")
                .receipt(receipt)
                .build();

        return paymentRepo.save(payment);
    }

    @Override
    public boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) throws RazorpayException {
        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", razorpayOrderId);
            attributes.put("razorpay_payment_id", razorpayPaymentId);
            attributes.put("razorpay_signature", razorpaySignature);

            boolean isValidSignature = Utils.verifyPaymentSignature(attributes, razorpayKeySecrete);

            if (isValidSignature){
                Payment payment = paymentRepo.findByRazorpayOrderId(razorpayOrderId)
                        .orElseThrow(() -> new RuntimeException("Payment not found"));

                payment.setRazorpayPaymentId(razorpayPaymentId);
                payment.setRazorpaySignature(razorpaySignature);
                payment.setStatus("paid");
                paymentRepo.save(payment);

                // upgrade the user subscription
                upgradeUserSubscription(payment.getUserId(), payment.getPlanType());
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("Error verifying the payment", e.getMessage());
            return false;
        }
    }

    @Override
    public List<Payment> getUserPayments(Object principal) {
        AuthResponse response = authService.getProfile(principal);

        return paymentRepo.findByUserIdOrderByCreatedAtDesc(response.getId());
    }

    @Override
    public Payment getPaymentDetails(String orderId) {
        return paymentRepo.findByRazorpayOrderId(orderId).orElseThrow(() ->
                new RuntimeException("Payment not found"));
    }

    private void upgradeUserSubscription(String userId, String planType) {
        User existingUser = userRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        existingUser.setSubscriptionPlan(planType);
        userRepo.save(existingUser);
        logger.info("User {} upgraded to {} plan", userId, planType);
    }
}
