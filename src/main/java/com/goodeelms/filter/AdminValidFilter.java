package com.goodeelms.filter;

import java.io.IOException;

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

/**
 * Servlet Filter implementation class StudentValidFilter
 */
@WebFilter(urlPatterns = {
	    "/admin/*",               // 기존 관리자 페이지들
	    "/common/board/admin/*"   // [추가] 게시판 관리 기능 주소
	})
public class AdminValidFilter extends HttpFilter implements Filter {
    public AdminValidFilter() {
        super();
    }
    
    public void init(FilterConfig fConfig) throws ServletException {
	}
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	HttpSession session = getSession(request);
    	String valid = null;
    	try {
    		valid = ((Integer)session.getAttribute("admin_id")).toString();
    	}
    	catch(NullPointerException e) {
    		valid = null;
    	}
    	catch(NumberFormatException e) {
    		valid = null;
    	}
    	if(valid == null || valid.isBlank()) {
    		HttpServletResponse rsp = (HttpServletResponse)response;
    		rsp.sendRedirect("/main.jsp?error=noDTO");
    		return;
    	}
//    	AdminDTO valid = (AdminDTO)session.getAttribute("adminDTO");
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
