package work.home.countries.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import work.home.countries.domain.Country;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Profile("textFile")
public class TextFileCountryListParser implements CountryListParser {

    @Value("${file}")
    private File file;

    @Override
    public List<Country> getCountryList() {
        ObjectMapper mapper = new ObjectMapper();
        List<Country> list = new ArrayList<>();
        try {
            FileInputStream inputStream = new FileInputStream(file);
            TypeReference<List<Country>> typeReference = new TypeReference<>() {
            };
            list = mapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
