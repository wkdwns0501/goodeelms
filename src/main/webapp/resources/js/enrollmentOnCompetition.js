/**
 * 
 * 
 */
const listArea = document.querySelector("#lectureListArea");
const cartArea = document.querySelector("#lectureCartArea");
const historyArea = document.querySelector("#lectureCompletedArea");

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
	
	const url = new URL(window.location.origin + '/student/loadLecture/comp');
	
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
	const url = new URL(window.location.origin + "/student/cart/loadComp");
	
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
}

async function loadHistory(){
	const url = new URL(window.location.origin + "/student/addlecture/nowEnroll");
		
		const res = await fetch(url.toString(), {
			method: 'get',
			headers: {
			    'Content-Type': 'application/x-www-form-urlencoded',
			    'X-Requested-With': 'fetch'
			  },
		});
		
	if(!res.ok){
		console.error('수강 이력 조회 실패', res.status);
	}
	
	const html = await res.text();
	historyArea.innerHTML = html;
	
	console.log(res.status);
	// 장바구니 html에서 요소 얻기	
	//const creditArea = historyArea.querySelector("#creditInfo");
	//totalCartCredit = creditArea.dataset.total;
	//limitCartCredit = creditArea.dataset.limit;
	
	loadLectures(currentCat, currentWord, currentPage);
}

async function executeAddLecture(lecId, stuId, credit){
	// 수강신청 유효 시간인지 검증
	executeUpdate();
	
	// 수강신청 시 한 학기 수강 가능 학점 비교
	if(Number(totalCartCredit) + Number(credit) > Number(limitCartCredit)){
		alert("최대 학점을 초과할 수 없습니다.")
		return;
	}
	
	confirm("한번 신청하면 취소할 수 없습니다. 신청하시겠습니까?");
	
	const url = new URL(window.location.origin + "/student/addlecture");
	
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
		switch(res.status){
			case 204:
				alert("해당 강의를 조회할 수 없습니다.");
				break;
			case 400:
				alert("이미 수강신청 등록 된 강의입니다.");
				break;
			case 406:
				alert("교양이 아닌 경우 전공과 부전공만 수강 가능합니다.");
				break;
			case 500:
				alert("수강신청에 실패했습니다. 인원 현재 신청 인원을 확인하세요.");
				break;
		}
    console.error('장바구니 추가 실패', res.status);
	}
	
	loadHistory();
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
		
		await executeAddLecture(addLec, addstu, credit);
	}
	
});

searchForm.addEventListener('submit', async (e) => {
	e.preventDefault();
	const word = document.querySelector("#lectureKeyword").value;
	
	await loadLectures(currentCat, word, 1);
})

loadCart();
loadHistory();