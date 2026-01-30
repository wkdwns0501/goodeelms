<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<footer class="app-footer d-flex align-items-center px-3">
	<div class="d-flex gap-3" id="hiddenTimeChange">
	  <div>© 2026 LMS Project</div>
	  <div id="timeView">${sessionScope.time}</div>
	</div>
  <div class="ms-auto">문의: admin@lms.local</div>
</footer>

<div class="modal fade" id="datePickerModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-sm modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header py-2">
        <h6 class="modal-title">날짜 선택</h6>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <div class="modal-body">
        <div class="row g-2 align-items-center">
          <div class="col-4">
            <select class="form-select form-select-sm" id="yearSelect"></select>
          </div>
          <div class="col-1 text-center small text-muted">년</div>

          <div class="col-3">
            <select class="form-select form-select-sm" id="monthSelect"></select>
          </div>
          <div class="col-1 text-center small text-muted">월</div>

          <div class="col-3">
            <select class="form-select form-select-sm" id="daySelect"></select>
          </div>
          <div class="col-1 text-center small text-muted">일</div>
        </div>

        <!-- 선택 결과 출력 (원하면 제거 가능) -->
        <div class="mt-3">
          <input type="text" class="form-control form-control-sm" id="pickedDate"
                 placeholder="yyyy-MM-dd" readonly>
        </div>
      </div>

      <div class="modal-footer py-2">
        <button type="button" class="btn btn-sm btn-outline-secondary" data-bs-dismiss="modal">취소</button>
        <button type="button" class="btn btn-sm btn-primary" id="confirmDateBtn">확인</button>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script type="text/javascript" src="/resources/js/timeChange.js"></script>
