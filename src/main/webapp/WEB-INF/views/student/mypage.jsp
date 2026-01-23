<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>

<!-- Bootstrap 5 -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>

<!-- layout CSS -->
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

</head>
<body>
	<%@ include file="/header.jsp" %>
 	<%@ include file="/sideNavbar.jsp" %>
  
	 <main class="content">
		 <div class="container-fluid">
		   <div class="page-shell">
		     <div class="page-title mb-4">
			    <h4 class="fw-bold">마이페이지</h4>
			    <p class="text-muted mb-3">학생 인적사항 조회 및 수정</p>
			    
			    <c:if test="${not empty msg}">
					  <div id="resultAlert" class="alert alert-success alert-dismissible fade show" role="alert">
					    <c:choose>
					      <c:when test="${msg == 'profile_ok'}">개인정보가 수정되었습니다.</c:when>
					      <c:when test="${msg == 'pw_ok'}">비밀번호가 변경되었습니다.</c:when>
					      <c:otherwise>처리가 완료되었습니다.</c:otherwise>
					    </c:choose>
					    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
					  </div>
					</c:if>
					
					<c:if test="${not empty err}">
					  <div id="resultAlert" class="alert alert-danger alert-dismissible fade show" role="alert">
					    <c:choose>
					      <c:when test="${err == 'profile_fail'}">수정에 실패했습니다.</c:when>
					      <c:when test="${err == 'pw_mismatch'}">현재 비밀번호가 올바르지 않습니다.</c:when>
					      <c:when test="${err == 'pw_rule'}">새 비밀번호는 영문 소문자와 숫자를 포함한 6자 이상이어야 합니다.</c:when>
					      <c:when test="${err == 'addr_rule'}">주소는 비워둘 수 없고 255자 이하로 입력해야 합니다.</c:when>
					      <c:when test="${err == 'acc_rule'}">계좌번호는 숫자 3자리-숫자 7자리 형식으로 입력해야 합니다.</c:when>
					      <c:when test="${err == 'profile_pw_mismatch'}">현재 비밀번호가 올바르지 않습니다.</c:when>
					      <c:when test="${err == 'pw_same'}">새 비밀번호는 현재 비밀번호와 다르게 입력해주세요.</c:when>
					      <c:otherwise>요청 처리 중 오류가 발생했습니다.</c:otherwise>
					    </c:choose>
					    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
					  </div>
					</c:if>
			    
				 </div>
			
					<div class="card shadow-sm">
					    <div class="card-body">
						 <div class="row align-items-start mb-4">
						    <div class="col-md-3 text-center">
				                <c:choose>
				                    <%-- UUID가 기본값이면 resources 폴더의 기본 이미지 출력 --%>
				                    <c:when test="${fn:trim(student.photoUUID) == 'default.jsp'}">
				                        <img src="<c:url value='/resources/images/defaultUserProfile.jpg'/>" 
				                             class="img-thumbnail shadow-sm" 
				                             style="width: 200px; height: 240px; object-fit: cover;">
				                    </c:when>
				                    <%-- UUID가 실제 파일명이면 업로드 폴더에서 출력 --%>
				                    <c:otherwise>
				                        <img src="<c:url value='/student/mypage/display?fileName='/>${student.photoUUID}" 
				                             class="img-thumbnail shadow-sm" 
				                             style="width: 200px; height: 240px; object-fit: cover;">
				                    </c:otherwise>
				                </c:choose>
				            </div>
					        <!-- 학생 기본 정보 -->
					       <div class="col-md-9">
					        <table class="table table-bordered align-middle mb-4">
					            <tbody>
					                <tr>
				                    <th class="table-light" style="width: 20%;">학번</th>
				                    <td>${student.studentNo}</td>
				                    <th class="table-light" style="width: 20%;">이름</th>
				                    <td>${student.studentName}</td>
					                </tr>
					                <tr>
				                    <th class="table-light">전화번호</th>
				                    <td>${student.studentPhone}</td>
				                    <th class="table-light">성별</th>
				                    <td>${student.studentGender}</td>
					                </tr>
					                <tr>
				                    <th class="table-light">주소</th>
				                    <td colspan="3">${student.studentAddress}</td>
					                </tr>
					                <tr>
				                    <th class="table-light">재적 상태</th>
				                    <td>${student.studentStatus}</td>
				                    <th class="table-light">이메일</th>
				                    <td>${student.studentEmail}</td>
					                </tr>
					                <tr>
				                    <th class="table-light">은행명 / 계좌 정보</th>
				                    <td colspan="3">
				                    	<c:set var="bankParts" value="${fn:split(student.studentBank, ' ')}"/>
				                    	${bankParts[0]} / ${bankParts[1]}
				                    </td>
					                </tr>
					            </tbody>
					        </table>
						  </div>
						</div>
					        <!-- 수정 버튼 -->
					        <div class="d-flex justify-content-end gap-2 mt-3">
				            <button class="btn btn-outline-success"
				                    data-bs-toggle="collapse"
				                    data-bs-target="#editForm">
				                개인정보 수정하기
				            </button>
				            
				          	<button class="btn btn-outline-primary"
								          data-bs-toggle="collapse"
								          data-bs-target="#pwFormWrap">
								      비밀번호 변경하기
								  	</button>
					        </div>
					
					        <!-- 수정 폼 -->
					        <div class="collapse mt-4" id="editForm">		        	
					           <div class="card card-body border">			            
				            		<h5 class="fw-bold mt-2">개인정보 수정</h5>
												<hr class="my-3">
					
					                <form method="post" id="profileForm"
					                      action="<c:url value='/student/mypage/update'/>" 
					                      enctype="multipart/form-data" novalidate>
					                      <!-- 현재 되어있는 사진 정보 hidden -->
								        	<input type="hidden" name="nowPhotoFile" value="${student.photoFile}">
			    							<input type="hidden" name="nowPhotoUUID" value="${student.photoUUID}">
										<div class="row g-3">
							                <div class="col-md-3 text-center border-end">
							                    <div class="mb-3">
							                        <label class="form-label d-block fw-bold">프로필 사진</label>
							                        <%-- 미리보기용 이미지 id="previewImg"** --%>
							                        <c:choose>
							                            <c:when test="${fn:trim(student.photoUUID) == 'default.jsp'}">
							                                <img id="previewImg" src="<c:url value='/resources/images/defaultUserProfile.jpg'/>" 
							                                     class="img-thumbnail" style="width: 160px; height: 200px; object-fit: cover;">
							                            </c:when>
							                            <c:otherwise>
							                                <img id="previewImg" src="<c:url value='/student/mypage/display?fileName='/>${student.photoUUID}" 
							                                     class="img-thumbnail" style="width: 160px; height: 200px; object-fit: cover;">
							                            </c:otherwise>
							                        </c:choose>
							                    </div>
							                    <%-- 파일 선택 input (name="profileFile" 등 백엔드와 맞출 이름) --%>
												<div class="input-group">
											        <input type="file" name="uploadFile" id="profileFileInput" 
											               class="form-control form-control-sm" accept="image/*">
											    </div> 
							                    <div class="form-text mt-2">새 사진을 선택하면 <br> 미리보기가 바뀝니다.</div>
							                </div>
								
													  <div class="col-md-9">
														<div class="row mb-3">
														  <!-- 전화번호 -->
														  <div class="col-md-6">
														    <label class="form-label">전화번호</label>
														
														    <input type="hidden" name="studentPhone" id="studentPhoneHidden" value="${student.studentPhone}"/>
														
														    <div class="d-flex align-items-center gap-2">
														      <select class="form-select" style="max-width: 120px;" id="phone1">
														        <option value="010" ${phone1=='010' ? 'selected' : ''}>010</option>
														        <option value="011" ${phone1=='011' ? 'selected' : ''}>011</option>
														        <option value="016" ${phone1=='016' ? 'selected' : ''}>016</option>
														        <option value="017" ${phone1=='017' ? 'selected' : ''}>017</option>
														        <option value="018" ${phone1=='018' ? 'selected' : ''}>018</option>
														        <option value="019" ${phone1=='019' ? 'selected' : ''}>019</option>
														      </select>
														
														      <span class="text-muted">-</span>
														
														      <input type="text" class="form-control" style="max-width: 140px;"
														             id="phone2" maxlength="4" value="${phone2}" placeholder="1234">
														
														      <span class="text-muted">-</span>
														
														      <input type="text" class="form-control" style="max-width: 140px;"
														             id="phone3" maxlength="4" value="${phone3}" placeholder="5678">
														    </div>
														    <div id="phoneError" class="invalid-feedback d-none">
																  전화번호는 가운데/끝 4자리 숫자로 입력해주세요.
																</div>
														  </div>
														
														  <!-- 이메일 -->
														  <div class="col-md-6">
														    <label class="form-label">이메일</label>
														
														    <input type="hidden" name="studentEmail" id="studentEmailHidden" value="${student.studentEmail}"/>
														
														    <div class="d-flex align-items-center gap-2">
														      <input type="text" class="form-control" id="emailId"
														             value="${emailId}" placeholder="아이디">
														
														      <span class="text-muted">@</span>
														
														      <select class="form-select" style="max-width: 180px;" id="emailDomain">
														        <option value="naver.com" ${emailDomain=='naver.com' ? 'selected' : ''}>naver.com</option>
														        <option value="daum.net" ${emailDomain=='daum.net' ? 'selected' : ''}>daum.net</option>
														        <option value="hanmail.net" ${emailDomain=='hanmail.net' ? 'selected' : ''}>hanmail.net</option>
														        <option value="google.com" ${emailDomain=='google.com' ? 'selected' : ''}>google.com</option>
														        <option value="goodee.ac.kr" ${emailDomain=='goodee.ac.kr' ? 'selected' : ''}>goodee.ac.kr</option>
														      </select>
														    </div>
														    <div id="emailError" class="invalid-feedback d-none">
																  이메일을 입력해주세요.
																</div>
														  </div>
														</div>

					                    <div class="mb-3">
				                        <label class="form-label">주소</label>
				                        <input type="text"
				                               name="student_address"
				                               class="form-control"
				                               id="studentAddress"
				                               value="${student.studentAddress}">
				                        <div id="addressError" class="invalid-feedback d-none">
																  주소는 비워둘 수 없고, 255자 이하로 입력해주세요.
																</div>
					                    </div>
					                    
					                    <div class="row mb-3">
														    <div class="col-md-5">
													        <label class="form-label">은행</label>
													        <select name="bank_name" class="form-select">
													            <option value="국민은행" ${bankName=='국민은행' ? 'selected' : ''}>국민은행</option>
																		  <option value="농협은행" ${bankName=='농협은행' ? 'selected' : ''}>농협은행</option>
																		  <option value="신한은행" ${bankName=='신한은행' ? 'selected' : ''}>신한은행</option>
																		  <option value="우리은행" ${bankName=='우리은행' ? 'selected' : ''}>우리은행</option>
																		  <option value="카카오뱅크" ${bankName=='카카오뱅크' ? 'selected' : ''}>카카오뱅크</option>
													        </select>
														    </div>
														    <div class="col-md-7">
													        <label class="form-label">계좌번호</label>
													        <input type="text"
													               name="account_number"
													               id="accountNumber"
													               value="${accountNumber}" 
													               class="form-control"
													               placeholder="숫자와 - 만 입력">
													        <div id="accountError" class="invalid-feedback d-none">
																	  계좌번호는 000-0000000 형식(숫자3자리-숫자7자리)으로 입력해주세요.
																	</div>
														    </div>
															</div>
															<!-- 현재 비밀번호 확인 -->
															<div class="mb-3">
															  <label for="confirmPassword" class="form-label">현재 비밀번호 확인</label>
															  <input type="password"
															         class="form-control"
															         id="confirmPassword"
															         name="confirmPassword"
															         placeholder="현재 비밀번호를 입력하세요">
															  <div id="confirmPwError" class="invalid-feedback d-none"></div>
															</div>
														  </div>
														 </div>
					                    <div class="text-end">
				                        <button type="submit" class="btn btn-success">
				                            저장
				                        </button>
				                        <button type="button"
				                                class="btn btn-secondary"
				                                data-bs-toggle="collapse"
				                                data-bs-target="#editForm">
				                            취소
				                        </button>
					                    </div>
					
					                </form>
					
					            </div>
					        </div>
									
									<div class="collapse mt-3" id="pwFormWrap">
									  <div class="card card-body border">
											
											<h5 class="fw-bold mt-2">비밀번호 변경</h5>
											<hr class="my-3">
											
									    <form id="pwForm" method="post" action="<c:url value='/student/mypage/password'/>" novalidate>
									
									      <div class="row g-3">
									        <div class="col-md-6">
									          <label class="form-label">현재 비밀번호</label>
									          <input type="password" name="currentPassword" class="form-control" placeholder="현재 비밀번호를 입력하세요" required>
									          <div id="currentPwError" class="invalid-feedback d-none">현재 비밀번호를 입력해주세요.</div>
									        </div>
									        <div class="col-md-6">
									          <label class="form-label">새 비밀번호</label>
									          <input type="password" name="newPassword" class="form-control" placeholder="바꿀 비밀번호를 입력하세요" required>
									          <div id="newPwError" class="invalid-feedback d-none"></div>
									        </div>
									      </div>
									
									      <div class="text-end mt-3">
									        <button type="submit" class="btn btn-primary">변경</button>
									        <button type="button"
									                class="btn btn-secondary"
									                data-bs-toggle="collapse"
									                data-bs-target="#pwFormWrap">
									          취소
									        </button>
									      </div>
									    </form>
									
									  </div>
									</div>
									
					    </div>
					</div>
		   			
			 </div>
		 </div>
	</main>
  
	<%@ include file="/footer.jsp" %>
		<script>
	// 화면 미리 보기 함수
	const fileInput = document.getElementById('profileFileInput');
	const previewImg = document.getElementById('previewImg');

	if(fileInput) {
	    fileInput.addEventListener('change', function(e) {
	        const file = e.target.files[0];
	        
	        if (file) {
	            // 1. 이미지 파일인지 간단히 체크
	            if (!file.type.startsWith('image/')) {
	                alert('이미지 파일만 선택 가능합니다.');
	                this.value = '';
	                return;
	            }

	            // 2. 파일을 읽어서 미리보기 이미지 소스 변경
	            const reader = new FileReader();
	            reader.onload = function(e) {
	                previewImg.src = e.target.result;
	            };
	            reader.readAsDataURL(file);
	        }
	    });
	}
	</script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
	<script src="<c:url value='/resources/js/updateStudentInfo.js'/>"></script>
  <!-- <script>
	  // 저장 결과 알림 4초 후 자동 숨김
	  const resultAlert = document.getElementById("resultAlert");
	  if (resultAlert) {
	    setTimeout(() => resultAlert.classList.add("d-none"), 4000);
	  }
	</script> -->
</body>
</html>