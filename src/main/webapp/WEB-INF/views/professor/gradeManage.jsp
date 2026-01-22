<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>성적 관리</title>

<!-- Bootstrap 5 -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>

<!-- layout CSS -->
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

<style>
  .badge-soft-lime{
    background-color: #d1f7c4;
    color: #1f4d2b;
    border: 1px solid #b7efaa;
  }
  .subline{
    font-size: 0.78rem;
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
            <h4 class="mb-0"><b>성적 관리</b></h4>
            <small class="text-muted">
              ${targetYear}년 ${targetSemester}학기 (직전학기 종강 강의)
            </small>
          </div>

          <!-- 저장 버튼: 수정 불가면 비활성 -->
          <button form="gradeForm" type="submit"
                  class="btn btn-success btn-sm"
                  <c:if test="${not isEditable}">disabled</c:if>>
            성적 저장
          </button>
        </div>

        <!-- 알림 영역: 저장 결과(자동 숨김) + 기간 안내(항상 표시) -->
				<!-- 저장 실패 -->
				<c:if test="${not empty param.error}">
				  <div id="resultAlert" class="alert alert-danger py-2" role="alert">
				    저장에 실패했습니다.
				    <!-- 실패 이유 작게 -->
				    <div class="small mt-1"><c:out value="${param.error}"/></div>
				  </div>
				</c:if>
				
				<!-- 저장 성공/안내 (msg가 있으면 표시) -->
				<c:if test="${not empty param.msg}">
				  <div id="resultAlert" class="alert alert-success py-2" role="alert">
				    <c:out value="${param.msg}"/>
				  </div>
				</c:if>
				
				<!-- 기간 안내는 항상 표시 -->
				<c:choose>
				  <c:when test="${isEditable}">
				    <div class="alert alert-info py-2 mb-2" role="alert">
				      현재는 <b>성적 기입 기간</b>입니다. 점수 선택 후 <b>성적 저장</b>을 누르세요.<br>
				      <span class="small text-muted">
					      * 기간 내 수정이 가능하고, 미입력 시 학생 성적 조회에 ‘미입력’으로 표시됩니다. (기간 종료 후 수정 제한)
					    </span>
				    </div>
				  </c:when>
				  <c:otherwise>
				    <div class="alert alert-info py-2 mb-2" role="alert">
				      현재는 <b>성적 기입 기간이 아닙니다.</b> 학생 목록은 조회할 수 있지만 점수 수정은 불가능합니다.
				    </div>
				  </c:otherwise>
				</c:choose>

        <!-- 강의 선택 + 검색 -->
        <div class="card shadow-sm border-0 mb-3">
          <div class="card-body">
            <form method="get" action="<c:url value='/professor/grade/list'/>" class="row g-2 align-items-center">
              <div class="col-md-4">
                <select name="lectureId" class="form-select" onchange="this.form.submit()">
                  <c:choose>
                    <c:when test="${empty lectureList}">
                      <option value="">강의가 없습니다</option>
                    </c:when>
                    <c:otherwise>
                      <c:forEach var="lec" items="${lectureList}">
                        <option value="${lec.lectureId}"
                          <c:if test="${lec.lectureId == selectedLectureId}">selected</c:if>>
                          ${lec.lectureName} - ${lec.lectureSection}
                        </option>
                      </c:forEach>
                    </c:otherwise>
                  </c:choose>
                </select>
              </div>

              <div class="col-md-5">
                <input type="text" name="keyword" class="form-control"
                       placeholder="학생 이름 검색"
                       value="<c:out value='${keyword}'/>">
              </div>

              <div class="col-md-3 d-flex gap-2">
                <button type="submit" class="btn btn-success w-100">검색</button>
                <a class="btn btn-outline-secondary w-100"
                   href="<c:url value='/professor/grade/list'>
                          <c:if test='${not empty selectedLectureId}'>
                            <c:param name='lectureId' value='${selectedLectureId}'/>
                          </c:if>
                        </c:url>">초기화</a>
              </div>

              <!-- 검색/페이징 유지용 -->
              <input type="hidden" name="page" value="1"/>
            </form>
            <div class="form-text mt-2">
              * 강의를 선택하면 해당 강의의 수강생 목록이 표시됩니다. (페이지당 10명)
            </div>
          </div>
        </div>

        <!-- 성적 저장 폼 (페이지 단위 일괄 저장) -->
        <form id="gradeForm" method="post" action="<c:url value='/professor/grade/update'/>">

          <!-- 현재 컨텍스트 유지 -->
          <input type="hidden" name="lectureId" value="<c:out value='${selectedLectureId}'/>"/>
          <input type="hidden" name="page" value="<c:out value='${page}'/>"/>
          <c:if test="${not empty keyword}">
            <input type="hidden" name="keyword" value="<c:out value='${keyword}'/>"/>
          </c:if>

          <!-- 테이블 -->
          <div class="card shadow-sm border-0">
            <div class="table-responsive">
              <table class="table table-hover align-middle mb-0 text-center">
                <thead class="table-light">
                  <tr>
                    <th style="width:180px;">학번</th>
                    <th>학생명</th>
                    <th style="width:180px;">현재 점수</th>
                    <th style="width:260px;">성적 입력</th>
                  </tr>
                </thead>

                <tbody>
                  <c:choose>
                    <c:when test="${empty selectedLectureId}">
                      <tr>
                        <td colspan="5" class="text-center text-muted py-5">
                          강의를 선택해주세요.
                        </td>
                      </tr>
                    </c:when>

                    <c:when test="${empty historyList}">
                      <tr>
                        <td colspan="5" class="text-center text-muted py-5">
                          조회 결과가 없습니다.
                        </td>
                      </tr>
                    </c:when>

                    <c:otherwise>
                      <c:forEach var="h" items="${historyList}">
                        <tr>
                          <td class="fw-semibold">
                            <span class="badge rounded-pill badge-soft-lime">
                              <c:out value="${h.studentNo}"/>
                            </span>

                            <!-- 배열 전송 -->
                            <input type="hidden" name="studentId" value="${h.studentId}"/>
                          </td>

                          <td class="text-center">
                            <div class="fw-semibold"><c:out value="${h.studentName}"/></div>
                          </td>

                          <td>
                            <!-- oldScore(hidden) : 변경 여부 판단용 -->
                            <c:choose>
                              <c:when test="${empty h.lectureScore}">
                                <span class="text-muted">미입력</span>
                                <input type="hidden" name="oldScore" value=""/>
                              </c:when>
                              <c:otherwise>
                                <span class="fw-semibold">${h.lectureScore}</span>
                                <input type="hidden" name="oldScore" value="${h.lectureScore}"/>
                              </c:otherwise>
                            </c:choose>
                          </td>

                          <td>
                            <select name="newScore" class="form-select form-select-sm mx-auto" style="max-width: 180px;"
                                    <c:if test="${not isEditable}">disabled</c:if>>
                              <!-- 미선택(= 변경 안 함), null이면 업데이트 스킵 -->
                              <option value="">(선택)</option>
                              <!-- value는 숫자, 표시는 알파벳 -->
                              <option value="4.5" <c:if test="${h.lectureScore == 4.5}">selected</c:if>>A+ (4.5)</option>
                              <option value="4.0" <c:if test="${h.lectureScore == 4.0}">selected</c:if>>A (4.0)</option>
                              <option value="3.5" <c:if test="${h.lectureScore == 3.5}">selected</c:if>>B+ (3.5)</option>
                              <option value="3.0" <c:if test="${h.lectureScore == 3.0}">selected</c:if>>B (3.0)</option>
                              <option value="2.5" <c:if test="${h.lectureScore == 2.5}">selected</c:if>>C+ (2.5)</option>
                              <option value="2.0" <c:if test="${h.lectureScore == 2.0}">selected</c:if>>C (2.0)</option>
                              <option value="1.5" <c:if test="${h.lectureScore == 1.5}">selected</c:if>>D+ (1.5)</option>
                              <option value="1.0" <c:if test="${h.lectureScore == 1.0}">selected</c:if>>D (1.0)</option>
                              <option value="0.5" <c:if test="${h.lectureScore == 0.5}">selected</c:if>>F (0.5)</option>
                              <option value="0.0" <c:if test="${h.lectureScore == 0.0}">selected</c:if>>F (0.0)</option>
                            </select>
                          </td>
                        </tr>
                      </c:forEach>
                    </c:otherwise>
                  </c:choose>

                </tbody>
              </table>
            </div>
          </div>
        </form>

        <!-- 페이징 -->
        <c:if test="${lastPage > 1 && not empty selectedLectureId}">
          <nav class="mt-3">
            <ul class="pagination justify-content-center">

              <!-- < : 이전 페이지 -->
              <li class="page-item ${page <= 1 ? 'disabled' : ''}">
                <a class="page-link"
                   href="<c:url value='/professor/grade/list'>
                          <c:param name='lectureId' value='${selectedLectureId}'/>
                          <c:param name='page' value='${page - 1}'/>
                          <c:if test='${not empty keyword}'>
                            <c:param name='keyword' value='${keyword}'/>
                          </c:if>
                        </c:url>">
                  &laquo;
                </a>
              </li>

              <!-- 페이지 번호 (navStart ~ navEnd) -->
              <c:forEach var="p" begin="${navStart}" end="${navEnd}">
                <li class="page-item ${p == page ? 'active' : ''}">
                  <a class="page-link"
                     href="<c:url value='/professor/grade/list'>
                            <c:param name='lectureId' value='${selectedLectureId}'/>
                            <c:param name='page' value='${p}'/>
                            <c:if test='${not empty keyword}'>
                              <c:param name='keyword' value='${keyword}'/>
                            </c:if>
                          </c:url>">
                    ${p}
                  </a>
                </li>
              </c:forEach>

              <!-- > : 다음 페이지 -->
              <li class="page-item ${page >= lastPage ? 'disabled' : ''}">
                <a class="page-link"
                   href="<c:url value='/professor/grade/list'>
                          <c:param name='lectureId' value='${selectedLectureId}'/>
                          <c:param name='page' value='${page + 1}'/>
                          <c:if test='${not empty keyword}'>
                            <c:param name='keyword' value='${keyword}'/>
                          </c:if>
                        </c:url>">
                  &raquo;
                </a>
              </li>

            </ul>
          </nav>
        </c:if>

      </div>
    </div>
  </main>

  <%@ include file="/footer.jsp" %>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  
  <script>
	  // 저장 결과 알림 4초 후 자동 숨김
	  const resultAlert = document.getElementById("resultAlert");
	  if (resultAlert) {
	    setTimeout(() => resultAlert.classList.add("d-none"), 4000);
	  }
	</script>
</body>
</html>
