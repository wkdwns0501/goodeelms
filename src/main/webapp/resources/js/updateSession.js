let timeLeft = 1800; 
function startTimer() {
    const timerContainer = document.getElementById("timerContainer");
    if (!timerContainer) return; 

    const cpath = timerContainer.dataset.contextPath;
    const display = document.getElementById("timer");
    const isLogin = timerContainer.dataset.isLogin === "true";

    if (isLogin && display) {
        let timeLeft = 1800;        
        const sessionTimer = setInterval(function() {
            let minutes = Math.floor(timeLeft / 60);
            let seconds = timeLeft % 60;

            display.innerText = `${minutes < 10 ? '0' : ''}${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;

            if (timeLeft <= 0) { 
                clearInterval(sessionTimer);
                alert("세션이 만료되었습니다.");
                location.href = cpath + "/common/logout";
            }
            
            timeLeft--; 
        }, 1000);
    }
}

window.onload = startTimer; 