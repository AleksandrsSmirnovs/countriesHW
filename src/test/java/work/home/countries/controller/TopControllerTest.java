package work.home.countries.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import work.home.countries.response.CountryAreaResponse;
import work.home.countries.response.CountryDensityResponse;
import work.home.countries.response.CountryPopulationResponse;
import work.home.countries.service.CountryService;
import work.home.countries.service.DatabaseCountryService;
import work.home.countries.service.NoDatabaseCountryService;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class TopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Test
    public void shouldReturnTopCountriesByPopulation() throws Exception {
        when(countryService.getTopCountriesByPopulation(anyInt())).thenReturn(List.of(
                new CountryPopulationResponse("1.", "Country 1", 30),
                new CountryPopulationResponse("2.", "Country 2", 20),
                new CountryPopulationResponse("3.", "Country 3", 10)
        ));
        mockMvc.perform(get("/countries/top/population/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].number").value("1."))
                .andExpect(jsonPath("$[0].name").value("Country 1"))
                .andExpect(jsonPath("$[0].population").value(30))
                .andExpect(jsonPath("$[1].number").value("2."))
                .andExpect(jsonPath("$[1].name").value("Country 2"))
                .andExpect(jsonPath("$[1].population").value(20))
                .andExpect(jsonPath("$[2].number").value("3."))
                .andExpect(jsonPath("$[2].name").value("Country 3"))
                .andExpect(jsonPath("$[2].population").value(10));

        verify(countryService, times(1)).getTopCountriesByPopulation(3);
        verifyNoMoreInteractions(countryService);
    }

    @Test
    public void shouldReturnTopCountriesByArea() throws Exception {
        when(countryService.getTopCountriesByArea(anyInt())).thenReturn(List.of(
                new CountryAreaResponse("1.", "Country 1", 25.5),
                new CountryAreaResponse("2.", "Country 2", 15.5),
                new CountryAreaResponse("3.", "Country 3", 5.5)
        ));
        mockMvc.perform(get("/countries/top/area/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].number").value("1."))
                .andExpect(jsonPath("$[0].name").value("Country 1"))
                .andExpect(jsonPath("$[0].area").value(25.50))
                .andExpect(jsonPath("$[1].number").value("2."))
                .andExpect(jsonPath("$[1].name").value("Country 2"))
                .andExpect(jsonPath("$[1].area").value(15.50))
                .andExpect(jsonPath("$[2].number").value("3."))
                .andExpect(jsonPath("$[2].name").value("Country 3"))
                .andExpect(jsonPath("$[2].area").value(5.50));

        verify(countryService, times(1)).getTopCountriesByArea(3);
        verifyNoMoreInteractions(countryService);
    }

    @Test
    public void shouldReturnTopCountriesByPopulationDensity() throws Exception {
        when(countryService.getTopCountriesByPopulationDensity(anyInt())).thenReturn(List.of(
                new CountryDensityResponse("1.", "Country 1", BigDecimal.valueOf(3.5)),
                new CountryDensityResponse("2.", "Country 2", BigDecimal.valueOf(2.5)),
                new CountryDensityResponse("3.", "Country 3", BigDecimal.valueOf(1.5))
        ));
        mockMvc.perform(get("/countries/top/density/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].number").value("1."))
                .andExpect(jsonPath("$[0].name").value("Country 1"))
                .andExpect(jsonPath("$[0].populationDensity").value(3.50))
                .andExpect(jsonPath("$[1].number").value("2."))
                .andExpect(jsonPath("$[1].name").value("Country 2"))
                .andExpect(jsonPath("$[1].populationDensity").value(2.50))
                .andExpect(jsonPath("$[2].number").value("3."))
                .andExpect(jsonPath("$[2].name").value("Country 3"))
                .andExpect(jsonPath("$[2].populationDensity").value(1.50));

        verify(countryService, times(1)).getTopCountriesByPopulationDensity(3);
        verifyNoMoreInteractions(countryService);
    }

}