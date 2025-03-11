package com.example.megacitycab.daos.interfaces;

import com.example.megacitycab.models.Driver;
import com.example.megacitycab.models.user.User;

import java.util.List;

public interface DriverDAO {
    boolean addDriver(Driver driver);
    Driver getDriverById(int id);
    boolean updateDriver(Driver driver);
    boolean deleteDriver(int id);
    List<Driver> getAllDrivers();
    boolean isEmailTaken(String email);
    boolean updateAvatarUrl(Driver driver);
    boolean updateStatus(int driverId, String status);
    List<Driver> getDriversBySearch(String search);
    List<Driver> getNonAssignedDrivers(int cabId);
}
