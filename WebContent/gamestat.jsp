<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>Landlord Game</title>
<script
  src="https://code.jquery.com/jquery-3.2.1.min.js"
  integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4="
  crossorigin="anonymous"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/meyer-reset/2.0/reset.min.css">
<link rel='stylesheet prefetch'
	href='http://fonts.googleapis.com/css?family=Roboto:400,100,300,500,700,900|RobotoDraft:400,100,300,500,700,900'>
<link rel='stylesheet prefetch'
	href='http://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'>

<link rel="stylesheet" href="css/style.css">

</head>
<body>
<%
int userid;
String error;
String username;

//System.out.println("----------------"+session.getAttribute("userid"));
//System.out.println("----------------"+session.getAttribute("username"));
if(session.getAttribute("userid")==null || session.getAttribute("username")==null){
    session.setAttribute("error", "Please Login First");
    error = "Please Login First";
    response.sendRedirect(response.encodeRedirectURL("index.jsp"));
}else{
	username = session.getAttribute("username").toString();
	userid =  Integer.parseInt(session.getAttribute("userid").toString());
}


%>
<div class="pen-title h1">
		<h1>Game Statistics</h1>
</div>
<table class="zui-table zui-table-zebra zui-table-horizontal">
<thead>
<tr>
        <th>Game Id</th>
        <th>Win or Lose</th>
        <th>Game Moves</th>
        
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${gameStats}" var="gameInfo">
        <tr>
            <td><c:out value="${gameInfo.gameId}" /></td>
            <td><c:out value="${gameInfo.winOrLose}" /></td>
            <td><c:out value="${gameInfo.gameStatString}" /></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div align="center">
		<div class="form">
			<form method="post" action="RoomServlet">
				<input type="hidden" id="userid" name="userid" value=${userid}>
				<input type="hidden" id="username" name="username" value=${username}> 
				
				<button>Click here to go back</button>
			</form>
		</div>
		
	</div>
</body>
</html>