package com.goodeelms.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.goodeelms.dto.ProfessorDTO;
import com.goodeelms.dto.StudentDTO;

/**
 * Servlet Filter implementation class StudentValidFilter
 */
@WebFilter("/professor/*")
public class ProfessorValidFilter extends HttpFilter implements Filter {
    public ProfessorValidFilter() {
        super();
    }
    
    public void init(FilterConfig fConfig) throws ServletException {
	}
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	// 0119 임욱 / 교수 회원가입의 경우 필터 제외
    	HttpServletRequest req = (HttpServletRequest) request;
    	if (req.getRequestURI().contains("/professor/signup")) {
            chain.doFilter(request, response);
            return;
    }
    	
    	HttpSession session = getSession(request);
    	String valid = null;
    	try {
    		valid = ((Integer)session.getAttribute("professor_id")).toString();
    	}
    	catch(NullPointerException e) {
    		valid = null;
    	}
    	if(valid == null || valid.isBlank()) {
    		HttpServletResponse rsp = (HttpServletResponse)response;
    		rsp.sendRedirect("/main.jsp?error=noDTO");
    		return;
    	}
//    	ProfessorDTO valid = (ProfessorDTO)session.getAttribute("professorDTO");
//    	if(valid == null) {
//    		HttpServletResponse rsp = (HttpServletResponse)response;
//    		rsp.sendRedirect("/main.jsp?error=noDTO");
//    		return;
//    	}
    	
    	chain.doFilter(request, response);
    }
    
    private HttpSession getSession(ServletRequest request) {
    	HttpServletRequest req = (HttpServletRequest) request;
    	HttpSession session = req.getSession();
    	return session;
    }
    
	public void destroy() {
	}

}
