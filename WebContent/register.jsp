<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>Landlord Game</title>

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/meyer-reset/2.0/reset.min.css">
<link rel='stylesheet prefetch'
	href='http://fonts.googleapis.com/css?family=Roboto:400,100,300,500,700,900|RobotoDraft:400,100,300,500,700,900'>
<link rel='stylesheet prefetch'
	href='http://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css'>

<link rel="stylesheet" href="css/style.css">


</head>

<body>

	<div class="pen-title">
		<h1>Landlord Game</h1>
	</div>
	<!-- Form Module-->
	<div class="module form-module">
		<div class="form">
			<h2>Create an account</h2>
			<p>${error}</p>
			<form method="post" action="RegServlet">
				<input type="text" name="username" placeholder="Username" /> 
				<input type="password" name="password1" placeholder="Password" />
				<input type="password" name="password2" placeholder="Retype Password" />
				<input type="text" name="email" placeholder="Email Address" />
				<input type="text" name="phone" placeholder="Phone Number" />
				<button>Register</button>
			</form>
		</div>
		<div class="cta">
			<a href="index.jsp">Already a User? Login Here</a>
		</div>
	</div>
	<script
		src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
	<script src="js/index.js"></script>

</body>
</html>
