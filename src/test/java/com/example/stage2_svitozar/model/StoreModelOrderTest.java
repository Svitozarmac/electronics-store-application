package com.example.stage2_svitozar.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StoreModelOrderTest { // Test the sorting of the items list
    @Test
    void testSortingByTypeName() { // Test the sorting of items by type name
        StoreModel store = StoreModel.getInstance();


        StoreModel.Customer customer1 = new StoreModel.Customer(1, "William", "willi@gmail.com", "0857685948");
        StoreModel.Customer customer2 = new StoreModel.Customer(2, "John", "john@gmail.com", "0874638201");
        StoreModel.Customer customer3 = new StoreModel.Customer(3, "Alice", "alice@gmail.com", "08544355526");

        StoreModel.Item item1 = new StoreModel.Item("Laptop", "MacBookPro2021", 1200.00 );
        StoreModel.Item item2 = new StoreModel.Item("Headphones", "Sony", 38.99 );
        StoreModel.Item item3 = new StoreModel.Item("Smartphone", "iPhone13", 450.00 );

        LocalDateTime orderDate = LocalDateTime.now();

        StoreModel.Order order1 = new StoreModel.Order(customer1, item1, orderDate, 1200.00);
        StoreModel.Order order2 = new StoreModel.Order(customer2, item2, orderDate, 38.99);
        StoreModel.Order order3 = new StoreModel.Order(customer3, item3, orderDate, 450.00);

        store.addOrder(order1);
        store.addOrder(order2);
        store.addOrder(order3);

        Collections.sort(store.getOrderList());

        ArrayList<StoreModel.Order> orders = store.getOrderList();

        // Assert that the orders are sorted by item type name
        assertEquals("Headphones", orders.get(0).getItem().getItem_type());
        assertEquals("Laptop", orders.get(1).getItem().getItem_type());
        assertEquals("Smartphone", orders.get(2).getItem().getItem_type());
    }
    @Test
    void testSortingByBrandName() { // If the item types are the same, compare brand names
        StoreModel store = StoreModel.getInstance();

        StoreModel.Customer customer1 = new StoreModel.Customer(1, "William", "willi@gmail.com", "0857685948");
        StoreModel.Customer customer2 = new StoreModel.Customer(2, "John", "john@gmail.com", "0874638201");
        StoreModel.Customer customer3 = new StoreModel.Customer(3, "Alice", "alice@gmail.com", "08544355526");

        StoreModel.Item item1 = new StoreModel.Item("Laptop", "MacBookPro2021", 1200.00 );
        StoreModel.Item item2 = new StoreModel.Item("Laptop", "Lenovo", 650.00 );
        StoreModel.Item item3 = new StoreModel.Item("Laptop", "Acer Aspire 5", 799.00 );

        LocalDateTime orderDate = LocalDateTime.now();

        StoreModel.Order order1 = new StoreModel.Order(customer1, item1, orderDate, 1200.00);
        StoreModel.Order order2 = new StoreModel.Order(customer2, item2, orderDate, 650.00);
        StoreModel.Order order3 = new StoreModel.Order(customer3, item3, orderDate, 799.00);

        store.addOrder(order1);
        store.addOrder(order2);
        store.addOrder(order3);

        Collections.sort(store.getOrderList());

        ArrayList<StoreModel.Order> orders = store.getOrderList();

        // Assert that the orders are sorted by item type first, then by brand name
        assertEquals("Acer Aspire 5", orders.get(0).getItem().getBrand_model());
        assertEquals("Lenovo", orders.get(1).getItem().getBrand_model());
        assertEquals("MacBookPro2021", orders.get(2).getItem().getBrand_model());


    }
}
