package com.homedepot.rma.service;

import com.homedepot.rma.dto.ChatMessage;
import com.homedepot.rma.dto.ChatResponse;
import com.homedepot.rma.model.Customer;
import com.homedepot.rma.model.Order;
import com.homedepot.rma.model.ReturnRequest;
import com.homedepot.rma.repository.CustomerRepository;
import com.homedepot.rma.repository.OrderRepository;
import com.homedepot.rma.repository.ReturnRequestRepository;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AIChatbotService {
    
    private static final Logger logger = LoggerFactory.getLogger(AIChatbotService.class);
    
    @Autowired(required = false)
    private OpenAiService openAiService;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ReturnRequestRepository returnRequestRepository;
    
    // In-memory session storage (in production, use Redis or database)
    private final Map<String, List<com.theokanning.openai.completion.chat.ChatMessage>> chatHistory = new ConcurrentHashMap<>();
    
    public ChatResponse processMessage(ChatMessage message) {
        try {
            String sessionId = message.getSessionId();
            if (sessionId == null) {
                sessionId = UUID.randomUUID().toString();
            }
            
            // Analyze intent and extract context
            String intent = analyzeIntent(message.getMessage());
            String context = extractContext(message);
            
            // Generate response
            String response;
            List<String> suggestedActions = new ArrayList<>();
            
            if (openAiService != null) {
                response = generateAIResponse(message, sessionId, context);
            } else {
                response = generateMockResponse(message, intent);
            }
            
            // Add suggested actions based on intent
            suggestedActions = generateSuggestedActions(intent, context);
            
            ChatResponse chatResponse = new ChatResponse(response, sessionId);
            chatResponse.setIntent(intent);
            chatResponse.setConfidence(0.85);
            chatResponse.setContext(context);
            chatResponse.setSuggestedActions(suggestedActions);
            
            return chatResponse;
            
        } catch (Exception e) {
            logger.error("Error processing chat message", e);
            return createErrorResponse(message.getSessionId());
        }
    }
    
    private String generateAIResponse(ChatMessage message, String sessionId, String context) {
        try {
            if (openAiService == null) {
                logger.info("OpenAI service not available, using mock response");
                return generateMockResponse(message, analyzeIntent(message.getMessage()));
            }
            
            // Get or create chat history for session
            List<com.theokanning.openai.completion.chat.ChatMessage> messages = 
                chatHistory.computeIfAbsent(sessionId, k -> new ArrayList<>());
            
            // Add system message if this is the first message
            if (messages.isEmpty()) {
                String systemPrompt = createSystemPrompt(context);
                messages.add(new com.theokanning.openai.completion.chat.ChatMessage("system", systemPrompt));
            }
            
            // Add user message
            messages.add(new com.theokanning.openai.completion.chat.ChatMessage("user", message.getMessage()));
            
            // Create chat completion request
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .maxTokens(300)
                .temperature(0.7)
                .build();
            
            ChatCompletionResult result = openAiService.createChatCompletion(request);
            String response = result.getChoices().get(0).getMessage().getContent();
            
            // Add assistant response to history
            messages.add(new com.theokanning.openai.completion.chat.ChatMessage("assistant", response));
            
            return response;
            
        } catch (Exception e) {
            logger.error("Error generating AI response", e);
            return generateMockResponse(message, analyzeIntent(message.getMessage()));
        }
    }
    
    private String createSystemPrompt(String context) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a helpful customer service chatbot for Home Depot's Return Merchandise Authorization (RMA) system. ");
        prompt.append("You help customers with product returns, track return status, and answer questions about return policies. ");
        prompt.append("\nKey information:\n");
        prompt.append("- Return window: 90 days for most items\n");
        prompt.append("- Large and hazardous items cannot be returned through self-service\n");
        prompt.append("- Returns can be done via store drop-off or shipping to warehouse\n");
        prompt.append("- Customers need order number to initiate returns\n");
        prompt.append("- RMA numbers are provided for tracking\n");
        
        if (context != null && !context.isEmpty()) {
            prompt.append("\nContext: ").append(context);
        }
        
        prompt.append("\nAlways be helpful, concise, and professional. If you cannot help with a specific issue, direct them to contact customer service.");
        
        return prompt.toString();
    }
    
    private String generateMockResponse(ChatMessage message, String intent) {
        String userMessage = message.getMessage().toLowerCase();
        
        switch (intent) {
            case "return_help":
                if (userMessage.contains("how") || userMessage.contains("start")) {
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
    
    private String extractContext(ChatMessage message) {
        StringBuilder context = new StringBuilder();
        
        if (message.getCustomerId() != null) {
            Optional<Customer> customer = customerRepository.findByCustomerId(message.getCustomerId());
            if (customer.isPresent()) {
                context.append("Customer: ").append(customer.get().getFirstName()).append(" ").append(customer.get().getLastName());
            }
        }
        
        if (message.getContext() != null) {
            context.append(" Context: ").append(message.getContext());
        }
        
        return context.toString();
    }
    
    private List<String> generateSuggestedActions(String intent, String context) {
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
            sessionId
        );
        response.setIntent("error");
        response.setConfidence(0.0);
        response.setSuggestedActions(Arrays.asList("Contact Support", "Try Again"));
        return response;
    }
    
    public void clearSession(String sessionId) {
        chatHistory.remove(sessionId);
    }
}
