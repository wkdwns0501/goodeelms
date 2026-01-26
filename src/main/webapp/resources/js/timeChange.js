/**
 * Zoned Time Change To Client
 */

// Bootstrap Modal 인스턴스 준비
const modalEl = document.querySelector('#datePickerModal');
const dateModal = new bootstrap.Modal(modalEl, { backdrop: 'static' });

const hiddenArea = document.querySelector("#hiddenTimeChange");
const timeArea = document.querySelector("#timeView");
const yearSel = document.querySelector('#yearSelect');
const monthSel = document.querySelector('#monthSelect');
const daySel = document.querySelector('#daySelect');
const pickedInput = document.querySelector('#pickedDate');
const confirmBtn = document.querySelector('#confirmDateBtn');

// 필요하면 여기서 선택 결과를 받는 콜백으로 연결
async function onDatePicked(yyyyMMdd) {
  console.log('선택된 날짜:', yyyyMMdd);
	
	const url = new URL(window.location.origin + "/setTime");
	await fetch(url.toString(), {
	    method: 'POST',
	    headers: {
	      'Content-Type': 'application/x-www-form-urlencoded',
	      'X-Requested-With': 'fetch'
	    },
	    body: new URLSearchParams({
	      date: yyyyMMdd
	    })
	  });
	loadTime();
}

async function loadTime() {
	const url = new URL(window.location.origin + "/setTime");
	console.log(url)
	const res = await fetch(url.toString(), {
	    method: 'GET',
	    headers: {
	      'Content-Type': 'application/x-www-form-urlencoded',
	      'X-Requested-With': 'fetch'
	    },
	  });
		
	if(!res.ok){
		alert("시간 오류 발생");
		return;
	}
	
	timeArea.innerHTML = await res.text();
}

function pad2(n) { return String(n).padStart(2, '0'); }

function fillYears(centerYear, range = 10) {
  yearSel.innerHTML = '';
  for (let y = centerYear - range; y <= centerYear + range; y++) {
    yearSel.add(new Option(String(y), String(y)));
  }
}

function fillMonths() {
  monthSel.innerHTML = '';
  for (let m = 1; m <= 12; m++) {
    monthSel.add(new Option(String(m), String(m)));
  }
}

function fillDays(y, m, keepDay = null) {
  daySel.innerHTML = '';
  const lastDay = new Date(y, m, 0).getDate(); // m: 1~12
  for (let d = 1; d <= lastDay; d++) {
    daySel.add(new Option(String(d), String(d)));
  }
  if (keepDay) daySel.value = String(Math.min(keepDay, lastDay));
}

function updatePickedPreview() {
  const y = yearSel.value;
  const m = pad2(monthSel.value);
  const d = pad2(daySel.value);
  pickedInput.value = `${y}-${m}-${d}`;
}

function initToToday() {
  const now = new Date();
  const y = now.getFullYear();
  const m = now.getMonth() + 1;
  const d = now.getDate();

  fillYears(y, 10);
  fillMonths();

  yearSel.value = String(y);
  monthSel.value = String(m);

  fillDays(y, m, d);
  daySel.value = String(d);

  updatePickedPreview();
}

// 셀렉트 변경 시: 말일 보정 + 프리뷰 갱신
yearSel.addEventListener('change', () => {
  const keepDay = Number(daySel.value || 1);
  fillDays(Number(yearSel.value), Number(monthSel.value), keepDay);
  updatePickedPreview();
});

monthSel.addEventListener('change', () => {
  const keepDay = Number(daySel.value || 1);
  fillDays(Number(yearSel.value), Number(monthSel.value), keepDay);
  updatePickedPreview();
});

daySel.addEventListener('change', updatePickedPreview);

// 모달 열기
hiddenArea.addEventListener('dblclick',(e) => {
	console.log("doubleClick");
	e.preventDefault();

	initToToday();
	dateModal.show();
})

// 확인
confirmBtn.addEventListener('click', () => {
  const result = pickedInput.value; // yyyy-MM-dd
  onDatePicked(result);
  dateModal.hide();
});

loadTime();
