package cellgraph;

import java.util.Currency;
import java.util.Objects;

public class MktDataIdentifier {
    private final String tenor;
    private final String id;


    private final Currency currency;

    public MktDataIdentifier(String tenor, Currency currency) {
        this.tenor = tenor;
        this.currency = currency;
        this.id = String.format("%s_%s",currency.getCurrencyCode(),tenor);
    }

    public String getTenor() {
        return tenor;
    }
    public String getId(){
        return id;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MktDataIdentifier that = (MktDataIdentifier) o;
        return tenor.equals(that.tenor) && currency.equals(that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenor, currency);
    }
}
