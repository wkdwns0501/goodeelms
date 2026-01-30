package com.goodeelms.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.goodeelms.dto.AcademicCalendarDTO;
import com.goodeelms.listener.LMSScheduleListener;
import com.goodeelms.service.AcademicCalendarService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/setCalendar/*")
public class CalendarController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = requestURI.substring(contextPath.length());
		
		if(command.equals("/admin/setCalendar")) {
		    Map<String, ZonedDateTime> map = LMSScheduleListener.getEventTimeMap();
		    Map<String, String> formattedMap = new HashMap<>();
		    
		    if (map != null && !map.isEmpty()) {
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		        
		        map.forEach((key, value) -> {
		            if (value != null) {
		                String displayDate;
		                
		                // 종료일은 화면에 보여줄 때만 다시 -1일 처리
		                if (key.endsWith("_end")) {
		                    displayDate = value.minusDays(1).format(formatter);
		                } else {
		                    displayDate = value.format(formatter);
		                }
		                
		                formattedMap.put(key, displayDate);
		                
		                // [디버그 콘솔] DB에 저장된 실제 값과 화면에 보낼 값을 비교해볼 수 있습니다.
		                System.out.println("DEBUG: [Key] " + key + " | [DB 실제값] " + value.format(formatter) + " | [화면 표시값] " + displayDate);
		            }
		        });
		    } else {
		        System.out.println("DEBUG: map이 null이거나 비어있습니다!");
		    }

		    request.setAttribute("schedule", formattedMap);
		    request.getRequestDispatcher("/WEB-INF/views/admin/setCalendar.jsp").forward(request, response);
		}
		  
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath(); 
		String command = requestURI.substring(contextPath.length());
		
		// 학생 학적 상태 변경 하기
		if(command.equals("/admin/setCalendar/update")) {
			int adminId = (Integer)session.getAttribute("admin_id");
			
			// 2. 파라미터 수집 및 서비스 호출
		    Enumeration<String> params = request.getParameterNames();
		    AcademicCalendarService service = AcademicCalendarService.getInstance();
		    
		    try {
		        while (params.hasMoreElements()) {
		            String paramName = params.nextElement(); 
		            String dateValue = request.getParameter(paramName);
		            
		            if (dateValue != null && !dateValue.trim().isEmpty()) {
		                AcademicCalendarDTO dto = new AcademicCalendarDTO();
		                dto.setAcademicEventName(paramName);
		                
		                // [핵심 로직] 종료일(_end)인 경우 하루를 더함
		                if (paramName.endsWith("_end")) {
		                    LocalDate originalDate = LocalDate.parse(dateValue);
		                    LocalDate plusOneDate = originalDate.plusDays(1);
		                    dto.setAcademicEventDate(plusOneDate.toString()); // 2025-01-15 -> 2025-01-16
		                } else {
		                    dto.setAcademicEventDate(dateValue);
		                }
		                
		                // 3. DB Update (Update or Insert)
		                service.updateCalendar(dto, adminId);
		            }
		        }
		        
		        Map<String, String> latestMap = service.getCalendarMap(); 
		        LMSScheduleListener.refreshSchedule(latestMap);
		        
		        // 5. 완료 후 리다이렉트
		        response.sendRedirect(request.getContextPath() + "/admin/setCalendar?status=success");
		        
		    } catch (Exception e) {
		        e.printStackTrace();
		        response.sendRedirect(request.getContextPath() + "/admin/setCalendar?error=updateFailed");
		    }
	}

}
	
}
