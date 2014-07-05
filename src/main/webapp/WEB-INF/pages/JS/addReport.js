/**
 * 

 */
function getLocation(lon,lat) {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition);
    } else { 
        lon.innerHTML = "Geolocation is not supported by this browser.";
    }
}
function showPosition(position) {
	lat = document.getElementById("lat");
	lon = document.getElementById("lon");
    lat.value=position.coords.latitude;
    lon.value=position.coords.longitude;	
}