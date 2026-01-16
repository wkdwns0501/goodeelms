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

import com.goodeelms.dto.StudentDTO;

/**
 * Servlet Filter implementation class StudentValidFilter
 */
@WebFilter("/student")
public class StudentValidFilter extends HttpFilter implements Filter {
    public StudentValidFilter() {
        super();
    }
    
    public void init(FilterConfig fConfig) throws ServletException {
	}
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	HttpSession session = getSession(request);
    	String valid = ((Integer)session.getAttribute("student_id")).toString();
    	if(valid == null || valid.isBlank()) {
    		HttpServletResponse rsp = (HttpServletResponse)response;
    		rsp.sendRedirect("/main.jsp?error=noDTO");
    		return;
    	}
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
