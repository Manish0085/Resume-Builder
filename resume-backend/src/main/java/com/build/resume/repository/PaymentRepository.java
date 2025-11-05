package com.build.resume.repository;

import com.build.resume.entity.Payment;
import com.build.resume.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);

    Optional<Payment> findByRazorpayPaymentId(String paymentId);

    List<Payment> findByUserIdOrderByCreatedAtDesc(String userId);

    List<Payment> findByStatus(String status);

}
