/**
 * 
 */
function formSubmit() {
		document.getElementById("logoutForm").submit();
	}
	function deleteAccount() {
		$.ajax("../accounts/deleteself").always(function() {
			document.getElementById("logoutForm").submit();
		});
	}
	function showEvac() {
		$.ajax("admin/addEventView").done(function(data) {
			$("#result").html(data);
		}).fail(function(data) {
			$("#result").html(data.responseText);
		}).always(function() {
			//alert("complete");
		});
	}
	