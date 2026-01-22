package com.goodeelms.service;

import java.util.ArrayList;

import com.goodeelms.dao.BoardDAO;
import com.goodeelms.dto.BoardDTO;

public class BoardService {
	public int insertBoard(BoardDTO boardDTO) {
		BoardDAO boardDAO = BoardDAO.getInstance();
		return boardDAO.insertBoard(boardDTO);
	}
	public int getBoardCount(String keyword) {
		BoardDAO boardDAO = BoardDAO.getInstance();
		return boardDAO.getBoardCount(keyword);
	}
	public ArrayList<BoardDTO> getBoardList(String keyword, int startRow, int pageSize){
		BoardDAO boardDAO = BoardDAO.getInstance();
		return boardDAO.getBoardList(keyword, startRow, pageSize);
	}
	public BoardDTO getBoardDetail(int boardId) {
		BoardDAO boardDAO = BoardDAO.getInstance();
		boardDAO.updateHit(boardId);
		return boardDAO.getBoardDetail(boardId);
	}
	public int deleteBoard(int boardId) {
		BoardDAO boardDAO = BoardDAO.getInstance();
		return boardDAO.deleteBoard(boardId);
	}
	public int updateBoard(BoardDTO boardDTO) {
		BoardDAO boardDAO = BoardDAO.getInstance();
		return boardDAO.updateBoard(boardDTO);
	}
}
