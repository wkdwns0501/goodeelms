/**
 * 
 * 
 * 
*/

document.querySelectorAll(".add-cart").forEach(btn => {
	btn.addEventListener('click', async ()=> {
		const lecId = btn.dataset.lec;
		const proName = btn.dataset.pro;
		const lecType = btn.dataset.major;
		await addCart(lecId, proName, lecType);
	});
});

async function addCart(lecId, professorName, lecType){
	const url = new URL(window.location.origin + "/addLectureCart");
	url.searchParams.set('lectureId', lecId);
	url.searchParams.set('lectureType', lecType);
	url.searchParams.set('professorName', professorName);
	
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