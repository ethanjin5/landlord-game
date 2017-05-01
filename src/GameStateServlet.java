import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
@WebServlet("/GameStateServlet")
public class GameStateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int timeoutInSeconds = 10*60;
	
	private int roomNumber = 0;
	private String roomName = "gameroom";
	private int userId1;
	private int userId2;
	private int userId3;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GameStateServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
			HttpSession session = request.getSession(true);
			session.setMaxInactiveInterval(timeoutInSeconds);
			String sessionid = session.getId();
			response.setHeader("SET-COOKIE", "JSESSIONID=" + sessionid + "; Secure; HttpOnly");
			String address = "";

			int userid = Integer.parseInt(request.getParameter("userid"));
			session.setAttribute("userid", userid);
			String username=request.getParameter("username");
			session.setAttribute("username", username);
			System.out.println("to search for game statistics, your id: "+userid+", your username: "+username);
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager
						.getConnection(
								"jdbc:mysql://ec2-34-195-151-200.compute-1.amazonaws.com:3306/landlord",
								"landlord", "admin");
				
				String query = "select * from games where user1=? or user2=? or user3=?";
				PreparedStatement stmt = con.prepareStatement(query);
				stmt.setInt(1,userid);
				stmt.setInt(2,userid);
				stmt.setInt(3,userid);
				ResultSet res = stmt.executeQuery();
				List<GameState> gameStats = new ArrayList<GameState>();
				while (res.next()){ //found game
					GameState game = new GameState();
					
					int gameId = res.getInt("id");
					game.setGameId(gameId);
					game.setUserId(userid);
					
					if(res.getString("winner") != null){
						String winnerUserNae = res.getString("winner");
						game.setWinOrLose(winnerUserNae.equalsIgnoreCase(username)?"Yes":"No");
					}else{
						game.setWinOrLose("Unfinished");
					}
					
					String queryMoney = "select * from users where id=?";
					PreparedStatement stmtMoney = con.prepareStatement(queryMoney);
					stmtMoney.setInt(1,userid);
					ResultSet resMoney = stmtMoney.executeQuery();
					if (resMoney.next()){
						game.setMoney(resMoney.getInt("money"));
					}
					
					String queryStat = "select * from game_stat where gameid=?";
					PreparedStatement stmtStat = con.prepareStatement(queryStat);
					stmtStat.setInt(1,gameId);
					ResultSet resStat = stmtStat.executeQuery();
					String allMoveString = "";
					while(resStat.next()){
						allMoveString = allMoveString+resStat.getString("move")+";";
						
					}
					game.setGameStatString(allMoveString);
					System.out.println(game.toString());
					
					gameStats.add(game);
					
				}
				
				request.setAttribute("gameStats", gameStats); 
				address = "/gamestat.jsp";
				con.close();
			}catch(Exception e){
				System.out.println("Exception in getting the game: "+e.toString());
			}
			
			RequestDispatcher dispatcher = request.getRequestDispatcher(address);
			dispatcher.forward(request, response);
					
	}
}
