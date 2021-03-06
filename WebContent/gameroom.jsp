<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/cards.css">
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
		} else if ((Integer) session.getAttribute("userid") == 0) {
			session.setAttribute("error", "Please Login First");
			response.sendRedirect(response.encodeRedirectURL("index.jsp"));
		}
		if ((Long)session.getAttribute("login-time")!=null){
			if (System.currentTimeMillis() - (Long)session.getAttribute("login-time") > 1800000){ //1,800,000 milliseconds = 30 minutes
				response.sendRedirect(response.encodeRedirectURL("LogoutServlet"));
			}
		}
	%>
	<script>
		var myIndex = ${myIndex};
		gameFinished = 0;
		if (!gameFinished){
			setInterval(getUpdate, 1000); //get update every 3 seconds
		}
		playerMove = "";
		function getUpdate() {
			$.ajax({
				url : "GameServlet",
				method : "GET"
			}).done(function(data) {
				//set cards for this user and counts for other other
				if (data.gameStarted==1 || (data.gameStarted ==0 && data.winnerIndex>=0)){
				$("#cardsArea").html("My Cards: " + displayCards(data.myCards));
				
				$("#userLeftTimer").html("");
				$("#userMiddleTimer").html("");
				$("#userRightTimer").html("");
				$("#userMiddleLandlord").html("");
				$("#userLeftLandlord").html("");
				$("#userRightLandlord").html("");
				if (data.myUserIndex == 0){
					$("#userMiddleName").html("User "+data.user0Name);
					$("#userLeftCards").html("Cards left: " + data.user1CardCount);
					$("#userRightCards").html("Cards left: " + data.user2CardCount);
					$("#userLeftName").html("User "+data.user1Name);
					$("#userRightName").html("User "+data.user2Name);
					$("#userLeftMoney").html("Money: "+data.user1Money);
					$("#userRightMoney").html("Money: "+data.user2Money);
					$("#userMiddleMoney").html("Money: "+data.user0Money);
					if (data.landlordIndex==0){
						$("#userMiddleLandlord").html("(Landlord)");
					}else if (data.landlordIndex==1){
						$("#userLeftLandlord").html("(Landlord)");
					}else if (data.landlordIndex==2){
						$("#userRightLandlord").html("(Landlord)");
					}
					if(data.currentUserIndex==1){
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
					$("#userLeftMoney").html("Money: "+data.user2Money);
					$("#userRightMoney").html("Money: "+data.user0Money);
					$("#userMiddleMoney").html("Money: "+data.user1Money);
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
					$("#userLeftMoney").html("Money: "+data.user0Money);
					$("#userRightMoney").html("Money: "+data.user1Money);
					$("#userMiddleMoney").html("Money: "+data.user2Money);
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
					$("#userLeftTimer").html("");
					$("#userMiddleTimer").html("");
					$("#userRightTimer").html("");
					gameFinished = 1;
				}else{
					$("#gameFinished").hide();
				}
			}else{
				$("#playerMoves").html("Waiting for other players to join room to start the game.");
			}
			});
		}
		
		function displayCards(cards){
			cards = cards.substr(1,cards.length-2).split(",");
			var html = "<div class='table playingCards'>";
			for (var i = 0;i<cards.length;i++){
				html += "<div class='card rank-"+rank(cards[i])+" "+suit(cards[i])+"'>";
				html += "<span class='rank'>"+rank2(cards[i])+"</span><span class='suit'>"+suit2(cards[i])+"</span><span style='float:left; margin-top:15px;'>"+cards[i]+"</span></div>";
			}
			html += "</div>";
			return html;
		}
		function rank(card){
			if (card.trim().substring(1) =="B"){
				return "big";
			}else if(card.trim().substring(1)=="L"){
				return "little";
			}else{
				return card.trim().substring(1).toLowerCase();
			}
		}
		
		function rank2(card){
			if (card.trim().substring(1) =="B"){
				return "+";
			}else if(card.trim().substring(1)=="L"){
				return "-";
			}else{
				return card.trim().substring(1);
			}
		}
		
		function suit(card){
			switch(card.trim().substring(0,1)){
			case "D":
				return "diams";
				break;
			case "H":
				return "hearts";
				break;
			case "S":
				return "spades";
				break;
			case "C":
				return "clubs";
				break;
			case "J":
				return "joker";
			}
		}
		
		function suit2(card){
			switch(card.trim().substring(0,1)){
			case "D":
				return "&diams;";
				break;
			case "H":
				return "&hearts;";
				break;
			case "S":
				return "&spades;";
				break;
			case "C":
				return "&clubs;";
				break;
			case "J":
				return "Joker";
			}
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
					$("#cardsArea").html("My Cards: " + displayCards(data.myCards));
					
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
						gameFinished = 1;
					}else{
						$("#gameFinished").hide();
					}
					
					
				});
				
			})
		})
		
	</script>
	<a href="LogoutServlet">Log Out</a>
	<div id="gamespace">
		Game Space
		<div id="playerMoves"></div>
	</div>
	<div id="leftuser">
		<div id="userLeftName"></div><div id="userLeftLandlord"></div><br>
		<div id="userLeftMoney"></div>
		<div id="userLeftCards"></div>
		<div id="userLeftTimer"></div>
	</div>
	<div id="rightuser">
		<div id="userRightName"></div><div id="userRightLandlord"></div><br>
		<div id="userRightMoney"></div>
		<div id="userRightCards"></div>
		<div id="userRightTimer"></div>
	</div>
	<div id="middleuser">
		<div id="userMiddleName"></div><div id="userMiddleLandlord"></div>
		<div id="userMiddleMoney"></div>
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