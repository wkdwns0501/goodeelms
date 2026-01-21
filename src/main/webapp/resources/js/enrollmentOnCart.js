/**
 * 
 * 
 */
const listArea = document.querySelector("#lectureListArea");
const cartArea = document.querySelector("#lectureCartArea");
const userId = document.querySelector("#sessionUserId")?.value;
const searchForm = document.querySelector("#lectureSearchForm");
const executeCheckEl = document.querySelector("#remainTimeSet");

// 강의 목록
let currentCat = "all";
let currentWord = "";
let currentPage = 1;

// 장바구니
let totalCartCredit = 0;
let limitCartCredit = 0;

// 허용 시간 체크
function executeUpdate(){
	const remain = executeCheckEl.dataset.remainTime;
	if(remain <= 0) {
		window.location.replace("/student/page/enrollment");
		return;
	}
}

// 카테고리 버튼 클래스 조절
function setActiveBtn(cat){
	document.querySelectorAll(".is-cat").forEach(btn =>{
		btn.classList.toggle('active', btn.dataset.cat ===cat);
	});
}

async function loadLectures(cat, word, page){
	
	const url = new URL(window.location.origin + '/student/loadLecture/cart');
	
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
	currentPage = page;
}

async function loadCart(){
	console.log("loadCart 호출됨");
	const url = new URL(window.location.origin + "/student/cart/loadCart");
	
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

	// 장바구니 html에서 요소 얻기	
	const creditArea = cartArea.querySelector("#creditInfo");
	totalCartCredit = creditArea.dataset.total;
	limitCartCredit = creditArea.dataset.limit;
	loadLectures(currentCat,currentWord, currentPage);
}

async function addCartLecture(lecId, stuId, credit){
	// 학점 있는지 검증
	if(!credit) return;
	// 장바구니 유효 시간인지 검증
	executeUpdate();
	if(Number(totalCartCredit) + Number(credit) > Number(limitCartCredit)){
		alert("최대 학점을 초과할 수 없습니다.")
		return;
	}
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
		if(res.status === 400){
			alert("이미 장바구니에 등록 된 강의입니다.");
		}
		else if(res.status === 406){
			alert("교양이 아닌 경우 전공과 부전공만 장바구니 추가 가능합니다.");
			console.log(res.status);
		}
		else if(res.status === 500){
			alert("장바구니 추가에 실패했습니다.");
		}
    console.error('장바구니 추가 실패', res.status);
    return;
  }
	
	loadCart();
}

async function deleteCartLecture(lectureId, studentId){
	// 장바구니 유효 시간인지 검증
	executeUpdate();
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

async function clearCart(studentId){
	// 장바구니 유효 시간인지 검증
	executeUpdate();
	
	const url = new URL(window.location.origin + "/student/cart/clearCart");
	const res = await fetch(url.toString(), {
		method: 'POST',
		headers: {
		    'Content-Type': 'application/x-www-form-urlencoded',
		    'X-Requested-With': 'fetch'
		  },
	  body: new URLSearchParams({
	    student_id: studentId
	  })
	});
	
	if (!res.ok) {
		if(res.status === 400){
			alert("장바구니가 비어있습니다.");
		}
		else if(res.status === 500){
			alert("장바구니 비우기 실패했습니다.");
		}
    console.error('장바구니 삭제 실패', res.status);
    return;
  }
	// 장바구니 불러오기
	loadCart();
}

document.querySelectorAll(".is-cat").forEach(btn => {
	btn.addEventListener('click', async() => {
		const cat = btn.dataset.cat;
		setActiveBtn(cat);
		await loadLectures(cat, currentWord, 1);
	});
});

listArea.addEventListener('click', async(e) =>{
	const a = e.target.closest("a.lectureCart-list");
	const c = e.target.closest("button.add-cart");
	
	if(!a && !c) return;
	e.preventDefault();
	
	if(a){
		const u = new URL(a.href, location.origin);
	  const page = Number(u.searchParams.get("viewPage") || 1);
		
	  await loadLectures(currentCat, currentWord, page);
	}
	else if (c){
		const addLec = c.dataset.lec;
		const addstu = c.dataset.stu;
		const credit = c.dataset.credit;
		
		await addCartLecture(addLec, addstu, credit);
	}
	
});

cartArea.addEventListener('click', async(e) =>{
	const delBtn = e.target.closest("button.delete-cart");
	const clearBtn = e.target.closest("button#clearBtn");
	
	const el = cartArea.querySelector("#cartEl");
	const studentId = el.dataset.user;
	if(!delBtn && !clearBtn) return;
	e.preventDefault();
	
	if(delBtn){
		const lectureId = delBtn.dataset.target;
		
		await deleteCartLecture(lectureId, studentId);
	}
	else if(clearBtn){
		await clearCart(studentId);
	}
	
});

searchForm.addEventListener('submit', async (e) => {
	e.preventDefault();
	const word = document.querySelector("#lectureKeyword").value;
	
	await loadLectures(currentCat, word, 1);
})

loadCart();