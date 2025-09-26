package com.example.stage2_svitozar.model;

import java.util.ArrayList;

// Create a concrete customer builder
public class ConcreteCustomerBuilder implements CustomerBuilder {
    private Integer customerID;
    private String name;
    private String email_address;
    private String phoneNumber;
    private ArrayList<StoreModel.Order> activeOrders;


    public void buildCustomerId(Integer customerID) {
        this.customerID = customerID;
    }

    public void buildCustomerName(String customerName) {
        this.name = customerName;
    }

    public void buildCustomerEmail(String customerEmail) {
        this.email_address = customerEmail;
    }

    public void buildCustomerPhoneNumber(String customerPhoneNumber) {
        this.phoneNumber = customerPhoneNumber;
    }

    public StoreModel.Customer buildCustomer() {
        return new StoreModel.Customer(customerID, name, email_address, phoneNumber);
    }
}
