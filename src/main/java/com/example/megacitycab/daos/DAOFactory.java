package com.example.megacitycab.daos;

import com.example.megacitycab.daos.impl.CustomerDAOImpl;
import com.example.megacitycab.daos.impl.DriverDAOImpl;
import com.example.megacitycab.daos.impl.UserDAOImpl;
import com.example.megacitycab.daos.interfaces.CustomerDAO;
import com.example.megacitycab.daos.interfaces.DriverDAO;
import com.example.megacitycab.daos.interfaces.UserDAO;

public class DAOFactory {
    public static DriverDAO getDriverDAO() {
        return new DriverDAOImpl();
    }

    public static UserDAO getUserDAO() {
        return new UserDAOImpl();
    }

    public static CustomerDAO getCustomerDAO() {
        return new CustomerDAOImpl();
    }
}
