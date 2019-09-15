package com.sbm.liveorderboard.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Order {

    private final String userId;
    private final OrderType type;
    private final BigDecimal quantity;
    private final BigDecimal pricePerKilogram;

    public Order(String userId, OrderType type, BigDecimal quantity, BigDecimal pricePerKilogram) {
        this.userId = userId;
        this.type = type;
        this.quantity = quantity;
        this.pricePerKilogram = pricePerKilogram;
    }

    public String getUserId() {
        return userId;
    }

    public OrderType getType() {
        return type;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getPricePerKilogram() {
        return pricePerKilogram;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(userId, order.userId) &&
                type == order.type &&
                Objects.equals(quantity, order.quantity) &&
                Objects.equals(pricePerKilogram, order.pricePerKilogram);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, type, quantity, pricePerKilogram);
    }
}
