package dev.julioperez.main.checkout;

import com.mercadopago.exceptions.MPException;

public interface CheckoutService {

    String generateCheckout( DonationRequestDTO donationRequestDTO) throws MPException;
}
