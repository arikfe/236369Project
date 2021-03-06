/**
 * 
 */
var map;
var markers = {};
var evacuations = [];
var selectedEvent;
var image;
var pos;



function bounceClosest() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			pos = new google.maps.LatLng(position.coords.latitude,
					position.coords.longitude);
			
			$.ajax({
				type : "GET",
				url : "/236369Project/evacuation/closestEvent",
				data : {
					lat : pos.lat(),
					lng : pos.lng()
				}
			}).done(function(data) {
				stopEventBounce(selectedEvent);
				selectedEvent = markers["e" + data.id];
				selectedEvent.setAnimation(google.maps.Animation.BOUNCE);
			}).fail(function(err) {
				alert(err);
			});
		}, function() {
			handleNoGeolocation(true);
		});
	} else {
		// Browser doesn't support Geolocation
		handleNoGeolocation(false);
	}
	
}
function bounceMine() {
	$.ajax({
		type : "GET",
		url : "/236369Project/evacuation/registeredEvent"
	}).done(function(data) {
		if (data != "") {
			stopEventBounce(selectedEvent);
			selectedEvent = markers["e" + data.id];
			selectedEvent.setAnimation(google.maps.Animation.BOUNCE);
		}
	}).fail(function(err) {
		alert(err);
	});
}
function stopEventBounce() {
	if (!(selectedEvent === undefined))
		selectedEvent.setAnimation(null);

}
function deletePost(_id) {
	if (confirm("Are you sure!") == true) {

		markers[_id].setMap(null);
		var path = ctx+"/reports/"+_id+"?"+csrfName+"="+csrfValue	;
		$.ajax({
			type : "delete",
			url : path			
		}).done(function(msg) {
			$("#row" + msg).remove();
		}).fail(function(err) {
			alert(err);
		});
	}
}

function bounceMarker(marker) {
	marker.setAnimation(google.maps.Animation.BOUNCE);
}
function stopBounce(marker) {
	marker.setAnimation(null);
}
function unregisterToEvent(_id) {
	$.ajax({
		type : "GET",
		url : "/236369Project/evacuation/leave",
		data : {
			id : _id
		}
	}).done(function(msg) {
		// alert("Data Saved: " + msg);
	}).fail(function(err) {
		alert(err);
	});
}
function registerToEvent(_id) {
	$.ajax({
		type : "GET",
		url : "/236369Project/evacuation/join",
		data : {
			id : _id
		}
	}).done(function(msg) {
		// alert("Data Saved: " + msg);
	}).fail(function(err) {
		alert(err);
	});
}
// middle man function for Marker event addition to map
function updateMarker(location, title, id, contentStr) {
	addMarker(location, title, id, contentStr);
}
// middle man function for evacuation event addition to map
function updateEvent(contentStr, lat, lon, id) {
	addEvacuation(new google.maps.LatLng(lat, lon), contentStr, id);

}
// Add a marker to the map and push to the array.
function addMarker(location, title, id, contentStr) {
	var infowindow = new google.maps.InfoWindow({
		content : contentStr
	});
	var marker = new google.maps.Marker({
		position : location,
		map : map,
		title : title,
		animation : google.maps.Animation.DROP
	});
	if (markers.hasOwnProperty(id))
		markers[id].setMap(null);
	markers[id] = marker;
	var row = $("#row" + id);
	row.hover(function() {
		bounceMarker(marker);
	}, function() {
		stopBounce(marker);
	});
	row.click(function() {
		panTo(marker);
	});
	if (contentStr != "")
		google.maps.event.addListener(marker, 'click', function() {
			infowindow.open(map, marker);
		});
}
function panTo(marker) {
	map.panTo(marker.position);
}
function addEvacuation(location, contentStr, id) {

	var infowindow = new google.maps.InfoWindow({
		content : contentStr
	});
	var marker = new google.maps.Marker({
		position : location,
		map : map,
		title : 'event',
		icon : image,
		animation : google.maps.Animation.DROP
	});
	google.maps.event.addListener(marker, 'click', function() {
		infowindow.open(map, marker);
	});
	markers["e" + id] = marker;
}
// Sets the map on all markers in the array.
function setAllMap(map) {
	for ( var key in markers) {
		if (!key.startsWith("e"))
			markers[key].setMap(map);
	}
}

// Removes the markers from the map, but keeps them in the array.
function clearMarkers() {
	setAllMap(null);
}

// Shows any markers currently in the array.
function showMarkers() {
	setAllMap(map);
}

// Deletes all markers in the array by removing references to them.
function deleteMarkers() {
	clearMarkers();
	markers = {};
}

function displayEventUsers(_id) {
	$.ajax({
		type : "GET",
		url : "/236369Project/evacuation/list",
		data : {
			id : _id
		}
	}).done(
			function(users) {
				var names = "";
				for ( var i in users) {

					var name = users[i].fname;
					names += "<a href='/236369Project/reports/user/"
							+ users[i].username + "'>" + name + "</a><br>";
				}
				$("#friend" + _id).html("<p>" + names + "</p>");

			}).fail(function(err) {
		alert(err);
	});
}
function listReports(_str) {
	$.ajax({
		type : "GET",
		url : "$/236369Project/reports/json/" + _str,

	}).done(function(reports) {

		for ( var i in reports) {
			var id = reports[i].id;
			var title = reports[i].title;
		}
	}).fail(function(err) {
		alert(err);
	});
}
function search(text) {
	$('[id^=row]').remove();
	clearMarkers();
	$.ajax({
		type : "GET",
		url : "/236369Project/reports/search/" ,
		data:{
			q : text.value
		}
	}).done(
			function(reports) {

				for ( var i in reports) {
					handleReportCreation(reports[i],
							currentUser, i,
							reports.length);
				}
			}).fail(function(err) {
		alert(err);
	});
}

$(document).ready(function() {
	google.maps.event.addDomListener(window, 'load', initialize);

});

function handleReportCreation(r, loggoedOnUser, i, length) {
	var body = $("#table_body");

	var markerImg = "";
	if (r.imageId == "")
		setTimeout(function() {
			updateMarker(new google.maps.LatLng(r.geolat, r.geolng), r.title,
					r.id, "");
		}, 500 + (i++ * (1000 / length)));
	else
		setTimeout(function() {
			updateMarker(new google.maps.LatLng(r.geolat, r.geolng), r.title,
					r.id, '<img src="/236369Project/download/' + r.imageId
							+ '"  height="64" width="64"> ');
		}, 500 + (i++ * 200));
	msg = "<tr id='row"
			+ r.id
			+ "'><td>"
			+ r.title
			+ "</td><td>"
			+ r.content
			+ " </td><td><a href='"+accountCtx +"/"
			+ r.username
			+ "/reports'>"
			+ r.username
			+ "</a></td><td>"
			+ new Date(r.expiration).toLocaleFormat('%d/%m/%Y %H:%M')
					.toString() + "</td>";
	if (loggoedOnUser == r.username || loggoedOnUser == 'admin')
		msg += "<td><input type='button' class='styledButton' value='Delete' onclick='deletePost("
				+ r.id + ")'></td>";
	msg += "</tr>";
	body.after(msg);
}