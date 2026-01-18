package com.goodeelms.service;

import com.goodeelms.dao.TuitionDAO;
import com.goodeelms.dto.TuitionPaymentDTO;

public class TuitionService {
	private TuitionDAO dao = TuitionDAO.getInstance();

	public TuitionPaymentDTO readTuition(int studentId) {
	    TuitionPaymentDTO dto = dao.getTuitionByStudentId(studentId);

	    if (dto == null) { // 미납 상태는 즉시 반환
	        dto = new TuitionPaymentDTO();
	        dto.setStudent_id(studentId);
	        dto.setPaymentAmount(0);
	        dto.setPaymentStatus("미납");
	        return dto; 
	    }

	    if (dto.getPaymentAmount() >= 4500000) {
	        dto.setPaymentStatus("완납");
	    } else if (dto.getPaymentAmount() > 0) {
	        dto.setPaymentStatus("부분납부");
	    } else {
	        dto.setPaymentStatus("미납");
	    }

	    return dto;
	}

	public boolean updatePayment(int studentId, int addAmount) {
		TuitionPaymentDTO dto = dao.getTuitionByStudentId(studentId);

		int totalPaid = addAmount;
		if (dto != null) {
			totalPaid += dto.getPaymentAmount();
		}
		String status = (totalPaid >= 4500000) ? "완납" : "부분납부";

		return dao.updatePayment(addAmount, status, studentId);
	}
}