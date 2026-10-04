package ru.honeystore.honey_store.orders;

import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDTO toDomain(OrderEntity orderEntity) {
        return new OrderDTO(
                orderEntity.getId(),
                orderEntity.getUserId(),
                orderEntity.getStartDate(),
                orderEntity.getEndDate(),
                orderEntity.getStatus()
        );
    }

    public OrderEntity toEntity(OrderDTO orderDTO) {
        return new OrderEntity(
                orderDTO.id(),
                orderDTO.userId(),
                orderDTO.startDate(),
                orderDTO.endDate(),
                orderDTO.status()
        );
    }

}
