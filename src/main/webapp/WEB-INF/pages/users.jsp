<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.technion.project.model.User"%>
<%@page session="true"%>
<html>
<head>

<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>

<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/table.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>


<script>
	function toggleEnabled(username){
		$.ajax("disable/"+username).done(function(data) {
		});
		}
	function deleteUser(username){
		$.ajax("delete/"+username).always(function(data) {
			$("#"+username).remove();
		});
		}
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
</script>
</head>
<body>

	<nav>
		<ul>
			<li><a href="#"><font color="#000000">${user.fname} </font></a>

				<ul>
					<a href="javascript:formSubmit()"> Logout</a>
				</ul></li>
			<li><a href="" onclick='show_more_menu()'>Users management</a></li>
			<li><a onclick='showEvac()'>Add evacuation</a></li>
		</ul>
	</nav>
	<table class="zebra">
	<tr><th>name</th>
	<th>user name</th>
	<th>enabled</th>
	<th>reports</th>
	<th>delete</th></tr>
	<%
		List<User> users = (List<User>) request.getAttribute("users");
		;
		for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
			User user = iterator.next();
	%>
		<tr id='<%=user.getUsername() %>'>
		<td><%=user.getFname()%></td>
		<td><%=user.getUsername() %></td>
		<td><input type="checkbox" <%=user.isEnabled()?"checked":"" %> onchange="toggleEnabled('<%=user.getUsername() %>')"></td>
		<td><a href='/project/reports/<%=user.getUsername() %>'>reports</a></td>
		<td><input type="button" value="delete" onclick="deleteUser('<%=user.getUsername() %>')"></td>
		</tr>
	<%
		}
	%>
	</table>
</body>
</html>