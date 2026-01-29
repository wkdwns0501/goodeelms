document.addEventListener("DOMContentLoaded", function() {
    const links = document.querySelectorAll('a');  // 헤더는 마이페이지만 클릭 안되게
    links.forEach(link => {
        if (link.textContent.trim() === '마이페이지') {
            link.style.pointerEvents = 'none';
            link.style.opacity = '0.5';
            link.classList.add('disabled');
        }
    });

    const bankAccountInput = document.getElementById('bankAccount');
    if (bankAccountInput) {
        bankAccountInput.addEventListener('input', function(e) {
            let value = e.target.value.replace(/[^0-9]/g, ''); 
            if (value.length > 3) {
                value = value.slice(0, 3) + '-' + value.slice(3, 10);
            }
            e.target.value = value;
        });
    }

    [document.getElementById('phone2'), document.getElementById('phone3')].forEach(input => {
        if (input) {
            input.addEventListener('input', function(e) {
                e.target.value = e.target.value.replace(/[^0-9]/g, '');
            });
        }
    });

    const signupForm = document.getElementById('signupForm');
    if (signupForm) {
        signupForm.onsubmit = function(e) {
            const errorArea = document.getElementById('error-message-area');
            const showError = (msg) => {
                errorArea.innerHTML = `
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="bi bi-exclamation-triangle"></i> ${msg}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>`;
                window.scrollTo(0, 0);
            };

            const originPw = document.getElementById('originPw').value;
            const newPw = document.getElementById('newPw').value;
            const confirmNewPw = document.getElementById('confirmNewPw').value;
            const p2 = document.getElementById('phone2').value;
            const p3 = document.getElementById('phone3').value;
            const eid = document.getElementById('emailId').value;
            const addr = document.getElementById('studentAddress').value;
            const bAccount = document.getElementById('bankAccount').value;
            const newPwRegex = /^(?=.*[a-z])(?=.*\d)[a-z\d]{6,}$/;

            if (!originPw || !newPw) {
                showError("비밀번호는 필수 입력 사항입니다.");
                return false;
            }
            if (newPw !== confirmNewPw) {
                showError("새 비밀번호 확인이 일치하지 않습니다.");
                return false;
            }
            if (newPw === originPw) {
                showError("새 비밀번호는 기존 비밀번호와 다르게 설정해야 합니다.");
                return false;
            }
            if (!newPwRegex.test(newPw)) {
                showError("새 비밀번호는 영문 소문자와 숫자를 포함한 6자 이상이어야 합니다.");
                return false;
            }
            if (!/^\d{3,4}$/.test(p2) || !/^\d{4}$/.test(p3)) {
                showError("연락처를 올바르게 입력해주세요.");
                return false;
            }
            if (!eid) {
                showError("이메일 아이디를 입력해주세요.");
                return false;
            }
            if (!/^\d{3}-\d{7}$/.test(bAccount)) {
                showError("계좌번호 형식을 확인해주세요 (000-0000000).");
                return false;
            }
            if (addr.trim().length < 5) {
                showError("주소를 상세히 입력해주세요.");
                return false;
            }

            document.getElementById('studentPhoneHidden').value =
                document.getElementById('phone1').value + "-" + p2 + "-" + p3;
            document.getElementById('studentEmailHidden').value =
                eid + "@" + document.getElementById('emailDomain').value;
            document.getElementById('studentBankHidden').value =
                document.getElementById('bankName').value + " " + bAccount;

            return true;
        };
    }
});