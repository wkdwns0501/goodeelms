package com.goodeelms.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;

import com.goodeelms.dto.BoardDTO;
import com.goodeelms.service.BoardService;

@WebServlet("/common/board/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath(); 
		String command = requestURI.substring(contextPath.length());
		
		if(command.equals("/common/board/list"))  {
			
			String keyword = request.getParameter("searchKeyword");
			if(keyword == null) keyword = "";
			
			String pageNumStr = request.getParameter("pageNum");
			int pageNum = (pageNumStr == null) ? 1 : Integer.parseInt(pageNumStr);
			
			int pageSize = 12;
			int startRow = (pageNum - 1) * pageSize;
			
			BoardService bs = new BoardService();
			int totalCount = bs.getBoardCount(keyword);
			ArrayList<BoardDTO> boardList = bs.getBoardList(keyword, startRow, pageSize);
			
			int totalPage = (int) Math.ceil((double) totalCount / pageSize);
			
			request.setAttribute("boardList"	, boardList);
			request.setAttribute("totalCount", totalCount);
			request.setAttribute("totalPage", totalPage);
			request.setAttribute("pageNum", pageNum);
			request.setAttribute("keyword", keyword);
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/boardList.jsp");
			rd.forward(request, response);
		}
		
		if(command.equals("/common/board/admin/write"))  {
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/boardWrite.jsp");
			rd.forward(request, response);
		}
		
		if(command.equals("/common/board/detail")) {
			String idStr = request.getParameter("id");
			if(idStr != null) {
				int boardId = Integer.parseInt(idStr);
				
				BoardService bs = new BoardService();
				BoardDTO board = bs.getBoardDetail(boardId);
				
				request.setAttribute("board", board);
				request.getRequestDispatcher("/WEB-INF/views/board/boardDetail.jsp").forward(request, response);
			}
		}
		
		if(command.equals("/common/board/admin/edit")) {
			int boardId = Integer.parseInt(request.getParameter("id"));
			
			BoardService bs = new BoardService();
			BoardDTO board = bs.getBoardDetail(boardId);
			
			request.setAttribute("board", board);
			request.getRequestDispatcher("/WEB-INF/views/board/boardUpdate.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath(); 
		String command = requestURI.substring(contextPath.length());
		
		
		if(command.equals("/common/board/admin/insert")) {
			BoardDTO boardDTO = new BoardDTO();
			boardDTO.setBoardTitle(request.getParameter("boardTitle"));
			boardDTO.setBoardContent(request.getParameter("boardContent"));
			boardDTO.setAdminId((Integer)session.getAttribute("admin_id"));
			String important = request.getParameter("isImportant");
			boardDTO.setIsImportant(important != null ? "Y" : "N");
			
			BoardService bs = new BoardService();
			int insertResult = bs.insertBoard(boardDTO);
			
			if(insertResult > 0) {
				// 글 등록 정상 완료
			response.sendRedirect(request.getContextPath() + "/common/board/list");	
			} else {
				System.out.println("글 등록 오류 발생");
			}
		}
		
		if(command.equals("/common/board/admin/delete")) {
			int boardId = Integer.parseInt(request.getParameter("id"));
			BoardService bs = new BoardService();
			int deleteResult = bs.deleteBoard(boardId);
			
			if (deleteResult > 0) {
				// 글 삭제 완료
			response.sendRedirect(request.getContextPath() + "/common/board/list");		
			} else {
				System.out.println("글 삭제중 오류 발생");
			}
		}
		
		if(command.equals("/common/board/admin/update")) {
			BoardDTO boardDTO = new BoardDTO();
			boardDTO.setBoardId(Integer.parseInt(request.getParameter("boardId")));
			boardDTO.setBoardTitle(request.getParameter("boardTitle"));
			boardDTO.setBoardContent(request.getParameter("boardContent"));
			boardDTO.setAdminId((Integer)session.getAttribute("admin_id"));
			String important = request.getParameter("isImportant");
			boardDTO.setIsImportant(important != null ? "Y" : "N"); 
			
			BoardService bs = new BoardService();
			int updateResult = bs.updateBoard(boardDTO);
			
			if (updateResult > 0) {
				// 글 삭제 완료
			response.sendRedirect(request.getContextPath() + "/common/board/list");		
			} else {
				System.out.println("글 수정중 오류 발생");
			}
		}
	}

}
