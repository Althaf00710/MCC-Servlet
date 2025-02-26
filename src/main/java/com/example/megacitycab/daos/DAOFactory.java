package com.example.megacitycab.daos;

import com.example.megacitycab.daos.impl.CustomerDAOImpl;
import com.example.megacitycab.daos.impl.DriverDAOImpl;
import com.example.megacitycab.daos.impl.UserDAOImpl;
import com.example.megacitycab.daos.impl.cab.CabBrandDAOImpl;
import com.example.megacitycab.daos.impl.cab.CabDAOImpl;
import com.example.megacitycab.daos.impl.cab.CabTypeDAOImpl;
import com.example.megacitycab.daos.interfaces.CustomerDAO;
import com.example.megacitycab.daos.interfaces.DriverDAO;
import com.example.megacitycab.daos.interfaces.UserDAO;
import com.example.megacitycab.daos.interfaces.cab.CabBrandDAO;
import com.example.megacitycab.daos.interfaces.cab.CabDAO;
import com.example.megacitycab.daos.interfaces.cab.CabTypeDAO;

public class DAOFactory {
    public static DriverDAO getDriverDAO() {
        return new DriverDAOImpl();
    }

    public static UserDAO getUserDAO() {
        return new UserDAOImpl();
    }

    public static CustomerDAO getCustomerDAO() { return new CustomerDAOImpl(); }

    public static CabTypeDAO getCabTypeDAO() { return new CabTypeDAOImpl(); }

    public static CabBrandDAO getCabBrandDAO() { return new CabBrandDAOImpl(); }

    public static CabDAO getCabDAO() { return new CabDAOImpl(); }
}
