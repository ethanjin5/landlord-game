<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 <link rel="stylesheet" type="text/css" href="css/style.css"> 
<meta charset="UTF-8">
<title>Welcome to Landlord Game</title>
<script
  src="https://code.jquery.com/jquery-3.2.1.min.js"
  integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4="
  crossorigin="anonymous"></script>
</head>
<body>



<%
if(session.getAttribute("userid")==null || (int)session.getAttribute("userid")==0){
    session.setAttribute("error", "Please Login First");
    response.sendRedirect(response.encodeRedirectURL("index.jsp"));
}
%>
<script>

	var menuId = 1;
	//setInterval(getUpdate, 3000); //get update every 3 seconds
	setInterval(function () {
		$.ajax({
			type: "GET",
			url: "GameServlet",
			success: function(data){
				alert(data);
			}
		});
		
	}, 3000);


function getUpdate(){
	$.ajax({
		type: "GET",
		url: "GameServlet",
		success: function(data){
			console.log("hello");
		}
	});
}

</script>

<div id="gamespace">Game Space</div>
${message}
<div id="leftuser">
User 1
</div>
<div id="rightuser">
User 3
</div>
<div id="middleuser">
User 2
</div>
<div>
<form action="GameServlet" method="post">  <input type="text" name="userInput" placeholder="Your Move:"/></form> 
	
</div>
</body>
</html>