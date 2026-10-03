package ru.honeystore.honey_store;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
// lombok
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public List<OrderDTO> getAllOrders() {
        List<OrderEntity> orderEntities = repository.findAll();

        return orderEntities.stream()
                .map(this::toDomainOrder)
                .toList();
    }

    public OrderDTO getOrderById(
            Long id
    ) {
        OrderEntity orderEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found order by id: " + id
                ));

        return toDomainOrder(orderEntity);
    }

    public OrderDTO createOrder(
            OrderDTO orderDTOToCreate
    ) {
        if (orderDTOToCreate.id() != null) {
            throw new IllegalArgumentException("Id should be empty");
        }

        if (orderDTOToCreate.status() != null) {
            throw new IllegalArgumentException("Status should be empty");
        }

        OrderEntity orderToSave = new OrderEntity(
                null,
                orderDTOToCreate.userId(),
                orderDTOToCreate.startDate(),
                orderDTOToCreate.endDate(),
                OrderStatus.PENDING
        );

        OrderEntity savedOrder = repository.save(orderToSave);

        return toDomainOrder(savedOrder);
    }

    public OrderDTO updateOrder(
            Long id,
            OrderDTO orderToUpdate
    ) {

        OrderEntity orderEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found order by id: " + id
                ));

        if (orderEntity.getStatus() != OrderStatus.PENDING) {
            throw new NoSuchElementException("Can't edit order with status: " + orderEntity.getStatus());
        }

        var orderToSave = new OrderEntity(
                orderEntity.getId(),
                orderToUpdate.userId(),
                orderToUpdate.startDate(),
                orderToUpdate.endDate(),
                OrderStatus.PENDING
        );

        var updatedOrder = repository.save(orderToSave);

        return toDomainOrder(updatedOrder);
    }

    @Transactional
    public void canselOrder(
            Long id
    ) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Not found order by id: " + id);
        }

        repository.setStatus(id, OrderStatus.CANCELLED);
    }

    public void approveOrder(
            Long id
    ) {
        OrderEntity orderEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found order by id: " + id
                ));

        if (orderEntity.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Cannot approve order: status=" + orderEntity.getStatus()
            );
        }

        orderEntity.setStatus(OrderStatus.APPROVED);
        repository.save(orderEntity);
    }

    private OrderDTO toDomainOrder(OrderEntity orderEntity) {
        return new OrderDTO(
                orderEntity.getId(),
                orderEntity.getUserId(),
                orderEntity.getStartDate(),
                orderEntity.getEndDate(),
                orderEntity.getStatus()
        );
    }

}
