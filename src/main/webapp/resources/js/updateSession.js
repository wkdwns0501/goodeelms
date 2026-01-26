const timerContainer = document.getElementById('timerContainer');
const isLogin = (timerContainer && timerContainer.getAttribute('data-is-login')) || "false";
const contextPath = (timerContainer && timerContainer.getAttribute('data-context-path')) || "";

let timeLeft = 1800; 
let timerInterval;

function startTimer() {
    if (!timerContainer || isLogin !== "true") {
        return;
    }

    updateTimerDisplay();

    timerInterval = setInterval(() => {
        if (timeLeft <= 0) {
            clearInterval(timerInterval);
            alert("세션이 만료되었습니다. 다시 로그인해주세요.");
            location.href = contextPath + "/common/login"; 
            return;
        }
        timeLeft--;
        updateTimerDisplay();
    }, 1000);
}

function updateTimerDisplay() {
    const timerDisplay = document.getElementById('timer');
    if (timerDisplay) {
        const minutes = Math.floor(timeLeft / 60);
        const seconds = timeLeft % 60;
        timerDisplay.innerText = `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
    }
}

const extendBtn = document.getElementById('extendBtn');
if (extendBtn) {
    extendBtn.onclick = function() {
        fetch(contextPath + "/common/extendSession", { method: 'POST' })
        .then(response => response.text())
        .then(data => {
            if (data === "ok") {
                timeLeft = 1800;
                updateTimerDisplay();
            } else if (data === "expired") {
                alert("세션이 이미 만료되었습니다.");
                location.href = contextPath + "/common/login"; 
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
    };
}

startTimer();