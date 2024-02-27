package com.payment.controller;

import com.payment.paylode.PaymentRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    // http://localhost:8080/api/payments/charge

    @PostMapping("/charge")
    public String charge(@RequestBody PaymentRequest paymentRequest) {
        Stripe.apiKey = stripeApiKey;
        try {
            PaymentIntent intent = createPaymentIntent(paymentRequest.getAmount(), paymentRequest.getCurrency(), paymentRequest.getPaymentMethod());
            return "Payment Succeeded" + intent.getStatus();
        } catch (StripeException e) {
            return "Payment failed: " + e.getMessage();
        }
    }

    private PaymentIntent createPaymentIntent(long amount, String currency, String paymentMethod) throws StripeException {
        PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                .setAmount(amount)
                .setCurrency(currency)
                .setPaymentMethod(paymentMethod)
                .build();
        return PaymentIntent.create(createParams);
    }
}
