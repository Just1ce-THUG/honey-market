package ru.honeystore.honey_store;

import java.time.LocalDate;

public record OrderDTO(
        Long id,
        Long userId,
        LocalDate startDate,
        LocalDate endDate,
        OrderStatus status
) {
}
