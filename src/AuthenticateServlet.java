import java.io.IOException;
import java.util.*;

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
@WebServlet("/AuthenticateServlet")
public class AuthenticateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int timeoutInSeconds = 10*60;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AuthenticateServlet() {
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
		String address = request.getParameter("address");
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(timeoutInSeconds);
		String sessionid = session.getId();
		response.setHeader("SET-COOKIE", "JSESSIONID=" + sessionid + "; Secure; HttpOnly");
		int result;
		if (session.getAttribute("userid")== null){
			request.setAttribute("result", 0);
			result = 1;
		}else{
			request.setAttribute("result", 1);
			result = 0;
		}
		request.setAttribute("address", address);
		response.setCharacterEncoding("UTF-8"); 
		response.setContentType("text/plain");
		response.getWriter().print(result);
	}
}
