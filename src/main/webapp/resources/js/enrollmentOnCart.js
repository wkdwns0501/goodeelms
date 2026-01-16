/**
 * 
 * 
 */
const listArea = document.querySelector("#lectureListArea");
const cartArea = document.querySelector("#lectureCartArea");
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
	
	const url = new URL(window.location.origin + '/loadLecture');
	
	url.searchParams.set('cat', cat);
	url.searchParams.set('id', userId);
	url.searchParams.set('viewPage', page);
	url.searchParams.set('search_word', word);
	
	const res = await fetch(url.toString(), {
	      method: 'GET',
	      headers: { 'X-Requested-With': 'fetch' }
	    });
	
	if (res.redirected) {
	  window.location.href = res.url; // 최종 도착지로 이동
	  return;
	}
			
	if (!res.ok) {
    console.error('강의 리스트 로드 실패', res.status);
    return;
	}
	
	const html = await res.text();
  listArea.innerHTML = html;
	
	currentCat = cat;
	currentWord = word;
}

async function loadCart(){
	console.log("loadCart 호출됨");
	const url = new URL(window.location.origin + "/student/cart");
	
	const res = await fetch(url.toString(), {
	      method: 'GET',
	      headers: { 'X-Requested-With': 'fetch' }
	    });
			
	if (!res.ok) {
    console.error('장바구니 로드 실패', res.status);
    return;
	}
	
	const html = await res.text();
	cartArea.innerHTML = html;
	
	loadLectures(currentCat,currentWord);
}

async function addCartLecture(lecId, stuId){
	const url = new URL(window.location.origin + "/student/cart/addCart");
	
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
    console.error('장바구니 추가 실패', res.status);
    return;
  }
	
	loadCart();
}

async function deleteCartLecture(lectureId, studentId){
	const url = new URL(window.location.origin + "/student/cart/deleteCart");
		
		const res = await fetch(url.toString(), {
			method: 'POST',
			headers: {
			    'Content-Type': 'application/x-www-form-urlencoded',
			    'X-Requested-With': 'fetch'
			  },
		  body: new URLSearchParams({
		    lecture_id: lectureId,
		    student_id: studentId
		  })
		});
		
		if (!res.ok) {
	    console.error('장바구니 삭제 실패', res.status);
	    return;
	  }
		
		loadCart();
}

document.querySelectorAll(".is-cat").forEach(btn => {
	btn.addEventListener('click', async() => {
		const cat = btn.dataset.cat;
		setActiveBtn(cat);
		await loadLectures(cat, currentWord);
	});
});

listArea.addEventListener('click', async(e) =>{
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
		const addstu = c.dataset.stu;
		
		await addCartLecture(addLec, addstu);
	}
	
});

cartArea.addEventListener('click', async(e) =>{
	const delBtn = e.target.closest("button.delete-cart");
	
	if(!delBtn) return;
	e.preventDefault();
	
	const url = new URL(location.origin);
	
	const lectureId = delBtn.dataset.target;
	const studentId = delBtn.dataset.user;
	
	await deleteCartLecture(lectureId, studentId);
});


searchForm.addEventListener('submit', async (e) => {
	e.preventDefault();
	const word = document.querySelector("#lectureKeyword").value;
	
	await loadLectures(currentCat, word);
})

loadCart();