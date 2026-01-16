/**
 * 
 * 
 * 
*/

document.querySelectorAll(".add-cart").forEach(btn => {
	btn.addEventListener('click', async ()=> {
		const lecId = btn.dataset.lec;
		const stuId = btn.dataset.stu;
		await addCart(lecId, proName, lecType);
	});
});

async function addCart(lecId, stuId){
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
	    lectureId: lecId,
	    lectureType: lecType,
			professorName: professorName
	  })
	});
}