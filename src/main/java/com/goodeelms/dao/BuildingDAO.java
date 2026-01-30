package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.goodeelms.dto.BuildingDTO;
import com.goodeelms.util.DBUtil;

public class BuildingDAO {
	private static final BuildingDAO instance = new BuildingDAO();
	
    private BuildingDAO() {}
    
    public static BuildingDAO getInstance() { 
    	return instance; 
    }

    public ArrayList<BuildingDTO> findAll() {
        String sql = "SELECT building_id, building_name FROM building ORDER BY building_name ASC";
        
        ArrayList<BuildingDTO> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                BuildingDTO building = new BuildingDTO();
                building.setBuildingId(rs.getInt("building_id"));
                building.setBuildingName(rs.getString("building_name"));
                list.add(building);
            }
        } catch (Exception e) {
            System.out.println("BuildingDAO.findAll 예외: " + e);
        }
        return list;
    }
	    
}
