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
import com.goodeelms.filter.XssFilter;
import com.goodeelms.service.BoardService;

@WebServlet("/common/board/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath(); 
		String command = requestURI.substring(contextPath.length());
		
		// 첫 진입 or 그냥 목록으로 올때
		if(command.equals("/common/board/list"))  {
			
			String keyword = request.getParameter("searchKeyword");
			if(keyword == null) keyword = "";
			
			// 페이지 번호
			String pageNumStr = request.getParameter("pageNum");
			int pageNum = (pageNumStr == null) ? 1 : Integer.parseInt(pageNumStr);
			
			// 한페이지 보여줄 게시글 수
			int pageSize = 12;
			// 해당 페이지의 첫번째 게시글의 인덱스(?)
			int startRow = (pageNum - 1) * pageSize;
			
			BoardService bs = new BoardService();
			int totalCount = bs.getBoardCount(keyword);
			ArrayList<BoardDTO> boardList = bs.getBoardList(keyword, startRow, pageSize);
			
			int totalPage = (int) Math.ceil((double) totalCount / pageSize);
			
			// 페이지 블록 설정 (예: 한 번에 10개씩 번호 보여주기)
			int pageBlock = 10; 
			int startPage = ((pageNum - 1) / pageBlock) * pageBlock + 1;
			int endPage = startPage + pageBlock - 1;

			// 마지막 페이지가 전체 페이지보다 크면 조절
			if (endPage > totalPage) {
			    endPage = totalPage;
			}

			request.setAttribute("startPage", startPage);
			request.setAttribute("endPage", endPage);
			request.setAttribute("boardList"	, boardList);
			request.setAttribute("totalCount", totalCount);
			request.setAttribute("totalPage", totalPage);
			request.setAttribute("pageNum", pageNum);
			request.setAttribute("keyword", keyword);
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/boardList.jsp");
			rd.forward(request, response);
		}
		
		// 작성 페이지 이동
		if(command.equals("/common/board/admin/write"))  {
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/boardWrite.jsp");
			rd.forward(request, response);
		}
		
		// 해당 게시글 상세보기로 이동
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
		
		// 관리자 : 수정 페이지로 이동
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
		
		// 게시글 등록
		if(command.equals("/common/board/admin/insert")) {
			BoardDTO boardDTO = new BoardDTO();
			
			String boardRawContent = request.getParameter("boardContent");
			String cleanContent = XssFilter.basicXssFilter(boardRawContent);
			
			String boardTitle = request.getParameter("boardTitle");
			boardTitle = boardTitle.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
			
			boardDTO.setBoardTitle(boardTitle);
			boardDTO.setBoardContent(cleanContent);
			boardDTO.setAdminId((Integer)session.getAttribute("admin_id"));
			// "중요(상단고정)" 체크 여부
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
		
		// 게시글 삭제
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
		
		// 게시글 수정
		if(command.equals("/common/board/admin/update")) {
			BoardDTO boardDTO = new BoardDTO();
			boardDTO.setBoardId(Integer.parseInt(request.getParameter("boardId")));
			
			String boardTitle = request.getParameter("boardTitle");
			boardTitle = boardTitle.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
			boardDTO.setBoardTitle(boardTitle);
			
			String boardRawContent = request.getParameter("boardContent");
			String cleanContent = XssFilter.basicXssFilter(boardRawContent);
			boardDTO.setBoardContent(cleanContent);
			
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
