package com.homedepot.rma.controller;

import com.homedepot.rma.dto.ChatMessage;
import com.homedepot.rma.dto.ChatResponse;
import com.homedepot.rma.service.SimpleChatbotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "*")
@Tag(name = "AI Chatbot", description = "AI-powered customer service chatbot for RMA assistance")
public class ChatbotController {
    
    private static final Logger logger = LoggerFactory.getLogger(ChatbotController.class);
    
    @Autowired
    private SimpleChatbotService chatbotService;
    
    @PostMapping("/chat")
    @Operation(summary = "Send message to AI chatbot", 
               description = "Send a message to the AI chatbot and receive a response with suggestions")
    public ResponseEntity<ChatResponse> chat(
            @RequestBody ChatMessage message,
            @Parameter(description = "Customer ID for context") 
            @RequestHeader(value = "X-Customer-Id", required = false) String customerId) {
        
        try {
            logger.info("Received chat message from session: {}", message.getSessionId());
            
            // Set customer ID from header if provided
            if (customerId != null) {
                message.setCustomerId(customerId);
            }
            
            ChatResponse response = chatbotService.processMessage(message);
            
            logger.info("Generated response for session: {} with intent: {}", 
                       response.getSessionId(), response.getIntent());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error processing chat message", e);
            return ResponseEntity.internalServerError()
                .body(new ChatResponse("I'm sorry, I'm experiencing technical difficulties. Please try again later.", 
                                     message.getSessionId()));
        }
    }
    
    @DeleteMapping("/session/{sessionId}")
    @Operation(summary = "Clear chat session", 
               description = "Clear the chat history for a specific session")
    public ResponseEntity<Void> clearSession(
            @Parameter(description = "Session ID to clear") 
            @PathVariable String sessionId) {
        
        try {
            logger.info("Clearing chat session: {}", sessionId);
            chatbotService.clearSession(sessionId);
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            logger.error("Error clearing session: {}", sessionId, e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/health")
    @Operation(summary = "Check chatbot health", 
               description = "Health check endpoint for the AI chatbot service")
    public ResponseEntity<String> health() {
        try {
            return ResponseEntity.ok("AI Chatbot service is running");
        } catch (Exception e) {
            logger.error("Health check error", e);
            return ResponseEntity.ok("AI Chatbot service is running (with errors)");
        }
    }
}
