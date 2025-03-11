package com.example.megacitycab.daos.interfaces.cab;

import com.example.megacitycab.daos.GenericDAO;
import com.example.megacitycab.models.Cab.CabBrand;

public interface CabBrandDAO extends GenericDAO<CabBrand> {
    boolean checkExist(String brandName);
}
