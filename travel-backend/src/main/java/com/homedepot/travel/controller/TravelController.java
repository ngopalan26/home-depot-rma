package com.homedepot.travel.controller;

import com.homedepot.travel.dto.TravelPlanRequest;
import com.homedepot.travel.dto.TravelPlanResponse;
import com.homedepot.travel.service.TravelService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/travel")
@CrossOrigin(origins = "*")
public class TravelController {
    
    private static final Logger logger = LoggerFactory.getLogger(TravelController.class);
    
    @Autowired
    private TravelService travelService;
    
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> info = new HashMap<>();
        info.put("service", "Travel Packing Assistant");
        info.put("version", "1.0.0");
        info.put("status", "UP");
        info.put("endpoints", new String[]{
            "GET /api/travel/health - Health check",
            "POST /api/travel/plans - Create travel plan",
            "GET /api/travel/plans/{id} - Get travel plan"
        });
        info.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(info);
    }
    
    @PostMapping("/plans")
    public ResponseEntity<?> createTravelPlan(@Valid @RequestBody TravelPlanRequest request) {
        try {
            logger.info("Received request to create travel plan for {}", request.getTravelerName());
            
            TravelPlanResponse response = travelService.createTravelPlan(request);
            
            logger.info("Successfully created travel plan with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid travel plan request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse("VALIDATION_ERROR", e.getMessage()));                                                                                                           
            
        } catch (Exception e) {
            logger.error("Error creating travel plan: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("INTERNAL_ERROR", "An unexpected error occurred"));
        }
    }
    
    @GetMapping("/plans/{id}")
    public ResponseEntity<?> getTravelPlan(@PathVariable Long id) {
        try {
            logger.info("Retrieving travel plan with ID: {}", id);
            
            TravelPlanResponse response = travelService.getTravelPlan(id);
            
            logger.info("Successfully retrieved travel plan with ID: {}", id);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            logger.warn("Travel plan not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("NOT_FOUND", e.getMessage()));
            
        } catch (Exception e) {
            logger.error("Error retrieving travel plan: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("INTERNAL_ERROR", "An unexpected error occurred"));
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "Travel Packing Assistant");
        health.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(health);
    }
    
    private Map<String, String> createErrorResponse(String errorCode, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", errorCode);
        error.put("message", message);
        error.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return error;
    }
}
