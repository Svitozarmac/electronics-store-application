package com.example.stage2_svitozar.controller;

import com.example.stage2_svitozar.db_connection.DatabaseConnection;
import com.example.stage2_svitozar.db_service.CustomerService;
import com.example.stage2_svitozar.model.ConcreteCustomerBuilder;
import com.example.stage2_svitozar.model.CustomerBuilder;
import com.example.stage2_svitozar.model.CustomerDirector;
import com.example.stage2_svitozar.view.StoreView;
import com.example.stage2_svitozar.model.StoreModel;



import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.io.*;
import java.util.*;

public class StoreController {
    private StoreView view;
    private StoreModel model;
    private Image storeIcon;
    public int stackCallCount = 0; // Variable to count recursive calls

    public StoreController(StoreView view_, StoreModel model_) { // constructor with view and model as arguments
        this.view = view_; // use to create a reference to view
        this.model = model_; // use to create a reference to model
    }

    public void setView(StoreView view) {
        this.view = view;
    }


    public Image getStoreIcon() {
        try {
            storeIcon = new Image(new FileInputStream("src/main/smartphone.png"));  // Author of the icon: <a href="https://www.flaticon.com/free-icons/app" title="app icons">App icons created by Freepik - Flaticon</a>
        }
        catch (FileNotFoundException e) {
            System.out.println("Icon file not found: " + e.getMessage());
        }
        return storeIcon;
    }

