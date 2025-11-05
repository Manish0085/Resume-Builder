package com.build.resume.service;

import com.build.resume.dto.CreateResumeRequest;
import com.build.resume.entity.Payment;
import com.build.resume.entity.Resume;
import com.razorpay.RazorpayException;

import java.util.List;

public interface PaymentService {

    Payment createdOrder(Object principal, String planType) throws RazorpayException;

    boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) throws RazorpayException;

    List<Payment> getUserPayments(Object principal);

    Payment getPaymentDetails(String orderId);
}
