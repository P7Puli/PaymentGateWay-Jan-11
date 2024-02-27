package com.payment.controller;


import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostMapping("/create-payment-intent")
    public String createPaymentIntent(@RequestParam Integer amount){

        Stripe.apiKey = stripeApiKey;

        try{
            PaymentIntent intent = PaymentIntent.create(
                    new PaymentIntentCreateParams.Builder()
                            .setCurrency("usd")
                            .setAmount((long) amount *100)
                            .build()
            );
            return  generateResponse(intent.getClientSecret());
        } catch (StripeException e){
            return  generateResponse("Error creating PaymentIntent:"+ e.getMessage());
        }
    }


    private String generateResponse(String clientSecret) {
        return "{\"clientSecret\":\""+ clientSecret + "\"}";
    }
}