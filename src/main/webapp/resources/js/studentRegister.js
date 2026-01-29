/**
 * 학생 등록 페이지 유효성 검사 전용 스크립트
 */

document.addEventListener("DOMContentLoaded", function() {
    
    // 1. 모든 입력 요소에 '입력 시 에러 해제' 이벤트 연결
    const allInputs = document.querySelectorAll('.form-control, .form-select');
    allInputs.forEach(input => {
        input.addEventListener("input", () => input.classList.remove("is-invalid"));
        input.addEventListener("change", () => input.classList.remove("is-invalid"));
    });

    // 2. [수정] 이름 특수문자/숫자 실시간 차단
    const nameInput = document.querySelector('[name="studentName"]');
    if (nameInput) {
        nameInput.addEventListener("input", function() {
            // 한글, 영문만 허용하는 정규식 (나머지는 빈 문자열로 대체)
            const regExp = /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z]/g;
            this.value = this.value.replace(regExp, "");
        });
    }

    // 3. 숫자만 허용 로직
    const onlyNumberFields = [
        document.querySelector('[name="identityFront"]'),
        document.querySelector('[name="identityBack"]'),
        document.querySelector('[name="studentNo"]')
    ];
    onlyNumberFields.forEach(el => {
        if (el) {
            el.addEventListener("input", function() {
                this.value = this.value.replace(/\D/g, "");
            });
        }
    });
});

// [전역 함수] 하이픈 자동 삽입
function autoHyphen(target) {
    let val = target.value.replace(/[^0-9]/g, '');
    if (val.length < 4) {
        target.value = val;
    } else if (val.length < 8) {
        target.value = val.substring(0, 3) + '-' + val.substring(3);
    } else {
        target.value = val.substring(0, 3) + '-' + val.substring(3, 7) + '-' + val.substring(7);
    }
    target.classList.remove("is-invalid");
}

// [최종 검증 함수]
function validateStudentForm() {
    const form = document.getElementById("studentForm");
    
    // 에러 초기화
    form.querySelectorAll(".is-invalid").forEach(el => el.classList.remove("is-invalid"));

    // 검증 순서 정의
    const fields = [
        { el: form.studentName, msg: "이름을 정확히 입력해주세요." },
        { el: form.studentGender, msg: "성별을 선택해주세요." },
        { el: form.majorId, msg: "학과를 선택해주세요." },
        { el: form.identityFront, msg: "주민번호 앞자리 6자리를 확인해주세요.", len: 6 },
        { el: form.identityBack, msg: "주민번호 뒷자리 7자리를 확인해주세요.", len: 7 },
        { el: form.studentNo, msg: "학번을 입력해주세요.", len: 9 },
        { el: form.studentPhone, msg: "핸드폰 번호 13자리를 확인해주세요.", len: 13 }
    ];

    for (let item of fields) {
        const value = item.el.value.trim();
        let isError = false;

        if (!value) {
            isError = true;
        } else if (item.len && value.length !== item.len) {
            isError = true;
        }

        if (isError) {
            item.el.classList.add("is-invalid");
            item.el.focus();
            // alert(item.msg); // 유저에게 알림을 주고 싶다면 활성화
            return false;
        }
    }
    
    return confirm("학생을 등록하시겠습니까?");
}

/**
 * 이름 및 학과 검색창: 한글, 영문만 허용 (특수문자 및 숫자 차단)
 */
function filterNameAndMajor(obj) {
    const regExp = /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z]/g;
    if (regExp.test(obj.value)) {
        obj.value = obj.value.replace(regExp, '');
    }
}

/**
 * 학번 검색창: 숫자만 허용
 */
function filterOnlyNumber(obj) {
    const regExp = /[^0-9]/g;
    if (regExp.test(obj.value)) {
        obj.value = obj.value.replace(regExp, '');
    }
}