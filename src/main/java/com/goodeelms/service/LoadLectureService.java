package com.goodeelms.service;

import java.util.ArrayList;
import java.util.List;
import com.goodeelms.dao.SelectLectureDAO;
import com.goodeelms.dto.LectureDTO;

public class LoadLectureService {
	
	public List<LectureDTO> getLectureList(String cat, int ...majorIds){
		
		if(cat == null || cat.isBlank()) {
			System.out.println("LoadLectureS에서 cat이 null일 수 없음");
			return null;
		}
		SelectLectureDAO dao = new SelectLectureDAO();
		
		List<LectureDTO> list = dao.SelectLectureOnCartDuration(cat, majorIds);
		
		if(list == null) {
			list = new ArrayList<LectureDTO>();
		}
		return list;
	}
}
