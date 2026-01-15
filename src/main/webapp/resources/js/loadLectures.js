/**
 * 
 * 
 */
const area = document.querySelector("#lectureListArea");
const userId = document.querySelector("#sessionUserId")?.value;

function setActiveBtn(cat){
	document.querySelectorAll(".is-cat").forEach(btn =>{
		btn.classList.toggle('active', btn.dataset.cat ===cat);
	});
}

async function loadLectures(cat){
	// 나중에 검색어도 같이 넘겨야함
	
	const url = new URL(window.location.origin + '/loadLecture');
	
	url.searchParams.set('cat', cat);
	url.searchParams.set('id', userId);
	
	const res = await fetch(url.toString(), {
	      method: 'GET',
	      headers: { 'X-Requested-With': 'fetch' }
	    });
		
	if (!res.ok) {
	      console.error('강의 리스트 로드 실패', res.status);
	      return;
    }
	
	const html = await res.text();
    lectureListArea.innerHTML = html;
}

document.querySelectorAll(".is-cat").forEach(btn => {
	btn.addEventListener('click', async() => {
		const cat = btn.dataset.cat;
		setActiveBtn(cat);
		await loadLectures(cat);
	});
});

loadLectures("all");