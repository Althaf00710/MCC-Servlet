package com.example.megacitycab.daos.interfaces.cab;

import com.example.megacitycab.daos.GenericDAO;
import com.example.megacitycab.models.Cab.CabAssign;

import java.util.List;

public interface CabAssignDAO{
    boolean addCabAssign(CabAssign cabAssign);
    boolean cancelCabAssign(int id);
    List<CabAssign> getActiveCabAssigns();
}
