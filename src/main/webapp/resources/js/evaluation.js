/**
 * 강의 평가시 사용되는 js
 */

// 페이지 로드 즉시 실행 함수
document.addEventListener("DOMContentLoaded", function() {
    const evalForm = document.getElementById("evalForm");
    console.log("폼 찾기 결과:", evalForm); // 브라우저 콘솔(F12)에서 확인 가능

    if (evalForm) {
        evalForm.onsubmit = function(event) {
            console.log("제출 시도됨"); // 이벤트 작동 확인용
            
            let total = 0;
            const count = 5;
			
            // 전체 문항 체크되어있는지 확인
            for (let i = 1; i <= count; i++) {
                const selected = document.querySelector('input[name="q' + i + '"]:checked');
                
                if (!selected) {
                    alert(i + "번 문항을 선택해 주세요.");
                    
                    // 선택 안 된 문항으로 화면 이동
                    const targetQuestion = document.getElementsByName("q" + i)[0].closest('.mb-5');
                    targetQuestion.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    
                    return false; // 제출 중단
                }
                total += parseInt(selected.value);
            }

            // 합계 대입
            const sumInput = document.getElementById("sumScore");
            if (sumInput) {
                sumInput.value = total;
                console.log("계산된 총점:", total);
            }
            
            return confirm("제출하시겠습니까?");
        };
    }
});

// select 박스 변경 함수도 스크립트 안에 같이 두세요
function changeLecture(lectureId) {
    if(lectureId) {
        location.href = "<c:url value='/student/evaluation/list'/>?lectureId=" + lectureId;
    }
}