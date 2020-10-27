package work.home.countries.response;

import java.math.BigDecimal;
import java.util.Objects;

public class CountryDensityResponse {

    private String number;
    private String name;
    private BigDecimal populationDensity;

    public CountryDensityResponse(String number, String name, BigDecimal populationDensity) {
        this.number = number;
        this.name = name;
        this.populationDensity = populationDensity;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPopulationDensity() {
        return populationDensity;
    }

    public void setPopulationDensity(BigDecimal populationDensity) {
        this.populationDensity = populationDensity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryDensityResponse that = (CountryDensityResponse) o;
        return Objects.equals(number, that.number) &&
                Objects.equals(name, that.name) &&
                Objects.equals(populationDensity, that.populationDensity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, name, populationDensity);
    }

    @Override
    public String toString() {
        return "\n" + number + name + ", population density = " + populationDensity.toString();
    }
}
