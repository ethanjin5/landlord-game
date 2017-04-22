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
		HttpSession session = request.getSession();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
//		System.out.println("***&&&&"+username);
		int loginAttempt = 0;
		if (session.getAttribute("loginAttempt") != null){
			loginAttempt = (int) session.getAttribute("loginAttempt");
		}
		if (loginAttempt <=3 && User.login(username,password)) {
			userid = User.getUser(username);
			//System.out.println("***&&&&"+username);
			session.setAttribute("username", username);
			session.setAttribute("userid", userid);
			session.removeAttribute("loginAttempt");
			address = "/waitingroom.jsp";
		} else {
			address = "/index.jsp";
			request.setAttribute("error","Invalid username or password. Please try again.");
			if (session.getAttribute("loginAttempt")!=null){
				session.setAttribute("loginAttempt", (int)session.getAttribute("loginAttempt")+1);
			}else{
				session.setAttribute("loginAttempt",1);
				request.setAttribute("error","You are locked out, please contact the administrator.");
			}
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(address);
		dispatcher.forward(request, response);
	}
}
