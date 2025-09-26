package com.example.stage2_svitozar.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StoreModelCustomerTest { // Testing integrity checks: customer name and email address
    @Test
    void testCreateValidCustomer() { // Testing Normal values
        // Create a valid customer
        StoreModel.Customer customer = new StoreModel.Customer(1, "Alice", "alice@gmail.com", "0830847564");
        // Check if the customer with valid name, email, phone number is created successfully
        assertEquals("Alice", customer.getCustomerName());
        assertEquals("alice@gmail.com", customer.getEmail_address());
        assertEquals("0830847564", customer.getPhoneNumber());
    }

    @Test
    void testInvalidCustomerBlank() { // Testing Abnormal values
        // Test if an exception is thrown when a customer is created with an invalid (blank) name
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new StoreModel.Customer(1, "", "alice@gmail.com", "0830847564");
        });
        assertEquals("Enter your name, email address and phone number.", exception.getMessage());

        // Test if an exception is thrown when a customer is created with an invalid (blank) email
        exception = assertThrows(IllegalArgumentException.class, () -> {
            new StoreModel.Customer(1, "Alice", "", "0830847564");
        });
        assertEquals("Enter your name, email address and phone number.", exception.getMessage());

        // Test if an exception is thrown when a customer is created with an invalid (blank) phone number
        exception = assertThrows(IllegalArgumentException.class, () -> {
            new StoreModel.Customer(1, "Alice", "alice@gmail.com", "");
        });
        assertEquals("Enter your name, email address and phone number.", exception.getMessage());
    }

    void testInvalidCustomerNull() { // Testing Abnormal values and Edge values. Files might contain missing fields.
        // Test if an exception is thrown when a customer is created with a null name
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new StoreModel.Customer(null, "", "alice@gmail.com", "0830847564" );
                });
        assertEquals("Enter your name, email address and phone number.", exception.getMessage());

        // Test if an exception is thrown when a customer is created with a null email
        exception = assertThrows(IllegalArgumentException.class, () -> {
            new StoreModel.Customer(1,"Alice", null, "0830847564");
        });
        assertEquals("Enter your name, email address and phone number.", exception.getMessage());

        // Test if an exception is thrown when a customer is created with a null phone number
        exception = assertThrows(IllegalArgumentException.class, () -> {
            new StoreModel.Customer(1, "Alice", "alice@gmail.com", null);
        });
        assertEquals("Enter your name, email address and phone number.", exception.getMessage());
    }
}
