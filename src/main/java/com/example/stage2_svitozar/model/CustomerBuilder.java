package com.example.stage2_svitozar.model;

// Create customer builder interface
public interface CustomerBuilder {
    void buildCustomerId(Integer customerID);
    void buildCustomerName(String customerName);
    void buildCustomerEmail(String customerEmail);
    void buildCustomerPhoneNumber(String customerPhoneNumber);
    StoreModel.Customer buildCustomer();
}
