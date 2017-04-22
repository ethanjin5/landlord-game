

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
 * Servlet implementation class HttpServlet
 */
@WebServlet("/RoomServlet")
public class RoomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private int roomNumber = 0;
	private String roomName = "room1";
	private Room room=null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RoomServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
			
			String address = "";
		
			String username=request.getParameter("username");
			System.out.println("===========username: "+username);
			int userid = Integer.parseInt(request.getParameter("userid"));
			System.out.println("===========userid: "+userid);
			User user1 = new User(userid,username,null,null);
			
			if(roomNumber==0){
				synchronized (this) {
					room = new Room(roomNumber, roomName, user1);
					roomNumber = roomNumber+1;
					//rooms.put(room.getRoomId(), room);
				}
				address = "/gameroom.jsp";
				
			}else if(roomNumber==1){
				if(!room.isFull()){
					synchronized (this) {
						room.addPlayer(user1);
						//rooms.put(room.getRoomId(), room);
					}
					address = "/gameroom.jsp";
				}else{
					address = "/index.jsp";
					request.setAttribute("error","Game room is full. Please come back later.");
					
				}
				
				
			}else{
				address = "/index.jsp";
				request.setAttribute("error","No game room is available. Please come back later.");
				
			}
			
			RequestDispatcher dispatcher = request.getRequestDispatcher(address);
			dispatcher.forward(request, response);
			
			
			
//			response.sendRedirect(
//					"chessing.do?roomId="+room.getRoomId()
//					+"&nickname="+"room1");
		}
}
