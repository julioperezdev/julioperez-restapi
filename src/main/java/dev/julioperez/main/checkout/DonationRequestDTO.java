package dev.julioperez.main.checkout;

import lombok.Data;

@Data
public class DonationRequestDTO {

    private Integer value;
    private String email;
    private String currency;
    private String name;
    private String surname;

}
