package com.sbm.liveorderboard.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class OrderSummary {

    private List<Entry> buying;

    private List<Entry> selling;

    public OrderSummary(List<Entry> buying, List<Entry> selling) {
        this.buying = buying;
        this.selling = selling;
    }

    public List<Entry> getBuying() {
        return buying;
    }

    public List<Entry> getSelling() {
        return selling;
    }

    public static class Entry {

        private BigDecimal quantity;

        private BigDecimal price;

        public Entry(BigDecimal quantity, BigDecimal price) {
            this.quantity = quantity;
            this.price = price;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }

        public BigDecimal getPrice() {
            return price;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry entry = (Entry) o;
            return Objects.equals(quantity, entry.quantity) &&
                    Objects.equals(price, entry.price);
        }

        @Override
        public int hashCode() {
            return Objects.hash(quantity, price);
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "quantity=" + quantity +
                    ", price=" + price +
                    '}';
        }
    }


}
