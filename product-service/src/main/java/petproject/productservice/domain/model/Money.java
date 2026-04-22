package petproject.productservice.domain.model;

import java.math.BigDecimal;

public final class Money {
    private final BigDecimal price;
    private final String currency;

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    private Money(BigDecimal price, String currency) {
        this.price = price;
        this.currency = currency;
    }

    public static Money create(BigDecimal price, String currency) {
        return new Money(price, currency);
    }


}
