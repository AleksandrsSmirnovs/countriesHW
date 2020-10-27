package work.home.countries.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "currency")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "currency_id")
    private Long id;

    private String code;
    private String name;
    private String symbol;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "country_currency",
            joinColumns = @JoinColumn(name = "currency_id"),
            inverseJoinColumns = @JoinColumn(name = "country_id")
    )
    private List<Country> countryList;

    public Currency() {
    }

    public Currency(String code, String name, String symbol) {
        this.code = code;
        this.name = name;
        this.symbol = symbol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(id, currency.id) &&
                Objects.equals(code, currency.code) &&
                Objects.equals(name, currency.name) &&
                Objects.equals(symbol, currency.symbol) &&
                Objects.equals(countryList, currency.countryList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, symbol, countryList);
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", countryList=" + countryList +
                '}';
    }
}
