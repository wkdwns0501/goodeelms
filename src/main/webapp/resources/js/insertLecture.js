/**
 * 
 * 
 */

(function () {
  const form = document.querySelector("#lectureForm");
  if (!form) return;

  // 요소들
  const nameEl = document.getElementById("lecture_name");
  const typeEl = document.getElementById("lecture_type");
  const creditEl = document.getElementById("lecture_credit");
  const yearEl = document.getElementById("lecture_year");
  const semesterEl = document.getElementById("lecture_semester");
  const capacityEl = document.getElementById("lecture_capacity");
  const descEl = document.getElementById("lecture_description");
  const buildingEl = document.getElementById("building_id");
  const roomEl = document.getElementById("lecture_room");
  const roomGuide = document.getElementById("roomGuide");

  // 에러 요소들
  const nameErr = document.getElementById("lectureNameError");
  const typeErr = document.getElementById("typeError");
  const creditErr = document.getElementById("creditError");
  const yearErr = document.getElementById("yearError");
  const semesterErr = document.getElementById("semesterError");
  const capacityErr = document.getElementById("capacityError");
  const descErr = document.getElementById("descError");
  const buildingErr = document.getElementById("buildingError");
  const roomErr = document.getElementById("roomError");

  // 설명 카운트
  const descCount = document.getElementById("descCount");

  // YYYY: 4자리(1900~2099)
  const YEAR_REGEX = /^(19|20)\d{2}$/;

  // 강의실 enable/disable
  function toggleRoomSelect() {
    if (!roomEl) return;

    const yearOk = yearEl && yearEl.value;
    const semesterOk = semesterEl && semesterEl.value;
    const buildingOk = buildingEl && buildingEl.value;

    if (yearOk && semesterOk && buildingOk) {
      roomEl.disabled = false;
      if (roomGuide) roomGuide.style.display = "none";
    } else {
      roomEl.disabled = true;
      roomEl.value = "";
      if (roomGuide) roomGuide.style.display = "block";
    }
  }

  if (yearEl) yearEl.addEventListener("input", toggleRoomSelect);
  if (semesterEl) semesterEl.addEventListener("change", toggleRoomSelect);
  if (buildingEl) buildingEl.addEventListener("change", toggleRoomSelect);

  toggleRoomSelect(); // 초기 실행

  // 설명 글자수 카운트
  function updateDesc() {
    if (!descEl || !descCount) return;

    const len = descEl.value.length;
    descCount.textContent = len;

    if (len > 1000) {
      if (descErr) descErr.classList.remove("d-none");
      descEl.classList.add("is-invalid");
    } else {
      if (descErr) descErr.classList.add("d-none");
      descEl.classList.remove("is-invalid");
    }
  }

  if (descEl) {
    descEl.addEventListener("input", updateDesc);
    updateDesc();
  }

  // 입력하면 에러 숨기기
  if (nameEl) nameEl.addEventListener("input", () => {
    if (nameErr) nameErr.classList.add("d-none");
    nameEl.classList.remove("is-invalid");
  });

  if (creditEl) creditEl.addEventListener("input", () => {
    if (creditErr) creditErr.classList.add("d-none");
    creditEl.classList.remove("is-invalid");
  });

  if (yearEl) yearEl.addEventListener("input", () => {
    if (yearErr) yearErr.classList.add("d-none");
    yearEl.classList.remove("is-invalid");
    toggleRoomSelect();
  });

  if (capacityEl) capacityEl.addEventListener("input", () => {
    if (capacityErr) capacityErr.classList.add("d-none");
    capacityEl.classList.remove("is-invalid");
  });

  // Select가 바뀌면 에러 숨기기
  if (typeEl) typeEl.addEventListener("change", () => {
    if (typeErr) typeErr.classList.add("d-none");
    typeEl.classList.remove("is-invalid");
  });

  if (semesterEl) semesterEl.addEventListener("change", () => {
    if (semesterErr) semesterErr.classList.add("d-none");
    semesterEl.classList.remove("is-invalid");
    toggleRoomSelect();
  });

  if (buildingEl) {
    buildingEl.addEventListener("change", () => {
      if (buildingErr) buildingErr.classList.add("d-none");
      buildingEl.classList.remove("is-invalid");
      toggleRoomSelect();
    });
  }
  
  if (roomEl) {
    roomEl.addEventListener("change", () => {
      if (roomErr) roomErr.classList.add("d-none");
      roomEl.classList.remove("is-invalid");
    });
  }

  //submit 때 전체 검증
  form.addEventListener("submit", function (e) {
    let ok = true;

    // 강의명 4~20
    if (!nameEl || nameEl.value.trim().length < 4 || nameEl.value.trim().length > 20) {
      if (nameErr) nameErr.classList.remove("d-none");
      if (nameEl) nameEl.classList.add("is-invalid");
      ok = false;
    }

    // 유형 선택
    if (!typeEl || !typeEl.value) {
      if (typeErr) typeErr.classList.remove("d-none");
      if (typeEl) typeEl.classList.add("is-invalid");
      ok = false;
    }

    // 학점 1~6
    const credit = Number(creditEl ? creditEl.value : NaN);
    if (!Number.isInteger(credit) || credit < 1 || credit > 6) {
      if (creditErr) creditErr.classList.remove("d-none");
      if (creditEl) creditEl.classList.add("is-invalid");
      ok = false;
    }

    // 년도 YYYY
    if (!yearEl || !YEAR_REGEX.test(yearEl.value.trim())) {
      if (yearErr) yearErr.classList.remove("d-none");
      if (yearEl) yearEl.classList.add("is-invalid");
      ok = false;
    }

    // 학기 선택
    if (!semesterEl || !semesterEl.value) {
      if (semesterErr) semesterErr.classList.remove("d-none");
      if (semesterEl) semesterEl.classList.add("is-invalid");
      ok = false;
    }

    // 정원 1~50
    const cap = Number(capacityEl ? capacityEl.value : NaN);
    if (!Number.isInteger(cap) || cap < 1 || cap > 50) {
      if (capacityErr) capacityErr.classList.remove("d-none");
      if (capacityEl) capacityEl.classList.add("is-invalid");
      ok = false;
    }

    // 설명 1000자 이하
    if (descEl && descEl.value.length > 1000) {
      if (descErr) descErr.classList.remove("d-none");
      descEl.classList.add("is-invalid");
      ok = false;
    }

    // 건물 선택
    if (!buildingEl || !buildingEl.value) {
      if (buildingErr) buildingErr.classList.remove("d-none");
      if (buildingEl) buildingEl.classList.add("is-invalid");
      ok = false;
    }

    // 강의실 선택 (3개 선택 전이면 disabled라서 여기서도 막아줌)
    if (!roomEl || roomEl.disabled || !roomEl.value) {
      if (roomEl) roomEl.classList.add("is-invalid");
      ok = false;
    }
	
	// 강의실 에러
	if (!roomEl || roomEl.disabled || !roomEl.value) {
	  if (roomErr) roomErr.classList.remove("d-none");
	  if (roomEl) roomEl.classList.add("is-invalid");
	  ok = false;
	}

    // ok = false 라면 기본이벤트 막기
    if (!ok) e.preventDefault();
  });
})();


// error alert 4초 후 자동 숨김
const errorAlert = document.getElementById("errorAlert");
if (errorAlert) {
  setTimeout(() => {
    errorAlert.classList.add("d-none");
  }, 4000);
}
