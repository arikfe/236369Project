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
		document.url=ctx;
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
//				$('input[type=button]')[0].click('unregisterToEvent('+_id+')');
				$('input[type=button]')[0].value= 'Leave';
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
//				$('input[type=button]')[0].click('registerToEvent('+_id+')');
				$('input[type=button]')[0].value= 'Join';
	}).fail(function(err) {
		alert(err.statusText);
	});
}