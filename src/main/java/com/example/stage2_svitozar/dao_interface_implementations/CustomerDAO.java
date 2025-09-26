package com.example.stage2_svitozar.dao_interface_implementations;

import com.example.stage2_svitozar.model.StoreModel;

import java.sql.SQLException;
import java.util.ArrayList;

// Data Access Layer
// Interface defining customer DB operations

public interface CustomerDAO {
    ArrayList<StoreModel.Customer> getAllCustomers_db() throws SQLException; // Load customers from Database
    int addCustomers_to_db() throws SQLException; // Save customers to the Database
}