    // Get background image for Stress Test tab
    public Image getHeapStackTabBackgroundImage() {
        try {
           Image image = new Image(getClass().getResource("/com/example/stage2_svitozar/images/Memory.jpg").toExternalForm()); // This image taken from: <a href="https://www.cyberpowerpc.com/blog/easy-tips-to-free-up-ram-on-your-gaming-pc/"> website, Source: Panda Security
            return image;
        }
        catch(Exception e) {
            System.out.println("Background image not found: " + e.getMessage());
            return null;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    // Customer tab - actions (buttons)
    /////////////////////////////////////////////////////////////////////////////////////////////
    public void addButton () {
        // Get user input
        String name = view.textFieldName.getText().trim();
        String email = view.textFieldEmail.getText().trim();
        String phoneNumber = view.textFieldPhoneNumber.getText().trim();

        try {
            // Create a Customer using the Builder Pattern
            CustomerBuilder builder = new ConcreteCustomerBuilder();
            CustomerDirector director = new CustomerDirector(builder);

            // Apply the form use case, where a customer ID is not needed by calling the appropriate method in CustomerDirector.
            StoreModel.Customer newCustomer = director.buildCustomerWithoutID(name, email, phoneNumber);

            // Add new customer to the customers list
            model.addCustomer(newCustomer);

            // Add customer to CustomerComboBox
            for (StoreModel.Customer customer: model.getCustomersList()) {
                view.customerComboBox.getItems().add((customer.toString()));
            }

            // Display confirmation message
            view.textArea.setText("✅ New customer: " +  newCustomer.getCustomerName() + " added successfully.\n");
        }
        catch (IllegalArgumentException e) {
            // If an exception occurs (invalid input), display the error message
            view.textArea.setText(e.getMessage());
        }
    }

    public void removeButton () {
        // Get customer's name
        String name = view.textFieldName.getText().trim();  // removes spaces before and after name
        if (name.isEmpty()) {
            view.textArea.setText("Please enter a customer name.");
        }
        else {
            // Try to remove the user
            boolean removed = model.removeCustomer(name);
            if (removed) {
                view.textArea.setText("✅ Customer " + name + " removed successfully.\n");
                // Clear the Customer ComboBox
                view.customerComboBox.getItems().clear();
                // Reload all customers from the updated list
                for (StoreModel.Customer customer: model.getCustomersList()) {
                    view.customerComboBox.getItems().add((customer.toString()));
                }
            }
            else {
                view.textArea.setText("Customer " + name + " not found!\n");
            }
        }
    }

    public void listButton () {
        String users_info = "";
        if (!model.getCustomersList().isEmpty()) {
            for (StoreModel.Customer customer: model.getCustomersList()) {
                users_info += customer.toString() + "\n";
            }
        }
        else {
            users_info += "No user(s) found in Electronics Store. ";
        }
        view.textArea.setText(users_info);
    }

    // Load Customer data
    public void loadFile() { // Using serializer
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(view.window);
        if (file != null) {
            try {
                // Reading the object from a file
                FileInputStream file1 = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(file1);

                // Create a temporary list for ComboBox to store the customers loaded from the file
//                ObservableList<String> customerData = FXCollections.observableArrayList();
                while (true) {
                    try {
                        // Clear the current customer list to avoid duplicates
                        model.getCustomersList().clear();
                        // Method for deserialization of object
                        while(true){
                            StoreModel.Customer customer = (StoreModel.Customer)in.readObject();
                            model.addCustomer(customer); // Add retrieved customer to the customers list
                        }
                    }
                    catch (EOFException e) {
                        break; // End of file reached
                    }
                }
                in.close();
                file1.close();

                // Pass loaded customer data into CustomerComboBox
                for (StoreModel.Customer customer: model.getCustomersList()) {
                    view.customerComboBox.getItems().add((customer.toString()));
                }

                // Display confirmation message
                view.textArea.setText("✅Customer data loaded successfully. \n");
            }
            catch (IOException | ClassNotFoundException ex) { // multi-catching (catching multiple exception types in a single catch block)
                view.textArea.setText("Error loading file: " + ex.getMessage());
            }
        }
    }

//    public void loadFile() {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Open File");
//        File file = fileChooser.showOpenDialog(view.window);
//        if (file != null) {
//            try {
//                BufferedReader reader = new BufferedReader(new FileReader(file));
//                StringBuilder content = new StringBuilder();
//                String line;
//
//                // Create a temporary list for ComboBox to store the customers loaded from the file
//                ObservableList<String> customerData = FXCollections.observableArrayList();
//                while ((line = reader.readLine()) != null) {
//                    content.append(line).append("\n");
//                    // Add customer to customers list
//                    String [] user_data = line.split(" - ");
//                    if (user_data.length == 2) {
//                        StoreModel.Customer newCustomer = new StoreModel.Customer(user_data[0], user_data[1]);
//                        model.getCustomersList().add(newCustomer); // Add retrieved customer to the customers list
//                        customerData.add(newCustomer.toString()); // Add retrieved customer to the ComboBox temporary list
//                    }
//                }
//                // Pass loaded customer data into CustomerComboBox
//                view.customerComboBox.getItems().addAll(customerData);
//                // Display confirmation message
//                view.textArea.setText("Opened: " + file.getName());
//                view.textArea.appendText("\n✅ User data loaded successfully. \n");
//
//                reader.close();
//            }
//            catch (IOException e) {
//                view.textArea.setText("Error opening file" + e.getMessage());
//            }
//        }
//    }

    // Save Customer data
    public void saveToFile() { // Using Serializer
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save to File");
        File file = fileChooser.showSaveDialog(view.window);

        if (file != null) {
            try {
                // Saving of object in a file
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);

                for (StoreModel.Customer customer: model.getCustomersList()) {
                    out.writeObject(customer);
                }

                out.close();
                fileOutputStream.close();

                // Display a success message
                view.textArea.setText("Data saved successfully.");
            }
            catch (IOException ex) {
                view.textArea.setText("Error saving to file: " + ex.getMessage());
            }
        }
    }

//    public void saveToFile() {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Save to File");
//        File file = fileChooser.showSaveDialog(view.window);
//        if (file != null) {
//            // We try to save the content
//            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))){ // Enable append
//                for (StoreModel.Customer customer: model.getCustomersList()) {
//                    writer.write(customer.toString());
//                }
//            } catch (IOException e) {
//                view.textArea.setText("Error saving to file: " + e.getMessage());
//            }
//        }
//    }

    public void exitApplication() {
        view.alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to save customer data before exiting?", ButtonType.YES, ButtonType.NO);
        view.alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                saveToFile();
            }
            view.window.close(); // Close the window in both cases
        });
    }
    /////////////////////////////////////////////////////////////////////////////////////////////
    // Item tab - actions (buttons)
    /////////////////////////////////////////////////////////////////////////////////////////////
    public void addButton2 () {
        // Get user input for item
        String itemType = view.textFieldItemType.getText().trim();
        String brand = view.textFieldBrand_Model.getText().trim();
        String price_input = view.textFieldPrice.getText().trim();

        try {
            // Check the input from user for blank fields before passing them to the constructor
            if (itemType.isEmpty() || brand.isEmpty() || price_input.isEmpty()) {
                throw new IllegalArgumentException("Enter type, brand and price of the item.");
            }

            Double price = Double.parseDouble(price_input); // Convert price_input from String to Double
            // Check the input for price to be a positive value
            if (price <= 0) {
                throw new IllegalArgumentException("Enter a valid positive price.");
            }

            StoreModel.Item newItem = new StoreModel.Item (itemType, brand, price);

            // Add new item to the list
            model.addItem(newItem);

            // Add item to ItemComboBox
            view.itemComboBox.getItems().add(newItem.toString());
            // Display confirmation message
            view.textAreaItem.setText(" New item: " + newItem + " added successfully.\n");
        }
        catch (NumberFormatException e) {
            // Handle invalid number format
            view.textAreaItem.setText("Please enter a valid price.");
        }
        catch (IllegalArgumentException ex) {
            view.textAreaItem.setText(ex.getMessage());
        }


    }

    public void removeButton2 () {
        // Get item type and brand_model
//        String itemType = view.textFieldItemType.getText().trim();
        String brand_model = view.textFieldBrand_Model.getText().trim(); // removes spaces before and after name

        if (brand_model.isEmpty()) {
            view.textAreaItem.setText("Please enter brand/model of the item in order to be deleted.");
        }
        else {
            // Try to remove the item
            boolean removed = model.removeItem(brand_model);
            if (removed) {
                view.textAreaItem.setText("✅ Item: "  + brand_model + " removed successfully.\n");
                // Clear the Item ComboBox
                view.itemComboBox.getItems().clear();
                // Reload all items from the updated items list
                for (StoreModel.Item item: model.getItemsList()) {
                    view.itemComboBox.getItems().add(item.toString());
                }
            }
            else {
                view.textAreaItem.setText("Item "  + brand_model  + " not found!\nPlease try again.\n");
            }
        }
    }

    public void listButton2 () {
        String items_info = "";
        if (!model.getItemsList().isEmpty()) {
            for (StoreModel.Item item: model.getItemsList()) {
                items_info += item.toString() + "\n";
            }
        }
        else {
            items_info += "No item(s) found in Electronics Store. ";
        }
        view.textAreaItem.setText(items_info);
    }

    // Load Item data
    public void loadFile2() { // Using serialization
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(view.window);

        if (file != null) {
            try {
                FileInputStream file1 = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(file1);

                // Clear the current item list to avoid duplicates
                model.getItemsList().clear();

                while (true) {
                    try {
                        // Method for deserialization of object
                        StoreModel.Item item = (StoreModel.Item)in.readObject();
                        model.addItem(item);
                    }
                    catch (EOFException e) {
                        break; // End of file reached
                    }
                }
                in.close();
                file1.close();

                // Pass loaded item data into ItemComboBox
                for (StoreModel.Item item: model.getItemsList()) {
                    view.itemComboBox.getItems().add((item.toString()));
                }

                // Display confirmation message
                view.textAreaItem.setText("✅ Item data loaded successfully.\n");
            }
            catch (IOException | ClassNotFoundException ex) {
                view.textAreaItem.setText("Error loading file: " + ex.getMessage());
            }
        }

    }
