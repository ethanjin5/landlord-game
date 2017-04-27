

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	private String roomName = "gameroom";
	private Room room=null;
	private int userId1;
	private int userId2;
	private int userId3;

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
//			System.out.println("===========username: "+username);
			int userid = Integer.parseInt(request.getParameter("userid"));
//			System.out.println("===========userid: "+userid);
			User user1 = User.getUser(userid);
			System.out.println("===========roomNumber: "+roomNumber);
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager
						.getConnection(
								"jdbc:mysql://ec2-34-195-151-200.compute-1.amazonaws.com:3306/landlord",
								"landlord", "admin");
				
				String query = "select * from room";
				PreparedStatement stmt = con.prepareStatement(query);
				ResultSet res = stmt.executeQuery();
				if (res.next()){ //found room
					roomNumber = res.getInt("roomid");
					roomName = res.getString("roomname");
					int userNumber = res.getInt("usernumber");
					int userid1 = res.getInt("user1");
					int userid2 = res.getInt("user2");
					int userid3 = res.getInt("user3");
					if(userid == userid1 || userid == userid2 || userid == userid3){
						System.out.println("Welcome back to the game: "+username);
						address = "/gameroom.jsp";
					}else{
						if(userNumber==3){
							
							address = "/index.jsp";
							request.setAttribute("error","Game room is full. Please come back later.");
						
						
						}else if(userNumber==2){
						
							synchronized (this) {
								room.addPlayer(user1);
								String queryUpdate = "UPDATE room SET user3=?, user1Index=?, usernumber=? where roomid=?";
								PreparedStatement stmtUpdate = con.prepareStatement(queryUpdate);
								stmtUpdate.setInt(1,userid);
								stmtUpdate.setInt(2,2);
								stmtUpdate.setInt(3,3);
								stmtUpdate.setInt(4,roomNumber);

								int countInsert = stmtUpdate.executeUpdate();
								if (countInsert>0){ //succesfully added user into database
									address = "/gameroom.jsp";
								}else{
									address = "/index.jsp";
									request.setAttribute("error","Can not add you to the game room now. Please come back later.");
								}
								
							}



						}else if(userNumber==1){
							synchronized (this) {
								room.addPlayer(user1);
								String queryUpdate = "UPDATE room SET user2=?, user2Index=?, usernumber=? where roomid=?";
								PreparedStatement stmtUpdate = con.prepareStatement(queryUpdate);
								stmtUpdate.setInt(1,userid);
								stmtUpdate.setInt(2,1);
								stmtUpdate.setInt(3,2);
								stmtUpdate.setInt(4,roomNumber);

								int countInsert = stmtUpdate.executeUpdate();
								if (countInsert>0){ //succesfully added user into database
									address = "/gameroom.jsp";
								}else{
									address = "/index.jsp";
									request.setAttribute("error","Can not add you to the game room now. Please come back later.");
								}
								//rooms.put(room.getRoomId(), room);
							}

						}
						
					}
					
					con.close();
					
				}else{ //room is not found, create a new room
					synchronized (this) {
						room = new Room(roomNumber, roomName, user1);
						roomNumber = roomNumber+1;
						//rooms.put(room.getRoomId(), room);
					}
					String queryInsert = "INSERT INTO room (roomid,roomname,user1,user2,user3,user1Index,user2Index,user3Index,usernumber) VALUES(?,?,?,?,?,?,?,?,?)";
					PreparedStatement stmtInsert = con.prepareStatement(queryInsert);
					stmtInsert.setInt(1,roomNumber);
					stmtInsert.setString(2,roomName);
					stmtInsert.setInt(3,userid);
					stmtInsert.setInt(4,-1);
					stmtInsert.setInt(5,-1);
					stmtInsert.setInt(6,0);
					stmtInsert.setInt(7,-1);
					stmtInsert.setInt(8,-1);
					stmtInsert.setInt(9,1);
					int countInsert = stmtInsert.executeUpdate();
					if (countInsert>0){ //succesfully added room into database
						address = "/gameroom.jsp";
					}else{
						address = "/index.jsp";
						request.setAttribute("error","Can not add game room now. Please come back later.");
					}
					
					con.close();
					
				}
			} catch (Exception e) {
				System.out.println("Exception in getting the game room: "+e.toString());
			}
			
			
			RequestDispatcher dispatcher = request.getRequestDispatcher(address);
			dispatcher.forward(request, response);
		}
}
