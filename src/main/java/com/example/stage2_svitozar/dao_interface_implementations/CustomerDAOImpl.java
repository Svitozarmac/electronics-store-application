package com.example.stage2_svitozar.dao_interface_implementations;

import com.example.stage2_svitozar.db_connection.DatabaseConnection;
import com.example.stage2_svitozar.model.ConcreteCustomerBuilder;
import com.example.stage2_svitozar.model.CustomerBuilder;
import com.example.stage2_svitozar.model.CustomerDirector;
import com.example.stage2_svitozar.model.StoreModel;

import java.sql.*;
import java.util.ArrayList;

// Implement CustomerDAO interface
public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public ArrayList<StoreModel.Customer> getAllCustomers_db() throws SQLException { // Method to Load Customers from DB
        ArrayList<StoreModel.Customer> customers = new ArrayList<>();
        String sql_query = "SELECT customerID, customerName, email, phoneNumber FROM Customer";

        try ( // try-with-resources block
            Connection conn = DatabaseConnection.getInstance().getConnection(); // Connect to the Database
            PreparedStatement stmt = conn.prepareStatement(sql_query)) // Create Statement
         {
            ResultSet rset = stmt.executeQuery(sql_query); // Execute Statement. The ResultSet contains the result of your SQL query

            // Create a Customer object using the Builder Pattern
            CustomerBuilder builder = new ConcreteCustomerBuilder();
            CustomerDirector director = new CustomerDirector(builder);

            // Check if the result set is empty
            if (!rset.isBeforeFirst()) {
                return customers; // Empty list
            }

            while (rset.next()) { // Repeatedly process(iterate) each row and retrieve all required customer info from columns
                Integer customerID = rset.getInt("customerID");
                String customer_name = rset.getString("customerName");
                String email_address = rset.getString("email");
                String phoneNumber = rset.getString("phoneNumber");

                // We apply a database use case, where customer ID is needed by calling the appropriate method in CustomerDirector.
                StoreModel.Customer newCustomer = director.buildCustomerWithID(customerID, customer_name, email_address, phoneNumber);
                customers.add(newCustomer);
            } // Both conn and stmt are automatically closed here
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Throw sqle exception to the calling method
        }

        return customers;
    }

    @Override
    public int addCustomers_to_db() throws SQLException{ // Method to save Customer information to Database
        ArrayList<StoreModel.Customer> customerList = StoreModel.getInstance().getCustomersList();
        int return_result = -1;
        if (customerList.isEmpty()) {
            return return_result; // Return - 1 if customerList is empty, so there is nothing to save
        }

        String sql = "INSERT INTO Customer (customerName, email, phoneNumber) VALUES (?, ?, ?)";

        try ( // try-with-resources block
            Connection conn = DatabaseConnection.getInstance().getConnection(); // Connect to the Database
            PreparedStatement pstmt = conn.prepareStatement(sql)
            ) {
            for (StoreModel.Customer customer: customerList) {
                pstmt.setString(1, customer.getCustomerName());
                pstmt.setString(2, customer.getEmail_address());
                pstmt.setString(3, customer.getPhoneNumber());
                pstmt.addBatch(); // Add this set of values to the batch
            }

            pstmt.executeBatch(); // Execute the batch insert

            return_result = 1; // Return 1 if customer data saved successfully to the database
        }  // Both conn and pstmt are automatically closed here
        catch (SQLException e) {
            e.printStackTrace();
            throw e; // Throw sqle exception to the calling method
        }
        return return_result;
    }
}
