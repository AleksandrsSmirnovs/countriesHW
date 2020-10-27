package work.home.countries.response;

import java.util.Objects;

public class CountryAreaResponse {

    private String number;
    private String name;
    private double area;

    public CountryAreaResponse(String number, String name, double area) {
        this.number = number;
        this.name = name;
        this.area = area;
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

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryAreaResponse that = (CountryAreaResponse) o;
        return Double.compare(that.area, area) == 0 &&
                Objects.equals(number, that.number) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, name, area);
    }

    @Override
    public String toString() {
        return "\n" + number + name + ", area = " + area;
    }
}
