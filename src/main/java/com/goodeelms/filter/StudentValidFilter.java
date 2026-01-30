package com.goodeelms.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class StudentValidFilter
 */
@WebFilter("/student/*")
public class StudentValidFilter extends HttpFilter implements Filter {
	public StudentValidFilter() {
		super();
	}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();

        if (uri.contains("studentSignUp.jsp") || 
            uri.contains("/student/signup") || 
            uri.contains("/common/login")) {
            
            chain.doFilter(request, response); 
            return;
        }
    	
    	HttpSession session = getSession(request);
    	String valid = null;
    	
    	try { 
    		valid = ((Integer)session.getAttribute("student_id")).toString(); 
    	} catch(NullPointerException | NumberFormatException e) { 
    		valid = null; 
    	}
    	
    	if(valid == null || valid.isBlank()) {
    		HttpServletResponse rsp = (HttpServletResponse)response;
    		rsp.sendRedirect("/main.jsp?error=noDTO");
    		return;
    	}
    	
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
