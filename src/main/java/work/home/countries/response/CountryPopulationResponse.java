package work.home.countries.response;

import java.util.Objects;

public class CountryPopulationResponse {

    private String number;
    private String name;
    private long population;

    public CountryPopulationResponse(String number, String name, long population) {
        this.number = number;
        this.name = name;
        this.population = population;
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

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryPopulationResponse that = (CountryPopulationResponse) o;
        return population == that.population &&
                Objects.equals(number, that.number) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, name, population);
    }

    @Override
    public String toString() {
        return "\n" + number + name + ", population = " + population;
    }
}
