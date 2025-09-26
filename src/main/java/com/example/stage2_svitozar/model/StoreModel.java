package com.example.stage2_svitozar.model;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

public class StoreModel { // This represents the data of the store
    // Store data
    private String storeName = "Electronics Store";
    public int NUM_ORDERS = 3;

    // Static member holds only one instance of the StoreModel class
    private static StoreModel single_instance = null;

    // Customer data
    private final ArrayList<Customer> customersList;

    // Item data
    private final ArrayList<Item> itemsList;

    // Purchase data
    private final ArrayList<Order> orderList;


    //////////////////////////////////////////////////////
    // Model Constructor
    //////////////////////////////////////////////////////
    // Private constructor
    private StoreModel() {
        this.customersList = new ArrayList<>();
        this.itemsList = new ArrayList<>();
        this.orderList = new ArrayList<>();
    }

    // Providing Global point of access
    public static StoreModel getInstance() {
        if(single_instance == null) {
            single_instance = new StoreModel();
        }
        return single_instance;
    }

    //////////////////////////////////////////////////////
    // Getters, Setters
    //////////////////////////////////////////////////////
    // Store
    public String getStoreName() {
        return storeName;
    }
    public void setStoreName(String newStoreName) {
        this.storeName = newStoreName;
    }

    // Get Customers list
    public ArrayList<Customer> getCustomersList() {
        return this.customersList;
    }

    // Add a customer to the customer list
    public void addCustomer (Customer newCustomer) {
        this.customersList.add(newCustomer);
    }

    // Remove a Customer from a Store
    public boolean removeCustomer(String name) {
        // Use Iterator (no index shifting issues)
        Iterator<Customer> iterator = customersList.iterator();
        while (iterator.hasNext()) {
            Customer customer = iterator.next();
            if (customer.getCustomerName().equalsIgnoreCase(name)) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }

    // Get Items list
    public ArrayList<Item> getItemsList() {
        return itemsList;
    }

    // Add an Item to the item list
    public void addItem(Item newItem) {
        this.itemsList.add(newItem);
    }

    // Remove an Item from a Store
    public boolean removeItem(String brand_model) {
        // Use Iterator (no index shifting)
        Iterator<Item> iterator = itemsList.iterator();

        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item.getBrand_model().equalsIgnoreCase(brand_model)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    // Get Orders List
    public ArrayList<Order> getOrderList() {
        return orderList;
    }

    // Add new order to the order list
    public void addOrder(Order newOrder) {
        this.orderList.add(newOrder);
    }


    ////////////////////////////////////////////////////////////
    // Inner Customer class
    ////////////////////////////////////////////////////////////
    public static class Customer implements Serializable {
       private Integer customerID;
       private String name;
       private String email_address;
       private String phoneNumber;
       private ArrayList<Order> activeOrders;

       // Customer constructor
       public Customer (Integer customerID, String name_, String email_, String phoneNumber_) {
           if (name_ == null || name_.isBlank() ||
               email_ == null || email_.isBlank() ||
               phoneNumber_ == null || phoneNumber_.isBlank())  {
               throw new IllegalArgumentException("Enter your name, email address and phone number.");
           }
           this.customerID = customerID;
           this.name = name_;
           this.email_address = email_;
           this.phoneNumber = phoneNumber_;
           this.activeOrders = new ArrayList<>(); // The new user has not purchased any item yet.
       }

       // Getters
       public Integer getCustomerID() {
           return customerID;
       }
       public String getCustomerName() {
           return name;
       }
       public String getEmail_address() {
           return email_address;
       }
       public String getPhoneNumber() {
            return phoneNumber;
       }
       // Setters
       public void setCustomerID(Integer customerID_) {
           this.customerID = customerID_;
       }
       public void setCustomerName(String customerName_) {
           this.name = customerName_;
       }
       public void setEmail_address(String email_address_) {
           this.email_address = email_address_;
       }
       public void setPhoneNumber(String phoneNumber_) {
           this.phoneNumber = phoneNumber_;
       }

       public ArrayList<Order> getActiveOrders() {
            return this.activeOrders;
        }

        // toString
       public String toString(){
           String print_info;
           // We append the user info
           print_info = this.name + " - " + this.email_address;
           return print_info;
       }
    }


    ////////////////////////////////////////////////////////////
    // Inner Item class
    ////////////////////////////////////////////////////////////
    public static class Item implements Serializable {
        private String item_type; // Smartphone
        private String brand_model; // iPhone13
        private Double price; // €950
        private Order currentOrdered;

        // Item constructor
        public Item (String item_type, String brand_model, Double price) {
            if (item_type == null || item_type.isBlank() ||
                brand_model == null || brand_model.isBlank()) {
                throw new IllegalArgumentException("Enter type and brand of the item.");
            }

            if (price == null || price <= 0) {
                throw new IllegalArgumentException("Enter a valid positive price.");
            }
            this.item_type = item_type;
            this.brand_model = brand_model;
            this.price = price;
            this.currentOrdered = null; // The item is not ordered
        }
        // Getters
        public String getItem_type() {
            return item_type;
        }
        public String getBrand_model() {
            return brand_model;
        }
        public Double getPrice() {
            return price;
        }

        public Order getCurrentOrdered() {
            return this.currentOrdered;
        }


        // Setter
        public void setCurrentOrdered(Order _newOrder) {
            this.currentOrdered = _newOrder;
        }

        //toString
        public String toString() {
            return  item_type + " | " + brand_model + " | " +  "€" + String.format("%.2f", price);
        }
    }

    ////////////////////////////////////////////////////////////
    // Inner Order class
    ////////////////////////////////////////////////////////////
    public static class Order implements Comparable<Order>, Serializable{
        private final Customer customer;
        private final Item item;
        private final String orderDate; // The date the order was placed.
        private final double totalAmount; // The total price of the order

        // Purchase constructor
        public Order(Customer customer, Item item, LocalDateTime orderDate, double totalAmount) {
            this.customer = customer;
            this.item = item;
            this.orderDate = orderDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            this.totalAmount = totalAmount;
        }

        // Getters
        // Given a concrete order (this), the function returns the date
        public LocalDateTime getOrderDate() {
            return LocalDateTime.parse(orderDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        public double getTotalAmount() {
            return totalAmount;
        }

        public Customer getCustomer() {
            return customer;
        }

        public Item getItem() {
            return item;
        }

        public String toString() {
            DateTimeFormatter customDateTimeFormat = DateTimeFormatter.ofPattern("d/M/yyyy HH:mm"); // Define a custom date-time format using DateTimeFormatter
            LocalDateTime formattedOrderDate = LocalDateTime.parse(orderDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            // Convert the LocalDateTime object into a formatted string with the pattern we defined above
            return this.customer.getCustomerName() + " " + item.getItem_type() + " " + item.getBrand_model() +" " + formattedOrderDate.format(customDateTimeFormat)  + " €" + String.format("%.2f", this.totalAmount) + "\n";
        }

        @Override
        public int compareTo(Order order) { // Order array is ordered by the product name (in alphabetical order)
            int compareItemType = this.item.getItem_type().compareTo(order.item.getItem_type());
            if (compareItemType == 0) { // If item types are the same,
                return this.item.getBrand_model().compareTo(order.item.getBrand_model()); // compare brand names
            }
            return compareItemType;
        }
    }

}
