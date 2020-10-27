package work.home.countries.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "timestamp")
public class Timestamp {

    @Id
    private int id;

    private Long timestamp;

    public Timestamp() {
    }

    public Timestamp(int id, Long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Timestamp timestamp1 = (Timestamp) o;
        return Objects.equals(timestamp, timestamp1.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp);
    }

    @Override
    public String toString() {
        return "Timestamp{" +
                "timestamp=" + timestamp +
                '}';
    }
}
