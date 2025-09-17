package com.homedepot.rma.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/simple-chat")
@CrossOrigin(origins = "*")
public class SimpleChatController {
    
    @PostMapping("/message")
    public Map<String, Object> sendMessage(@RequestBody Map<String, Object> request) {
        try {
            String message = (String) request.get("message");
            String sessionId = (String) request.get("sessionId");
            
            if (sessionId == null || sessionId.isEmpty()) {
                sessionId = UUID.randomUUID().toString();
            }
            
            // Generate response based on message content
            String response = generateResponse(message != null ? message : "");
            List<String> actions = generateActions(message != null ? message : "");
            
            Map<String, Object> result = new HashMap<>();
            result.put("response", response);
            result.put("sessionId", sessionId);
            result.put("timestamp", new Date().toString());
            result.put("suggestedActions", actions);
            result.put("intent", "general");
            result.put("confidence", 0.9);
            
            return result;
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("response", "I'm sorry, I'm having trouble right now. Please try again.");
            error.put("sessionId", UUID.randomUUID().toString());
            error.put("timestamp", new Date().toString());
            error.put("suggestedActions", Arrays.asList("Try Again", "Contact Support"));
            return error;
        }
    }
    
    private String generateResponse(String message) {
        String lowerMessage = message.toLowerCase();
        
        if (lowerMessage.contains("hello") || lowerMessage.contains("hi")) {
            return "Hello! I'm here to help you with your Home Depot returns. I can assist with starting a return, tracking existing returns, or answering policy questions. How can I help you today?";
        }
        
        if (lowerMessage.contains("return") && lowerMessage.contains("help")) {
            return "I can help you with returns! To start a return, you'll need your order number. Click 'Create Return' on your dashboard, enter your order number, select the items you want to return, choose a reason, and select either store drop-off or shipping. Would you like me to guide you through this process?";
        }
        
        if (lowerMessage.contains("track")) {
            return "To track your return, you'll need your RMA number. You can find it in your email confirmation or on your dashboard. Enter the RMA number in the 'Track Return' section. What's your RMA number?";
        }
        
        if (lowerMessage.contains("policy")) {
            return "Home Depot's return policy allows returns within 90 days for most items with receipt. Large and hazardous items require special handling and cannot be returned through self-service. What specific policy question do you have?";
        }
        
        return "I'm here to help with Home Depot returns! I can help you start a return, track an existing return, or answer questions about our return policy. What would you like to do?";
    }
    
    private List<String> generateActions(String message) {
        String lowerMessage = message.toLowerCase();
        
        if (lowerMessage.contains("return") && lowerMessage.contains("help")) {
            return Arrays.asList("Start a Return", "View Return Policy", "Find My Orders");
        }
        
        if (lowerMessage.contains("track")) {
            return Arrays.asList("Track Return", "View Return History", "Contact Support");
        }
        
        return Arrays.asList("Start a Return", "Track Return", "View Orders");
    }
    
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "OK");
        status.put("message", "Simple chatbot is running");
        return status;
    }
}
