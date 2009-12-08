var xmlHttp = false;
function getReq() {
	try {
	  xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
	} catch (e) {
	  try {
	    xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	  } catch (e2) {
	    xmlHttp = false;
	  }
	}
	if (!xmlHttp && typeof XMLHttpRequest != 'undefined') {
	  xmlHttp = new XMLHttpRequest();
	}
}

getReq();

function callServer(tarURL) { 
	xmlHttp.open("GET", tarURL, true);  
	xmlHttp.onreadystatechange = callback; 
	xmlHttp.send(null);
}