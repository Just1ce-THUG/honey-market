package ru.honeystore.honey_store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Modifying
    @Query("""
           update OrderEntity o
           set o.status = :status
           where o.id = :id
           """)
    void setStatus(
            @Param("id") Long id,
            @Param("status") OrderStatus orderStatus
    );
}
