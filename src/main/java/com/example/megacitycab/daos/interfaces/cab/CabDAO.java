package com.example.megacitycab.daos.interfaces.cab;

import com.example.megacitycab.daos.GenericDAO;
import com.example.megacitycab.models.Cab.Cab;

import java.util.List;

public interface CabDAO extends GenericDAO<Cab> {
    boolean checkExist (String registrationNumber);
    List<Cab> getCabsByCabType (int cabTypeId);
}

