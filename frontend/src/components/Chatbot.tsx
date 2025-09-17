import React, { useState, useRef, useEffect } from 'react';
import {
  Box,
  Paper,
  TextField,
  IconButton,
  Typography,
  Fab,
  Collapse,
  Avatar,
  Chip,
  CircularProgress,
  Divider
} from '@mui/material';
import {
  Chat as ChatIcon,
  Close as CloseIcon,
  Send as SendIcon,
  SmartToy as BotIcon,
  Person as PersonIcon
} from '@mui/icons-material';
import { apiService } from '../services/api';

interface ChatMessage {
  message: string;
  sessionId?: string;
  customerId?: string;
  context?: string;
}

// ChatResponse interface moved to types or inline usage

interface ChatBubble {
  text: string;
  isUser: boolean;
  timestamp: Date;
  suggestedActions?: string[];
}

const Chatbot: React.FC = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [messages, setMessages] = useState<ChatBubble[]>([]);
  const [inputValue, setInputValue] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [sessionId, setSessionId] = useState<string>('');
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  useEffect(() => {
    if (isOpen && messages.length === 0) {
      // Send initial greeting
      sendInitialGreeting();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isOpen]);

  const sendInitialGreeting = async () => {
    const initialMessage: ChatMessage = {
      message: "Hello",
      sessionId: sessionId || undefined,
      customerId: localStorage.getItem('customerId') || undefined
    };

    try {
      const response = await apiService.sendChatMessage(initialMessage);
      setSessionId(response.sessionId);
      
      const botMessage: ChatBubble = {
        text: response.response,
        isUser: false,
        timestamp: new Date(),
        suggestedActions: response.suggestedActions
      };
      
      setMessages([botMessage]);
    } catch (error) {
      console.error('Error sending initial greeting:', error);
      const fallbackMessage: ChatBubble = {
        text: "Hello! I'm here to help you with your Home Depot returns. How can I assist you today?",
        isUser: false,
        timestamp: new Date(),
        suggestedActions: ["Start a Return", "Track Return", "View Orders"]
      };
      setMessages([fallbackMessage]);
    }
  };

  const sendMessage = async () => {
    if (!inputValue.trim()) return;

    const userMessage: ChatBubble = {
      text: inputValue,
      isUser: true,
      timestamp: new Date()
    };

    setMessages(prev => [...prev, userMessage]);
    setInputValue('');
    setIsLoading(true);

    try {
      const chatMessage: ChatMessage = {
        message: inputValue,
        sessionId: sessionId,
        customerId: localStorage.getItem('customerId') || undefined
      };

      const response = await apiService.sendChatMessage(chatMessage);
      setSessionId(response.sessionId);

      const botMessage: ChatBubble = {
        text: response.response,
        isUser: false,
        timestamp: new Date(),
        suggestedActions: response.suggestedActions
      };

      setMessages(prev => [...prev, botMessage]);
    } catch (error) {
      console.error('Error sending message:', error);
      const errorMessage: ChatBubble = {
        text: "I'm sorry, I'm having trouble processing your request. Please try again.",
        isUser: false,
        timestamp: new Date()
      };
      setMessages(prev => [...prev, errorMessage]);
    } finally {
      setIsLoading(false);
    }
  };

  const handleSuggestedAction = (action: string) => {
    setInputValue(action);
    setTimeout(() => sendMessage(), 100);
  };

  const handleKeyPress = (event: React.KeyboardEvent) => {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      sendMessage();
    }
  };

  const clearChat = () => {
    setMessages([]);
    setSessionId('');
    if (isOpen) {
      sendInitialGreeting();
    }
  };

  const formatTime = (date: Date) => {
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  };

  return (
    <>
      {/* Chat Fab Button */}
      <Fab
        color="primary"
        aria-label="chat"
        onClick={() => setIsOpen(!isOpen)}
        sx={{
          position: 'fixed',
          bottom: 20,
          right: 20,
          zIndex: 1000
        }}
      >
        {isOpen ? <CloseIcon /> : <ChatIcon />}
      </Fab>

      {/* Chat Window */}
      <Collapse in={isOpen}>
        <Paper
          elevation={8}
          sx={{
            position: 'fixed',
            bottom: 90,
            right: 20,
            width: 350,
            height: 500,
            zIndex: 999,
            display: 'flex',
            flexDirection: 'column',
            borderRadius: 2
          }}
        >
          {/* Header */}
          <Box
            sx={{
              p: 2,
              backgroundColor: 'primary.main',
              color: 'white',
              borderTopLeftRadius: 8,
              borderTopRightRadius: 8,
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between'
            }}
          >
            <Box display="flex" alignItems="center" gap={1}>
              <BotIcon />
              <Typography variant="h6">HD Assistant</Typography>
            </Box>
            <IconButton
              size="small"
              onClick={clearChat}
              sx={{ color: 'white' }}
            >
              <Typography variant="caption">Clear</Typography>
            </IconButton>
          </Box>

          {/* Messages */}
          <Box
            sx={{
              flex: 1,
              p: 1,
              overflow: 'auto',
              backgroundColor: '#f5f5f5'
            }}
          >
            {messages.map((message, index) => (
              <Box key={index} sx={{ mb: 1 }}>
                <Box
                  display="flex"
                  justifyContent={message.isUser ? 'flex-end' : 'flex-start'}
                  alignItems="flex-start"
                  gap={1}
                >
                  {!message.isUser && (
                    <Avatar sx={{ width: 32, height: 32, bgcolor: 'primary.main' }}>
                      <BotIcon sx={{ fontSize: 20 }} />
                    </Avatar>
                  )}
                  
                  <Paper
                    elevation={1}
                    sx={{
                      p: 1.5,
                      maxWidth: '70%',
                      backgroundColor: message.isUser ? 'primary.main' : 'white',
                      color: message.isUser ? 'white' : 'text.primary',
                      borderRadius: 2,
                      borderTopLeftRadius: message.isUser ? 2 : 0.5,
                      borderTopRightRadius: message.isUser ? 0.5 : 2
                    }}
                  >
                    <Typography variant="body2">
                      {message.text}
                    </Typography>
                    <Typography
                      variant="caption"
                      sx={{
                        display: 'block',
                        mt: 0.5,
                        opacity: 0.7,
                        fontSize: '0.7rem'
                      }}
                    >
                      {formatTime(message.timestamp)}
                    </Typography>
                  </Paper>

                  {message.isUser && (
                    <Avatar sx={{ width: 32, height: 32, bgcolor: 'grey.400' }}>
                      <PersonIcon sx={{ fontSize: 20 }} />
                    </Avatar>
                  )}
                </Box>

                {/* Suggested Actions */}
                {message.suggestedActions && message.suggestedActions.length > 0 && (
                  <Box
                    sx={{
                      mt: 1,
                      ml: message.isUser ? 0 : 5,
                      display: 'flex',
                      flexWrap: 'wrap',
                      gap: 0.5
                    }}
                  >
                    {message.suggestedActions.map((action, actionIndex) => (
                      <Chip
                        key={actionIndex}
                        label={action}
                        size="small"
                        variant="outlined"
                        clickable
                        onClick={() => handleSuggestedAction(action)}
                        sx={{ fontSize: '0.7rem' }}
                      />
                    ))}
                  </Box>
                )}
              </Box>
            ))}

            {isLoading && (
              <Box display="flex" alignItems="center" gap={1} sx={{ mt: 1 }}>
                <Avatar sx={{ width: 32, height: 32, bgcolor: 'primary.main' }}>
                  <BotIcon sx={{ fontSize: 20 }} />
                </Avatar>
                <Paper
                  elevation={1}
                  sx={{
                    p: 1.5,
                    backgroundColor: 'white',
                    borderRadius: 2,
                    borderTopLeftRadius: 0.5
                  }}
                >
                  <CircularProgress size={16} />
                  <Typography variant="caption" sx={{ ml: 1 }}>
                    Typing...
                  </Typography>
                </Paper>
              </Box>
            )}

            <div ref={messagesEndRef} />
          </Box>

          <Divider />

          {/* Input */}
          <Box sx={{ p: 1, backgroundColor: 'white' }}>
            <Box display="flex" gap={1}>
              <TextField
                fullWidth
                size="small"
                placeholder="Type your message..."
                value={inputValue}
                onChange={(e) => setInputValue(e.target.value)}
                onKeyPress={handleKeyPress}
                disabled={isLoading}
                multiline
                maxRows={3}
              />
              <IconButton
                color="primary"
                onClick={sendMessage}
                disabled={!inputValue.trim() || isLoading}
              >
                <SendIcon />
              </IconButton>
            </Box>
          </Box>
        </Paper>
      </Collapse>
    </>
  );
};

export default Chatbot;
