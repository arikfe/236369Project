/**
 * 
 */
function formSubmit() {
		document.getElementById("logoutForm").submit();
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
	
	function fileChanged(file) {
		var r = confirm("Are you sure");
		if (r == true)
			upload();
	}
	function upload() {
		var data = new FormData();
		jQuery.each($('#file')[0].files, function(i, file) {
		    data.append('file', file);
		});
		$.ajax({
		    url: adminCtx + '/importfile?'+csrfName+'='+csrfValue,
		    '${_csrf.parameterName}' :csrfValue,
		    data: data,
		    cache: false,
		    enctype: "multipart/form-data",
		    contentType: false,
		    processData: false,
		    type: 'POST',
		    success: function(data){
		        alert(data);
		    }
		});
	
	}