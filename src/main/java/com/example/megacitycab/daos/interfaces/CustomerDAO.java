package com.example.megacitycab.daos.interfaces;

import com.example.megacitycab.daos.GenericDAO;
import com.example.megacitycab.models.Customer;

import java.util.Map;

public interface CustomerDAO extends GenericDAO<Customer> {
    Map<String, Boolean> checkCustomerExists(String email, String nicNumber, String countryCode, String phoneNumber);
    Customer getCustomerByEmail(String email);
}
