package ru.honeystore.honey_store.orders;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDate;

public record OrderDTO(
        @Null
        Long id,

        @NotNull
        Long userId,

        @FutureOrPresent
        @NotNull
        LocalDate startDate,

        @FutureOrPresent
        @NotNull
        LocalDate endDate,

        OrderStatus status
) {
}
