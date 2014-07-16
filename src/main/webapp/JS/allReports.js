/**
 * 
 */
	var map;
	var markers = [];
	var evacuations = [];
	var image;

	function deletePost(_id)
	{
		$.ajax({
			type : "GET",
			url : "delete",
			data : {
				id : _id
			}
		}).done(function(msg) {
			$("#row"+msg).remove();
		}).fail(function(err) {
			alert(err);
		});
	}
	
	function bounceMarker(marker)
	{
		marker.setAnimation(google.maps.Animation.BOUNCE);
	}
	function stopBounce(marker)
	{
		marker.setAnimation(null);
	}
	function unregisterToEvent(_id) {
		$.ajax({
			type : "GET",
			url : "/project/evacuation/leave",
			data : {
				id : _id
			}
		}).done(function(msg) {
			//alert("Data Saved: " + msg);
		}).fail(function(err) {
			alert(err);
		});
	}
	function registerToEvent(_id) {
		$.ajax({
			type : "GET",
			url : "/project/evacuation/join",
			data : {
				id : _id
			}
		}).done(function(msg) {
			//alert("Data Saved: " + msg);
		}).fail(function(err) {
			alert(err);
		});
	}
	function updateEvent(contentStr,lat,lon)
	{
			addEvacuation(new google.maps.LatLng(lat,
					lon), contentStr);
		
	}
	// Add a marker to the map and push to the array.
	function addMarker(location, title,id) {
		var marker = new google.maps.Marker({
			position : location,
			map : map,
			title : title,
			animation : google.maps.Animation.DROP
		});
		markers.push(marker);
		var row = $("#row"+id);
		row.hover(function(){
			bounceMarker(marker);
			},function(){
				stopBounce(marker);
				});
		row.click(function(){
			panTo(marker);
			});
	}
	function panTo(marker){
		map.panTo(marker.position);
	}
	function addEvacuation(location, contentStr) {

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
		markers.push(marker);
	}
	// Sets the map on all markers in the array.
	function setAllMap(map) {
		for (var i = 0; i < markers.length; i++) {
			markers[i].setMap(map);
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
		markers = [];
	}

	$(document).ready(function(){
		google.maps.event.addDomListener(window, 'load', initialize);
	
	});