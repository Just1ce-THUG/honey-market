package ru.honeystore.honey_store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/order")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class); // lombok sf5x

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    } // lombok requaredargsconstructor

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(
            @PathVariable("id") Long id
    ) {
        log.info("Called orderById: id = " + id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getOrderById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        log.info("Called getAllOrders");
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getAllOrders());
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(
            @RequestBody OrderDTO orderDTOToCreate
    ) {
        log.info("Called createOrder");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(orderDTOToCreate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(
            @PathVariable("id") Long id,
            @RequestBody OrderDTO orderDTOToEdit
    ) {
        log.info("Called editOrder: id={}, orderToEdit={}", id, orderDTOToEdit);
        OrderDTO editedOrderDTO = orderService.updateOrder(id, orderDTOToEdit);
        return ResponseEntity.status(HttpStatus.OK)
                .body(editedOrderDTO);
    }

    @DeleteMapping("/{id}/cansel")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable("id") Long id
    ) {
        log.info("Called deleteOrder: id={}", id);

        orderService.canselOrder(id);

        return ResponseEntity.ok()
                    .build();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approvedOrder(
            @PathVariable("id") Long id
    ) {
        log.info("Called approvedOrder");
        orderService.approveOrder(id);
        return ResponseEntity.ok()
                .build();
    }

}
