package com.goodeelms.util;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletResponse;

public class AlertUtil {
	
	public static void alertAndRedirect(HttpServletResponse response, String msg, String url) throws IOException {
	    response.setContentType("text/html; charset=UTF-8");
	    PrintWriter out = response.getWriter();
	    out.println("<script>alert('" + msg + "'); location.href='" + url + "';</script>");
	    out.flush();
	}
}
