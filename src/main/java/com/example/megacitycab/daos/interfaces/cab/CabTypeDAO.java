package com.example.megacitycab.daos.interfaces.cab;

import com.example.megacitycab.daos.GenericDAO;
import com.example.megacitycab.models.Cab.CabType;

public interface CabTypeDAO extends GenericDAO<CabType> {
    boolean checkExist(String name);
    boolean updateImageUrl(CabType cabType);
}



