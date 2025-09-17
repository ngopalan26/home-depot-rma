package com.homedepot.rma.repository;

import com.homedepot.rma.enums.ReturnStatus;
import com.homedepot.rma.model.Customer;
import com.homedepot.rma.model.ReturnRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, Long> {
    
    Optional<ReturnRequest> findByRmaNumber(String rmaNumber);
    
    List<ReturnRequest> findByCustomer(Customer customer);
    
    List<ReturnRequest> findByStatus(ReturnStatus status);
    
    @Query("SELECT r FROM ReturnRequest r WHERE r.customer = :customer ORDER BY r.createdAt DESC")
    List<ReturnRequest> findByCustomerOrderByCreatedAtDesc(@Param("customer") Customer customer);
    
    boolean existsByRmaNumber(String rmaNumber);
}
