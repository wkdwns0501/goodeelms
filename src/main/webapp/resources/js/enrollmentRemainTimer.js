/**
 * 
 * 
 */
// 목표 종료 시각(예: 서버에서 내려준 종료시간을 ms로)
const timerEl = document.querySelector("#enrollTimer");
const remainEl = document.querySelector("#remainTimeSet");
const endUtcMs = Number(remainEl.dataset.endTime);
let nowTime = Number(remainEl.dataset.nowTime);

let lastText = null;
let rafPending = false;

function pad2(n) {
  return String(n).padStart(2, "0");
}

function formatRemain(ms) {
  if (ms <= 0) return "00:00";

  const totalSec = Math.floor(ms / 1000);
  const s = totalSec % 60;
  const m = Math.floor(totalSec / 60) % 60;
  const h = Math.floor(totalSec / 3600);

  // 1시간 이상이면 hh:mm:ss, 아니면 mm:ss
  return h > 0 ? `${pad2(h)}:${pad2(m)}:${pad2(s)}` : `${pad2(m)}:${pad2(s)}`;
}

function render(text) {
  // DOM 반영은 한 프레임에 최대 1번만 하도록 방어(불필요한 연쇄 렌더 방지)
  if (rafPending) return;
  rafPending = true;

  requestAnimationFrame(() => {
    rafPending = false;
    if (timerEl && timerEl.textContent !== text) timerEl.textContent = text;
  });
}

function tick() {
  if (!timerEl || !Number.isFinite(endUtcMs) || !Number.isFinite(nowTime)){
		return;
	}  
  const remain = endUtcMs - nowTime;
  const text = formatRemain(remain);
	
  if (text !== lastText) {
    lastText = text;
    render(text);
  }
	// 남은 시간 data-set에 부여하기 -> 장바구니, 강의목록 js에서 확인용
	remainEl.dataset.remainTime = remain;

  if (remain <= 0) {
    // 종료 처리(원하면 버튼 disable, 폼 lock 등)
    timerEl.classList.remove("text-bg-success");
    timerEl.classList.add("text-bg-secondary");
    return;
  }

  // "정확히 초 경계"에 맞춰 다음 tick 예약 (드리프트 최소화)
  const nextInMs = 1000 - (Date.now() % 1000);
	nowTime += 1000;
  setTimeout(tick, nextInMs);
}

// 시작
tick();
