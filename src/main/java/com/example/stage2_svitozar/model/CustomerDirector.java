package com.example.stage2_svitozar.model;

public class CustomerDirector {  // This class directs builder usage
    private CustomerBuilder customer_builder;

    public CustomerDirector(CustomerBuilder builder){
        this.customer_builder = builder;
    }

    // Create a customer with an ID (database use case)
    public StoreModel.Customer buildCustomerWithID(Integer custID, String name, String email_address, String phoneNumber) {
        customer_builder.buildCustomerId(custID);
        customer_builder.buildCustomerName(name);
        customer_builder.buildCustomerEmail(email_address);
        customer_builder.buildCustomerPhoneNumber(phoneNumber);
        return customer_builder.buildCustomer();
    }

    // Create a customer without an ID (completing form use case)
    public StoreModel.Customer buildCustomerWithoutID(String name, String email_address, String phoneNumber) {
        customer_builder.buildCustomerId(null); // No ID
        customer_builder.buildCustomerName(name);
        customer_builder.buildCustomerEmail(email_address);
        customer_builder.buildCustomerPhoneNumber(phoneNumber);
        return customer_builder.buildCustomer();
    }

}
