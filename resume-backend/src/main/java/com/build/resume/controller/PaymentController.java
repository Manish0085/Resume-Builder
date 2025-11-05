package com.build.resume.controller;

import com.build.resume.dto.AuthResponse;
import com.build.resume.dto.LoginRequest;
import com.build.resume.dto.RegisterRequest;
import com.build.resume.entity.Payment;
import com.build.resume.service.AuthService;
import com.build.resume.service.PaymentService;
import com.build.resume.service.impl.FileUploadService;
import com.razorpay.RazorpayException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.build.resume.util.AppConstants.*;

@RestController
@RequestMapping(PAYMENT)
public class PaymentController {

    private final AuthService authService;
    private final PaymentService paymentService;

    public PaymentController(AuthService authService, PaymentService paymentService) {
        this.authService = authService;
        this.paymentService = paymentService;
    }

    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);


    @PostMapping(CREATE_ORDER)
    public ResponseEntity<?> craetedOrder(@RequestBody Map<String, String> request,
                                          Authentication authentication) throws RazorpayException {

        String planType = request.get("planType");
        if (!PREMIUM.equalsIgnoreCase(planType)){
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid plan type"));
        }
        Payment payment = paymentService.createdOrder(authentication.getPrincipal(), planType);
        Map<String, Object> response = Map.of(
                "orderId", payment.getRazorpayOrderId(),
                "amount", payment.getAmount(),
                "currency", payment.getCurrency(),
                "receipt", payment.getReceipt()
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping(VERIFY_PAYMENT)
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> request) throws RazorpayException {

        String razorpayOrderId = request.get("razorpay_order_id");
        String razorpayPaymentId = request.get("razorpay_payment_id");
        String razorpaySignature = request.get("razorpay_signature");


        if (Objects.isNull(razorpayOrderId) ||
                Objects.isNull(razorpayPaymentId) ||
                Objects.isNull(razorpaySignature)){
            return ResponseEntity.badRequest().body(Map.of("message", "Missing required payment parameters"));
        }

        boolean isValid = paymentService.verifyPayment(razorpayOrderId, razorpayPaymentId, razorpaySignature);

        if (isValid){
            return ResponseEntity.ok(Map.of(
                    "message", "Payment verified successfully",
                    "status", "success"
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Payment Verification Failed"));
        }
    }

    @PostMapping(HISTORY)
    public ResponseEntity<?> getPaymentHistory(Authentication authentication){
        List<Payment> payments = paymentService.getUserPayments(authentication.getPrincipal());
        return ResponseEntity.ok(payments);

    }

    @GetMapping(ORDER)
    public ResponseEntity<?> getOrderDetails(@PathVariable String orderId){

        Payment payment = paymentService.getPaymentDetails(orderId);

        return ResponseEntity.ok(payment);
    }

}
