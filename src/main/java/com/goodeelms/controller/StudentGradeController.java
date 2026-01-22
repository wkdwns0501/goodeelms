package com.goodeelms.controller;

import java.io.IOException;

import com.goodeelms.service.StudentGradeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/student/grade/*")
public class StudentGradeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final StudentGradeService gradeService = StudentGradeService.getInstance();

    private String getPath(HttpServletRequest request) {
        String path = request.getPathInfo();
        return (path == null) ? "" : path;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = getPath(request);
        if ("/list".equals(path) || "/".equals(path) || "".equals(path)) {
            handleList(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/student/grade/list");
    }
    
    // 성적 조회 화면 (목록)
    private void handleList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer studentId = (Integer) request.getSession().getAttribute("student_id");
        if (studentId == null) {
            response.sendRedirect(request.getContextPath() + "/common/login");
            return;
        }
        
        
        
        
        
    }

}
