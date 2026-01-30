package com.goodeelms.service;

import java.util.ArrayList;

import com.goodeelms.dao.BuildingDAO;
import com.goodeelms.dto.BuildingDTO;

public class BuildingService {
    private static final BuildingService instance = new BuildingService();
    
    private final BuildingDAO dao = BuildingDAO.getInstance();

    private BuildingService() {}
    
    public static BuildingService getInstance() { return instance; }

    public ArrayList<BuildingDTO> getAll() {
        return dao.findAll();
    }
}
