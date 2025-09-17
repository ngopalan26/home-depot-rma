package com.homedepot.rma.controller;

import com.homedepot.rma.dto.ReturnRequestDto;
import com.homedepot.rma.dto.ReturnResponseDto;
import com.homedepot.rma.enums.ReturnStatus;
import com.homedepot.rma.service.ReturnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/returns")
@Tag(name = "Return Management", description = "APIs for managing merchandise returns")
@CrossOrigin(origins = "*")
public class ReturnController {
    
    @Autowired
    private ReturnService returnService;
    
    @PostMapping
    @Operation(summary = "Create a new return request", 
               description = "Creates a new return request for eligible merchandise")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Return request created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Order or customer not found"),
        @ApiResponse(responseCode = "409", description = "Item not eligible for return")
    })
    public ResponseEntity<ReturnResponseDto> createReturnRequest(
            @Parameter(description = "Customer ID for authentication")
            @RequestHeader("X-Customer-ID") String customerId,
            @Valid @RequestBody ReturnRequestDto returnRequestDto) {
        
        try {
            ReturnResponseDto response = returnService.createReturnRequest(returnRequestDto, customerId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to create return request: " + e.getMessage());
        }
    }
    
    @GetMapping("/{rmaNumber}")
    @Operation(summary = "Get return request by RMA number", 
               description = "Retrieves a specific return request using its RMA number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Return request found"),
        @ApiResponse(responseCode = "404", description = "Return request not found")
    })
    public ResponseEntity<ReturnResponseDto> getReturnRequest(
            @Parameter(description = "RMA number of the return request")
            @PathVariable String rmaNumber) {
        
        try {
            ReturnResponseDto response = returnService.getReturnRequest(rmaNumber);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException("Return request not found: " + e.getMessage());
        }
    }
    
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get all returns for a customer", 
               description = "Retrieves all return requests for a specific customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer returns retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<List<ReturnResponseDto>> getCustomerReturns(
            @Parameter(description = "Customer ID")
            @PathVariable String customerId) {
        
        try {
            List<ReturnResponseDto> responses = returnService.getCustomerReturns(customerId);
            return ResponseEntity.ok(responses);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to retrieve customer returns: " + e.getMessage());
        }
    }
    
    @PutMapping("/{rmaNumber}/status")
    @Operation(summary = "Update return request status", 
               description = "Updates the status of a return request (admin function)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status updated successfully"),
        @ApiResponse(responseCode = "404", description = "Return request not found")
    })
    public ResponseEntity<Void> updateReturnStatus(
            @Parameter(description = "RMA number of the return request")
            @PathVariable String rmaNumber,
            @Parameter(description = "New status for the return request")
            @RequestParam ReturnStatus status) {
        
        try {
            returnService.updateReturnStatus(rmaNumber, status);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to update return status: " + e.getMessage());
        }
    }
    
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Returns the health status of the service")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Return service is healthy");
    }
}
