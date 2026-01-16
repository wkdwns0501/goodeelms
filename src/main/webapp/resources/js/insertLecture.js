/**
 * 
 * 
 */
console.log("insertLecture.js loaded");

(function () {
  const form = document.querySelector("#lectureForm");
  if (!lectureForm) return;

  // 요소들
  const nameEl = document.getElementById("lecture_name");
  const typeEl = document.getElementById("lecture_type");
  const creditEl = document.getElementById("lecture_credit");
  const yearEl = document.getElementById("lecture_year");
  const semesterEl = document.getElementById("lecture_semester");
  const capacityEl = document.getElementById("lecture_capacity");
  const descEl = document.getElementById("lecture_description");
  const buildingEl = document.getElementById("building_id");

  // 에러 메세지 요소들
  const nameErr = document.getElementById("lectureNameError");
  const typeErr = document.getElementById("typeError");
  const creditErr = document.getElementById("creditError");
  const yearErr = document.getElementById("yearError");
  const semesterErr = document.getElementById("semesterError");
  const capacityErr = document.getElementById("capacityError");
  const descErr = document.getElementById("descError");
  const buildingErr = document.getElementById("buildingError");

  // 설명 카운트
  const descCount = document.getElementById("descCount");

  // YYYY: 4자리(1900~2099)
  const YEAR_REGEX = /^(19|20)\d{2}$/;

  // 설명 글자수 카운트
  function updateDesc() {
    if (!descEl || !descCount) return;
    const len = descEl.value.length;
    descCount.textContent = len;

    if (len > 1000) {
      descErr && descErr.classList.remove("d-none");
      descEl.classList.add("is-invalid");
    } else {
      descErr && descErr.classList.add("d-none");
      descEl.classList.remove("is-invalid");
    }
  }
	
  if (descEl) {
    descEl.addEventListener("input", updateDesc);
    updateDesc();
  }

  // 입력하면 에러 숨기기
  if (nameEl) nameEl.addEventListener("input", () => { nameErr.classList.add("d-none"); nameEl.classList.remove("is-invalid"); });
  if (creditEl) creditEl.addEventListener("input", () => { creditErr.classList.add("d-none"); creditEl.classList.remove("is-invalid"); });
  if (yearEl) yearEl.addEventListener("input", () => { yearErr.classList.add("d-none"); yearEl.classList.remove("is-invalid"); });
  if (capacityEl) capacityEl.addEventListener("input", () => { capacityErr.classList.add("d-none"); capacityEl.classList.remove("is-invalid"); });
	
	// Select가 바뀌면 에러 숨기기
  if (typeEl) typeEl.addEventListener("change", () => { typeErr.classList.add("d-none"); typeEl.classList.remove("is-invalid"); });
  if (semesterEl) semesterEl.addEventListener("change", () => { semesterErr.classList.add("d-none"); semesterEl.classList.remove("is-invalid"); });
	if (buildingEl) {
	  buildingEl.addEventListener("change", () => {
	    if (buildingErr) buildingErr.classList.add("d-none");
	    buildingEl.classList.remove("is-invalid");
	  });
	}


  // submit 때 전체 검증
  form.addEventListener("submit", function (e) {
    let ok = true;

    // 강의명 4~20
    if (!nameEl || nameEl.value.trim().length < 4 || nameEl.value.trim().length > 20) {
      nameErr.classList.remove("d-none");
      nameEl.classList.add("is-invalid");
      ok = false;
    }

    // 유형 선택
    if (!typeEl || !typeEl.value) {
      typeErr.classList.remove("d-none");
      typeEl.classList.add("is-invalid");
      ok = false;
    }

    // 학점 1~6
    const credit = Number(creditEl ? creditEl.value : NaN);
    if (!Number.isInteger(credit) || credit < 1 || credit > 6) {
      creditErr.classList.remove("d-none");
      creditEl.classList.add("is-invalid");
      ok = false;
    }

    // 년도 YYYY
    if (!yearEl || !YEAR_REGEX.test(yearEl.value.trim())) {
      yearErr.classList.remove("d-none");
      yearEl.classList.add("is-invalid");
      ok = false;
    }

    // 학기 선택
    if (!semesterEl || !semesterEl.value) {
      semesterErr.classList.remove("d-none");
      semesterEl.classList.add("is-invalid");
      ok = false;
    }

    // 정원 1~50
    const cap = Number(capacityEl ? capacityEl.value : NaN);
    if (!Number.isInteger(cap) || cap < 1 || cap > 50) {
      capacityErr.classList.remove("d-none");
      capacityEl.classList.add("is-invalid");
      ok = false;
    }

    // 설명 1000자 이하(선택)
    if (descEl && descEl.value.length > 1000) {
      descErr.classList.remove("d-none");
      descEl.classList.add("is-invalid");
      ok = false;
    }

    // 건물 선택
    if (!buildingEl || !buildingEl.value) {
      buildingErr.classList.remove("d-none");
      buildingEl.classList.add("is-invalid");
      ok = false;
    }
		
		// ok = false 라면 기본이벤트 막기
    if (!ok) e.preventDefault();
  });
})();
