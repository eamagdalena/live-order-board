package com.sbm.liveorderboard.repository;

import com.sbm.liveorderboard.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.sbm.liveorderboard.model.OrderType.BUY;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class OrderRepositoryTest {

    private OrderRepository repository;

    @BeforeEach
    void init() {
        repository = new OrderRepository();
    }

    @Test
    void persist_GivenAnOrderToPersist_ShouldBePossibleToFindIt() {
        Order order = new Order("user", BUY, ONE, TEN);
        repository.persist(order);
        assertThat(repository.streamByType(BUY).collect(toList())).contains(order);
    }

    @Test
    void remove_GivenAnOrderPersisted_ShouldBePossibleToDeleteIt() {
        Order order = new Order("user", BUY, ONE, TEN);
        repository.persist(order);
        assertThat(repository.remove(order)).isTrue();
    }

    @Test
    void remove_GivenAnOrderNotPersisted_ShouldReturnDeleteFalse() {
        assertThat(repository.remove(new Order("user", BUY, ONE, TEN))).isFalse();
    }

    @Test
    void streamByType_GivenNoOrdersPersisted_ShouldReturnEmpty() {
        assertThat(repository.streamByType(BUY).collect(toList())).isEmpty();
    }

    @Test
    void streamByType_GivenSeveralOrdersArePersisted_ShouldReturnAllOfThem() {
        Order order = new Order("user", BUY, ONE, TEN);
        Order order2 = new Order("user2", BUY, ONE, TEN);
        Order order3 = new Order("user3", BUY, ONE, TEN);

        repository.persist(order);
        repository.persist(order2);
        repository.persist(order3);

        assertThat(repository.streamByType(BUY).collect(toList())).contains(
                order, order2, order3
        );
    }

}