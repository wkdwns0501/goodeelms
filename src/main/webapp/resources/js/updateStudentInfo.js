/**
 * 
 * 
 */
// 인적사항 수정 검증
(function () {
  const form = document.getElementById("profileForm");
  if (!form) return;

  // 요소들
  const phone1El = document.getElementById("phone1");
  const phone2El = document.getElementById("phone2");
  const phone3El = document.getElementById("phone3");
  const phoneHiddenEl = document.getElementById("studentPhoneHidden");

  const emailIdEl = document.getElementById("emailId");
  const emailDomainEl = document.getElementById("emailDomain");
  const emailHiddenEl = document.getElementById("studentEmailHidden");
  const accountEl = document.getElementById("accountNumber");
  const addressEl = document.getElementById("studentAddress");
  const confirmPwEl = document.getElementById("confirmPassword");

	const accountRegex = /^\d{3}-\d{7}$/;

  // 에러 요소들
  const phoneErr = document.getElementById("phoneError");
  const emailErr = document.getElementById("emailError");
  const addressErr = document.getElementById("addressError");
  const accountErr = document.getElementById("accountError");
  const confirmPwErr = document.getElementById("confirmPwError");

  // 에러 표시/숨김
  function showInvalid(el, errEl) {
    if (el) el.classList.add("is-invalid");
    if (errEl) {
			errEl.classList.remove("d-none");
			errEl.classList.add("d-block");
		}
  }
  function hideInvalid(el, errEl) {
    if (el) el.classList.remove("is-invalid");
    if (errEl) {
			errEl.classList.add("d-none");
			errEl.classList.remove("d-block");
			//errEl.textContent = "";
	  }
  }

  // 전화번호 숫자만 필터링
  function onlyDigits(s) {
    return (s || "").replace(/\D/g, "");
  }

  // 입력하면 에러 숨기기 + 숫자만 유지
  if (phone2El) {
    phone2El.addEventListener("input", () => {
      phone2El.value = onlyDigits(phone2El.value).slice(0, 4);
      hideInvalid(phone2El, phoneErr);
      hideInvalid(phone3El, phoneErr);
    });
  }
  if (phone3El) {
    phone3El.addEventListener("input", () => {
      phone3El.value = onlyDigits(phone3El.value).slice(0, 4);
      hideInvalid(phone2El, phoneErr);
      hideInvalid(phone3El, phoneErr);
    });
  }
  if (phone1El) {
    phone1El.addEventListener("change", () => {
      hideInvalid(phone2El, phoneErr);
      hideInvalid(phone3El, phoneErr);
    });
  }

  // 이메일 입력하면 에러 숨기기
  if (emailIdEl) {
    emailIdEl.addEventListener("input", () => {
      hideInvalid(emailIdEl, emailErr);
    });
  }
  if (emailDomainEl) {
    emailDomainEl.addEventListener("change", () => {
      hideInvalid(emailIdEl, emailErr);
    });
  }
	
	if (addressEl) addressEl.addEventListener("input", () => hideInvalid(addressEl, addressErr));
	if (accountEl) accountEl.addEventListener("input", () => hideInvalid(accountEl, accountErr));
	if (confirmPwEl) confirmPwEl.addEventListener("input", () => hideInvalid(confirmPwEl, confirmPwErr));
	
	// submit 때 전체 검증
	form.addEventListener("submit", function (e) {
	  let ok = true;
		
		// phone
	  const p1 = phone1El ? phone1El.value : "";
	  const p2 = onlyDigits(phone2El ? phone2El.value : "");
	  const p3 = onlyDigits(phone3El ? phone3El.value : "");

	  // 일단 둘 다 에러 숨김 (초기화)
	  hideInvalid(phone2El, phoneErr);
	  hideInvalid(phone3El, phoneErr);

	  // 각각 따로 체크
		if (p2.length !== 4) { showInvalid(phone2El, phoneErr); ok = false; }
		if (p3.length !== 4) { showInvalid(phone3El, phoneErr); ok = false; }

	  // 둘 다 OK일 때만 hidden 조합
	  if (ok && phoneHiddenEl) phoneHiddenEl.value = p1 + "-" + p2 + "-" + p3;

	  // email
	  const id = emailIdEl ? emailIdEl.value.trim() : "";
	  const domain = emailDomainEl ? emailDomainEl.value : "";

	  hideInvalid(emailIdEl, emailErr);

	  if (!id) {
	    showInvalid(emailIdEl, emailErr);
	    ok = false;
	  } else if (emailHiddenEl) {
	    emailHiddenEl.value = id + "@" + domain;
	  }
		
		// address
		const addr = addressEl ? addressEl.value.trim() : "";
		if (!addr || addr.length > 255) {
		  showInvalid(addressEl, addressErr);
		  ok = false;
		}

		// account (공백 제거 후 검사)
		const acc = accountEl ? accountEl.value.replace(/\s+/g, "") : "";
		if (!accountRegex.test(acc)) {
		  showInvalid(accountEl, accountErr);
		  ok = false;
		} else {
		  accountEl.value = acc;
		}
		
		// 현재 비밀번호 확인
		const confirmPw = confirmPwEl ? confirmPwEl.value.trim() : "";
		if (!confirmPw) {
		  showInvalid(confirmPwEl, confirmPwErr);
		  if (confirmPwErr) confirmPwErr.textContent = "현재 비밀번호를 입력해주세요.";
		  ok = false;
		}

	  if (!ok) e.preventDefault();
	});
})();

// 비밀번호 검증
(function () {
  const form = document.getElementById("pwForm");
  if (!form) return;

  const curEl = document.querySelector("#pwForm input[name='currentPassword']");
  const newEl = document.querySelector("#pwForm input[name='newPassword']");

  const curErr = document.getElementById("currentPwError");
  const newErr = document.getElementById("newPwError");

  // 영소문자 + 숫자 6자리 이상
  const newPwRegex = /^(?=.*[a-z])(?=.*\d)[a-z\d]{6,}$/;

  function showInvalid(el, errEl) {
    if (el) el.classList.add("is-invalid");
    if (errEl) errEl.classList.remove("d-none");
  }
  function hideInvalid(el, errEl) {
    if (el) el.classList.remove("is-invalid");
    if (errEl) {
			errEl.classList.add("d-none");
			//errEl.textContent = "";
		}
  }

  if (curEl) curEl.addEventListener("input", () => hideInvalid(curEl, curErr));
  if (newEl) newEl.addEventListener("input", () => hideInvalid(newEl, newErr));

	form.addEventListener("submit", function (e) {
	  let ok = true;

	  const cur = curEl ? curEl.value : "";
	  const npw = newEl ? newEl.value : "";

	  hideInvalid(curEl, curErr);
	  hideInvalid(newEl, newErr);

	  // 현재 비번 빈 값 검증
	  if (!cur) {
	    showInvalid(curEl, curErr);
	    ok = false;
	  }

	  // 새 비번 형식 검증
	  if (!newPwRegex.test(npw)) {
	    showInvalid(newEl, newErr);
	    newErr.textContent = "새 비밀번호는 영문 소문자와 숫자를 포함한 6자 이상이어야 합니다.";
	    ok = false;
	  }

	  // 현재 비번과 동일 검증
	  if (cur && npw && cur === npw) {
	    showInvalid(newEl, newErr);
	    newErr.textContent = "새 비밀번호는 현재 비밀번호와 다르게 입력해주세요.";
	    ok = false;
	  }

	  if (!ok) e.preventDefault();
	});

})();


