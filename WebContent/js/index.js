// Toggle Function
$('.cta').click(function(){
  // Switches the Icon
  $(this).children('i').toggleClass('fa-pencil');
  if ($(this).children('a').text() == "Login"){
	  $(this).children('a').text('Not a user? Register');
  }
  else{
	  $(this).children('a').text('Login');
  }
  // Switches the forms  
  $('.form').animate({
    height: "toggle",
    'padding-top': 'toggle',
    'padding-bottom': 'toggle',
    opacity: "toggle"
  }, "slow");
});