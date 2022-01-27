package dev.julioperez.main.shared.ip;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IpUtilsTest {

    @InjectMocks
    IpUtils ipUtils;

    @Test
    void itShouldGetCountryByIpAddressHappyCase() throws IOException, GeoIp2Exception {
       //given
        String ipAddress = "200.55.107.159";
       //when
        String countryByIpAddress = ipUtils.getCountryByIpAddress(ipAddress);
        //then
        assertFalse(countryByIpAddress.isEmpty());
    }
}