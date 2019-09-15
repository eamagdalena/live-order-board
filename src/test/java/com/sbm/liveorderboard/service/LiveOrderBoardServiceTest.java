package com.sbm.liveorderboard.service;

import com.sbm.liveorderboard.exception.OrderNotFoundException;
import com.sbm.liveorderboard.model.Order;
import com.sbm.liveorderboard.model.OrderSummary;
import com.sbm.liveorderboard.model.OrderSummary.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.sbm.liveorderboard.model.OrderType.BUY;
import static com.sbm.liveorderboard.model.OrderType.SELL;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LiveOrderBoardServiceTest {

    private LiveOrderBoardService service;

    @BeforeEach
    void init() {
        service = new LiveOrderBoardService();
    }

    @Test
    void summary_givenAcceptanceCriteriaExample_ShouldReturnExpectedOutput() {

        service.registerOrder(new Order("user1", SELL, new BigDecimal("3.5"), new BigDecimal("306")));
        service.registerOrder(new Order("user2", SELL, new BigDecimal("1.2"), new BigDecimal("310")));
        service.registerOrder(new Order("user3", SELL, new BigDecimal("1.5"), new BigDecimal("307")));
        service.registerOrder(new Order("user4", SELL, new BigDecimal("2.0"), new BigDecimal("306")));

        OrderSummary summary = service.summary();

        assertThat(summary.getBuying()).isEmpty();
        assertThat(summary.getSelling()).containsExactly(
                new Entry(new BigDecimal("5.5"), new BigDecimal("306")),
                new Entry(new BigDecimal("1.5"), new BigDecimal("307")),
                new Entry(new BigDecimal("1.2"), new BigDecimal("310"))
        );
    }

    @Test
    void summary_givenSeveralBuyOrders_ShouldBeSummarizedDecremental() {
        service.registerOrder(new Order("user1", BUY, new BigDecimal("7"), new BigDecimal("300")));
        service.registerOrder(new Order("user1", BUY, new BigDecimal("11"), new BigDecimal("400")));
        service.registerOrder(new Order("user2", BUY, new BigDecimal("9"), new BigDecimal("500")));

        assertThat(service.summary().getBuying()).containsExactly(
                new Entry(new BigDecimal("9"), new BigDecimal("500")),
                new Entry(new BigDecimal("11"), new BigDecimal("400")),
                new Entry(new BigDecimal("7"), new BigDecimal("300"))
        );
    }

    @Test
    void summary_givenSeveralSellOrders_ShouldBeSummarizedIncrementally() {
        service.registerOrder(new Order("user1", SELL, new BigDecimal("7"), new BigDecimal("300")));
        service.registerOrder(new Order("user1", SELL, new BigDecimal("11"), new BigDecimal("400")));
        service.registerOrder(new Order("user2", SELL, new BigDecimal("9"), new BigDecimal("500")));

        assertThat(service.summary().getSelling()).containsExactly(
                new Entry(new BigDecimal("7"), new BigDecimal("300")),
                new Entry(new BigDecimal("11"), new BigDecimal("400")),
                new Entry(new BigDecimal("9"), new BigDecimal("500"))
        );
    }

    @Test
    void summary_givenSeveralBuyOrdersOfSamePrice_ShouldCombineOnSummary() {
        service.registerOrder(new Order("user1", BUY, new BigDecimal("1.5"), new BigDecimal("306")));
        service.registerOrder(new Order("user1", BUY, new BigDecimal("2.5"), new BigDecimal("306")));
        service.registerOrder(new Order("user2", BUY, new BigDecimal("6.5"), new BigDecimal("306")));

        assertThat(service.summary().getBuying().get(0)).isEqualTo(new Entry(new BigDecimal("10.5"), new BigDecimal("306")));
    }

    @Test
    void summary_givenSeveralSellOrdersOfSamePrice_ShouldCombineOnSummary() {
        service.registerOrder(new Order("user1", SELL, new BigDecimal("1.5"), new BigDecimal("306")));
        service.registerOrder(new Order("user1", SELL, new BigDecimal("2.5"), new BigDecimal("306")));
        service.registerOrder(new Order("user2", SELL, new BigDecimal("6.5"), new BigDecimal("306")));

        assertThat(service.summary().getSelling().get(0)).isEqualTo(new Entry(new BigDecimal("10.5"), new BigDecimal("306")));
    }

    @Test
    void summary_givenEmptyOrderBook_ShouldReturnEmptySummary() {
        OrderSummary summary = service.summary();
        assertThat(summary.getBuying()).isEmpty();
        assertThat(summary.getSelling()).isEmpty();
    }

    /* --- */

    @Test
    void cancelOrder_happyPath() {
        service.registerOrder(new Order("foobar", BUY, ONE, TEN));
        service.cancelOrder(new Order("foobar", BUY, ONE, TEN));

        assertThat(service.summary().getBuying()).isEmpty();
    }

    @Test
    void cancelOrder_GivenTypeIsWrong_ShouldNotFindTheOrder() {
        service.registerOrder(new Order("foobar", BUY, ONE, TEN));
        assertThrows(OrderNotFoundException.class, () -> service.cancelOrder(new Order("foobar", SELL, ONE, TEN)));
    }

    @Test
    void cancelOrder_GivenUserIsDifferent_ShouldNotFindTheOrder() {
        service.registerOrder(new Order("foobar", BUY, ONE, TEN));
        assertThrows(OrderNotFoundException.class, () -> service.cancelOrder(new Order("foobar2", BUY, ONE, TEN)));
    }

    @Test
    void cancelOrder_GivenPriceIsDifferent_ShouldNotFindTheOrder() {
        service.registerOrder(new Order("foobar", BUY, ONE, TEN));
        assertThrows(OrderNotFoundException.class, () -> service.cancelOrder(new Order("foobar", BUY, ONE, ONE)));
    }

    /* --- */

    @Test
    void registerOrder_happyPath() {
        service.registerOrder(new Order("foobar", BUY, ONE, TEN));

        assertThat(service.summary().getBuying().get(0).getQuantity()).isEqualTo(ONE);
        assertThat(service.summary().getBuying().get(0).getPrice()).isEqualTo(TEN);

    }

    @Test
    void registerOrder_GivenMultipleOrders_ShouldAllAppearOnSummary() {
        service.registerOrder(new Order("foobar1", BUY, ONE, TEN));
        service.registerOrder(new Order("foobar2", BUY, ONE, new BigDecimal("20")));
        service.registerOrder(new Order("foobar3", SELL, ONE, TEN));
        service.registerOrder(new Order("foobar4", SELL, ONE, new BigDecimal("20")));

        OrderSummary summary = service.summary();

        assertThat(summary.getBuying()).contains(new Entry(ONE, TEN));
        assertThat(summary.getBuying()).contains(new Entry(ONE, new BigDecimal("20")));
        assertThat(summary.getSelling()).contains(new Entry(ONE, TEN));
        assertThat(summary.getSelling()).contains(new Entry(ONE, new BigDecimal("20")));
    }

}