package com.sbm.liveorderboard.service;

import com.sbm.liveorderboard.exception.InvalidOrderException;
import com.sbm.liveorderboard.model.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.sbm.liveorderboard.model.OrderType.BUY;
import static java.math.BigDecimal.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderValidationServiceTest {

    private final static BigDecimal NEGATIVE_NUMBER = new BigDecimal("-1.5");

    private OrderValidationService service = new OrderValidationService();

    @Test
    void validateOrder_happyPath() {
        service.validateOrder(new Order("foobar", BUY, TEN, ONE));
    }

    @Test
    void validateOrder_givenZeroQuantity_ShouldThrowException() {
        assertThrows(InvalidOrderException.class, () -> service.validateOrder(new Order("foobar", BUY, ZERO, ONE)));
    }

    @Test
    void validateOrder_givenNegativeQuantity_ShouldThrowException() {
        assertThrows(InvalidOrderException.class, () -> service.validateOrder(new Order("foobar", BUY, NEGATIVE_NUMBER, ONE)));
    }

    @Test
    void validateOrder_givenZeroPrice_ShouldThrowException() {
        assertThrows(InvalidOrderException.class, () -> service.validateOrder(new Order("foobar", BUY, ONE, ZERO)));
    }

    @Test
    void validateOrder_givenNegativePrice_ShouldThrowException() {
        assertThrows(InvalidOrderException.class, () -> service.validateOrder(new Order("foobar", BUY, ONE, NEGATIVE_NUMBER)));
    }

    @Test
    void validateOrder_givenNoUser_ShouldThrowException() {
        assertThrows(InvalidOrderException.class, () -> service.validateOrder(new Order(null, BUY, ONE, TEN)));
    }

    @Test
    void validateOrder_givenEmptyUser_ShouldThrowException() {
        assertThrows(InvalidOrderException.class, () -> service.validateOrder(new Order("", BUY, ONE, TEN)));
    }

    @Test
    void validateOrder_givenNoType_ShouldThrowException() {
        assertThrows(InvalidOrderException.class, () -> service.validateOrder(new Order("foobar", null, ONE, TEN)));
    }

}