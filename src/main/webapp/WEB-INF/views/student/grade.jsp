<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>성적조회</title>

<!-- Bootstrap 5 -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>

<!-- layout CSS -->
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

<style>
  .grade-table {
    table-layout: fixed;
    width: 100%;
  }

  .truncate {
    display: block;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  
  .table td {
	    padding-top: 15px !important;    /* 위쪽 여백 */
	    padding-bottom: 15px !important; /* 아래쪽 여백 */
	    vertical-align: middle;          /* 내용이 세로 가운데 오도록 설정 */
	}

  .badge-soft-lime{
    background-color: #d1f7c4;
    color: #1f4d2b;
    border: 1px solid #b7efaa;
  }

  .subline{
    font-size: 0.78rem;
  }

  .badge-grade {
    background-color: #eef2f6;
    color: #2f4f6f;
    border: 1px solid #cfd8e3;
    font-weight: 500;
  }
</style>

</head>
<body>
  <%@ include file="/header.jsp" %>
  <%@ include file="/sideNavbar.jsp" %>

  <main class="content">
    <div class="container-fluid">
      <div class="page-shell">

        <!-- 상단 타이틀 -->
        <div class="d-flex align-items-center justify-content-between mb-3">
          <div>
            <h4 class="mb-0"><b>성적 조회</b></h4>
            <small class="text-muted">
              직전학기 성적 및 전체 성적 이력을 확인할 수 있습니다.
            </small>
          </div>
        </div>

        <!-- 탭 -->
        <ul class="nav nav-tabs mb-3">
          <li class="nav-item">
            <!-- recent 탭: page/keyword 제거 -->
            <a class="nav-link ${tab == 'recent' ? 'active' : ''}"
               href="<c:url value='/student/grade/list'><c:param name='tab' value='recent'/></c:url>">
              직전학기 성적
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link ${tab == 'all' ? 'active' : ''}"
               href="<c:url value='/student/grade/list'><c:param name='tab' value='all'/></c:url>">
              전체 성적 이력
            </a>
          </li>
        </ul>

        <!-- recent 탭 -->
        <c:if test="${tab == 'recent'}">

          <!-- 안내 카드 -->
          <div class="card shadow-sm border-0 mb-3">
            <div class="card-body">
              <div class="d-flex flex-wrap align-items-center justify-content-between gap-2">
                <div>
                  <div class="fw-semibold">
                    조회 대상 학기: <span class="text-success">${targetYear}년 ${targetSemester}학기</span>
                  </div>
                  <div class="text-muted subline mt-1">
                    <c:choose>
                      <c:when test="${isEvalPeriod}">
                        * 현재는 <b>강의평가 기간</b>입니다. 평가 완료 시 성적이 즉시 공개됩니다.
                      </c:when>
                      <c:otherwise>
                        * 강의평가 기간이 아니므로 성적이 공개됩니다.
                      </c:otherwise>
                    </c:choose>
                  </div>
                </div>

                <c:if test="${isEvalPeriod}">
                  <span class="badge rounded-pill text-bg-warning">강의평가 기간</span>
                </c:if>
              </div>

              <!-- 잠금 경고 -->
              <c:if test="${isEvalPeriod && !recentUnlocked}">
                <div class="alert alert-warning mt-3 mb-0">
                  강의평가를 완료해야 성적을 조회할 수 있습니다.
                  <span class="ms-2">미완료 강의 수: <b><c:out value="${missingEvalCount}" /></b></span>
                </div>
              </c:if>
            </div>
          </div>

          <!-- 성적 테이블 -->
          <div class="card shadow-sm border-0">
            <div class="table-responsive">
              <table class="table table-hover align-middle mb-0 text-center grade-table">
                <thead class="table-light">
                  <tr>
                    <th style="width:110px;">강의코드</th>
                    <th style="width:clamp(220px, 30vw, 360px);">강의명</th>
                    <th style="width:140px;">교수</th>
                    <th style="width:140px;">분반</th>
                    <th style="width:140px;">성적</th>
                    <th style="width:140px;">재수강 여부</th>
                  </tr>
                </thead>
                <tbody>
                 <!-- 잠금이면 목록 숨김 -->
                  <c:choose>
                    <c:when test="${isEvalPeriod && !recentUnlocked}">
                      <tr>
                        <td colspan="6" class="text-center text-muted py-5">
                          강의평가 완료 후 성적이 공개됩니다.
                        </td>
                      </tr>
                    </c:when>

                    <c:when test="${empty recentList}">
                      <tr>
                        <td colspan="6" class="text-center text-muted py-5">
                          조회 결과가 없습니다.
                        </td>
                      </tr>
                    </c:when>

                    <c:otherwise>
                      <c:forEach var="row" items="${recentList}">
                        <tr>
                          <!-- 강의코드 -->
                          <td class="fw-semibold">
                            <span class="badge rounded-pill badge-soft-lime">
                              <c:out value='${row.lectureCodeDisplay}'/>
                            </span>
                          </td>

                          <!-- 강의명 -->
                          <td class="text-center">
                            <span class="fw-semibold truncate" title="<c:out value='${row.lectureName}'/>">
                              <c:out value="${row.lectureName}"/>
                            </span>
                          </td>
													
													<!-- 교수 -->
                          <td><c:out value='${row.professorName}'/></td>
                          
                          <!-- 분반 -->
                          <td><c:out value='${row.lectureSection}'/></td>

                          <!-- 성적 -->
                          <td>
                            <c:choose>
                              <c:when test="${empty row.lectureScore}">
                                <span class="text-muted">미입력</span>
                              </c:when>

                              <c:otherwise>
                                <span class="badge rounded-pill badge-grade fs-6 px-2 py-1">
                                  <c:choose>
                                    <c:when test="${row.lectureScore >= 4.5}">A+ (${row.lectureScore})</c:when>
                                    <c:when test="${row.lectureScore >= 4.0}">A (${row.lectureScore})</c:when>
                                    <c:when test="${row.lectureScore >= 3.5}">B+ (${row.lectureScore})</c:when>
                                    <c:when test="${row.lectureScore >= 3.0}">B (${row.lectureScore})</c:when>
                                    <c:when test="${row.lectureScore >= 2.5}">C+ (${row.lectureScore})</c:when>
                                    <c:when test="${row.lectureScore >= 2.0}">C (${row.lectureScore})</c:when>
                                    <c:when test="${row.lectureScore >= 1.5}">D+ (${row.lectureScore})</c:when>
                                    <c:when test="${row.lectureScore >= 1.0}">D (${row.lectureScore})</c:when>
                                    <c:otherwise>F (${row.lectureScore})</c:otherwise>
                                  </c:choose>
                                </span>
                              </c:otherwise>
                            </c:choose>
                          </td>

                          <!-- 재수강 여부 -->
                          <td>
                            <c:choose>
                              <c:when test="${empty row.lectureScore}">
                                <span class="badge rounded-pill bg-secondary">미정</span>
                              </c:when>
                              <c:when test="${row.lectureScore < 3.5}">
                                <span class="badge rounded-pill bg-info">재수강 가능</span>
                              </c:when>
                              <c:otherwise>
                                <span class="badge rounded-pill bg-light text-dark border">재수강 불가</span>
                              </c:otherwise>
                            </c:choose>
                          </td>
                        </tr>
                      </c:forEach>
                    </c:otherwise>
                  </c:choose>
                </tbody>
              </table>
            </div>
          </div>

        </c:if>

        <!-- all 탭 -->
        <c:if test="${tab == 'all'}">

          <!-- 검색 카드 -->
          <div class="card shadow-sm border-0 mb-3">
            <div class="card-body">
              <form method="get" action="<c:url value='/student/grade/list'/>" class="row g-2 align-items-center">
                <input type="hidden" name="tab" value="all"/>
                <div class="col-md-8">
                  <input type="text" name="keyword" class="form-control"
                         placeholder="강의명 또는 교수명 검색"
                         value="<c:out value='${keyword}'/>">
                </div>
                <div class="col-md-4 d-flex gap-2">
                  <button type="submit" class="btn btn-success w-100">검색</button>
                  <a class="btn btn-outline-secondary w-100"
                     href="<c:url value='/student/grade/list'><c:param name='tab' value='all'/></c:url>">초기화</a>
                </div>
              </form>
              <div class="form-text mt-2">* 검색은 강의명/교수명 기준이며, 페이지당 10개씩 표시됩니다.</div>
            </div>
          </div>
          
          <div class="alert alert-light border small text-muted mb-3">
					  ※ 전체 성적 이력에는 <b>직전학기 성적은 포함되지 않습니다.</b>
					  직전학기 성적은 상단 탭에서 확인할 수 있습니다.
					</div>
          
          <!-- 테이블 -->
          <div class="card shadow-sm border-0">
            <div class="table-responsive">
              <table class="table table-hover align-middle mb-0 text-center grade-table">
                <thead class="table-light">
                  <tr>
                    <th style="width:110px;">강의코드</th>
                    <th style="width:clamp(220px, 30vw, 360px);">강의명</th>
                    <th style="width:140px;">교수</th>
                    <th style="width:140px;">분반</th>
                    <th style="width:140px;">성적</th>
                    <th style="width:140px;">재수강 여부</th>
                  </tr>
                </thead>

                <tbody>
                  <c:choose>
                    <c:when test="${empty allList}">
                      <tr>
                        <td colspan="6" class="text-center text-muted py-5">
                          조회 결과가 없습니다.
                        </td>
                      </tr>
                    </c:when>

                    <c:otherwise>
                      <c:forEach var="row" items="${allList}">
                        <tr>
                          <td class="fw-semibold">
                            <span class="badge rounded-pill badge-soft-lime">
                              <c:out value="${row.lectureCodeDisplay}"/>
                            </span>
                          </td>

                          <td class="text-center">
                            <span class="fw-semibold truncate" title="<c:out value='${row.lectureName}'/>">
                              <c:out value="${row.lectureName}"/>
                            </span>
                            <div class="text-muted truncate subline">
                              <c:out value="${row.lectureYear}"/> - <c:out value="${row.lectureSemester}"/>
                            </div>
                          </td>
													
                          <td><c:out value="${row.professorName}"/></td>
													
                          <td><c:out value="${row.lectureSection}"/></td>

                          <td>
                            <c:choose>
                              <c:when test="${empty row.lectureScore}">
                                <span class="text-muted">미입력</span>
                              </c:when>
                              <c:otherwise>
                                <span class="badge rounded-pill badge-grade fs-6 px-2 py-1">
                                  <c:choose>
                                    <c:when test="${row.lectureScore >= 4.5}">A+ (${row.lectureScore})</c:when>
                                    <c:when test="${row.lectureScore >= 4.0}">A (${row.lectureScore})</c:when>
                                    <c:when test="${row.lectureScore >= 3.5}">B+ (${row.lectureScore})</c:when>
                                    <c:when test="${row.lectureScore >= 3.0}">B (${row.lectureScore})</c:when>
                                    <c:when test="${row.lectureScore >= 2.5}">C+ (${row.lectureScore})</c:when>
                                    <c:when test="${row.lectureScore >= 2.0}">C (${row.lectureScore})</c:when>
                                    <c:when test="${row.lectureScore >= 1.5}">D+ (${row.lectureScore})</c:when>
                                    <c:when test="${row.lectureScore >= 1.0}">D (${row.lectureScore})</c:when>
                                    <c:otherwise>F (${row.lectureScore})</c:otherwise>
                                  </c:choose>
                                </span>
                              </c:otherwise>
                            </c:choose>
                          </td>

                          <td>
                            <c:choose>
                              <c:when test="${empty row.lectureScore}">
                                <span class="badge rounded-pill bg-secondary">미정</span>
                              </c:when>
                              <c:when test="${row.lectureScore < 3.5}">
                                <span class="badge rounded-pill bg-info">재수강 가능</span>
                              </c:when>
                              <c:otherwise>
                                <span class="badge rounded-pill bg-light text-dark border">재수강 불가</span>
                              </c:otherwise>
                            </c:choose>
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
          <c:if test="${lastPage > 1}">
            <nav class="mt-3">
              <ul class="pagination justify-content-center">

                <!-- 이전(한 칸) -->
                <li class="page-item ${page <= 1 ? 'disabled' : ''}">
                  <a class="page-link"
                     href="<c:url value='/student/grade/list'>
                            <c:param name='tab' value='all'/>
                            <c:param name='page' value='${page - 1}'/>
                            <c:if test='${not empty keyword}'>
                              <c:param name='keyword' value='${keyword}'/>
                            </c:if>
                          </c:url>">
                    &laquo;
                  </a>
                </li>

                <!-- 번호 -->
                <c:forEach var="p" begin="${navStart}" end="${navEnd}">
                  <li class="page-item ${p == page ? 'active' : ''}">
                    <a class="page-link"
                       href="<c:url value='/student/grade/list'>
                              <c:param name='tab' value='all'/>
                              <c:param name='page' value='${p}'/>
                              <c:if test='${not empty keyword}'>
                                <c:param name='keyword' value='${keyword}'/>
                              </c:if>
                            </c:url>">
                      ${p}
                    </a>
                  </li>
                </c:forEach>

                <!-- 다음(한 칸) -->
                <li class="page-item ${page >= lastPage ? 'disabled' : ''}">
                  <a class="page-link"
                     href="<c:url value='/student/grade/list'>
                            <c:param name='tab' value='all'/>
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

        </c:if>

      </div>
    </div>
  </main>

  <%@ include file="/footer.jsp" %>

  <c:if test="${not empty param.error}">
    <c:choose>
      <c:when test="${param.error == 'noDTO'}">
        <script type="text/javascript">
          alert("로그인 후 이용 가능합니다.");
        </script>
      </c:when>
      <c:when test="${param.error == 'NoAccessEnrollTime'}">
        <script type="text/javascript">
          alert("이용 가능한 기간이 아닙니다.");
        </script>
      </c:when>
    </c:choose>
  </c:if>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
