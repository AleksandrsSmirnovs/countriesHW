package work.home.countries.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import work.home.countries.exception.NotFoundException;
import work.home.countries.response.CountryPatternResponse;
import work.home.countries.service.CountryService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class PatternControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Test
    public void shouldReturnCountriesFoundByPattern() throws Exception {
        when(countryService.getCountriesByPattern(anyString())).thenReturn(List.of(
                new CountryPatternResponse("Country 1"),
                new CountryPatternResponse("Country 2")
        ));
        mockMvc.perform(get("/countries/pattern/*somePattern*"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Country 1"))
                .andExpect(jsonPath("$[1].name").value("Country 2"));
        verify(countryService, times(1)).getCountriesByPattern("*somePattern*");
        verifyNoMoreInteractions(countryService);
    }

    @Test
    public void shouldReturnErrorDtoWhenNotFoundByPattern() throws Exception {
        when(countryService.getCountriesByPattern(anyString())).thenThrow(new NotFoundException("some message"));
        mockMvc.perform(get("/countries/pattern/*somePattern*"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("some message")));
        verify(countryService, times(1)).getCountriesByPattern("*somePattern*");
        verifyNoMoreInteractions(countryService);
    }
}