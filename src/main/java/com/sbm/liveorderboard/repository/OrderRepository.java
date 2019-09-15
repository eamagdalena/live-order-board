package com.sbm.liveorderboard.repository;

import com.sbm.liveorderboard.model.Order;
import com.sbm.liveorderboard.model.OrderType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class OrderRepository {

    private List<Order> orders = new ArrayList<>();

    public void persist(Order order) {
        orders.add(order);
    }

    public boolean remove(Order order) {
        return orders.remove(order);
    }

    /* We expose the API as if the "database" was indexed by order type for simplicity
     * (we could have also kept an index here using a hashmap)
     * */
    public Stream<Order> streamByType(OrderType type) {
        return orders.stream().filter(order -> order.getType() == type);
    }

}
