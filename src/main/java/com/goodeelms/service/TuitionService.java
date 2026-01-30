package com.goodeelms.service;

import com.goodeelms.dao.TuitionDAO;
import com.goodeelms.dto.TuitionPaymentDTO;

public class TuitionService {
	private TuitionDAO dao = TuitionDAO.getInstance();

	public TuitionPaymentDTO readTuition(int studentId) {
	    TuitionPaymentDTO dto = dao.getTotalAmountAndData(studentId);
	    
	    if (dto == null || dto.getPaymentDate() == null) {
	        dto = new TuitionPaymentDTO();
	        dto.setStudent_id(studentId);
	        dto.setPaymentStatus("미납");
	    } else {
	        dto.setPaymentStatus(dto.getPaymentAmount() >= 4500000 ? "완납" : "부분납부");
	    }
	    return dto;
	}

	public TuitionPaymentDTO getTuitionPaymentAfterPay(int studentId, int addAmount) {
	    int currentTotal = dao.getTotalAmountByStudentId(studentId);
	    
	    String status = (currentTotal + addAmount >= 4500000) ? "완납" : "부분납부";
	    
	    dao.insertPayment(studentId, addAmount, status); 
	    
	    return readTuition(studentId);
	}

}