//    public void loadFile2() { // Load button
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Open File");
//        File file = fileChooser.showOpenDialog(view.window);
//
//        if (file != null) {
//            try {
//                BufferedReader reader = new BufferedReader(new FileReader(file));
//                    StringBuilder content = new StringBuilder();
//                    String line;
//
//                    // Create a temporary list for ComboBox to store the loaded items
//                    ObservableList<String> itemData = FXCollections.observableArrayList();
//
//                    while ((line = reader.readLine()) != null) {
//                        content.append(line).append("\n");
//                        // Add customer to customers list
//                        String [] item_data = line.split("\\|");
//                        if (item_data.length == 3) {
//                            try { // Handle the case if the price is not a numerical value
//                                double price = Double.parseDouble(item_data[2].replace("€",""));
//                                StoreModel.Item newItem = new StoreModel.Item (item_data[0], item_data[1], price);
//                                model.getItemsList().add(newItem);
//                                // Automatically add item data to the Item ComboBox
//                                itemData.add(newItem.toString());
//                            }
//                            catch (NumberFormatException e) {
//                                view.textAreaItem.setText("Invalid price format: " + item_data[2]);
//                                return; // Stop further processing if error occurs
//                            }
//                        }
//                    }
//                    // Update ComboBox
//                    view.itemComboBox.getItems().addAll(itemData);
//                    // Display confirmation message
//                    view.textAreaItem.setText("Opened: " + file.getName());
//                    view.textAreaItem.appendText("\n✅ Item data loaded successfully.\n");
//
//                    reader.close();
//            }
//            catch (IOException e) {
//                view.textAreaItem.setText("Error opening file" + e.getMessage());
//            }
//        }
//
//    }

    // Save Item data to file
    public void saveToFile2() { // Using Serializer
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save to File");
        File file = fileChooser.showSaveDialog(view.window);
        if (file != null) {
            try {
                // Saving of object in a file
                FileOutputStream file1 = new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(file1);

                // Method for serialization of object
                for (StoreModel.Item item: model.getItemsList()) {
                    out.writeObject(item);
                }

                out.close();
                file1.close();

                // Display a success message
                view.textAreaItem.setText("Data saved successfully.");
            }
            catch (IOException ex) {
                view.textAreaItem.setText("Error saving to file: " + ex.getMessage());
            }
        }
    }
