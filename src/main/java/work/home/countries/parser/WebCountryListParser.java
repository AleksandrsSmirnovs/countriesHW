package work.home.countries.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import work.home.countries.domain.Country;
import work.home.countries.exception.ExternalApiException;

import java.util.Arrays;
import java.util.List;

@Service
@Profile("web")
public class WebCountryListParser implements CountryListParser {

    private static final Logger log = LoggerFactory.getLogger(WebCountryListParser.class);
    private final RestTemplate restTemplate;

    @Value("${url}")
    private String url;

    public WebCountryListParser(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Country> getCountryList() {
        Country[] countries = restTemplate.getForObject(url, Country[].class);
        if (countries == null) {
            throw new ExternalApiException("Cannot receive data from external resource");
        }
        return Arrays.asList(countries);
    }
}
