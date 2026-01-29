let currentRole = 'STUDENT';

function toggleRole(role) {
	currentRole = role;
	const label = document.getElementById('idLabel');
	const input = document.getElementById('mainIdInput');
	const pwArea = document.getElementById('professorPwArea');
	const rrnArea = document.getElementById('studentRrnArea');
	const helpText = document.getElementById('idHelpText');

	input.value = ""; // 역할 바뀔 때 입력값 초기화

	if (role === 'STUDENT') {
		label.innerText = "학번";
		input.placeholder = "9자리 숫자";
		input.maxLength = 9; 
		pwArea.style.display = 'none';
		rrnArea.style.display = 'block';
		if (helpText) helpText.style.display = 'inline';
	} else {
		label.innerText = "교수 이메일";
		input.placeholder = "example@goodee.ac.kr";
		input.maxLength = 30; 
		pwArea.style.display = 'block';
		rrnArea.style.display = 'none';
		if (helpText) helpText.style.display = 'none';
	}
}

function validateIdInput(el) {
	if (currentRole === 'STUDENT') {
		el.value = el.value.replace(/[^0-9]/g, '').substring(0, 9); 	// 학생일 때만 숫자 이외의 문자 제거 및 9자리 제한
	}
}

function showMessage(msg, type = 'danger') {
	const area = document.getElementById('message-area');
	const wrapper = document.createElement('div');
	wrapper.className = `alert alert-${type} alert-dismissible fade show shadow-sm`;
	wrapper.role = 'alert';

	const iconClass = type === 'danger' ? 'bi-exclamation-triangle' : 'bi-info-circle';

	wrapper.innerHTML = `
        <i class="bi ${iconClass} me-2"></i>
        <span></span>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `;

	wrapper.querySelector('span').innerText = msg;

	area.innerHTML = '';
	area.appendChild(wrapper);
	window.scrollTo({ top: 0, behavior: 'smooth' });
}

function handleResetSubmit() {
	const idVal = document.getElementById('mainIdInput').value;
	const form = document.getElementById('resetForm');

	if (currentRole === 'STUDENT') {
		const front = document.getElementById('rrn-front').value;
		const back = document.getElementById('rrn-back').value;

		if (!idVal || !/^\d{9}$/.test(idVal)) {
			showMessage("학번은 숫자 9자리여야 합니다.");
			return;
		}
		if (front.length !== 6 || back.length !== 7) {
			showMessage("주민번호를 정확히 입력해 주세요.");
			return;
		}

		document.getElementById('studentIdentityNum').value = front + "-" + back;
		document.getElementById('hiddenUserId').value = idVal;
		document.getElementById('hiddenProfessorEmail').disabled = true;
		document.getElementById('studentIdentityNum').disabled = false;
	} else {
		const newPw = document.getElementById('newPassword').value;
		const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}$/;

		if (!idVal || !idVal.includes('@')) {
			showMessage("유효한 이메일을 입력하세요.");
			return;
		}
		if (!newPw || !pwRegex.test(newPw)) {
			showMessage("비밀번호는 6자 이상, 영문/숫자 조합이어야 합니다.");
			return;
		}

		document.getElementById('hiddenProfessorEmail').value = idVal;
		document.getElementById('hiddenUserId').disabled = true;
		document.getElementById('studentIdentityNum').disabled = true;
	}
	form.submit();
}