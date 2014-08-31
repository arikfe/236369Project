var map;
var pos;
var marker;
$(function() {
	$("#expiration").datepicker();
});
function sumbitForm() {
	$("#expiration")[0].value = $("#expiration")[0].value + " "
			+ $("#number")[0].value;
	return true;
}
function findAddress(lat, lon) {
	$.get("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat
			+ "," + lon, function(data) {
		var street = data.results[0].formatted_address;
		document.getElementById("address").value = street;

	});
}
function initialize() {
	var mapOptions = {
		zoom : 6
	};
	map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
	google.maps.event.addListener(map, 'click', function(event) {
		marker.setMap(null);
		marker = new google.maps.Marker({
			position : event.latLng,
			map : map
		});
		updateFields(event.latLng);
	});
	google.maps.event.addListener(map, 'dblclick', function(event) {
		map = marker.getMap();

		smoothZoom(map, 12, map.getZoom()); // call smoothZoom, parameters map,
											// final zoomLevel, and starting
											// zoom level
	});
	// Try HTML5 geolocation
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			var pos = new google.maps.LatLng(position.coords.latitude,
					position.coords.longitude);

			marker = new google.maps.Marker({
				map : map,
				position : pos,
				content : 'Location found using HTML5.'
			});

			map.setCenter(pos);
			updateFields(pos);

			$("#submit").removeAttr("disabled");

			$("#status").empty();
		}, function() {
			handleNoGeolocation(true);
		});
	} else {
		// Browser doesn't support Geolocation
		handleNoGeolocation(false);
	}
}
// the smooth zoom function
function smoothZoom(map, max, cnt) {
	if (cnt >= max) {
		return;
	} else {
		z = google.maps.event.addListener(map, 'zoom_changed', function(event) {
			google.maps.event.removeListener(z);
			smoothZoom(map, max, cnt + 1);
		});
		setTimeout(function() {
			map.setZoom(cnt);
		}, 80); // 80ms is what I found to work well on my system -- it might
				// not work well on all systems
	}
}
function handleNoGeolocation(errorFlag) {
	var content;
	if (errorFlag) {
		content = 'Error: The Geolocation service failed.';
	} else {
		content = 'Error: Your browser doesn\'t support geolocation.';
	}

	var options = {
		map : map,
		position : new google.maps.LatLng(60, 105),
		content : content
	};

	var infowindow = new google.maps.InfoWindow(options);
	map.setCenter(options.position);
}
function updateFields(position) {
	lat = document.getElementById("lat");
	lon = document.getElementById("lon");
	lat.value = position.lat();
	lon.value = position.lng();
	findAddress(position.lat(), position.lng());
}
