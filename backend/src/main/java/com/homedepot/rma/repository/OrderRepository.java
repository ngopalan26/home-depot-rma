package com.homedepot.rma.repository;

import com.homedepot.rma.model.Customer;
import com.homedepot.rma.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Optional<Order> findByOrderNumber(String orderNumber);
    
    List<Order> findByCustomer(Customer customer);
    
    List<Order> findByCustomerAndOrderDateBetween(Customer customer, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT o FROM Order o WHERE o.customer = :customer AND o.orderDate >= :cutoffDate")
    List<Order> findEligibleOrdersForReturn(@Param("customer") Customer customer, @Param("cutoffDate") LocalDateTime cutoffDate);
    
    boolean existsByOrderNumber(String orderNumber);
}
