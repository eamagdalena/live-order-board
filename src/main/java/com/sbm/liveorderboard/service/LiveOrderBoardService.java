package com.sbm.liveorderboard.service;

import com.sbm.liveorderboard.exception.OrderNotFoundException;
import com.sbm.liveorderboard.model.Order;
import com.sbm.liveorderboard.model.OrderSummary;
import com.sbm.liveorderboard.model.OrderSummary.Entry;
import com.sbm.liveorderboard.model.OrderType;
import com.sbm.liveorderboard.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.sbm.liveorderboard.model.OrderType.BUY;
import static com.sbm.liveorderboard.model.OrderType.SELL;
import static java.util.stream.Collectors.toList;

public class LiveOrderBoardService {

    private OrderRepository repository = new OrderRepository();

    private OrderValidationService orderValidationService = new OrderValidationService();

    public void registerOrder(Order order) {

        orderValidationService.validateOrder(order);

        repository.persist(order);

    }

    public void cancelOrder(Order order) {

        boolean removed = repository.remove(order);

        if (!removed) {
            throw new OrderNotFoundException();
        }
    }

    public OrderSummary summary() {

        return new OrderSummary(
                getSummaryEntriesOfType(BUY),
                getSummaryEntriesOfType(SELL)
        );

    }

    /* Use and abuse of java streams for cleanliness */
    private List<Entry> getSummaryEntriesOfType(OrderType type) {

        return repository.streamByType(type)
                .collect(
                        Collectors.groupingBy(
                                Order::getPricePerKilogram,
                                Collectors.reducing(BigDecimal.ZERO, Order::getQuantity, BigDecimal::add)
                        )
                )
                .entrySet()
                .stream()
                .map(
                        mapEntry -> new Entry(mapEntry.getValue(), mapEntry.getKey())
                )
                .sorted(
                        type == SELL ?
                                Comparator.comparing(Entry::getPrice) :
                                Comparator.comparing(Entry::getPrice).reversed()
                )
                .collect(toList());
    }

}
