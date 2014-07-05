/**
 * 
 * 
 */
function getLocation() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(showPosition);
	} else {
		alert("Geolocation is not supported by this browser.");
	}
}
function showPosition(position) {
	lat = document.getElementById("lat");
	lon = document.getElementById("lon");
	lat.value = position.coords.latitude;
	lon.value = position.coords.longitude;
	$("#submit").removeAttr("disabled");
	;
	$("#status").empty();

}