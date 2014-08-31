/**
 * 
 */
var map;
var markers = {};
var evacuations = [];
var selectedEvent;
var image;
var pos;
$(document).ready(function() {
	google.maps.event.addDomListener(window, 'load', initialize);
	
});

function loadReports(user){
	$
	.ajax({
		type : "GET",
		url : reportCtx + "/" + user,
		contentType : "application/json",
	})
	.done(
			function(reports) {

				for ( var i in reports) {
					handleReportCreation(
							reports[i],
							ctx,
							i);
				}
			}).fail(function(err) {
		alert(err.statusText);
	});
}
function loadEvents(){
	$
	.ajax({
		type : "GET",
		url : evacuationURL+"/",
		contentType : "application/json",
	})
	.done(
			function(events) {
				$
				.ajax({
					type : "GET",
					url : accountCtx+"/"+currentUser+"/event",
					contentType : "application/json",
				})
				.done(
						function(event) {
							
							for ( var e in events) 
							{
								handleSingleEvent(events[e],event,e);
							}
						}).fail(function(err) {
					alert(err.statusText);
				});
				
			}).fail(function(err) {
		alert(err.statusText);
	});
}
function handleSingleEvent(e,event,i){
	var userRegistered = "";
	var functionName = "registerToEvent";
	var actionName = "register";
//	var userRegistered = event != "";
//
	if ( event != "" && e.id == event.id) {
		functionName = "un" + functionName;
		userRegistered = "";
		actionName = "unregister";
	}
	var totalcapacity = e.capacity - e.registeredUsers.length;
	var contentStr = '<div id="friend'+e.id+'><input type="button" class="styledButton" onclick="'
			+ functionName
			+ '('
			+ e.id
			+ ')" '
			+ userRegistered
			+ 'value="'
			+ actionName
			+ '" />'
			+ '<input type="button" class="styledButton" value="show users" onclick="displayEventUsers('+e.id+')"/>'
			+ '<p>capacity left: '
			+ totalcapacity
			+ '</p>'
			+ '<p>evacuation time: '
			+ new Date( e.estimated).toLocaleFormat('%d/%m/%Y %H:%M')
			+ '<p><a href="'+evacuationURL+'/id/'+e.id+'">show Event</a>'
			+ '</p>';
	
	setTimeout(updateEvent(contentStr, e.geolat, e.geolng,
			e.id), 500 + (i * 200));
	msg = "<div class='menu-item' id='row"+e.id+"'>" + "<h4><a href='#'>" +shorten(e.means)
	+ "</a></h4>" + " <ul > " + " <li>capacity: " + e.capacity
	+ "</li>" + " <li>means: " + e.means
	+ "</li>"
	+ new Date(e.estimated).toLocaleFormat('%d/%m/%Y %H:%M') + "</li>";
	var body = $("#events");
	body.append(msg);

}
function shorten(string){
	if(string.length > 25) {
	    string = string.substring(0,24)+"...";
	}
	return string;
}
function initialize() {

	$.ajax(ctx+"/menu").done(function(result) {
		$("#menu").html(result);
	}).error(function(res) {
		alert(res);
	});

	image = {
		url : ctx+'/IMG/car.png',
		// This marker is 20 pixels wide by 32 pixels tall.
		size : new google.maps.Size(32, 37),
		// The origin for this image is 0,0.
		origin : new google.maps.Point(0, 0),
		// The anchor for this image is the base of the flagpole at 0,32.
		anchor : new google.maps.Point(18, 32)
	};

	var haightAshbury = new google.maps.LatLng(midLat, midLng);
	var mapOptions = {
		zoom : 11,
		center : haightAshbury
	};
	map = new google.maps.Map(document.getElementById('map-canvas'),
			mapOptions);

	var i = 0;
	var body = $("#table_body");
	var msg;
	loadReports("");
	loadEvents();
//
//	<c:forEach var="e" items="${events}">
//	</c:forEach>

}
function bounceClosest() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			pos = new google.maps.LatLng(position.coords.latitude,
					position.coords.longitude);

			$.ajax({
				type : "GET",
				url : ctx + "/evacuation/closestEvent",
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
		url : accountCtx + "/" + currentUser + "/event"
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

function createReportBU() {
	// $.ajax({
	// type : "GET",
	// url : ctx + "/reports/export"
	//		
	//
	// }).done(function(reports) {
	// alert(reports);
	//
	// }).fail(function(err) {
	// alert(err);
	// });
	windows.location = ctx + "/reports/export";

}
function stopEventBounce() {
	if (!(selectedEvent === undefined))
		selectedEvent.setAnimation(null);
}

function deletePost(_id) {
	if (confirm("Are you sure!") == true) {

		markers[_id].setMap(null);
		var path = ctx + "/reports/" + _id + "?" + csrfName + "=" + csrfValue;
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
	$.ajax(
			{
				type : "PUT",
				url : ctx + "/evacuation/id/" + _id + "/leave?" + csrfName
						+ "=" + csrfValue

			}).done(function(msg) {
		// alert("Data Saved: " + msg);
	}).fail(function(err) {
		alert(err.statusText);
	});
}
function registerToEvent(_id) {
	$.ajax(
			{
				type : "PUT",
				url : ctx + "/evacuation/id/" + _id + "/join?" + csrfName + "="
						+ csrfValue

			}).done(function(msg) {
		// alert("Data Saved: " + msg);
	}).fail(function(err) {
		alert(err.statusText);
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
		url : ctx + "/evacuation/id/" + _id + "/users/",
		contentType : "application/json"
	}).done(
			function(users) {
				var names = "";

				for ( var i in users) {
					var name = users[i].fname;
					names += "<a href='" + accountCtx + "/" + users[i].username
							+ "/reports'>" + name + "</a><br>";
				}
				$("#friend" + _id).html("<p>" + names + "</p>");

			}).fail(function(err) {
		alert(err.statusText);
	});
}
function listReports(_str) {
	$.ajax({
		type : "GET",
		url : ctx + "/reports/json/" + _str,
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
		url : ctx + "/reports/search/",
		data : {
			q : text.value
		}
	}).done(function(reports) {

		for ( var i in reports) {
			handleReportCreation(reports[i], currentUser, i, reports.length);
		}
	}).fail(function(err) {
		alert(err);
	});
}


function handleReportCreation(r, loggoedOnUser, i, length) {
	var body = $("#reports");

	// var markerImg = "";
	if (r.imageId == "")
		setTimeout(function() {
			updateMarker(new google.maps.LatLng(r.geolat, r.geolng), r.title,
					r.id, "");
		}, 500 + (i++ * (1000 / length)));
	else
		setTimeout(function() {
			updateMarker(new google.maps.LatLng(r.geolat, r.geolng), r.title,
					r.id, '<a href="' + ctx + '/download/' + r.imageId
							+ '" data-lightbox="' + r.imageId
							+ '" data-title="' + r.title + '"><img src="' + ctx
							+ '/download/' + r.imageId
							+ '"  height="64" width="64"> ');
		}, 500 + (i++ * 200));
	msg = "<div class='menu-item' id='row"+r.id+"'>" + "<h4><a href='#'>" + r.title
			+ "</a></h4>" + " <ul > " + " <li>description: " + r.content
			+ "</li>" +
					"<li> " + "<a href='" + accountCtx + "/"
			+ r.username + "/reports'>user:" + r.username + "</a></li>"
			+ "<li>expire time: "
			+ new Date(r.expiration).toLocaleFormat('%d/%m/%Y %H:%M') + "</li>";
	if (loggoedOnUser == r.username || loggoedOnUser == 'admin')
		msg += "<li><input type='button' class='styledButton' value='Delete' onclick='deletePost("
				+ r.id + ")'></li>";
	msg += "</ul></div>";
	body.append(msg);
}
