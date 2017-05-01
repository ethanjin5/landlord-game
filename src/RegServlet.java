import java.io.IOException;
import java.util.Arrays;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class StudentServlet
 */
@WebServlet("/RegServlet")
public class RegServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int timeoutInSeconds = 10*60;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String address = "";
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(timeoutInSeconds);
		String sessionid = session.getId();
		response.setHeader("SET-COOKIE", "JSESSIONID=" + sessionid + "; Secure; HttpOnly");
		//get user inputs
		String username = request.getParameter("username");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		//patterns for strong password
		String pattern1 = "^.*[A-Z].*$";  //check for upper case letter
		String pattern2 = "^.*[a-z].*$";  //check for lower case letter
		String pattern3 = "^.*[0-9].*$";  //check for number
		String pattern4 = "^.*[!@#$&*].*$"; //check for special character
		
		String patternForEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		String patternForPhone = "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$";
		
		if(User.dupUser(username)){
			request.setAttribute("error", "Username already used, please try another one");
			address="/register.jsp";
		}
		else if (!password1.equals(password2)){ //check if passwords entered are the same
			request.setAttribute("error",
					"Passwords are different, please try again");
			address="/register.jsp";
		}else if(password1.length()<10 || !password1.matches(pattern1) || !password1.matches(pattern2)
				|| !password1.matches(pattern3) || !password1.matches(pattern4)){ //check if password is strong
			request.setAttribute("error","Password does not meet minimum requirements: must be at least 10 characters including uppercase and lowercase letters, special and alphanumeric characters.");
			address="/register.jsp";
		}else if(!email.matches(patternForEmail)){
			request.setAttribute("error","Please enter an valid email address.");
			address="/register.jsp";
		}else if(!phone.matches(patternForPhone)){
			request.setAttribute("error","Please enter an valid phone number.");
			address="/register.jsp";
		}else{
			if (User.register(username, password1, password2, email, phone)) { //register user into database
				int userid = User.getUser(username);
				session.setAttribute("userid", userid);
				address = "/waitingroom.jsp";
			} else {
				address = "/register.jsp";
				request.setAttribute("error", "unable to register, please try again");
			}
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(address);
		dispatcher.forward(request, response);
	}
}
