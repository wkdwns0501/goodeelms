/**
 * Zoned Time Change To Client
 */

// 모든 로직을 DOM이 준비된 후 실행되도록 감쌉니다.
document.addEventListener('DOMContentLoaded', () => {
    
    const modalEl = document.querySelector('#datePickerModal');
    if (!modalEl) return; // 모달이 없는 페이지면 종료
    
    const dateModal = new bootstrap.Modal(modalEl, { backdrop: 'static' });

    const hiddenArea = document.querySelector("#hiddenTimeChange");
    const timeArea = document.querySelector("#timeView");
    const yearSel = document.querySelector('#yearSelect');
    const monthSel = document.querySelector('#monthSelect');
    const daySel = document.querySelector('#daySelect');
    const pickedInput = document.querySelector('#pickedDate');
    const confirmBtn = document.querySelector('#confirmDateBtn');

    // --- 내부 함수들은 유지하되 안전하게 호출 ---
    async function onDatePicked(yyyyMMdd) {
        const url = new URL(window.location.origin + "/setTime");
        await fetch(url.toString(), {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'fetch'
            },
            body: new URLSearchParams({ date: yyyyMMdd })
        });
        loadTime();
    }

    async function loadTime() {
        if (!timeArea) return;
        const url = new URL(window.location.origin + "/setTime");
        const res = await fetch(url.toString(), { method: 'GET' });
        if(res.ok) timeArea.innerHTML = await res.text();
    }

    // (fillYears, fillMonths, fillDays, updatePickedPreview, initToToday 함수는 기존과 동일하게 유지)
    // ... [기존 코드의 함수들] ...

    function fillYears(centerYear, range = 10) {
        yearSel.innerHTML = '';
        for (let y = centerYear - range; y <= centerYear + range; y++) {
            yearSel.add(new Option(String(y), String(y)));
        }
    }
    function fillMonths() {
        monthSel.innerHTML = '';
        for (let m = 1; m <= 12; m++) monthSel.add(new Option(String(m), String(m)));
    }
    function fillDays(y, m, keepDay = null) {
        daySel.innerHTML = '';
        const lastDay = new Date(y, m, 0).getDate();
        for (let d = 1; d <= lastDay; d++) daySel.add(new Option(String(d), String(d)));
        if (keepDay) daySel.value = String(Math.min(keepDay, lastDay));
    }
    function updatePickedPreview() {
        const y = yearSel.value;
        const m = String(monthSel.value).padStart(2, '0');
        const d = String(daySel.value).padStart(2, '0');
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

    // --- 이벤트 리스너 등록 ---
    if (yearSel) yearSel.addEventListener('change', () => {
        fillDays(Number(yearSel.value), Number(monthSel.value), Number(daySel.value));
        updatePickedPreview();
    });
    if (monthSel) monthSel.addEventListener('change', () => {
        fillDays(Number(yearSel.value), Number(monthSel.value), Number(daySel.value));
        updatePickedPreview();
    });
    if (daySel) daySel.addEventListener('change', updatePickedPreview);

    if (hiddenArea) {
        hiddenArea.addEventListener('dblclick', (e) => {
            e.preventDefault();
            initToToday();
            dateModal.show();
        });
    }

    if (confirmBtn) {
        // 기존 리스너와 충돌을 피하기 위해 한번만 등록되도록 처리
        confirmBtn.onclick = () => {
            const result = pickedInput.value;
            onDatePicked(result);
            dateModal.hide();
        };
    }

    loadTime();
});