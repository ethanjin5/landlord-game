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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		int error =0;
		String error_message= "";
		
		
		if (myGame==null){ //initialize game
			ArrayList <User>users = new ArrayList<User>();
			//hard coded users
			users.add( User.getUser(1) );
			users.add( User.getUser(2) );
			users.add( User.getUser(5) );
			ArrayList cards = new ArrayList(); // create a list of number for cards
			for (int i = 1; i<=54; i++){
				cards.add(i); //add number as cards into array
			}
			Collections.shuffle(cards); //shuffle the cards
			ArrayList user1Cards = new ArrayList();
			ArrayList user2Cards = new ArrayList();
			ArrayList user3Cards = new ArrayList();
			ArrayList landlordCards = new ArrayList();
	
			for (int i = cards.size()-3-1; i >= 0; i--){ //assign 17 cards for each user. leave 3 for landlord
			    user1Cards.add(cards.get(i));
			    cards.remove(i);
			    i--;
			    user2Cards.add(cards.get(i));
			    cards.remove(i);
			    i--;
			    user3Cards.add(cards.get(i));
			    cards.remove(i);
			}
			for (int i =0; i<cards.size(); i++){ //three landlord cards saved for whoever picked landlord
				landlordCards.add(cards.get(i));
			}
			users.get(0).setMyCards(user1Cards);
			users.get(1).setMyCards(user2Cards);
			users.get(2).setMyCards(user3Cards);
			users.get(0).setMyIndex(0);
			users.get(1).setMyIndex(1);
			users.get(2).setMyIndex(2);
			
			myGame = new Game(0,"pickLandlord","message for picking landlord",users,landlordCards);
		}else if(myGame.getRequestMove().equals("pickLandlord")){
			if (request.getParameter("userInput")!=null){
				String userInput = request.getParameter("userInput");
				
				ArrayList<User> users = myGame.getUsers();
				User myUser = (User)myGame.getUsers().get(myGame.getMyUserIndex());
				if (userInput.equals("call")){
					myUser.setMyMove(userInput);
					myGame.setLandlordIndex(myUser.getMyIndex());
				}else if(userInput.matches("^.*[0-9].*$")){
					myUser.setMyMove(userInput);
				}else if(userInput.equals("Pass")){
					myUser.setMyMove(userInput);
				}else{
					error = 1;
					error_message = "Invalid Input";
				}
				if (error!=1){//valid input, move on to next User
					//all three user passed
					if (users.get(0).getMyMove().equals("Pass") && users.get(1).getMyMove().equals("Pass")
							&& users.get(2).getMyMove().equals("Pass")){
						myGame.setRequestMove("pickCard");
					}else{
						for(int i=myGame.getMyUserIndex();i<=users.size();i++){	
							if(users.get(i).getMyMove().equals("Pass")){
								continue;
							}else if (users.get(i).getMyMove().equals("call")){
								for( int j = myGame.getCurrentUserIndex()+1; j<users.size();j++){
									if (!users.get(j).equals("Pass")){
										myGame.setCurrentUserIndex(j);
									}
									if (j==2){ //set index back to start at 0
										j=0;
									}
								}
							}
						}
					}
				}
			}
		}

		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    if (error!=1){
	    	response.getWriter().write(myGame.toJson().toString());
	    }else{
	    	JSONObject result = new JSONObject();
	    	result.put("error", error);
	    	result.put("error_message", error_message);
	    	response.getWriter().write(result.toString());
	    }
		
		
	}
}
