<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />
<style>
    .card { border-radius: 15px; border: none; }
    .info-label { font-weight: 600; color: #6c757d; width: 120px; }
    .form-label { font-weight: bold; font-size: 0.85rem; color: #495057; }
    .bg-light-custom { background-color: #f8f9fa; }
</style>
</head>
<body>
    <%@ include file="/header.jsp"%>
    <%@ include file="/sideNavbar.jsp"%>

    <main class="content">
        <div class="container-fluid">
            <div class="row justify-content-center">
                <div class="col-md-10 col-lg-7">
                    
                    <div class="mt-5 mb-4 px-2">
                        <h4 class="fw-bold text-primary">마이페이지</h4>
                        <p class="text-muted small">정보를 확인하고 필요한 경우 수정해 주세요.</p>
                    </div>

                    <c:if test="${not empty msg or not empty err}">
                        <div id="resultAlert" class="alert ${not empty msg ? 'alert-success' : 'alert-danger'} alert-dismissible fade show shadow-sm" role="alert">
                            <c:choose>
                                <c:when test="${msg == 'profile_ok'}">정보가 성공적으로 수정되었습니다.</c:when>
                                <c:when test="${err == 'pw_mismatch'}">현재 비밀번호가 일치하지 않습니다.</c:when>
                                <c:when test="${err == 'pw_rule'}">비밀번호 규칙을 확인해 주세요.</c:when>
                                <c:otherwise>오류가 발생했습니다. 다시 시도해 주세요.</c:otherwise>
                            </c:choose>
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <div class="card shadow-sm mb-5">
                        <div class="card-body p-4">
                            
                            <div id="infoView">
                                <h6 class="fw-bold mb-4 text-secondary">기본 인적사항</h6>
                                <div class="row g-3 mb-4">
                                    <div class="col-md-6 d-flex"><span class="info-label">학번</span><span>${student.studentNo}</span></div>
                                    <div class="col-md-6 d-flex"><span class="info-label">이름</span><span>${student.studentName}</span></div>
                                    <div class="col-md-6 d-flex"><span class="info-label">연락처</span><span>${student.studentPhone}</span></div>
                                    <div class="col-md-6 d-flex"><span class="info-label">이메일</span><span>${student.studentEmail}</span></div>
                                    <div class="col-12 d-flex"><span class="info-label">주소</span><span>${student.studentAddress}</span></div>
                                </div>
                                <div class="text-end">
                                    <button class="btn btn-primary px-4" onclick="toggleEdit(true)">정보 수정하기</button>
                                </div>
                            </div>

                            <div id="infoEdit" class="d-none">
                                <h6 class="fw-bold mb-4 text-primary">개인정보 및 비밀번호 수정</h6>
                                <form method="post" id="profileForm" action="<c:url value='/student/mypage/update'/>" novalidate>
                                    
                                    <div class="row mb-3">
                                        <div class="col-md-6">
                                            <label class="form-label small">전화번호</label>
                                            <input type="hidden" name="studentPhone" id="studentPhoneHidden" value="${student.studentPhone}"/>
                                            <div class="input-group input-group-sm">
                                                <select class="form-select" id="phone1" style="max-width: 80px;">
                                                    <option value="010" ${phone1=='010' ? 'selected' : ''}>010</option>
                                                    <option value="011" ${phone1=='011' ? 'selected' : ''}>011</option>
                                                </select>
                                                <input type="text" class="form-control" id="phone2" maxlength="4" value="${phone2}">
                                                <input type="text" class="form-control" id="phone3" maxlength="4" value="${phone3}">
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label small">이메일</label>
                                            <input type="hidden" name="studentEmail" id="studentEmailHidden" value="${student.studentEmail}"/>
                                            <div class="input-group input-group-sm">
                                                <input type="text" class="form-control" id="emailId" value="${emailId}">
                                                <span class="input-group-text">@</span>
                                                <select class="form-select" id="emailDomain">
                                                    <option value="naver.com" ${emailDomain=='naver.com' ? 'selected' : ''}>naver.com</option>
                                                    <option value="google.com" ${emailDomain=='google.com' ? 'selected' : ''}>google.com</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label small">주소</label>
                                        <input type="text" name="student_address" class="form-control form-control-sm" value="${student.studentAddress}">
                                    </div>

                                    <div class="row mb-4">
                                        <div class="col-md-4">
                                            <label class="form-label small">은행</label>
                                            <select name="bank_name" class="form-select form-select-sm">
                                                <option value="국민은행" ${bankName=='국민은행' ? 'selected' : ''}>국민은행</option>
                                                <option value="신한은행" ${bankName=='신한은행' ? 'selected' : ''}>신한은행</option>
                                                <option value="우리은행" ${bankName=='우리은행' ? 'selected' : ''}>우리은행</option>
                                                <option value="카카오뱅크" ${bankName=='카카오뱅크' ? 'selected' : ''}>카카오뱅크</option>
                                            </select>
                                        </div>
                                        <div class="col-md-8">
                                            <label class="form-label small">계좌번호</label>
                                            <input type="text" name="account_number" class="form-control form-control-sm" value="${accountNumber}">
                                        </div>
                                    </div>

                                    <div class="p-3 bg-light-custom rounded-3 mb-4">
                                        <p class="small fw-bold text-muted mb-3"><i class="bi bi-lock me-1"></i>비밀번호 변경 (필요 시에만 입력)</p>
                                        <div class="row g-2">
                                            <div class="col-md-6">
                                                <input type="password" name="newPassword" class="form-control form-control-sm" placeholder="새 비밀번호">
                                            </div>
                                            <div class="col-md-6">
                                                <input type="password" name="confirmNewPassword" class="form-control form-control-sm" placeholder="새 비밀번호 확인">
                                            </div>
                                        </div>
                                        <p class="text-muted mt-2 mb-0" style="font-size: 0.75rem;">* 변경을 원치 않으시면 비워두세요.</p>
                                    </div>

                                    <div class="mb-4">
                                        <label class="form-label small text-danger">현재 비밀번호 확인 (필수)</label>
                                        <input type="password" name="confirmPassword" class="form-control" placeholder="현재 비밀번호를 입력해야 저장이 가능합니다." required>
                                    </div>

                                    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                        <button type="button" class="btn btn-outline-secondary px-4" onclick="toggleEdit(false)">취소</button>
                                        <button type="submit" class="btn btn-success px-5">저장하기</button>
                                    </div>
                                </form>
                            </div>

                        </div>
                    </div>

                </div>
            </div>
        </div>
    </main>

    <%@ include file="/footer.jsp"%>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="<c:url value='/resources/js/updateStudentInfo.js'/>"></script>
    <script>
        // 폼 전환 함수
        function toggleEdit(isEdit) {
            const view = document.getElementById('infoView');
            const edit = document.getElementById('infoEdit');
            if(isEdit) {
                view.classList.add('d-none');
                edit.classList.remove('d-none');
            } else {
                view.classList.remove('d-none');
                edit.classList.add('d-none');
            }
        }

        // 결과 알림 자동 숨김
        const resultAlert = document.getElementById("resultAlert");
        if (resultAlert) {
            setTimeout(() => {
                const bsAlert = new bootstrap.Alert(resultAlert);
                bsAlert.close();
            }, 4000);
        }
    </script>
</body>
</html>