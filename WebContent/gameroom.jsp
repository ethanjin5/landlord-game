<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/style.css">
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.2.1.min.js"
	integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4="
	crossorigin="anonymous"></script>
</head>
<body>
	<%
		if (session.getAttribute("userid") == null) {
			session.setAttribute("error", "Please Login First");
			response.sendRedirect(response.encodeRedirectURL("index.jsp"));
		} else if ((int) session.getAttribute("userid") == 0) {
			session.setAttribute("error", "Please Login First");
			response.sendRedirect(response.encodeRedirectURL("index.jsp"));
		}
	%>
	<script>
		var myIndex = ${myIndex};
		setInterval(getUpdate, 3000); //get update every 3 seconds
		playerMove = "";
		function getUpdate() {
			$.ajax({
				url : "GameServlet",
				method : "GET"
			}).done(function(data) {
				console.log(data);
				//set cards for this user and counts for other other
				$("#cardsArea").html("My Cards: " + data.myCards);
				
				if (data.myUserIndex == 0){
					$("#userMiddleCards").html("Cards left: " + data.user0CardCount);
					$("#userMiddleName").html("User "+data.user0Name);
					$("#userLeftCards").html("Cards left: " + data.user1CardCount);
					$("#userRightCards").html("Cards left: " + data.user2CardCount);
					$("#userLeftName").html("User "+data.user1Name);
					$("#userRightName").html("User "+data.user2Name);
				}else if(data.myUserIndex == 1){
					$("#userMiddleCards").html("Cards left: " + data.user1CardCount);
					$("#userMiddleName").html("User "+data.user1Name);
					$("#userLeftCards").html("Cards left: " + data.user2CardCount);
					$("#userLeftName").html("User "+data.user2Name);
					$("#userRightName").html("User "+data.user0Name);
					$("#userRightCards").html("Cards left: " + data.user0CardCount);
				}else if (data.myUserIndex == 2){
					$("#userMiddleCards").html("Cards left: " + data.user2CardCount);
					$("#userMiddleName").html("User "+data.user2Name);
					$("#userLeftCards").html("Cards left: " + data.user0CardCount);
					$("#userLeftName").html("User "+data.user0Name);
					$("#userRightName").html("User "+data.user1Name);
					$("#userRightCards").html("Cards left: " + data.user1CardCount);
				}
				if (data.currentUserIndex == myIndex) {
					$("#inputArea input").prop('disabled', false);
					$("#tip").html(data.tip);
				} else {
					$("#inputArea input").prop('disabled', true);
					$("#tip").html("");
				}
				if (data.playerMove != playerMove){
					$("#playerMoves").append("<br>"+data.playerMove);
					playerMove = data.playerMove;
				}
				
			});
		}
		$(function(){
			$("#submitInput").click(function(){
				$.ajax({
					url : "GameServlet",
					method : "POST",
					data:{"userInput":$("#userInput").val()}
				}).done(function(data) {
					$("#userInput").val("");
					//set cards for this user and counts for other other
					$("#cardsArea").html("My Cards: " + data.myCards);
					
					if (data.myUserIndex == 0){
						$("#userLeftCards").html("Cards left: " + data.user1CardCount);
						$("#userRightCards").html("Cards left: " + data.user2CardCount);
					}else if(data.myUserIndex == 1){
						$("#userLeftCards").html("Cards left: " + data.user2CardCount);
						$("#userRightCards").html("Cards left: " + data.user0CardCount);
					}else if (data.myUserIndex == 2){
						$("#userLeftCards").html("Cards left: " + data.user0CardCount);
						$("#userRightCards").html("Cards left: " + data.user1CardCount);
					}
					
					if (data.currentUserIndex == myIndex) {
						$("#inputArea input").prop('disabled', false);
						$("#tip").html(data.tip);
					} else {
						$("#inputArea input").prop('disabled', true);
						$("#tip").html("");
					}
					if (data.playerMove != playerMove){
						$("#playerMoves").append("<br>"+data.playerMove);
						playerMove = data.playerMove;
					}
					
					
				});
				
			})
		})
		
	</script>

	<div id="gamespace">
		Game Space
		<div id="playerMoves"></div>
	</div>
	<div id="leftuser">
		<div id="userLeftName"></div>
		<div id="userRightCards"></div>
	</div>
	<div id="rightuser">
		<div id="userLeftName"></div>
		<div id="userRightCards"></div>
	</div>
	<div id="middleuser">
		<div id="userMiddleName"></div>
		<div id="cardsArea"></div>
		<div id="inputArea">
			<div id="tip"></div>
			<input id="userInput" type="text" name="userInput" placeholder="Your Move:" />
			<button id="submitInput">Submit</button>
		</div>
	</div>
	<div></div>
</body>
</html>