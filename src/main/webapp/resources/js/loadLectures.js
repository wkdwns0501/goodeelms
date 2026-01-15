/**
 * 
 * 
 */
const area = document.querySelector("#lectureListArea");
const userId = document.querySelector("#sessionUserId")?.value;
const searchForm = document.querySelector("#lectureSearchForm");

let currentCat = "all";
let currentWord = "";

function setActiveBtn(cat){
	document.querySelectorAll(".is-cat").forEach(btn =>{
		btn.classList.toggle('active', btn.dataset.cat ===cat);
	});
}

async function loadLectures(cat, word, page = 1){
	// 나중에 검색어도 같이 넘겨야함
	
	const url = new URL(window.location.origin + '/loadLecture');
	
	url.searchParams.set('cat', cat);
	url.searchParams.set('id', userId);
	url.searchParams.set('viewPage', page);
	url.searchParams.set('search_word', word);
	
	const res = await fetch(url.toString(), {
	      method: 'GET',
	      headers: { 'X-Requested-With': 'fetch' }
	    });
		
	if (!res.ok) {
    console.error('강의 리스트 로드 실패', res.status);
    return;
	}
	
	const html = await res.text();
  area.innerHTML = html;
	
	currentCat = cat;
	currentWord = word;
}

async function addCartLecture(lecId, stuId){
	const url = new URL(window.location.origin + "/addLectureCart");
	url.searchParams.set('lectureId', lecId);
	url.searchParams.set('studentId', stuId);
	
	const res = await fetch(url.toString(), {
		method: 'POST',
		headers: {
		    'Content-Type': 'application/x-www-form-urlencoded',
		    'X-Requested-With': 'fetch'
		  },
	  body: new URLSearchParams({
	    lecture_id: lecId,
	    student_id: stuId
	  })
	});
	
	if (!res.ok) {
    console.error('장바구니 로드', res.status);
    return;
  }
	
}

document.querySelectorAll(".is-cat").forEach(btn => {
	btn.addEventListener('click', async() => {
		const cat = btn.dataset.cat;
		setActiveBtn(cat);
		await loadLectures(cat, currentWord);
	});
});

area.addEventListener('click', async(e) =>{
	const a = e.target.closest("a.lectureCart-list");
	const c = e.target.closest("button.add-cart");
	
	if(!a && !c) return;
	e.preventDefault();
	
	if(a){
		const u = new URL(a.href, location.origin);
		console.log(u);
	  const page = Number(u.searchParams.get("viewPage") || 1);
	
	  await loadLectures(currentCat, currentWord, page);
	}
	else if (c){
		const addLec = c.dataset.lec;
		// const addstu = u.dataset.stu;
		const addstu = 50;
		
		
		await addCartLecture(addLec, addstu);
	}
	
});

searchForm.addEventListener('submit', async (e) => {
	e.preventDefault();
	const word = document.querySelector("#lectureKeyword").value;
	
	await loadLectures(currentCat, word);
})

loadLectures(currentCat, currentWord);