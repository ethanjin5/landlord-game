import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

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
//		int userIndex = 1;  // set to -1, currently 1 for testing
//		if (session.getAttribute("userIndex")!= null && session.getAttribute("userIndex") != ""){
//			userIndex = (int)session.getAttribute("userIndex");
//		}
//		
//		ArrayList <User>users = new ArrayList<User>();
//		//hard coded users
//		users.add( User.getUser(1) );
//		users.add( User.getUser(2) );
//		users.add( User.getUser(5) );
//		if (myGame==null && users.get(0)!=null && users.get(1)!=null && users.get(2)!=null){ //initialize game
//			ArrayList cards = new ArrayList(); // create a list of number for cards
//			for (int i = 1; i<=54; i++){
//				cards.add(i); //add number as cards into array
//			}
//			Collections.shuffle(cards); //shuffle the cards
//			ArrayList user1Cards = new ArrayList();
//			ArrayList user2Cards = new ArrayList();
//			ArrayList user3Cards = new ArrayList();
//			ArrayList landlordCards = new ArrayList();
//	
//			for (int i = cards.size()-3-1; i >= 0; i--){ //assign 17 cards for each user. leave 3 for landlord
//			    user1Cards.add(cards.get(i));
//			    cards.remove(i);
//			    i--;
//			    user2Cards.add(cards.get(i));
//			    cards.remove(i);
//			    i--;
//			    user3Cards.add(cards.get(i));
//			    cards.remove(i);
//			}
//			for (int i =0; i<cards.size(); i++){ //three landlord cards saved for whoever picked landlord
//				landlordCards.add(cards.get(i));
//			}
//			users.get(0).setMyCards(user1Cards);
//			users.get(1).setMyCards(user2Cards);
//			users.get(2).setMyCards(user3Cards);
//			users.get(0).setMyIndex(0);
//			users.get(1).setMyIndex(1);
//			users.get(2).setMyIndex(2);
//			
//			myGame = new Game(0,"pickLandlord","message for picking landlord",users,landlordCards);
//		}else{
//			response.setContentType("application/json");
//		    response.setCharacterEncoding("UTF-8");
//		    response.getWriter().write(myGame.toJson(userIndex).toString());
//		}
		
		String message = "Example.";
		//response.setHeader("Access-Control-Allow-Origin", "*");
		
		response.setContentType("text/plain");
		response.setHeader("Access-Control-Allow-Origin", "*");
		 PrintWriter printWriter  = response.getWriter();
		 printWriter.write(message);
		 printWriter.close();
		 response.setStatus(HttpServletResponse.SC_OK);
  		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		int userIndex = 1;  // set to -1, currently 1 for testing
		if (session.getAttribute("userIndex")!= null && session.getAttribute("userIndex") != ""){
			userIndex = (int)session.getAttribute("userIndex");
		}
		int error =0; //initialize error variable with 0 - no error
		String error_message= "";
		ArrayList <User>users = new ArrayList<User>();
		//hard coded users
		users.add( User.getUser(1) );
		users.add( User.getUser(2) );
		users.add( User.getUser(5) );
//		if(myGame.getRequestMove().equals("pickLandlord")){
//			if (request.getParameter("userInput")!=null){
//				String userInput = request.getParameter("userInput");
//				User myUser = (User)myGame.getUsers().get(myGame.getCurrentUserIndex());
//				if (userInput.equals("Call")){
//					myUser.setMyMove(userInput);
//					myGame.setLandlordIndex(myUser.getMyIndex());
//				}else if(userInput.equals("bid * 2") || userInput.equals("bid * 3")){
//					if (myUser.getMyMove().equals("Call")){
//						if (userInput.equals("bid * 2") && myUser.getMoney()>=myGame.getBid()*2){
//							myGame.setBid(myGame.getBid()*2);
//						}else if (userInput.equals("bid * 3") && myUser.getMoney()>=myGame.getBid()*2){
//							myGame.setBid(myGame.getBid()*3);
//						}else{ //not enough money
//							error = 1;
//							error_message = "You don't have enough money to bid this high";
//						}
//					}else{
//						error = 1;
//						error_message = "You need to call landlord to bidz";
//					}
//					myUser.setMyMove(userInput);
//				}else if(userInput.equals("Pass")){
//					myUser.setMyMove(userInput);
//				}else{
//					error = 1;
//					error_message = "Invalid Input";
//				}
//				if (error!=1){//valid input, move on to next User
//					//all three user passed
//					if (myGame.hasNextUserMove()){//next user to pick landlord or increase bid
//						myGame.setCurrentUserIndex(myGame.getNextUserIndex());
//					}
//					else{ //pick landlord and bid phase over. continue to next phase
//						myGame.setRequestMove("pickCard");
//					}
//				}
//			}else{ //input is null
//				error = 1;
//				error_message="Invalid Input";
//			}
//		}else if(myGame.getRequestMove().equals("pickLandlord")){
//			//drawing cards, etc
//		}
//
//		response.setContentType("application/json");
//	    response.setCharacterEncoding("UTF-8");
//	    if (error!=1 && userIndex != -1){
//	    	response.getWriter().write(myGame.toJson(userIndex).toString());
//	    }else{
//	    	JSONObject result = new JSONObject();
//	    	result.put("error", error);
//	    	result.put("error_message", error_message);
//	    	response.getWriter().write(result.toString());
//	    }
	    
	    	//communicating a simple String message.
	  		String message = "Example source code of Servlet to JSP communication.";
	  		response.getWriter().write("Hi there");

	  		//communicating a Vector object
//	  		Vector vecObj = new Vector();
//	  		vecObj.add("Servlet to JSP communicating an object");
//	  		request.setAttribute("vecBean",vecObj);

	  		//Servlet JSP communication
	  		
		
		
	}
}
