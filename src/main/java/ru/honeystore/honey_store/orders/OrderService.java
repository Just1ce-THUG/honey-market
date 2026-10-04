package ru.honeystore.honey_store.orders;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

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
        if (orderDTOToCreate.status() != null) {
            throw new IllegalArgumentException("Status should be empty");
        }
        if (!orderDTOToCreate.endDate().isAfter(orderDTOToCreate.startDate())) {
            throw new IllegalArgumentException("End date should be after start date");
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

        if (!orderEntity.getStatus().equals(OrderStatus.PENDING)) {
            throw new IllegalStateException("Can't edit order with status: " + orderEntity.getStatus());
        }
        if (!orderToUpdate.endDate().isAfter(orderToUpdate.startDate())) {
            throw new IllegalArgumentException("End date should be after start date");
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
        var order = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found order by id:" + id));

        if (order.getStatus().equals(OrderStatus.CANCELLED)) {
            throw new IllegalStateException("Order is already cancelled");
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
