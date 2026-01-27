<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 작성</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
<link rel="stylesheet" href="<c:url value='/resources/css/layout.css'/>" />

<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-lite.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-lite.min.js"></script>

<style>
  .write-container { max-width: 1200px; margin: 0 auto; }
  /* 에디터 내부 글꼴 설정 */
  .note-editable { background-color: white; font-size: 1rem; }
  .card-header { font-weight: bold; }
  .active > .page-link {
    background-color: #0d6efd;
    border-color: #0d6efd;
  }
  /* 화살표가 두 개씩 나오는 버그 수정 */
  .note-editor .dropdown-toggle::after {
      display: none;
  }
</style>
</head>
<body>
    <%@ include file="/header.jsp" %>
    <%@ include file="/sideNavbar.jsp" %>
  
    <main class="content">
        <div class="container py-4">
            <div class="write-container">
                <div class="card shadow-sm">
                    <div class="card-header bg-primary text-white py-3">
                        <h5 class="mb-0">게시글 작성</h5>
                    </div>
                    
                    <div class="card-body p-4">
                        <form action="<c:url value='/common/board/admin/insert'/>" method="post" onsubmit="return validateForm()">
                            
                            <div class="mb-4">
                                <label for="title" class="form-label fw-bold">제목</label>
                                <input type="text" class="form-control" id="title" name="boardTitle" 
                                       placeholder="공지사항 제목을 입력하세요">
                            </div>

                            <div class="mb-4 d-flex align-items-center">
                                <div class="form-check form-switch">
                                    <input class="form-check-input" type="checkbox" id="isImportant" name="isImportant" value="Y">
                                    <label class="form-check-label text-danger fw-bold" for="isImportant">상단 고정</label>
                                </div>
                            </div>

                            <div class="mb-4">
                                <label class="form-label fw-bold">내용</label>
                                <textarea id="summernote" name="boardContent"></textarea>
                            </div>

                            <hr class="my-4">

                            <div class="d-flex justify-content-center gap-3">
                                <button type="button" class="btn btn-outline-secondary px-4" onclick="history.back()">취소</button>
                                <button type="submit" class="btn btn-primary px-5">등록하기</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <%@ include file="/footer.jsp" %>

    <script>
      $(document).ready(function() {
        $('#summernote').summernote({
          placeholder: '공지내용을 상세히 작성해 주세요. (이미지 첨부 가능)',
          tabsize: 2,
          height: 400,
          lang: 'ko-KR',
          toolbar: [
            // [툴바 구성: 글체, 크기, 색상 등]
            ['style', ['style']],
            ['font', ['bold', 'underline', 'clear']],
            ['fontsize', ['fontsize']],
            ['color', ['color']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['table', ['table']],
            ['insert', ['link', 'picture']],
            ['view', ['codeview', 'help']]
          ]
        });
      });
      
      function validateForm() {
    	    const title = $('#title').val().trim();
    	    // Summernote의 내용을 가져옴
    	    const content = $('#summernote').summernote('code');
    	    
    	    // 제목 검사
    	    if (title === "") {
    	        alert("제목을 입력해 주세요.");
    	        $('#title').focus();
    	        return false;
    	    }

    	    // 내용 검사 (태그를 제외한 순수 텍스트가 있는지 확인)
    	    // 에디터가 비어있어도 기본적으로 <p><br></p> 등이 들어있을 수 있으므로 텍스트만 추출해 검사합니다.
    	    const plainText = $('<div>').html(content).text().trim();
    	    
    	    if (plainText === "" && content.indexOf('<img') === -1) {
    	        // 텍스트도 없고 이미지도 없는 경우
    	        alert("내용을 입력해 주세요.");
    	        $('#summernote').summernote('focus');
    	        return false;
    	    }

    	    return confirm('등록하시겠습니까?'); // 모두 통과 시 제출
    	}
    </script>
</body>
</html>