<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<html>
<head>

<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>

<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/table.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>


<script>
	function formSubmit() {
		document.getElementById("logoutForm").submit();
	}
	function showEvac() {
		$.ajax("addEventView").done(function(data) {
			$("#result").html(data);
		}).fail(function(data) {
			alert(data.responseText	);
		}).always(function() {
			//alert("complete");
		});
	}
</script>
</head>
<body>

	<nav>
		<ul>
			<li><a href="#"><font color="#000000">${fname} </font></a>

				<ul>
					<a href="javascript:formSubmit()"> Logout</a>
				</ul></li>
			<li><a href="" onclick='show_more_menu()'>Users management</a></li>
			<li><a onclick='showEvac()'>Add evacuation</a></li>
		</ul>
	</nav>
	<div id='result'></div>


</body>
</html>