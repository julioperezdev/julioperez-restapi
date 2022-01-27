package dev.julioperez.main.checkout;

import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.*;
import dev.julioperez.main.shared.ip.IpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CheckoutServiceImplementation implements CheckoutService{

    @Value("${MercadoLibre.secretKey}")
    private String MERCADO_PAGO_SECRET_KEY;

    @Value("${Checkout.item.imageurl}")
    private String ITEM_IMAGE_URL;

    private final String EXCLUDED_PAYMENT_TYPE_TICKET = "ticket";
    private final String ITEM_TITLE = "Donación a Julio Perez";
    private final String ITEM_CATEGORY_ID_LEARNING = "learnings";
    private final String ALTERNATIVE_WHEN_NULL_STRING = null;
    private final String DESCRIPTION_IN_DEBIT_CARD_TICKET = "donación a Julioperez.dev";
    private final String BACK_URL_SUCCESS = "http://localhost:3000/apoyo/completado";
    private final String BACK_URL_FAILURE = "http://localhost:3000/apoyo/postergado";

    private final IpUtils ipUtils;


    public CheckoutServiceImplementation(IpUtils ipUtils) {
        this.ipUtils = ipUtils;
    }

    @Override
    public String generateCheckout(DonationRequestDTO donationRequestDTO) throws MPException{
        return this.generatePreferenceByMercadoLibre(donationRequestDTO);

    }

    private String generatePreferenceByMercadoLibre(DonationRequestDTO donationRequestDTO) throws MPException {

        MercadoPago.SDK.setAccessToken(MERCADO_PAGO_SECRET_KEY);

        Preference preferenceStarted = this.initPreference();

        Preference preferenceWithMethodDefined = this.definePaymentMethod(preferenceStarted);

        Preference preferenceWithItemAdded = this.defineItemAndAddItToPreference(preferenceWithMethodDefined, donationRequestDTO);

        Preference preferenceWithPayerDefined = this.definePayer(preferenceWithItemAdded, donationRequestDTO);

        Preference preferenceWithDescriptionBank = this.defineDescriptionOnResumeOfBank(preferenceWithPayerDefined);

        Preference preferenceWithBackUrlDefined = this.defineBackUrls(preferenceWithDescriptionBank);

        Preference preferenceSaved = preferenceWithBackUrlDefined.save();

        System.out.println(preferenceSaved.getSandboxInitPoint());

        return preferenceSaved.getSandboxInitPoint();
    }

    private Preference initPreference(){
        Preference preference = new Preference();
        preference.setBinaryMode(true);
        return preference;
    }

    private Preference definePaymentMethod(Preference preference){
        PaymentMethods paymentMethods = new PaymentMethods();

        ExcludedPaymentType excludedPaymentType = new ExcludedPaymentType();
        excludedPaymentType.setId(EXCLUDED_PAYMENT_TYPE_TICKET);

        paymentMethods.appendExcludedPaymentTypes(excludedPaymentType);

        return preference.setPaymentMethods(paymentMethods);
    }

    private Preference defineItemAndAddItToPreference(Preference preference, DonationRequestDTO donationRequestDTO){
        Item item = new Item();
        item.setTitle(ITEM_TITLE)
                .setQuantity(1)
                .setUnitPrice((float) donationRequestDTO.getValue())
                .setPictureUrl(ITEM_IMAGE_URL)
                .setCurrencyId(donationRequestDTO.getCurrency())
                .setCategoryId(ITEM_CATEGORY_ID_LEARNING);

        return preference.appendItem(item);
    }

    private Preference defineDescriptionOnResumeOfBank(Preference preference){
        return preference.setStatementDescriptor(DESCRIPTION_IN_DEBIT_CARD_TICKET);
    }

    private Preference definePayer(Preference preference, DonationRequestDTO donationRequestDTO){
        Payer payer = new Payer();
        payer.setEmail(donationRequestDTO.getEmail());
        payer.setName(Optional
                .of(donationRequestDTO.getName())
                .orElse(ALTERNATIVE_WHEN_NULL_STRING));
        payer.setSurname(Optional
                .of(donationRequestDTO.getSurname())
                .orElse(ALTERNATIVE_WHEN_NULL_STRING));

        return preference.setPayer(payer);
    }

    private Preference defineBackUrls(Preference preference){
        BackUrls backUrls = new BackUrls();
        backUrls.setSuccess(BACK_URL_SUCCESS);
        backUrls.setFailure(BACK_URL_FAILURE);

        preference.setBackUrls(backUrls);

        return preference.setAutoReturn(Preference.AutoReturn.approved);
    }
}
