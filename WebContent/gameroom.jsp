<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/style.css">
<meta charset="UTF-8">
<title>Landlord Game</title>
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
		setInterval(getUpdate, 1000); //get update every 3 seconds
		playerMove = "";
		function getUpdate() {
			$.ajax({
				url : "GameServlet",
				method : "GET"
			}).done(function(data) {
				console.log(data);
				//set cards for this user and counts for other other
				$("#cardsArea").html("My Cards: " + data.myCards);
				$("#userLeftTimer").html("");
				$("#userMiddleTimer").html("");
				$("#userRightTimer").html("");
				if (data.myUserIndex == 0){
					$("#userMiddleName").html("User "+data.user0Name);
					$("#userLeftCards").html("Cards left: " + data.user1CardCount);
					$("#userRightCards").html("Cards left: " + data.user2CardCount);
					$("#userLeftName").html("User "+data.user1Name);
					$("#userRightName").html("User "+data.user2Name);
					if (data.landlordIndex==0){
						$("#userMiddleLandlord").html("(Landlord)");
					}else if (data.landlordIndex==1){
						$("#userLeftLandlord").html("(Landlord)");
					}else if (data.landlordIndex==2){
						$("#userRightLandlord").html("(Landlord)");
					}
					if(data.currentUserIndex==1){
						console.log(data.countdown);
						$("#userLeftTimer").html("Time Left: "+data.countdown);
					}else if(data.currentUserIndex==2){
						$("#userRightTimer").html("Time Left: "+data.countdown);
					}
				}else if(data.myUserIndex == 1){
					$("#userMiddleName").html("User "+data.user1Name);
					$("#userLeftCards").html("Cards left: " + data.user2CardCount);
					$("#userLeftName").html("User "+data.user2Name);
					$("#userRightName").html("User "+data.user0Name);
					$("#userRightCards").html("Cards left: " + data.user0CardCount);
					$("#userMiddleLandlord").html("");
					$("#userLeftLandlord").html("");
					$("#userRightLandlord").html("");
					if (data.landlordIndex==1){
						$("#userMiddleLandlord").html("(Landlord)");
					}else if (data.landlordIndex==2){
						$("#userLeftLandlord").html("(Landlord)");
					}else if (data.landlordIndex==0){
						$("#userRightLandlord").html("(Landlord)");
					}
					if(data.currentUserIndex==2){
						$("#userLeftTimer").html("Time Left: "+data.countdown);
					}else if(data.currentUserIndex==0){
						$("#userRightTimer").html("Time Left: "+data.countdown);
					}
				}else if (data.myUserIndex == 2){
					$("#userMiddleName").html("User "+data.user2Name);
					$("#userLeftCards").html("Cards left: " + data.user0CardCount);
					$("#userLeftName").html("User "+data.user0Name);
					$("#userRightName").html("User "+data.user1Name);
					$("#userRightCards").html("Cards left: " + data.user1CardCount);
					if (data.landlordIndex==2){
						$("#userMiddleLandlord").html("(Landlord)");
					}else if (data.landlordIndex==0){
						$("#userLeftLandlord").html("(Landlord)");
					}else if (data.landlordIndex==1){
						$("#userRightLandlord").html("(Landlord)");
					}
					if(data.currentUserIndex==0){
						$("#userLeftTimer").html("Time Left: "+data.countdown);
					}else if(data.currentUserIndex==1){
						$("#userRightTimer").html("Time Left: "+data.countdown);
					}
				}
				if (data.currentUserIndex == myIndex) {
					$("#inputArea input").prop('disabled', false);
					$("#tip").html("Tip: "+data.tip+"<br>");
					$("#userMiddleTimer").html("Time Left: "+data.countdown);
				} else {
					$("#inputArea input").prop('disabled', true);
					$("#tip").html("");
				}
				if (data.playerMove != playerMove){
					$("#playerMoves").append("<br>"+data.playerMove);
					$('#gamespace').animate({scrollTop: $('#gamespace').prop("scrollHeight")}, 200);
					playerMove = data.playerMove;
				}
				if (data.winnerIndex>=0){
					$("#gameFinished").show();
					$("#inputArea input").prop('disabled', true);
				}else{
					$("#gameFinished").hide();
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
						$("#tip").html("Tip: "+data.tip+"<br>");
					} else {
						$("#inputArea input").prop('disabled', true);
						$("#tip").html("");
					}
					if (data.playerMove != playerMove){
						$("#playerMoves").append("<br>"+data.playerMove);
						$('#playerMoves').animate({scrollTop: $('#playerMoves').prop("scrollHeight")}, 200);
						playerMove = data.playerMove;
					}
					if (data.winnerIndex>=0){
						$("#gameFinished").show();
						$("#inputArea input").prop('disabled', true);
					}else{
						$("#gameFinished").hide();
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
		<div id="userLeftName"></div><div id="userLeftLandlord"></div><br>
		<div id="userLeftCards"></div>
		<div id="userLeftTimer"></div>
	</div>
	<div id="rightuser">
		<div id="userRightName"></div><div id="userRightLandlord"></div><br>
		<div id="userRightCards"></div>
		<div id="userRightTimer"></div>
	</div>
	<div id="middleuser">
		<div id="userMiddleName"></div><div id="userMiddleLandlord"></div><br>
		<div id="cardsArea"></div><br>
		<div id="inputArea">
			<div id="tip"></div><br>
			<input id="userInput" type="text" name="userInput" placeholder="Your Move:" />
			<button id="submitInput">Submit</button> <div id="userMiddleTimer"></div>
		</div>
		<div id="gameFinished">
			<a href="waitingroom.jsp">Click Here</a> to return to waitingroom.
		</div>
	</div>
	<div></div>
	<div align="center">
		<div class="form">
			<form method="post" action="GameStateServlet">
				<input type="hidden" id="userid" name="userid" value=${userid}>
				<input type="hidden" id="username" name="username" value=${username}> 
				<input type="hidden" id="myIndex" name="myIndex" value=${myIndex}> 
				
				<button>Click here to check game statistics</button>
			</form>
		</div>
		
	</div>
</body>
</html>