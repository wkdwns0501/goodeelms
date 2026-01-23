package com.goodeelms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.goodeelms.dto.BoardDTO;
import com.goodeelms.util.DBUtil;

public class BoardDAO {
	private static final BoardDAO instance = new BoardDAO();
	
	private BoardDAO() {
	}
	 
	public static BoardDAO getInstance() {
		return instance;
	}

	public int getBoardCount(String keyword) {
		int total = 0;
		String sql = "SELECT COUNT(*) FROM board WHERE board_title LIKE ? OR board_content LIKE ?";
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, "%" + keyword + "%");
			pstmt.setString(2, "%" + keyword + "%");
			try (ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) total = rs.getInt(1);
			}
		} catch (Exception e) {
			System.out.println("getBoardCount() 예외 발생: " + e);
		} return total;
	}
	
	public ArrayList<BoardDTO> getBoardList(String keyword, int startRow, int pageSize) {
		ArrayList<BoardDTO> list = new ArrayList<BoardDTO>();
		
		String sql = "SELECT * FROM board WHERE board_title LIKE ? OR board_content LIKE ? " +
					 "ORDER BY board_important DESC, board_id DESC LIMIT ?, ?";
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, "%" + keyword + "%");
			pstmt.setString(2, "%" + keyword + "%");
			pstmt.setInt(3, startRow);
			pstmt.setInt(4, pageSize);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) {
					BoardDTO dto = new BoardDTO();
					dto.setBoardId(rs.getInt("board_id"));
					dto.setBoardTitle(rs.getString("board_title"));
					dto.setBoardContent(rs.getString("board_content"));
					dto.setIsImportant(rs.getString("board_important"));
					dto.setBoardRegAt(rs.getObject("board_reg_at", java.time.LocalDateTime.class));
					dto.setHit(rs.getInt("board_hit"));
					list.add(dto);
				}
			}
		} catch (Exception e) {
			System.out.println("getBoardList() 예외 발생: " + e);
		} return list;
	}

	
	
	public int insertBoard(BoardDTO boardDTO) {
		String sql = "INSERT INTO board (board_title, board_content, admin_id, board_important) " +
					 "VALUES (?, ?, ?, ?) ";
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, boardDTO.getBoardTitle());
			pstmt.setString(2, boardDTO.getBoardContent());
			pstmt.setInt(3, boardDTO.getAdminId());
			pstmt.setString(4, boardDTO.getIsImportant());
			return pstmt.executeUpdate();
		} catch (Exception e) {
		  System.out.println("insertBoard() 예외 발생: " + e);
		} return 0;
	}

	public BoardDTO getBoardDetail(int boardId) {
		BoardDTO dto = null;
		String sql = "SELECT * FROM board WHERE board_id = ?";
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, boardId);
			
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					dto = new BoardDTO();
					dto.setBoardId(rs.getInt("board_id"));
					dto.setBoardTitle(rs.getString("board_title"));
					dto.setBoardContent(rs.getString("board_content"));
					dto.setBoardRegAt(rs.getObject("board_reg_at", java.time.LocalDateTime.class));
					dto.setIsImportant(rs.getString("board_important"));
					dto.setHit(rs.getInt("board_hit"));
				}
			}
		} catch (Exception e) {
			System.out.println("getBoardDetail() 예외 발생: " + e);
		} return dto;
	}
	
	// 조회수 증가
	public void updateHit(int boardId) {
		String sql = "UPDATE board SET board_hit = board_hit + 1 WHERE board_id = ?";
		try (Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, boardId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("updateHit() 예외 발생: " + e);
		}
	}

	public int deleteBoard(int boardId) {
		String sql = "DELETE FROM board WHERE board_id = ? ";
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			 pstmt.setInt(1, boardId);
			 return pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("deleteBoard() 예외 발생: " + e);
		} return 0;
	}

	public int updateBoard(BoardDTO boardDTO) {
		String sql = "UPDATE board SET board_title = ?, board_content = ?, admin_id = ?, board_important = ? " +
					 "WHERE board_id = ? ";
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			 pstmt.setString(1, boardDTO.getBoardTitle());
			 pstmt.setString(2, boardDTO.getBoardContent());
			 pstmt.setInt(3, boardDTO.getAdminId());
			 pstmt.setString(4, boardDTO.getIsImportant());
			 pstmt.setInt(5, boardDTO.getBoardId()); 
			 return pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("updateBoard() 예외 발생: " + e);
		} return 0;
	}	
	
	
}
