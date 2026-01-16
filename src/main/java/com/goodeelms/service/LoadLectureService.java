package com.goodeelms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.goodeelms.dao.MajorDAO;
import com.goodeelms.dao.ProfessorDAO;
import com.goodeelms.dao.SelectLectureDAO;
import com.goodeelms.dto.LectureDTO;
import com.goodeelms.dto.MajorDTO;
import com.goodeelms.dto.ProfessorDTO;

public class LoadLectureService {
	MajorDAO majorDAO = MajorDAO.getInstance();
	ProfessorDAO professorDAO = ProfessorDAO.getInstance();
	
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
	
	public List<LectureDTO> getLectureOfStudent(Set<Integer> lectureIdSet){
		SelectLectureDAO dao = new SelectLectureDAO();
		List<LectureDTO> list = dao.getLecturesOfStudent(lectureIdSet);
		
		Set<Integer> majorIdSet = new HashSet<Integer>();
		Set<Integer> professorIdSet = new HashSet<Integer>();
		if(list.size() == 0) return new ArrayList<LectureDTO>();
		for(LectureDTO dto : list) {
			majorIdSet.add(dto.getMajorId());
			professorIdSet.add(dto.getProfessorId());
		}
		
		List<MajorDTO> majorList = majorDAO.getTargetMajor(majorIdSet);
		List<ProfessorDTO> professorList = professorDAO.getTargetProfessor(professorIdSet);
		Map<Integer, String> majorMap = new HashMap<Integer, String>();
		Map<Integer, String> professorMap = new HashMap<Integer, String>();
		
		// 1 : 김아무개
		for(MajorDTO dto : majorList) {
			majorMap.put(dto.getMajorId(), dto.getMajorName());
		}
		for(ProfessorDTO dto : professorList) {
			professorMap.put(dto.getProfessorId(), dto.getProfessorName());
		}
		
		for(LectureDTO dto : list) {
			dto.setMajorName(majorMap.getOrDefault(dto.getMajorId(),"미지정"));
			dto.setProfessorName(professorMap.getOrDefault(dto.getProfessorId(),"미지정"));
		}
		return list;
	}
	
	public boolean validLectureListParams(String cat) {
		if(cat == null || cat.isBlank()) {
			return false;
		}
		return true;
	}
}
