package com.homedepot.rma.service;

import com.homedepot.rma.dto.ReturnRequestDto;
import com.homedepot.rma.dto.ReturnResponseDto;
import com.homedepot.rma.enums.ReturnMethod;
import com.homedepot.rma.enums.ReturnStatus;
import com.homedepot.rma.model.*;
import com.homedepot.rma.repository.CustomerRepository;
import com.homedepot.rma.repository.OrderRepository;
import com.homedepot.rma.repository.ReturnRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ReturnService {
    
    @Autowired
    private ReturnRequestRepository returnRequestRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private QrCodeService qrCodeService;
    
    @Autowired
    private ShippingLabelService shippingLabelService;
    
    public ReturnResponseDto createReturnRequest(ReturnRequestDto returnRequestDto, String customerId) {
        // Find customer
        Customer customer = customerRepository.findByCustomerId(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        // Find order
        Order order = orderRepository.findByOrderNumber(returnRequestDto.getOrderNumber())
            .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Validate order ownership
        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("Order does not belong to customer");
        }
        
        // Check return eligibility
        validateReturnEligibility(order, returnRequestDto);
        
        // Generate RMA number
        String rmaNumber = generateRmaNumber();
        
        // Create return request
        ReturnRequest returnRequest = new ReturnRequest();
        returnRequest.setRmaNumber(rmaNumber);
        returnRequest.setOrder(order);
        returnRequest.setCustomer(customer);
        returnRequest.setReason(returnRequestDto.getReason());
        returnRequest.setMethod(returnRequestDto.getMethod());
        returnRequest.setNotes(returnRequestDto.getNotes());
        
        // Save return request
        returnRequest = returnRequestRepository.save(returnRequest);
        
        // Create return items
        for (com.homedepot.rma.dto.ReturnItemDto itemDto : returnRequestDto.getReturnItems()) {
            OrderItem orderItem = order.getOrderItems().stream()
                .filter(item -> item.getId().equals(itemDto.getOrderItemId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order item not found"));
            
            ReturnItem returnItem = new ReturnItem();
            returnItem.setReturnRequest(returnRequest);
            returnItem.setOrderItem(orderItem);
            returnItem.setQuantityToReturn(itemDto.getQuantityToReturn());
            returnItem.setCondition(itemDto.getCondition());
            returnItem.setNotes(itemDto.getNotes());
        }
        
        // Generate return method specific data
        if (returnRequestDto.getMethod() == ReturnMethod.DROP_OFF_STORE) {
            String qrCodeData = qrCodeService.generateQrCode(returnRequest);
            returnRequest.setQrCodeData(qrCodeData);
        } else if (returnRequestDto.getMethod() == ReturnMethod.SHIP_TO_WAREHOUSE) {
            String shippingLabelUrl = shippingLabelService.generateShippingLabel(returnRequest);
            returnRequest.setShippingLabelUrl(shippingLabelUrl);
        }
        
        // Update status
        returnRequest.setStatus(ReturnStatus.APPROVED);
        returnRequest.setProcessedDate(LocalDateTime.now());
        
        returnRequestRepository.save(returnRequest);
        
        // Convert to response DTO
        return convertToResponseDto(returnRequest);
    }
    
    public ReturnResponseDto getReturnRequest(String rmaNumber) {
        ReturnRequest returnRequest = returnRequestRepository.findByRmaNumber(rmaNumber)
            .orElseThrow(() -> new RuntimeException("Return request not found"));
        
        return convertToResponseDto(returnRequest);
    }
    
    public List<ReturnResponseDto> getCustomerReturns(String customerId) {
        Customer customer = customerRepository.findByCustomerId(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        List<ReturnRequest> returnRequests = returnRequestRepository.findByCustomerOrderByCreatedAtDesc(customer);
        
        return returnRequests.stream()
            .map(this::convertToResponseDto)
            .toList();
    }
    
    public void updateReturnStatus(String rmaNumber, ReturnStatus status) {
        ReturnRequest returnRequest = returnRequestRepository.findByRmaNumber(rmaNumber)
            .orElseThrow(() -> new RuntimeException("Return request not found"));
        
        returnRequest.setStatus(status);
        
        if (status == ReturnStatus.COMPLETED) {
            returnRequest.setCompletedDate(LocalDateTime.now());
        }
        
        returnRequestRepository.save(returnRequest);
    }
    
    private void validateReturnEligibility(Order order, ReturnRequestDto returnRequestDto) {
        // Check if order is within return policy timeframe (90 days)
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(90);
        if (order.getOrderDate().isBefore(cutoffDate)) {
            throw new RuntimeException("Order is outside return policy timeframe");
        }
        
        // Check if items are eligible for return
        for (com.homedepot.rma.dto.ReturnItemDto itemDto : returnRequestDto.getReturnItems()) {
            OrderItem orderItem = order.getOrderItems().stream()
                .filter(item -> item.getId().equals(itemDto.getOrderItemId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order item not found"));
            
            // Check if item is large or hazardous
            if (Boolean.TRUE.equals(orderItem.getIsLargeItem()) || Boolean.TRUE.equals(orderItem.getIsHazardous())) {
                throw new RuntimeException("Item is not eligible for self-service return: " + orderItem.getProductName());
            }
            
            // Check return quantity
            if (itemDto.getQuantityToReturn() > orderItem.getQuantity()) {
                throw new RuntimeException("Return quantity exceeds purchased quantity for item: " + orderItem.getProductName());
            }
        }
    }
    
    private String generateRmaNumber() {
        return "RMA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private ReturnResponseDto convertToResponseDto(ReturnRequest returnRequest) {
        ReturnResponseDto responseDto = new ReturnResponseDto();
        responseDto.setRmaNumber(returnRequest.getRmaNumber());
        responseDto.setOrderNumber(returnRequest.getOrder().getOrderNumber());
        responseDto.setReason(returnRequest.getReason());
        responseDto.setMethod(returnRequest.getMethod());
        responseDto.setStatus(returnRequest.getStatus());
        responseDto.setNotes(returnRequest.getNotes());
        responseDto.setTrackingNumber(returnRequest.getTrackingNumber());
        responseDto.setQrCodeData(returnRequest.getQrCodeData());
        responseDto.setShippingLabelUrl(returnRequest.getShippingLabelUrl());
        responseDto.setRequestedDate(returnRequest.getRequestedDate());
        responseDto.setProcessedDate(returnRequest.getProcessedDate());
        responseDto.setCompletedDate(returnRequest.getCompletedDate());
        
        return responseDto;
    }
}
