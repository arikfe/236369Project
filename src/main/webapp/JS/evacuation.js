/**
 * 
 */
function addUser(eid,name){
	$.ajax({
		type : "put",
		url : evacuationURL +"/id/"+eid+"/joinUser?"+csrfName+"="+csrfValue,
		data : {
			username : name
		}
	}).done(function(data) {
		
	}).fail(function(err) {
		alert(err);
	});
}
function leaveUser(_id,name){
	$.ajax({
		type : "put",
		url : evacuationURL +"/id/"+_id+"/leaveUser?"+csrfName+"="+csrfValue,
		data : {
			username : name
		}
	}).done(function(data) {
		$("#"+name).remove();
	}).fail(function(err) {
		alert(err);
	});
}
function deleteEvent(id){
	$.ajax({
		type : "delete",
		url : evacuationURL +"/id/"+id+"?"+csrfName+"="+csrfValue
		
	}).done(function(data) {
		window.location = ctx;
	}).fail(function(err) {
		alert(err);
	});
}
function registerToEvent(_id) {
	$.ajax(
			{
				type : "PUT",
				url : ctx + "/evacuation/id/" + _id + "/join?" + csrfName + "="
						+ csrfValue

			}).done(function(msg) {
				$("#action")[0].innerHTML = '<input type="button" onclick="unregisterToEvent('+_id+ ')" value="Leave">';
	}).fail(function(err) {
		alert(err.statusText);
	});
}
function unregisterToEvent(_id) {
	$.ajax(
			{
				type : "PUT",
				url : ctx + "/evacuation/id/" + _id + "/leave?" + csrfName
						+ "=" + csrfValue

			}).done(function(msg) {
				$("#action")[0].innerHTML= '<input type="button" onclick="registerToEvent('+_id+ ')" value="Join">';
	}).fail(function(err) {
		alert(err.statusText);
	});
}

