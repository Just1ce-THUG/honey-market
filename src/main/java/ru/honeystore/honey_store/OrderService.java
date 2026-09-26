package ru.honeystore.honey_store;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Service
// lombok
public class OrderService {

    private final Map<Long, OrderDTO> orderMap;
    private final AtomicLong idCounter;

    public OrderService() {
        orderMap = new HashMap<>();
        idCounter = new AtomicLong();
    }

    public OrderDTO getOrderById(
            Long id
    ) {
        if (!orderMap.containsKey(id)) {
            throw new NoSuchElementException("Not found order by id: " + id);
        }
        return orderMap.get(id);
    }

    public List<OrderDTO> findAllOrders() {
        return orderMap.values().stream().toList();
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

        OrderDTO newOrderDTO = new OrderDTO(
                idCounter.incrementAndGet(),
                orderDTOToCreate.userId(),
                orderDTOToCreate.startDate(),
                orderDTOToCreate.endDate(),
                OrderStatus.PENDING
        );

        orderMap.put(newOrderDTO.id(), newOrderDTO);

        return newOrderDTO;
    }

    public OrderDTO editOrder(
            Long id,
            OrderDTO editOrderDTO
    ) {
        OrderDTO order = orderMap.get(id);
        if (!orderMap.containsKey(id)) {
            throw new NoSuchElementException("Not found order by id: " + id);
        }
        if (order.status() != OrderStatus.PENDING) {
            throw new NoSuchElementException("Can't edit order with status: " + order.status());
        }

        OrderDTO editedOrderDTO = new OrderDTO(
                id,
                editOrderDTO.userId(),
                editOrderDTO.startDate(),
                editOrderDTO.endDate(),
                orderMap.get(id).status()
                );

        orderMap.put(order.id(), editedOrderDTO);

        return editedOrderDTO;
    }

    public void deleteOrder(
            Long id
    ) {
        if (!orderMap.containsKey(id)) {
            throw new NoSuchElementException("Not found order by id: " + id);
        }
        orderMap.remove(id);
    }
}
