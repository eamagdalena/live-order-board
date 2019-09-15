package com.sbm.liveorderboard.service;

import com.sbm.liveorderboard.exception.InvalidOrderException;
import com.sbm.liveorderboard.model.Order;

import static java.math.BigDecimal.ZERO;

class OrderValidationService {

    void validateOrder(Order order) throws InvalidOrderException {

        if (order.getType() == null) {
            throw new InvalidOrderException("Order type is required");
        }

        if (order.getUserId() == null || order.getUserId().isEmpty()) {
            throw new InvalidOrderException("User ID is required");
        }

        if (order.getPricePerKilogram() == null) {
            throw new InvalidOrderException("Price per kilogram is required");
        }

        if (order.getPricePerKilogram().compareTo(ZERO) <= 0) {
            throw new InvalidOrderException("Price per kilogram must be greater than zero");
        }

        if (order.getQuantity() == null) {
            throw new InvalidOrderException("Quantity is required");
        }

        if (order.getQuantity().compareTo(ZERO) <= 0) {
            throw new InvalidOrderException("Quantity must be greater than zero");
        }

    }

}
