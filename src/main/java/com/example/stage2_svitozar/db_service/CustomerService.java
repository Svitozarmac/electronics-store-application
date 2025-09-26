package com.example.stage2_svitozar.db_service;

import com.example.stage2_svitozar.dao_interface_implementations.CustomerDAO;
import com.example.stage2_svitozar.dao_interface_implementations.CustomerDAOImpl;
import com.example.stage2_svitozar.model.StoreModel;

import java.sql.SQLException;
import java.util.ArrayList;

// CustomerService class connects controller with DAO layer
public class CustomerService {
    private static CustomerDAO customerDAO = new CustomerDAOImpl();

    public static ArrayList<StoreModel.Customer> loadCustomers() throws SQLException, ClassNotFoundException{
        return customerDAO.getAllCustomers_db();
    }
    public static int saveCustomers() throws SQLException, ClassNotFoundException {
            return customerDAO.addCustomers_to_db();
    }

}
