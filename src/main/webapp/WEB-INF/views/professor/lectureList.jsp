<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>강의 목록 - GoodeeLMS</title>

<!-- Bootstrap 5 -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>

<!-- layout CSS -->
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

<link rel="stylesheet" href="<c:url value='/resources/css/lectureDetailModal.css'/>" />

<style>
  .lecture-table {
    table-layout: fixed;
    width: 100%;
  }

  .lecture-table .truncate {
    display: block;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  
	.td-room {
	  max-width: clamp(9rem, 14vw, 14rem); /* 화면에 따라 9~14rem 사이 */
	}
	
	.table td {
	    padding-top: 15px !important;    /* 위쪽 여백 */
	    padding-bottom: 15px !important; /* 아래쪽 여백 */
	    vertical-align: middle;          /* 내용이 세로 가운데 오도록 설정 */
	}
  
  /* 강의 코드 */
  .badge-soft-lime{
	  background-color: #d1f7c4; /* 연한 연두 */
	  color: #1f4d2b;            /* 글자 진초록 */
	  border: 1px solid #b7efaa; /* 살짝 테두리 */
	}
	
	/* 전공 */
	.badge-major {
	  background-color: #eef2f6;   /* 연한 블루그레이 */
	  color: #2f4f6f;
	  border: 1px solid #cfd8e3;
	  font-weight: 500;
	}
	
	/* 교양 */
	.badge-general {
	  background-color: #f7f5f2;   /* 연한 베이지 */
	  color: #6b5e4f;
	  border: 1px solid #e0dbd3;
	  font-weight: 500;
	}
	
	.subline{
	  font-size: 0.78rem;
	}
	
	/* 강의 상태 서브 뱃지  */
	.badge-lecture-status {
	  font-size: 0.7rem;
	  padding: 2px 6px;
	  border-radius: 999px;
	  margin-left: 6px;
	  vertical-align: middle;
	  font-weight: 400;
	  line-height: 1.2;
	}
	
	/* 상태별 색상 */
	.badge-status-open {        /* 개강 */
	  background-color: #e6f4ea;
	  color: #1e7e34;
	}
	
	.badge-status-ready {       /* 예정 */
	  background-color: #eef2f6;
	  color: #5f6f82;
	}
	
	.badge-status-end {         /* 종강 */
	  background-color: #f1f1f1;
	  color: #8a8a8a;
	}
	
	/* 종강 강의 행 흐림 처리 */
	tr.lecture-ended {
  	opacity: 0.55;
	}
	
	/* 종강 행 안의 링크는 기본 색 유지 */
	tr.lecture-ended a {
	  color: inherit;
	}
</style>

</head>
<body>
  <%@ include file="/header.jsp" %>
  <%@ include file="/sideNavbar.jsp" %>
  
  <main class="content">
	  <div class="container-fluid">
	    <div class="page-shell">
	       <!-- 상단 타이틀 + 버튼 -->
        <div class="d-flex align-items-center justify-content-between mb-3">
          <div>
            <h4 class="mb-0"><b>강의 관리</b></h4>
            <small class="text-muted">소속 학과의 개설 강의를 관리할 수 있습니다.</small>
          </div>

          <c:if test="${not empty sessionScope.professor_id}">
					  <c:choose>
					
					    <c:when test="${sessionScope.professor_status == '휴직'}">
					      <span onclick="alert('휴직 중인 교수는 강의 등록이 불가합니다.');"
								      style="display:inline-block; cursor:not-allowed;">
								  <button type="button" class="btn btn-outline-secondary btn-sm" disabled>
								    + 강의 등록
								  </button>
								</span>
					    </c:when>
					
					    <c:otherwise>
					      <c:choose>
					        <c:when test="${isLectureInsertPeriod}">
					          <a class="btn btn-success btn-sm" href="<c:url value='/professor/lecture/add'/>">+ 강의 등록</a>
					        </c:when>
					        <c:otherwise>
					          <a class="btn btn-success btn-sm"
					             href="<c:url value='/professor/lecture/list'/>"
					             onclick="alert('현재 등록기간이 아닙니다.'); return false;">+ 강의 등록</a>
					        </c:otherwise>
					      </c:choose>
					    </c:otherwise>
					
					  </c:choose>
					</c:if>

        </div>
        
        <c:if test="${param.msg == 'insert_ok'}">
				  <div id="successAlert" class="alert alert-success alert-dismissible fade show" role="alert">
				    강의가 등록되었습니다.
				    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
				  </div>
				</c:if>
        

        <!-- 검색창 -->
        <div class="card shadow-sm border-0 mb-3">
          <div class="card-body">
            <form method="get" action="<c:url value='/professor/lecture/list'/>" class="row g-2 align-items-center">
            		<input type="hidden" name="page" value="1" />
                <div class="col-md-2">
							    <select name="statusFilter" class="form-select"
        									onchange="this.form.page.value=1; this.form.submit();">
									  <option value="ACTIVE" ${statusFilter eq 'ACTIVE' ? 'selected' : ''}>진행중 (예정/개강)</option>
									  <option value="ALL" ${statusFilter eq 'ALL' ? 'selected' : ''}>전체 (예정/개강/종강)</option>
									</select>
							  </div>
							
							  <div class="col-md-6">
							    <input type="text" name="keyword" class="form-control"
							           placeholder="강의명 또는 교수명 또는 건물명 검색"
							           value="<c:out value='${keyword}'/>">
							  </div>
              <div class="col-md-4 d-flex gap-2">
                <button type="submit" class="btn btn-success w-100">검색</button>
                <a class="btn btn-outline-secondary w-100" href="<c:url value='/professor/lecture/list'/>">초기화</a>
              </div>
            </form>
            <div class="form-text mt-2">* 검색은 강의명/교수명/건물명 기준이며, 페이지당 10개씩 표시됩니다.</div>
          </div>
        </div>

        <!-- 테이블 -->
        <div class="card shadow-sm border-0">
				  <div class="table-responsive">
				    <table class="table table-hover align-middle mb-0 text-center lecture-table">
			        <thead class="table-light">
			          <tr>
			            <th style="width:110px;">강의코드</th>
			            <th style="width:clamp(220px, 30vw, 360px);">강의명</th>
			            <th style="width:120px;">교수</th>
			            <th style="width:100px;">유형</th>
			            <th style="width:80px;">학점</th>
			            <th style="width:120px;">학기</th>
			            <th style="width:80px;">분반</th>
			            <th style="width:150px;">강의실</th>
			            <th style="width:100px;">정원</th>
			          </tr>
			        </thead>
			
			        <tbody>
			          <c:choose>
			            <c:when test="${empty lectures}">
			              <tr>
			                <td colspan="9" class="text-center text-muted py-5">
			                  조회 결과가 없습니다.
			                </td>
			              </tr>
			            </c:when>
			
			            <c:otherwise>
			              <c:forEach var="lec" items="${lectures}">
			                <tr class="${lec.lectureStatus eq '종강' ? 'lecture-ended' : ''}">
			                  <td class="fw-semibold">
			                  	<span class="badge rounded-pill badge-soft-lime">
			                  		<c:out value="${lec.lectureCodeDisplay}"/>
			                  	</span>
			                  </td>
			                  
												<td class="text-center">
												  <a class="fw-semibold text-decoration-none truncate d-block lecture-detail-link"
													   href="#"
													     data-name="<c:out value='${lec.lectureName}'/>"
														   data-code="<c:out value='${lec.lectureCodeDisplay}'/>"
														   data-prof="<c:out value='${lec.professorName}'/>"
														   data-type="<c:out value='${lec.lectureType}'/>"
														   data-credit="<c:out value='${lec.lectureCredit}'/>"
														   data-year="<c:out value='${lec.lectureYear}'/>"
														   data-sem="<c:out value='${lec.lectureSemester}'/>"
														   data-section="<c:out value='${lec.lectureSection}'/>"
														   data-building="<c:out value='${lec.buildingName}'/>"
														   data-room="<c:out value='${lec.lectureRoom}'/>"
														   data-current="<c:out value='${lec.lectureCurrentPeople}'/>"
														   data-capacity="<c:out value='${lec.lectureCapacity}'/>"
														   data-status="<c:out value='${lec.lectureStatus}'/>"
														   data-desc="<c:out value='${lec.lectureDescription}'/>"
														   title="<c:out value='${lec.lectureName}'/>">
														  <c:out value="${lec.lectureName}"/>
														  <c:choose>
														    <c:when test="${lec.lectureStatus eq '개강'}">
														      <span class="badge-lecture-status badge-status-open">개강</span>
														    </c:when>
														    <c:when test="${lec.lectureStatus eq '예정'}">
														      <span class="badge-lecture-status badge-status-ready">예정</span>
														    </c:when>
														    <c:when test="${lec.lectureStatus eq '종강'}">
														      <span class="badge-lecture-status badge-status-end">종강</span>
														    </c:when>
														  </c:choose>
													</a>
												</td>

			                  <td><c:out value="${lec.professorName}"/></td>
			                  <td>
												  <span class="badge rounded-pill
												    ${lec.lectureType eq '전공' ? 'badge-major' : 'badge-general'}">
												    <c:out value="${lec.lectureType}"/>
												  </span>
												</td>
			                  <td><c:out value="${lec.lectureCredit}"/></td>
			                  <td><c:out value="${lec.lectureYear}"/> 
			                  - 
			                  <c:out value="${lec.lectureSemester}"/></td>
			                  <td><c:out value="${lec.lectureSection}"/></td>
			                  <td class="text-center td-room">
												  <span class="truncate"
												        title="<c:out value='${lec.buildingName} ${lec.lectureRoom}호'/>">
												    <c:out value="${lec.buildingName}" /> <c:out value="${lec.lectureRoom}" />호
												  </span>
												</td>
			                  <td>
												  <c:set var="filled" value="${lec.lectureCurrentPeople >= lec.lectureCapacity}" />
												  <span class="badge rounded-pill ${filled ? 'text-bg-danger' : 'text-bg-secondary'}">
												    <c:out value="${lec.lectureCurrentPeople}"/> / <c:out value="${lec.lectureCapacity}"/>
												  </span>
												</td>
			                </tr>
			              </c:forEach>
			            </c:otherwise>
			          </c:choose>
			        </tbody>
			      </table>
			    </div>
			  </div>

        <!-- 페이징 -->
				<c:if test="${totalPage > 1}">
				  <nav class="mt-3">
				    <ul class="pagination justify-content-center">
				
				      <!-- << : 이전 블록 -->
				      <li class="page-item ${startPage <= 1 ? 'disabled' : ''}">
				        <a class="page-link"
				           href="<c:url value='/professor/lecture/list'>
				                  <c:param name='page' value='${prevBlockPage}'/>
				                  <c:if test='${not empty keyword}'>
				                    <c:param name='keyword' value='${keyword}'/>
				                  </c:if>
				                  <c:if test='${not empty statusFilter}'>
				                    <c:param name='statusFilter' value='${statusFilter}'/>
				                  </c:if>
				                </c:url>">
				          &laquo;&laquo;
				        </a>
				      </li>
				
				      <!-- < : 이전 페이지 -->
				      <li class="page-item ${page <= 1 ? 'disabled' : ''}">
				        <a class="page-link"
				           href="<c:url value='/professor/lecture/list'>
				                  <c:param name='page' value='${page - 1}'/>
				                  <c:if test='${not empty keyword}'>
				                    <c:param name='keyword' value='${keyword}'/>
				                  </c:if>
				                  <c:if test='${not empty statusFilter}'>
				                    <c:param name='statusFilter' value='${statusFilter}'/>
				                  </c:if>
				                </c:url>">
				          &laquo;
				        </a>
				      </li>
				
				      <!-- 페이지 번호 (startPage ~ endPage) -->
				      <c:forEach var="p" begin="${startPage}" end="${endPage}">
				        <li class="page-item ${p == page ? 'active' : ''}">
				          <a class="page-link"
				             href="<c:url value='/professor/lecture/list'>
				                    <c:param name='page' value='${p}'/>
				                    <c:if test='${not empty keyword}'>
				                      <c:param name='keyword' value='${keyword}'/>
				                    </c:if>
				                    <c:if test='${not empty statusFilter}'>
				                      <c:param name='statusFilter' value='${statusFilter}'/>
				                    </c:if>
				                  </c:url>">
				            ${p}
				          </a>
				        </li>
				      </c:forEach>
				
				      <!-- > : 다음 페이지 -->
				      <li class="page-item ${page >= totalPage ? 'disabled' : ''}">
				        <a class="page-link"
				           href="<c:url value='/professor/lecture/list'>
				                  <c:param name='page' value='${page + 1}'/>
				                  <c:if test='${not empty keyword}'>
				                    <c:param name='keyword' value='${keyword}'/>
				                  </c:if>
				                  <c:if test='${not empty statusFilter}'>
				                    <c:param name='statusFilter' value='${statusFilter}'/>
				                  </c:if>
				                </c:url>">
				          &raquo;
				        </a>
				      </li>
				
				      <!-- >> : 다음 블록 -->
				      <li class="page-item ${endPage >= totalPage ? 'disabled' : ''}">
				        <a class="page-link"
				           href="<c:url value='/professor/lecture/list'>
				                  <c:param name='page' value='${nextBlockPage}'/>
				                  <c:if test='${not empty keyword}'>
				                    <c:param name='keyword' value='${keyword}'/>
				                  </c:if>
				                  <c:if test='${not empty statusFilter}'>
				                    <c:param name='statusFilter' value='${statusFilter}'/>
				                  </c:if>
				                </c:url>">
				          &raquo;&raquo;
				        </a>
				      </li>
				
				    </ul>
				  </nav>
				</c:if>

	    </div>
	  </div>
	</main>
  
  <!-- 강의 상세 모달 -->
	<div class="modal fade" id="lectureDetailModal" tabindex="-1" aria-hidden="true">
	  <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable"> 
	  	<div class="modal-content border-0 shadow"> 
	  		<div class="modal-header bg-dark text-white"> 
	  			<h5 class="modal-title fw-bold"><span id="mLectureName"></span><span id="mLectureStatus"></span></h5>
	      	<button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
	      </div>
	
	      <div class="modal-body p-4">
	        
	        <div class="d-inline-flex align-items-center mb-4 text-muted meta-line">
	          <div class="me-3">
	             <i class="bi bi-person-fill"></i> <span id="mLectureProf" class="fw-semibold text-dark"></span>
	          </div>
	          <div class="vr mx-2"></div>
	          <div class="ms-3">
	             <i class="bi bi-geo-alt-fill"></i> <span id="mLecturePlace"></span>
	          </div>
	        </div>
	
	        <div class="card border-0 shadow-sm mb-4 bg-light info-table">
	          <div class="card-body p-0">
	            <table class="table table-borderless mb-0 info-table">
	              <tbody>
	                <tr class="border-bottom">
	                  <th class="ps-4 py-3 text-secondary" style="width:100px;">강의코드</th>
	                  <td class="py-3" id="mLectureCode"></td> <th class="py-3 text-secondary" style="width:100px;">이수구분</th>
	                  <td class="pe-4 py-3" id="mLectureType"></td> </tr>
	                <tr class="border-bottom">
	                  <th class="ps-4 py-3 text-secondary">학점</th>
	                  <td class="py-3" id="mLectureCredit"></td>
	                  <th class="py-3 text-secondary">개설학기</th>
	                  <td class="pe-4 py-3" id="mLectureTerm"></td>
	                </tr>
	                <tr class="border-bottom">
	                  <th class="ps-4 py-3 text-secondary">분반</th>
	                  <td class="py-3" id="mLectureSection"></td>
	                  <th class="py-3 text-secondary">현재/정원</th>
	                  <td class="pe-4 py-3" id="mLectureCapacity"></td> </tr>
	              </tbody>
	            </table>
	          </div>
	        </div>
	
	        <div>
	          <h6 class="fw-bold mb-2 text-secondary"><small>강의 설명</small></h6>
	          <div class="p-3 bg-light rounded border text-secondary" style="min-height: 80px; white-space: pre-line;"><span id="mLectureDesc">등록된 강의 설명이 없습니다.</span></div>
	        </div>
	
	      </div>
	
	      <div class="modal-footer border-top-0">
	        <button type="button" class="btn btn-secondary px-4" data-bs-dismiss="modal">닫기</button>
	      </div>
	    </div>
	  </div>
	</div>
  
  <%@ include file="/footer.jsp" %>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  
  <script>
  	// 성공 alert 4초
	  const success = document.getElementById("successAlert");
	  if (success) setTimeout(() => success.classList.add("d-none"), 4000);
	</script>
	
	<script> 
		// 모달 스크립트
	  let lastClickedLink = null;
	
	  // 클릭 이벤트: 링크 클릭 시 변수 저장 및 모달 열기
	  document.addEventListener("click", (e) => {
	    const link = e.target.closest(".lecture-detail-link");
	    if (!link) return;
	
	    e.preventDefault();
	    lastClickedLink = link;
	
	    const modalEl = document.getElementById("lectureDetailModal");
	    const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
	    
	    // 모달이 뜨기 전에 데이터를 채워야 깜빡임X
	    populateModalData(link.dataset);
	    modal.show();
	  });
	
	  // 데이터 채우는 함수
		function populateModalData(d) {
		    const modalEl = document.getElementById("lectureDetailModal");
		    // 일반 텍스트 설정
		    const setText = (sel, val) => {
		      const el = modalEl.querySelector(sel);
		      if (el) el.textContent = val ?? "";
		    };
		    // 태그(뱃지) 설정
		    const setHtml = (sel, html) => {
		      const el = modalEl.querySelector(sel);
		      if (el) el.innerHTML = html ?? "";
		    };
		
		    // --- 기본 정보 (텍스트) ---
		    setText("#mLectureName", d.name);
		 		// 강의 상태 뱃지
		    const statusEl = document.querySelector("#mLectureStatus");
		    statusEl.innerHTML = "";
		    if (d.status === "개강") {
		      statusEl.innerHTML =
		        '<span class="badge-lecture-status badge-status-open ms-2">개강</span>';
		    } else if (d.status === "예정") {
		      statusEl.innerHTML =
		        '<span class="badge-lecture-status badge-status-ready ms-2">예정</span>';
		    } else if (d.status === "종강") {
		      statusEl.innerHTML =
		        '<span class="badge-lecture-status badge-status-end ms-2">종강</span>';
		    }
		    setText("#mLectureProf", d.prof);
		    setText("#mLecturePlace", d.building + " " + d.room + "호");
		    setText("#mLectureCredit", d.credit + "학점");
		    setText("#mLectureTerm", d.year + "년 " + d.sem + "학기");
		    setText("#mLectureSection", d.section + "분반");
		    // 강의 설명 (없으면 문구 표시)
		    const description = d.desc ? d.desc.trim() : "";
    		setText("#mLectureDesc", description ? description : "등록된 강의 설명이 없습니다.");
		    // 강의코드
		    setHtml("#mLectureCode", 
		      '<span class="badge rounded-pill badge-soft-lime fs-6 fw-normal">' + d.code + '</span>'
		    );
		    // 이수구분
		    const typeText = d.type; // "전공" or "교양"
				const typeClass = (typeText === "전공") ? "badge-major" : "badge-general";
				setHtml(
				  "#mLectureType",
				  '<span class="badge rounded-pill ' + typeClass + ' fs-6 fw-normal">' +
				    typeText +
				  "</span>"
				);
		    // 정원(꽉 찼으면 빨강, 아니면 회색)
		    const current = parseInt(d.current);
		    const capacity = parseInt(d.capacity);
		    // 정원이 꽉 찼는지 확인
		    const isFull = current >= capacity;
		    const badgeClass = isFull ? 'text-bg-danger' : 'text-bg-secondary';
		    setHtml("#mLectureCapacity", 
		      '<span class="badge rounded-pill ' + badgeClass + ' fs-6 fw-normal">' + 
		       current + ' / ' + capacity + 
		      '</span>'
		    );
		}
	</script>

</body>
</html>