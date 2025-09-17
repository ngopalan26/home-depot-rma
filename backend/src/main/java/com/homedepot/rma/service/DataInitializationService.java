package com.homedepot.rma.service;

import com.homedepot.rma.enums.OrderStatus;
import com.homedepot.rma.enums.ProductCategory;
import com.homedepot.rma.model.*;
import com.homedepot.rma.repository.CustomerRepository;
import com.homedepot.rma.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class DataInitializationService implements CommandLineRunner {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Override
    public void run(String... args) throws Exception {
        initializeSampleData();
    }
    
    private void initializeSampleData() {
        // Create sample customers
        Customer customer1 = new Customer("CUST001", "John", "Doe", "john.doe@email.com");
        customer1.setPhone("+1-555-0123");
        customer1 = customerRepository.save(customer1);
        
        Customer customer2 = new Customer("CUST002", "Jane", "Smith", "jane.smith@email.com");
        customer2.setPhone("+1-555-0456");
        customer2 = customerRepository.save(customer2);
        
        Customer customer3 = new Customer("CUST003", "Bob", "Johnson", "bob.johnson@email.com");
        customer3.setPhone("+1-555-0789");
        customer3 = customerRepository.save(customer3);
        
        // Create sample orders
        Order order1 = new Order("ORD-2024-001", customer1, new BigDecimal("299.99"), LocalDateTime.now().minusDays(30));
        order1.setStatus(OrderStatus.COMPLETED);
        
        OrderItem item1 = new OrderItem(order1, "PROD001", "DeWalt Cordless Drill", "DW-20V-DRILL", 1, new BigDecimal("199.99"), ProductCategory.TOOLS);
        item1.setProductDescription("20V MAX Cordless Drill/Driver Kit");
        
        OrderItem item2 = new OrderItem(order1, "PROD002", "Screwdriver Set", "SD-32PC-SET", 1, new BigDecimal("49.99"), ProductCategory.TOOLS);
        item2.setProductDescription("32-Piece Screwdriver Set");
        
        OrderItem item3 = new OrderItem(order1, "PROD003", "Safety Glasses", "SG-CLEAR", 2, new BigDecimal("24.99"), ProductCategory.SAFETY);
        item3.setProductDescription("Clear Safety Glasses");
        
        order1.setOrderItems(Arrays.asList(item1, item2, item3));
        order1 = orderRepository.save(order1);
        
        // Order 2 - Has large items (not eligible for self-service return)
        Order order2 = new Order("ORD-2024-002", customer2, new BigDecimal("1299.99"), LocalDateTime.now().minusDays(15));
        order2.setStatus(OrderStatus.COMPLETED);
        
        OrderItem item4 = new OrderItem(order2, "PROD004", "Lawn Mower", "LM-21IN-SELF", 1, new BigDecimal("399.99"), ProductCategory.GARDEN);
        item4.setProductDescription("21-Inch Self-Propelled Lawn Mower");
        item4.setIsLargeItem(true);
        
        OrderItem item5 = new OrderItem(order2, "PROD005", "Garden Hose", "GH-50FT-PRO", 1, new BigDecimal("39.99"), ProductCategory.GARDEN);
        item5.setProductDescription("50-Foot Professional Garden Hose");
        
        order2.setOrderItems(Arrays.asList(item4, item5));
        order2 = orderRepository.save(order2);
        
        // Order 3 - Has hazardous items (not eligible for self-service return)
        Order order3 = new Order("ORD-2024-003", customer3, new BigDecimal("89.99"), LocalDateTime.now().minusDays(7));
        order3.setStatus(OrderStatus.COMPLETED);
        
        OrderItem item6 = new OrderItem(order3, "PROD006", "Paint Thinner", "PT-1GAL", 2, new BigDecimal("29.99"), ProductCategory.PAINT);
        item6.setProductDescription("1-Gallon Paint Thinner");
        item6.setIsHazardous(true);
        
        OrderItem item7 = new OrderItem(order3, "PROD007", "Paint Brush Set", "PB-5PC-SET", 1, new BigDecimal("29.99"), ProductCategory.PAINT);
        item7.setProductDescription("5-Piece Paint Brush Set");
        
        order3.setOrderItems(Arrays.asList(item6, item7));
        order3 = orderRepository.save(order3);
        
        // Order 4 - All items eligible for return
        Order order4 = new Order("ORD-2024-004", customer1, new BigDecimal("159.97"), LocalDateTime.now().minusDays(5));
        order4.setStatus(OrderStatus.COMPLETED);
        
        OrderItem item8 = new OrderItem(order4, "PROD008", "LED Light Bulbs", "LED-A19-4PK", 1, new BigDecimal("19.99"), ProductCategory.LIGHTING);
        item8.setProductDescription("4-Pack A19 LED Light Bulbs");
        
        OrderItem item9 = new OrderItem(order4, "PROD009", "Door Handle", "DH-BRUSHED-NICKEL", 1, new BigDecimal("39.99"), ProductCategory.HARDWARE);
        item9.setProductDescription("Brushed Nickel Door Handle");
        
        OrderItem item10 = new OrderItem(order4, "PROD010", "Electrical Outlet", "EO-GFCI-WHITE", 2, new BigDecimal("49.99"), ProductCategory.ELECTRICAL);
        item10.setProductDescription("GFCI Electrical Outlet");
        
        order4.setOrderItems(Arrays.asList(item8, item9, item10));
        order4 = orderRepository.save(order4);
        
        System.out.println("Sample data initialized successfully!");
    }
}
