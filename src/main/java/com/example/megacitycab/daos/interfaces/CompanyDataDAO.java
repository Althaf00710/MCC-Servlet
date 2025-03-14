package com.example.megacitycab.daos.interfaces;

import com.example.megacitycab.models.CompanyData;

public interface CompanyDataDAO {
    boolean updateData(CompanyData companyData);
    CompanyData getCompanyData();
}



