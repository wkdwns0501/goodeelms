package com.goodeelms.service;

import java.util.ArrayList;
import java.util.List;
import com.goodeelms.dao.SelectLectureDAO;
import com.goodeelms.dto.LectureDTO;

public class LoadLectureService {
	
	public List<LectureDTO> getLectureList(String cat, String searchWord, int viewPage, int viewLen, int ...majorIds){
		
		if(!validLectureListParams(cat)) {
			System.out.println("LoadLectureS에서 cat이 null일 수 없음");
			return null;
		}
		SelectLectureDAO dao = new SelectLectureDAO();
		
		List<LectureDTO> list = dao.SelectLectureOnCartDuration(cat, searchWord, viewPage, viewLen, majorIds);
		
		if(list == null) {
			list = new ArrayList<LectureDTO>();
		}
		return list;
	}
	
	public int getLecturesCount(String cat, String searchWord, int ...majorIds) {
		SelectLectureDAO dao = new SelectLectureDAO();
		
		return dao.getLectureListOnCartCount(cat, searchWord, majorIds);
	}
	
	public boolean validLectureListParams(String cat) {
		if(cat == null || cat.isBlank()) {
			return false;
		}
		return true;
	}
}
