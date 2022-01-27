package dev.julioperez.main.shared.ip;

import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class IpUtils {


    public String getCountryByIpAddress(String publicIp) throws IOException, GeoIp2Exception {
        WebServiceClient geoIp2Connection = createConnectionWithGeoIp2();
        //WebServiceClient geoIp2Connection = new WebServiceClient.Builder(666460, "pfV8bKb4oRsZwt3n").host("geolite.info").build();
        InetAddress ipAddress = InetAddress.getByName(publicIp);
        CountryResponse response = geoIp2Connection.country(ipAddress);
        Country country = response.getCountry();
        System.out.println(country.getIsoCode());            // 'US'
        System.out.println(country.getName());
        return country.getName();
    }

    private static WebServiceClient createConnectionWithGeoIp2(){
        return new WebServiceClient.Builder(666460, "pfV8bKb4oRsZwt3n").host("geolite.info").build();
    }
}
