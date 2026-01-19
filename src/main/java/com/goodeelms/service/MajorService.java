package com.goodeelms.service;

import java.util.List;

import com.goodeelms.dao.MajorDAO;
import com.goodeelms.dto.MajorDTO;

public class MajorService {
	MajorDAO dao = MajorDAO.getInstance();
	
	public List<MajorDTO> getAllMajor() {
	    return dao.findAll();
	}
}