//    public void saveToFile2 () {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Save to File");
//        File file = fileChooser.showSaveDialog(view.window);
//        if (file != null) {
//            // We try to save the content
//            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))){ // Enable append
//                for (StoreModel.Item item: model.getItemsList()) {
//                    writer.write(item.toString());
//                }
//            } catch (IOException e) {
//                view.textAreaItem.setText("Error saving to file: " + e.getMessage());
//            }
//        }
//    }

    public void exitApplication2 () {
        view.alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to save item data before exiting?", ButtonType.YES, ButtonType.NO);
        view.alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                saveToFile2();
            }
            view.window.close();

        });
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    // Purchase tab - actions (buttons)
    /////////////////////////////////////////////////////////////////////////////////////////////

    public void placeOrderButtonAction () {
        // Get the selected customer and item from the ComboBox
        String selectedCustomerString = view.customerComboBox.getValue();
        String selectedItemString = view.itemComboBox.getValue();

        // Check if the user has selected the customer and item from ComboBoxes
        if (selectedCustomerString == null || selectedItemString == null) {
            view.textAreaOrder.setText("Please select a customer and product to proceed with an order.");
        }

        StoreModel.Customer selectedCustomer = returnCustomer(selectedCustomerString); // return customer as an object from the customers list
        StoreModel.Item selectedItem = returnItem(selectedItemString); // return item as an object from the items list

        // If the user has not reached the order limit and the item is not ordered
        if ((customerAllowedToMakeOrder(selectedCustomer)) && (!itemIsOrdered(selectedItem))) {
            LocalDateTime currentDateTime = LocalDateTime.now();

            // Add new order
            StoreModel.Order newOrder = new StoreModel.Order(selectedCustomer, selectedItem, currentDateTime, selectedItem.getPrice());
            model.addOrder(newOrder); // Add new order to the order list
            // Add new order to active order customer's list
            selectedCustomer.getActiveOrders().add(newOrder);
            // Set item to be ordered
            selectedItem.setCurrentOrdered(newOrder);

            // Display confirmation message
            String formattedPrice = String.format("%.2f", selectedItem.getPrice());
            view.textAreaOrder.setText("Congratulations! \n" + selectedCustomer.getCustomerName() + ", your order has been successfully placed!\n" +
                    "Your order: \n" + selectedItem.getItem_type() + " " + selectedItem.getBrand_model() + "\n" +
                    "Total price: €" + formattedPrice +  "\n");

        }

        else if (!customerAllowedToMakeOrder(selectedCustomer)) {
            view.textAreaOrder.setText("Sorry, you cannot place more than " + model.NUM_ORDERS +" orders.");
        }
        else if (itemIsOrdered(selectedItem)) {
            view.textAreaOrder.setText("Sorry, this product is currently ordered.");
        }
    }

    // Convert String into Customer object
    public StoreModel.Customer returnCustomer(String customer_) {
        for (StoreModel.Customer customer: model.getCustomersList()) {
            if (customer.toString().equals(customer_))
                return customer;
        }
        return null;
    }

    // Convert String into Item object
    public StoreModel.Item returnItem(String item_) {
        for (StoreModel.Item item: model.getItemsList()) {
            if (item.toString().equals(item_))
                return item;
        }
        return null;
    }

    // Check if the customer exceeds the order limit
    public boolean customerAllowedToMakeOrder (StoreModel.Customer customer) {
        if (customer.getActiveOrders().size() < 3)
            return true; // Allowed
        return false; // Not allowed
    }

    // Check if the item has already been ordered
    public boolean itemIsOrdered (StoreModel.Item item) {
        if (item.getCurrentOrdered() != null)
            return true; // Item is currently ordered
        return false; // Item is not ordered
    }


    // Sort button
    public void sortButton() {
        // Get the selected sort option from the SortComboBox
        String selectedSortOption = view.sortOrdersByComboBox.getValue();

        if (model.getOrderList().isEmpty()) {
            view.textAreaOrder.setText("No orders available.\n");
            return;
        }

        StringBuilder orderOverview = new StringBuilder();
        DateTimeFormatter customDateTimeFormat = DateTimeFormatter.ofPattern("d/M/yyyy"); // Define a custom date-time format using DateTimeFormatter

        if (selectedSortOption.equals("Date Purchased")) { // Sorting the order array by date purchased
            // Define max lengths for dynamic column width
            int maxItemLength = 0;
            int maxCustomerLength = 0;
            int maxDateLength = 0;
            int maxPriceLength = 0;

            // Sorting the array
            model.getOrderList().sort(new OrderDateComparator());

            // Find the maximum lengths of each column
            for (StoreModel.Order order : model.getOrderList()) {
                maxItemLength = Math.max(maxItemLength, (order.getItem().getItem_type() + " " + order.getItem().getBrand_model()).length());
                maxCustomerLength = Math.max(maxCustomerLength, order.getCustomer().getCustomerName().length());
                maxDateLength = Math.max(maxDateLength, order.getOrderDate().format(customDateTimeFormat).length());
                maxPriceLength = Math.max(maxPriceLength, String.format("€%.2f", order.getTotalAmount()).length());
            }

            String header = String.format("%-" + (maxDateLength + 2) + "s %-" + (maxCustomerLength + 2) + "s %-" + (maxItemLength + 2) + "s %-" + (maxPriceLength + 2) + "s\n", "Order date", "Customer", "Item", "Total Cost");
            String divider = "-".repeat(header.length()) + "\n";

            for (StoreModel.Order order: model.getOrderList()) {
                String formattedDateTime = order.getOrderDate().format(customDateTimeFormat); // Convert the LocalDateTime object into a formatted string with the pattern we defined above
                String formattedCost = String.format("€%.2f", order.getTotalAmount());
                orderOverview.append(String.format("%-" + (maxDateLength + 2) + "s %-" + (maxCustomerLength + 2) + "s %-" + (maxItemLength + 2) + "s %-" + (maxPriceLength + 2) + "s\n",
                        formattedDateTime, order.getCustomer().getCustomerName(),
                        order.getItem().getItem_type() + " " + order.getItem().getBrand_model(), formattedCost));
            }
            view.textAreaOrder.setText(header + divider + orderOverview);
        }
        else if (selectedSortOption.equals("Item Name")){ // Order array is ordered by the product name (in alphabetical order)

            // Define max lengths for dynamic column width
            int maxItemLength = 0;
            int maxCustomerLength = 0;
            int maxDateLength = 0;
            int maxPriceLength = 0;

            // Sorting the array
            Collections.sort(model.getOrderList());

            // Find the maximum lengths of each column
            for (StoreModel.Order order : model.getOrderList()) {
                maxItemLength = Math.max(maxItemLength, (order.getItem().getItem_type() + " " + order.getItem().getBrand_model()).length());
                maxCustomerLength = Math.max(maxCustomerLength, order.getCustomer().getCustomerName().length());
                maxDateLength = Math.max(maxDateLength, order.getOrderDate().format(customDateTimeFormat).length());
                maxPriceLength = Math.max(maxPriceLength, String.format("€%.2f", order.getTotalAmount()).length());
            }

            String header = String.format("%-" + (maxItemLength + 2) + "s %-" + (maxCustomerLength + 2) + "s %-" + (maxDateLength + 2) + "s %-" + (maxPriceLength + 2) + "s\n", "Item", "Customer", "Order date", "Total cost");
            String divider = "-".repeat(header.length()) + "\n";

            for (StoreModel.Order order : model.getOrderList()) {
                String formattedDateTime = order.getOrderDate().format(customDateTimeFormat); // Convert the LocalDateTime object into a formatted string with the pattern we defined above
                String formattedCost = String.format("€%.2f", order.getTotalAmount());
                orderOverview.append(String.format("%-" + (maxItemLength + 2) + "s %-" + (maxCustomerLength + 2) + "s %-" + (maxDateLength + 2) + "s %-" + (maxPriceLength + 2) + "s\n",
                        order.getItem().getItem_type() + " " + order.getItem().getBrand_model(),
                        order.getCustomer().getCustomerName(), formattedDateTime, formattedCost));
            }
            view.textAreaOrder.setText(header + divider + orderOverview);

        }
    }


    public static class OrderDateComparator implements Comparator<StoreModel.Order> {
        @Override
        public int compare(StoreModel.Order order1, StoreModel.Order order2) {
            // Ordered based on the dates purchased by the customer
            return order1.getOrderDate().compareTo(order2.getOrderDate());
        }
    }

    // Select a product and see how many are in stock; how many sold last month.
    public void selectProductReport() {
        String selectedProduct = view.itemComboBox.getValue();

        StoreModel.Item selectedItem = returnItem(selectedProduct); // Convert String into Item object
        int countItem = 0; // Count how many occurrences of this item occurs in a list
        int countOrderedItems = 0; // Count in case if this item is ordered
        int stock = 0;

        // How many are in stock
        for (StoreModel.Item item : model.getItemsList()) { // Count total quantity of selected product
            if (selectedItem.getItem_type().equals(item.getItem_type())) {
                countItem++;
            }
        }
        for (StoreModel.Order order : model.getOrderList()) { // Count how many items of this type are ordered
            if (selectedItem.getItem_type().equals(order.getItem().getItem_type())) {
                    countOrderedItems++;
                }
        }

        stock = countItem - countOrderedItems; // see how many are in stock
        // How many sold last month
        LocalDateTime currentDateTime = LocalDateTime.now();
        int current_month = currentDateTime.getMonthValue(); // Get current month
        int count_last_month_sales = 0;
        int last_month = (current_month == 1) ? 12 : current_month - 1;  // If the current month is January, the last month becomes 12 (December)

        for (StoreModel.Order order: model.getOrderList()) { // Extract the orders from last month
            if ((selectedItem.getItem_type().equals(order.getItem().getItem_type())) && (last_month == order.getOrderDate().getMonthValue()))
                count_last_month_sales ++;
        }

        String item_sales_report = "Item & Sales Report\n" +"-------------------\n"+ "Item: " + selectedItem.getItem_type() + "\n" +
              "In stock: " + stock + "\n" + "Sold last month: " + count_last_month_sales;
        view.textAreaOrder.setText(item_sales_report);
    }

    //  What did customer A purchase last month and their active orders
    public void selectCustomerReport() {
        String selectedCustomer = view.customerComboBox.getValue();
        StoreModel.Customer customer = returnCustomer(selectedCustomer); // Convert String into Customer object

        LocalDateTime currentDateTime = LocalDateTime.now(); // Get current date
        int current_month = currentDateTime.getMonthValue(); // Get current month
        int last_month = (current_month == 1) ? 12 : current_month - 1;  // If the current month is January, the last month becomes 12 (December)
        StringBuilder orders_last_month = new StringBuilder("Your orders for the last month:\n");
        StringBuilder active_current_orders = new StringBuilder("Your active orders:\n");

        boolean hasLastMonthOrders = false;
        boolean hasActiveOrders = false;

        // When app is run, you load the Store data from file, and you need to check if customer has active orders, and if not, check order list and if customer data matches, add this active orders to customer active orders list.
        // Check orders for last month for selected customer
        for (StoreModel.Order order: model.getOrderList()) {
            boolean sameCustomer = order.getCustomer().getCustomerName().equals(customer.getCustomerName())
                    && order.getCustomer().getEmail_address().equals(customer.getEmail_address());
            int orderMonth = order.getOrderDate().getMonthValue();

            if (sameCustomer) {
                // Get active orders
                if (orderMonth == current_month) {
                    if (!customer.getActiveOrders().contains(order)) {
                        customer.getActiveOrders().add(order);
                    }
                    active_current_orders.append(order);
                    hasActiveOrders = true;
                }
                // What did customer purchase last month
                if (orderMonth == last_month) {
                    orders_last_month.append(order);
                    hasLastMonthOrders = true;
                }
            }
        }

        // If no orders were found for last month, inform about it
        if (!hasLastMonthOrders) {
            orders_last_month = new StringBuilder("There are no orders for the last month.");
        }
        // If no orders were found for current month (active orders), inform about it
        if (!hasActiveOrders) {
            active_current_orders = new StringBuilder("There are no active orders.");
        }
        // Display customer order information
        view.textAreaOrder.setText(orders_last_month.toString() + "\n" + active_current_orders.toString());
    }

    // MenuBar Save, Load
    // Save
    public void saveStoreData(){ // Implementing serialization using multithreading
        // Implementing the Runnable interface using a Java Lambda expression
        Runnable saveTask = () -> {
            // Check if the store data is available to save
            if (model.getCustomersList().isEmpty() && model.getItemsList().isEmpty() && model.getOrderList().isEmpty()) {
                // Pop-up failed message
                Platform.runLater(() -> {
                    view.alert = new Alert(Alert.AlertType.WARNING);
                    view.alert.setTitle("No Store Data");
                    view.alert.setHeaderText(null);
                    view.alert.setContentText("There is no store data to save.");
                    view.alert.getButtonTypes().setAll(ButtonType.OK);
                    view.alert.showAndWait();
                });
                return; // Exit thread early
            }

            String filename = "./saveStoreData_file/StoreData.txt";
            File checkFile = new File(filename);
            if (!checkFile.exists()) {
                // File does not exist
                Platform.runLater(() -> {
                    view.alert = new Alert(Alert.AlertType.WARNING);
                    view.alert.setTitle("File Not Found");
                    view.alert.setHeaderText(null);
                    view.alert.setContentText("File not found.");
                    view.alert.getButtonTypes().setAll(ButtonType.OK);
                    view.alert.showAndWait();
                });
                return; // Exit thread early
            }

            try {
                // Saving objects to file (Object Serialisation)
                FileOutputStream file = new FileOutputStream(checkFile);
                ObjectOutputStream out = new ObjectOutputStream(file);

                // Method for serialisation of object
                // We save all store information(objects): customers, items and orders sequentially
                // Write customer objects
                for (StoreModel.Customer customer: model.getCustomersList()) {
                    out.writeObject(customer);
                }
                // Write item objects
                for (StoreModel.Item item: model.getItemsList()) {
                    out.writeObject(item);
                }
                // Write order objects
                for (StoreModel.Order order: model.getOrderList()) {
                    out.writeObject(order);
                }

                // Pop-up success message
                Platform.runLater(() -> { // Ensures UI updates happen on the JavaFX Application Thread
                    view.alert = new Alert(Alert.AlertType.INFORMATION);
                    view.alert.setTitle("Save Complete");
                    view.alert.setHeaderText(null);
                    view.alert.setContentText("✅ Store data saved successfully!");
                    view.alert.getButtonTypes().setAll(ButtonType.OK);
                    view.alert.showAndWait();
                });

                out.close();
                file.close();
            }
            catch (IOException ex) {
                // Pop-up failed message
                Platform.runLater(() -> { // Ensures UI updates happen on the JavaFX Application Thread
                            view.alert = new Alert(Alert.AlertType.ERROR);
                            view.alert.setTitle("Failed to save data");
                            view.alert.setContentText("Error saving to file:\n" + ex.getMessage());
                            view.alert.getButtonTypes().setAll(ButtonType.OK);
                            view.alert.showAndWait();
                });
            }
        };
        // Run this task in the background
        Thread saveData = new Thread(saveTask); // Starting a saveData thread with a Runnable
        saveData.start(); // Run saveTask in a separate thread
    }

    // Load
    public void loadStoreData(){ // Implementing deserialization using multithreading
        // Implementing the Runnable interface using a Java Lambda expression
        Runnable loadTask = () -> {
            String filename = "./saveStoreData_file/StoreData.txt";
            File checkFile = new File(filename);
            // Check if file exists or is empty
            if (!checkFile.exists() || checkFile.length() == 0) {
                // File is empty or does not exist
                Platform.runLater(() -> {
                    view.alert = new Alert(Alert.AlertType.WARNING);
                    view.alert.setTitle("Empty File");
                    view.alert.setHeaderText(null);
                    view.alert.setContentText("No data to load. The file is empty.");
                    view.alert.getButtonTypes().setAll(ButtonType.OK);
                    view.alert.showAndWait();
                });
                return; // Exit early
            }

            try {
                // Reading objects from a file (Object Deserialization)
                FileInputStream file = new FileInputStream(checkFile);
                ObjectInputStream in = new ObjectInputStream(file);

                // Create a temporary list for ComboBox to store the loaded customers
                ObservableList<String> customerData = FXCollections.observableArrayList();
                // Create a temporary list for ComboBox to store the loaded items
                ObservableList<String> itemData = FXCollections.observableArrayList();

                // Clear the current customer list to avoid duplicates
                model.getCustomersList().clear();
                // Clear the current item list to avoid duplicates
                model.getItemsList().clear();
                // Clear the current order list to avoid duplicates
                model.getOrderList().clear();

                while(true) {
                    try {
                        // Method for deserialization of object
                        Object obj = in.readObject(); // We check objects from a file to match: customer, item and order object

                        // Read customer data
                        if (obj instanceof StoreModel.Customer customer) {
                            model.addCustomer(customer);
                        }
                        // Read item data
                        else if (obj instanceof StoreModel.Item item) {
                            model.addItem(item);
                        }
                        // Read order data
                        else if (obj instanceof StoreModel.Order order) {
                            model.addOrder(order);
                        }
                    }
                    catch(EOFException eof){
                        break; // End of file reached
                    }
                }

                // Pass loaded customer data into CustomerComboBox
                for (StoreModel.Customer customer: model.getCustomersList()) {
                    customerData.add(customer.toString());
                    view.customerComboBox.getItems().setAll(customerData);
                }

                // Pass loaded item data into ItemComboBox
                for (StoreModel.Item item: model.getItemsList()) {
                    itemData.add(item.toString());
                    view.itemComboBox.getItems().setAll(itemData);
                }

                // Pop-up success message
                Platform.runLater(() -> {
                    view.alert = new Alert(Alert.AlertType.INFORMATION);
                    view.alert.setTitle("Load Complete");
                    view.alert.setHeaderText(null);
                    view.alert.setContentText("✅ Store data loaded successfully!");
                    view.alert.getButtonTypes().setAll(ButtonType.OK);
                    view.alert.showAndWait();
                });

                in.close();
                file.close();

            }
            catch (IOException | ClassNotFoundException e) {
                Platform.runLater(() -> {
                    view.alert = new Alert(Alert.AlertType.ERROR);
                    view.alert.setTitle("Failed to Load Data");
                    view.alert.setHeaderText(null);
                    view.alert.setContentText("Error loading from file:\n" + e.getMessage());
                    view.alert.getButtonTypes().setAll(ButtonType.OK);
                    view.alert.showAndWait();
                });
            }
        };

        // Run this task in the background
        Thread loadData = new Thread(loadTask); // Starting a loadData thread with a Runnable
        loadData.start(); // Run loadTask in a separate thread
    }

    // This method is used to test database connection
    public void TestDBConnection() throws SQLException{
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {

        }
        catch (SQLException sqle) {
            throw sqle;
        }
    }

    // Menu Bar
    // Database -> Load Customer Data
    public void loadCustomerDataFromMySQL() {
        try {
            // Call service layer to load customers from DB
            ArrayList<StoreModel.Customer> loaded_customers = CustomerService.loadCustomers();
            if (loaded_customers.isEmpty()) {
                // Display information message
                showAlert(Alert.AlertType.INFORMATION, "Database is Empty", "No customers found in the database.");
            }
            else {
                // Clear current list to avoid duplicates
                model.getCustomersList().clear();

                // Add loaded customers to the Store Model
                for (StoreModel.Customer newCustomer: loaded_customers) {
                    model.addCustomer(newCustomer);
                }

                // Pass the loaded customers to the CustomerComboBox
                view.customerComboBox.getItems().clear();
                for (StoreModel.Customer customer: model.getCustomersList()) {
                    view.customerComboBox.getItems().add(customer.toString());
                }
                // Display confirmation message
                showAlert(Alert.AlertType.CONFIRMATION, "Database Load Complete", "✅ Customers were successfully loaded from the database.");
            }

        }
        catch (SQLException sqle) {
            // Display error message
            showAlert(Alert.AlertType.ERROR, "Failed to Load Data from Database", "Error loading customers from the database: \n" + sqle.getMessage());
        } catch (Exception e) {
            // Inform about unexpected error occurred
            showAlert(Alert.AlertType.ERROR, "Unexpected Error", "Unexpected error occured: \n" + e.getMessage());
        }
    }

    // Database -> Save Customer Data
    public void saveCustomerDataToMySQL() {
        int return_result = -1;
        try{
            return_result = CustomerService.saveCustomers();

           if (return_result == -1) { // There is no customer data in Store
               showAlert(Alert.AlertType.INFORMATION, "No Customer Data in Store", "No customers to insert into the database.");
           }
           else { // return_result = 1, success
               // Display confirmation message
               showAlert(Alert.AlertType.CONFIRMATION, "Saving Customer Data Completed", "✅ Customer data saved to the database successfully.");
           }
        }
        catch (SQLException sqle) {
           // Display error message
           showAlert(Alert.AlertType.ERROR, "Saving Failed", "An error occurred while saving customer data.\n" + sqle.getMessage());
        }
        catch (Exception e) {
            // Inform about unexpected error occurred
            showAlert(Alert.AlertType.ERROR, "Unexpected Error", "Unexpected error occured: \n" + e.getMessage());
        }

    }

    // This method is used to reuse showing alerts
    private void showAlert(Alert.AlertType alertType, String title, String message, ButtonType... buttonTypes) {
        view.alert = new Alert(alertType);
        view.alert.setTitle(title);
        view.alert.setHeaderText(null);
        view.alert.setContentText(message);

        // If button types are provided, set them, else set default OK button
        if (buttonTypes.length > 0) {
            view.alert.getButtonTypes().setAll(buttonTypes);
        }
        else {
            view.alert.getButtonTypes().setAll(ButtonType.OK);
        }

        view.alert.show();
    }

    /**
     * Create an experiment and measure
     * a) How many objects can be created,
     * b) How long (sec) it takes to throw memory/Stack Exceptions
     * c)  When the creation of these objects starts slowing down
     */
    public void heapExperiment() throws OutOfMemoryError {
        // Create an ArrayList to store a large number of objects in Heap memory
        List<long[]> heap_object_array =  new ArrayList<>();
        view.textAreaStressTest.clear();

        long heapMemorySize = Runtime.getRuntime().totalMemory(); // Total heap memory in bytes
        long heapMaxSize = Runtime.getRuntime().maxMemory(); // Max heap memory the JVM will attempt to use (limit)

        // Count the number of created objects
        int count = 0;

        // Track the total heap memory used
        double total_objects_size = 0.0; // Mb

        // Record the current start time of the experiment
        long startTime = System.currentTimeMillis();

        // Variables to track slowdown
        long lastCheckpointTime = startTime;
        final int CHECK_INTERVAL = 100; // Every 100 objects we check if average time needed to create these objects is slowing down
        final double slowdownThreshold = 2.0; // Variable to track if average time increases more than this threshold
        double previousAverageTime = 0.0;

        // Heap Experiment Report
        StringBuilder heap_experiment_report = new StringBuilder();

        // Slowdown record
        StringBuilder slow_down_records = new StringBuilder();

        view.textAreaStressTest.appendText("Heap Experiment Started.\n" +
                "Heap Memory Size: "+ String.format("%.2f", (double)heapMemorySize/(1024 * 1024)) + " MB\n" +
                "Heap Max Memory Size: "+ String.format("%.2f", (double)heapMaxSize/(1024 * 1024)) + " MB\n\n");

        // Until memory is full, create new objects
        while (true) try {
            // Create new object
            long[] new_object = new long[100000]; // The size of this newly created object is ~ 800 Kb
            heap_object_array.add(new_object); // Add newly created object to our heap array
            count++;

            // Add size of this object to total
            total_objects_size += (new_object.length * 8.0) / (1024 * 1024); // 8 bytes per long
            // Every 100 objects, check average time taken
            if (count % CHECK_INTERVAL == 0) {
                long now = System.currentTimeMillis();
                double intervalTime = (now - lastCheckpointTime) / 1000.0; // interval time in seconds (time taken for serving this current 100 objects)
                double averageTimePerObject = intervalTime / CHECK_INTERVAL; // Average time taken to create the object

                // Define slowdown point
                if ((previousAverageTime > 0) && (averageTimePerObject > (previousAverageTime * slowdownThreshold))) {
                    // Record slowdown point
                    slow_down_records.append("⚠️ Slowing detected after: ")
                            .append(count)
                            .append(" objects. Average creation time per object: ")
                            .append(String.format("%.4f", averageTimePerObject))
                            .append(" seconds\n");
                }
                previousAverageTime = averageTimePerObject;
                lastCheckpointTime = now;

            }
        } catch (OutOfMemoryError e) {
            // When OutOfMemoryError is thrown, record the time this happened
            long endTime = System.currentTimeMillis();
            // How long (sec) it takes to throw memory Exception
            double timeTaken = (endTime - startTime) / 1000.0; // seconds

            // Build Heap Report
            heap_experiment_report.append("Heap Memory Limit Reached\n")
                    .append("Heap Memory used to create objects: ").append(String.format("%.2f", total_objects_size)).append(" MB (~")
                    .append(String.format("%.2f", total_objects_size / 1024)).append(" GB).\n")
                    .append("Objects created: ").append(count).append(".\n")
                    .append("Time until OutOfMemoryError: ").append(timeTaken).append(" seconds.\n")
                    .append("Exception: ").append(e.getMessage()).append("\n");

            // Append slowdown records if any
            if (!slow_down_records.isEmpty()) {
                heap_experiment_report.append("Slowdown Observations:\n").append(slow_down_records);
            }

            // Display Heap Report
            view.textAreaStressTest.setText(heap_experiment_report.toString());

            throw e;
        }
    }

    // Stack Experiment
    public void stackExperiment() {
        view.textAreaStressTest.clear();
        // Record the current start time of the experiment
        long startTime = System.currentTimeMillis();
        stackCallCount = 0; // Reset count before starting

        try {
            // Call the method that repeatedly makes method calls to fill up the stack memory
            recursiveCall(startTime);
        }
        // If there is no memory left in the stack for storing method call, JVM will throw StackOverflow error
        catch (StackOverflowError e) {
            long endTime = System.currentTimeMillis();
            double totalTimeTaken = (endTime - startTime) / 1000.0; // How long (seconds) it takes to throw a StackOverflowError

            // Build Stack Experiment Report
            StringBuilder stackExperimentReport  = new StringBuilder("\nStack Overflow Experiment\n")
                    .append("Method calls made: ").append(stackCallCount)
                    .append("\nTime until StackOverflowError: ").append(String.format("%.4f", totalTimeTaken)).append(" seconds.\n")
                    .append("Exception: ").append(e.getMessage());

            // Display Stack Experiment Report
            view.textAreaStressTest.appendText(stackExperimentReport.toString());
        }
    }

    // Recursive method
    public void recursiveCall(long startTime) {
        stackCallCount++;
        if(stackCallCount % 500 == 0) {
            long now = System.currentTimeMillis();
            double time_taken = (now - startTime) / 1000.0; // How long (seconds) it takes to make 500 recursive calls
            view.textAreaStressTest.appendText("→ Recursive calls: " + stackCallCount + " | Time taken: " + String.format("%.4f", time_taken) + " seconds.\n");
        }
        recursiveCall(startTime); // Call the method recursively until a StackOverflowError occurs
    }


    public void exitApplication3 () {
        view.alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to place order before exiting?", ButtonType.YES, ButtonType.NO);
        view.alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                view.alert.close();
                view.textAreaOrder.clear();
            }
            else {
                view.window.close();
            }
        });
    }




}