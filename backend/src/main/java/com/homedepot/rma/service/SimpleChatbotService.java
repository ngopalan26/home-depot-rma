package com.homedepot.rma.service;

import com.homedepot.rma.dto.ChatMessage;
import com.homedepot.rma.dto.ChatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SimpleChatbotService {
    
    private static final Logger logger = LoggerFactory.getLogger(SimpleChatbotService.class);
    
    public ChatResponse processMessage(ChatMessage message) {
        try {
            String sessionId = message.getSessionId();
            if (sessionId == null || sessionId.isEmpty()) {
                sessionId = UUID.randomUUID().toString();
            }
            
            String intent = analyzeIntent(message.getMessage());
            String response = generateResponse(message.getMessage(), intent);
            List<String> suggestedActions = generateSuggestedActions(intent);
            
            ChatResponse chatResponse = new ChatResponse(response, sessionId);
            chatResponse.setIntent(intent);
            chatResponse.setConfidence(0.85);
            chatResponse.setSuggestedActions(suggestedActions);
            
            logger.info("Processed chat message with intent: {}", intent);
            return chatResponse;
            
        } catch (Exception e) {
            logger.error("Error processing chat message", e);
            return createErrorResponse(message.getSessionId());
        }
    }
    
    private String analyzeIntent(String message) {
        String lowerMessage = message.toLowerCase();
        
        if (lowerMessage.contains("return") && (lowerMessage.contains("how") || lowerMessage.contains("start") || lowerMessage.contains("create"))) {
            return "return_help";
        }
        if (lowerMessage.contains("track") || lowerMessage.contains("status") || lowerMessage.contains("rma")) {
            return "track_return";
        }
        if (lowerMessage.contains("policy") || lowerMessage.contains("rule") || lowerMessage.contains("can i return")) {
            return "policy_question";
        }
        if (lowerMessage.contains("order") && (lowerMessage.contains("find") || lowerMessage.contains("lookup") || lowerMessage.contains("search"))) {
            return "order_lookup";
        }
        if (lowerMessage.contains("hello") || lowerMessage.contains("hi") || lowerMessage.contains("help")) {
            return "greeting";
        }
        
        return "general_question";
    }
    
    private String generateResponse(String userMessage, String intent) {
        String lowerMessage = userMessage.toLowerCase();
        
        switch (intent) {
            case "return_help":
                if (lowerMessage.contains("how") || lowerMessage.contains("start")) {
                    return "To start a return, you'll need your order number. Click 'Create Return' on your dashboard, enter your order number, select the items you want to return, choose a reason, and select either store drop-off or shipping. Would you like me to guide you through this process?";
                }
                return "I can help you with returns! You can return most items within 90 days. What specific return question do you have?";
                
            case "track_return":
                return "To track your return, you'll need your RMA number. You can find it in your email confirmation or on your dashboard. Enter the RMA number in the 'Track Return' section. What's your RMA number?";
                
            case "policy_question":
                return "Home Depot's return policy allows returns within 90 days for most items with receipt. Large and hazardous items require special handling and cannot be returned through self-service. What specific policy question do you have?";
                
            case "order_lookup":
                return "I can help you find your order! You can look up orders using your order number or the email address used for the purchase. Go to 'Order Lookup' on the main page. Do you have your order number?";
                
            case "greeting":
                return "Hello! I'm here to help you with your Home Depot returns. I can assist with starting a return, tracking existing returns, or answering policy questions. How can I help you today?";
                
            default:
                return "I'm here to help with Home Depot returns! I can help you start a return, track an existing return, or answer questions about our return policy. What would you like to do?";
        }
    }
    
    private List<String> generateSuggestedActions(String intent) {
        List<String> actions = new ArrayList<>();
        
        switch (intent) {
            case "return_help":
                actions.add("Start a Return");
                actions.add("View Return Policy");
                actions.add("Find My Orders");
                break;
            case "track_return":
                actions.add("Track Return");
                actions.add("View Return History");
                actions.add("Contact Support");
                break;
            case "order_lookup":
                actions.add("Order Lookup");
                actions.add("Return Dashboard");
                break;
            case "greeting":
                actions.add("Start a Return");
                actions.add("Track Return");
                actions.add("View Orders");
                break;
            default:
                actions.add("Return Dashboard");
                actions.add("Contact Support");
        }
        
        return actions;
    }
    
    private ChatResponse createErrorResponse(String sessionId) {
        ChatResponse response = new ChatResponse(
            "I'm sorry, I'm having trouble processing your request right now. Please try again or contact customer support for assistance.",
            sessionId != null ? sessionId : UUID.randomUUID().toString()
        );
        response.setIntent("error");
        response.setConfidence(0.0);
        response.setSuggestedActions(Arrays.asList("Contact Support", "Try Again"));
        return response;
    }
    
    public void clearSession(String sessionId) {
        logger.info("Cleared session: {}", sessionId);
        // In this simple version, we don't maintain session state
    }
}
