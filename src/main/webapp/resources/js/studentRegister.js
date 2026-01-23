
/**
 * 학생 등록 페이지 유효성 검사 전용 스크립트
 */


document.addEventListener("DOMContentLoaded", function() {
    
    // [추가] 모든 입력 요소에 '입력 시 에러 해제' 이벤트 연결
    const allInputs = document.querySelectorAll('.form-control, .form-select');
    allInputs.forEach(input => {
        // 타이핑하거나 선택을 바꾸면 빨간 테두리(is-invalid) 즉시 제거
        input.addEventListener("input", () => input.classList.remove("is-invalid"));
        input.addEventListener("change", () => input.classList.remove("is-invalid"));
    });

    // 이름 숫자 차단 로직 (기존 유지)
    const nameInput = document.querySelector('[name="studentName"]');
    if (nameInput) {
        nameInput.addEventListener("input", function() {
            this.value = this.value.replace(/[0-9]/g, "");
        });
    }

    // 숫자만 허용 로직 (기존 유지)
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

//[전역 함수] HTML의 oninput="autoHyphen(this)"에서 호출함
function autoHyphen(target) {
    // 1. 숫자만 남기기
    let val = target.value.replace(/[^0-9]/g, '');
    
    // 2. 하이픈 자동 삽입 로직
    if (val.length < 4) {
        target.value = val;
    } else if (val.length < 8) {
        target.value = val.substring(0, 3) + '-' + val.substring(3);
    } else {
        target.value = val.substring(0, 3) + '-' + val.substring(3, 7) + '-' + val.substring(7);
    }

    // 3. 입력하는 순간 빨간 테두리(에러) 제거
    target.classList.remove("is-invalid");
}

// [수정] 위에서부터 하나씩 검사하는 방식으로 변경
function validateStudentForm() {
    const form = document.getElementById("studentForm");
    
    // 에러 초기화 (이전 에러 표시들 싹 지우기)
    form.querySelectorAll(".is-invalid").forEach(el => el.classList.remove("is-invalid"));

    // 검증 대상 및 순서 정의
    const fields = [
        { el: form.studentName, msg: "이름을 입력해주세요." },
        { el: form.studentGender, msg: "성별을 선택해주세요." },
        { el: form.majorId, msg: "학과를 선택해주세요." },
        { el: form.identityFront, msg: "주민번호 앞자리를 확인해주세요.", len: 6 },
        { el: form.identityBack, msg: "주민번호 뒷자리를 확인해주세요.", len: 7 },
        { el: form.studentNo, msg: "학번을 입력해주세요." },
        { el: form.studentPhone, msg: "핸드폰 번호를 확인해주세요.", len: 13 }
    ];

    // 위에서부터 하나씩 순회하며 검사
    for (let item of fields) {
        const value = item.el.value.trim();
        let isError = false;

        if (!value) {
            isError = true; // 값이 비어있을 때
        } else if (item.len && value.length !== item.len) {
            isError = true; // 길이가 맞지 않을 때 (주민번호, 폰 등)
        }

        if (isError) {
            item.el.classList.add("is-invalid"); // 해당 요소만 빨갛게
            item.el.focus(); // 사용자가 바로 수정할 수 있게 커서 이동
            // alert(item.msg); // 필요하다면 메시지 띄우기 (선택사항)
            return false; // 여기서 중단 (다음 요소는 검사 안 함)
        }
    }
    
    return true; // 모두 통과하면 제출
}