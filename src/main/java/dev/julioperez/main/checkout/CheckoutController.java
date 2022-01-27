package dev.julioperez.main.checkout;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.mercadopago.exceptions.MPException;
import dev.julioperez.main.shared.ip.IpUtils;
import dev.julioperez.main.shared.rest.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/checkout")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.PUT})
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final IpUtils ipUtils;

    public CheckoutController(CheckoutService checkoutService, IpUtils ipUtils) {
        this.checkoutService = checkoutService;
        this.ipUtils = ipUtils;
    }

    @PutMapping("/generate")
    public RestResponse<String> generateCheckout(@RequestBody DonationRequestDTO donationRequestDTO) throws MPException{
        String checkoutUrl = checkoutService.generateCheckout(donationRequestDTO);
        return new RestResponse<>(HttpStatus.CREATED, "Success operation",Optional.of(checkoutUrl).orElse("-"));
    }

    @PutMapping("/decide/country/{ipAddress}")
    public RestResponse<String> decideIsFromArgentina(@PathVariable String ipAddress) throws IOException, GeoIp2Exception {
        String countryByIpAddress = ipUtils.getCountryByIpAddress(ipAddress);
        return new RestResponse<>(HttpStatus.CREATED, "Success operation",Optional.of(countryByIpAddress).orElse("-"));
    }

}
