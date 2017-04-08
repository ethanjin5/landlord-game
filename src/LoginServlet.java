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
	/*	String address = "";
		StudentDAO studentLookup = new StudentDAO();
		int studentId = Integer.parseInt(request.getParameter("id"));
		if (studentLookup.findStudent(studentId)) {
			StudentBean student = studentLookup.getStudent(studentId);
			request.setAttribute("student", student);
			address = "/Student.jsp";
		} else {
			address = "/NoSuchStudent.jsp";
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(address);
		dispatcher.forward(request, response);
		*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("asdf");
		String address = "";
		User user;
		HttpSession session = request.getSession();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (User.login(username,password)) {
			user = User.getUser(username);
			session.setAttribute("user", user);
			address = "/waitingroom.html";
		} else {
			address = "/index.html";
			request.setAttribute("error-message","invalid username or password. Please try again.");
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(address);
		dispatcher.forward(request, response);
	}
}
