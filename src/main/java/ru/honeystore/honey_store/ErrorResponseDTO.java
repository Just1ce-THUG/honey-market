package ru.honeystore.honey_store;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        String message,
        String detailedMessage,
        LocalDateTime errorTime
) {
}
