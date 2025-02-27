package com.example.megacitycab.daos.impl;

import com.example.megacitycab.daos.interfaces.CompanyDataDAO;
import com.example.megacitycab.models.CompanyData;
import com.example.megacitycab.utils.DbConfig;

public class CompanyDataDAOImpl implements CompanyDataDAO {
    private final DbConfig dbConfig = DbConfig.getInstance();

    @Override
    public boolean updateData(CompanyData companyData) {
        return false;
    }

    @Override
    public CompanyData getCompanyData() {
        return null;
    }
}
