import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class StudentServlet
 */
@WebServlet("/GameServlet")
public class GameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int timeoutInSeconds = 10*60;

	private Game myGame = null;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GameServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(timeoutInSeconds);
		String sessionid = session.getId();
		response.setHeader("SET-COOKIE", "JSESSIONID=" + sessionid + "; Secure; HttpOnly");
		int userIndex = -1;  // set to -1, currently 1 for testing
		if (session.getAttribute("myIndex")!= null && session.getAttribute("myIndex") != ""){
			userIndex = (int)session.getAttribute("myIndex");
		}
		
		ArrayList <User>users = new ArrayList<User>();
		//hard coded users
		users.add( User.getUser(1) );
		users.add( User.getUser(2) );
		users.add( User.getUser(5) );
		if (myGame==null && users.get(0)!=null && users.get(1)!=null && users.get(2)!=null){ //initialize game
			GameClient gameclient = new GameClient();
			ArrayList user1Cards = new ArrayList();
			ArrayList user2Cards = new ArrayList();
			ArrayList user3Cards = new ArrayList();
			ArrayList landlordCards = new ArrayList();
	
			for (int i = gameclient.getDeck().getCards().size()-3-1; i >= 0; i--){ //assign 17 cards for each user. leave 3 for landlord
			    user1Cards.add(gameclient.getDeck().getCards().get(i));
			    gameclient.getDeck().getCards().remove(i);
			    i--;
			    user2Cards.add(gameclient.getDeck().getCards().get(i));
			    gameclient.getDeck().getCards().remove(i);
			    i--;
			    user3Cards.add(gameclient.getDeck().getCards().get(i));
			    gameclient.getDeck().getCards().remove(i);
			}
			for (int i =0; i<gameclient.getDeck().getCards().size(); i++){ //three landlord cards saved for whoever picked landlord
				landlordCards.add(gameclient.getDeck().getCards().get(i));
			}
			users.get(0).setMyCards(user1Cards);
			users.get(1).setMyCards(user2Cards);
			users.get(2).setMyCards(user3Cards);
			users.get(0).setMyIndex(0);
			users.get(1).setMyIndex(1);
			users.get(2).setMyIndex(2);
			
			myGame = new Game(0,"pickLandlord","Please call to be the landlord!","Available inputs includes \"Call\" and \"Pass\"",users,landlordCards);
			myGame.setGameClient(gameclient);
		}else{
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(myGame.toJson(userIndex).toString());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(timeoutInSeconds);
		String sessionid = session.getId();
		response.setHeader("SET-COOKIE", "JSESSIONID=" + sessionid + "; Secure; HttpOnly");
		
		int userIndex = -1;  // set to -1
		if (session.getAttribute("myIndex")!= null && session.getAttribute("myIndex") != ""){
			userIndex = (int)session.getAttribute("myIndex"); //valid user index
		}
		int error =0; //initialize error variable with 0 - no error
		//hard coded users
		if(myGame.getRequestMove().equals("pickLandlord")){
			if (request.getParameter("userInput")!=null && userIndex == myGame.getCurrentUserIndex()){
				String userInput = request.getParameter("userInput");
				User myUser = (User)myGame.getUsers().get(userIndex);
				if (userInput.equals("Call")){
					myUser.setMyMove(userInput);
					StatLogger.log(1,"User "+myUser.getUsername()+" called landlor");
					myGame.setPlayerMove("User "+myUser.getUsername()+" called landlord.");
					myGame.setLandlordIndex(myUser.getMyIndex());
				}else if(userInput.equals("bid * 2") || userInput.equals("bid * 3")){
					if (myUser.getMyMove()!=null && myUser.getMyMove().equals("Call")){
						if (userInput.equals("bid * 2") && myUser.getMoney()>=myGame.getBid()*2){
							myGame.setBid(myGame.getBid()*2);
						}else if (userInput.equals("bid * 3") && myUser.getMoney()>=myGame.getBid()*2){
							myGame.setBid(myGame.getBid()*3);
						}else{ //not enough money
							error = 1;
							myGame.setTip("You don't have enough money to bid this high");
						}
					}else{
						error = 1;
						myGame.setTip("You need to call landlord to bid");
					}
					myUser.setMyMove(userInput);
				}else if(userInput.equals("Pass")){
					myUser.setMyMove(userInput);
					StatLogger.log(1,"User "+myUser.getUsername()+" passed");
					myGame.setPlayerMove("User "+myUser.getUsername()+" passed.");
				}else{
					error = 1;
					myGame.setTip("Invalid Input");
				}
				if (error!=1){//valid input, move on to next User
					//all three user passed
					if (myGame.hasNextUserMove()){//next user to pick landlord or increase bid
						myGame.setCurrentUserIndex(myGame.getNextUserIndex());
						myGame.setTip("You may call or increase bid amount. Available inputs:\"Call\",\"bid *2\",\"bid * 3\",\"Pass\"");
					}
					else{ //pick landlord and bid phase over. continue to next phase
						myGame.setRequestMove("pickCard");
						myGame.setTip("Please enter your hand. You may play any hand you want. enter cards separated by comma");
					}
				}
			}else{ //input is null
				System.out.println("error");
				error = 1;
				myGame.setTip("Invalid Input");
			}
		}else if(myGame.getRequestMove().equals("pickCard")){
			if (request.getParameter("userInput")!=null && userIndex == myGame.getCurrentUserIndex()){
				
			}
		}

		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    //if (error!=1 && userIndex != -1){
	    	response.getWriter().write(myGame.toJson(userIndex).toString());
	    //}else{
	    //	JSONObject result = new JSONObject();
	    //	result.put("error", error);
	    //	response.getWriter().write(result.toString());
	    //}
		
		
	}
}
