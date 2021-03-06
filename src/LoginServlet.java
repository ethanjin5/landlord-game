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
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int timeoutInSeconds = 10*60;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
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
		int userid;
		if (request.getSession(false)!=null){
			request.getSession(false).invalidate();
		}
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(timeoutInSeconds);
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if (User.login(username,password)) { //check if username password correct, user not locked out
			userid = User.getUser(username);
			session.setAttribute("username", username);
			session.setAttribute("userid", userid);
			session.setAttribute( "login-time", System.currentTimeMillis() );
			address = "/waitingroom.jsp";
		} else {
			address = "/index.jsp";
			request.setAttribute("error","Invalid username or password. Please try again.");
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(address);
		dispatcher.forward(request, response);
	}
}
