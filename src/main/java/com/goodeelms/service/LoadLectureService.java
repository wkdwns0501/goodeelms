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
	
	public List<LectureDTO> getLectureList(String cat, String searchWord, int viewPage, int viewLen, Set<Integer> lectureCodeSet, int ...majorIds){
		
		if(!validLectureListParams(cat)) {
			System.out.println("LoadLectureS에서 cat이 null일 수 없음");
			return null;
		}

		// 동명이인 있을 수 있으니 Set에 Id 담아
		Set<Integer> professorIdSet = getProfessorIds(searchWord);
		
		SelectLectureDAO dao = new SelectLectureDAO();
		List<LectureDTO> list = dao.SelectLectures(cat, searchWord, viewPage, viewLen, lectureCodeSet, professorIdSet, majorIds);
		
		if(list == null || list.size() == 0) {
			return list = new ArrayList<LectureDTO>();
		}
		
		Set<Integer> majorIdSet = new HashSet<Integer>();
		if(list == null || list.size() == 0) return new ArrayList<LectureDTO>();
		for(LectureDTO dto : list) {
			majorIdSet.add(dto.getMajorId());
			// 강의명으로 검색하면 교수 이름들 담아야함
			professorIdSet.add(dto.getProfessorId());
		}
		
		Map<Integer, String> majorMap = getMajorNames(majorIdSet);
		Map<Integer, String> professorMap = getprofessorNames(professorIdSet);
		for(LectureDTO dto : list) {
			dto.setMajorName(majorMap.getOrDefault(dto.getMajorId(),"미지정"));
			dto.setProfessorName(professorMap.getOrDefault(dto.getProfessorId(),"미지정"));
		}
		
		return list;
	}
	
	public int getLecturesCount(String cat, String searchWord, Set<Integer> lectureCodeSet, int ...majorIds) {
		SelectLectureDAO dao = new SelectLectureDAO();
		
		// 동명이인 있을 수 있으니 Set에 Id 담아
		Set<Integer> professorIdSet = getProfessorIds(searchWord);
		return dao.getLectureListOnCartCount(cat, searchWord, lectureCodeSet, professorIdSet, majorIds);
	}
	
	public List<LectureDTO> getLectureOfStudentCart(Set<Integer> lectureIdSet){
		SelectLectureDAO dao = new SelectLectureDAO();
		List<LectureDTO> list = dao.getLecturesOfStudent(lectureIdSet);
		
		Set<Integer> majorIdSet = new HashSet<Integer>();
		Set<Integer> professorIdSet = new HashSet<Integer>();
		if(list == null || list.size() == 0) return new ArrayList<LectureDTO>();
		for(LectureDTO dto : list) {
			majorIdSet.add(dto.getMajorId());
			professorIdSet.add(dto.getProfessorId());
		}
		
		Map<Integer, String> majorMap = getMajorNames(majorIdSet);
		Map<Integer, String> professorMap = getprofessorNames(professorIdSet);
		
		for(LectureDTO dto : list) {
			dto.setMajorName(majorMap.getOrDefault(dto.getMajorId(),"미지정"));
			dto.setProfessorName(professorMap.getOrDefault(dto.getProfessorId(),"미지정"));
		}
		return list;
	}
	
	public Map<Integer,String> getMajorNames(Set<Integer> majorSet){
		List<MajorDTO> majorList = majorDAO.getTargetMajor(majorSet);
		Map<Integer, String> majorMap = new HashMap<Integer, String>();
		
		for(MajorDTO dto : majorList) {
			majorMap.put(dto.getMajorId(), dto.getMajorName());
		}
		
		return majorMap;
	}
	
	public Map<Integer,String> getprofessorNames(Set<Integer> professorSet){
		List<ProfessorDTO> professorList = professorDAO.getTargetProfessor(professorSet);
		Map<Integer, String> professorMap = new HashMap<Integer, String>();
		
		for(ProfessorDTO dto : professorList) {
			professorMap.put(dto.getProfessorId(), dto.getProfessorName());
		}
		
		return professorMap;
	}
	
	public Set<Integer> getProfessorIds(String searchWord){
		List<ProfessorDTO> professorNames = new ArrayList<ProfessorDTO>();
		if(searchWord != null && !searchWord.isBlank()) {
			professorNames = professorDAO.getProfessorIdsFromName(searchWord);
		}
		
		Set<Integer> professorIdSet = new HashSet<Integer>();
		if(professorNames != null && professorNames.size() > 0) {
			for(ProfessorDTO dto : professorNames) {
				int id = dto.getProfessorId();
				if(id > 0) {
					professorIdSet.add(id);
				}
			}
		}
		// new HashSet 해놔서 빈 Set이라도 전달 됨
		return professorIdSet;
	}
	public boolean validLectureListParams(String cat) {
		if(cat == null || cat.isBlank()) {
			return false;
		}
		return true;
	}
	public Set<Integer> getNotAbleReEnrollmentCodes(int studentId){
		SelectLectureDAO dao = new SelectLectureDAO();
		return dao.getNotAbleEnrollmentCodes(studentId);
	}
}
