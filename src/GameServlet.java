import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

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
		//get user from room table 
		if (myGame==null && users.size()<3){
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager
						.getConnection(
								"jdbc:mysql://ec2-34-195-151-200.compute-1.amazonaws.com:3306/landlord",
								"landlord", "admin");
				String query = "select user1,user2,user3 from room";
				PreparedStatement stmt = con.prepareStatement(query);
				ResultSet res = stmt.executeQuery();
				if (res.next()){ 
					if (res.getInt("user1")>=0){
						users.add(User.getUser(res.getInt("user1")));
					}
					if (res.getInt("user2")>=0){
						users.add(User.getUser(res.getInt("user2")));
					}
					if (res.getInt("user3")>=0){
						users.add(User.getUser(res.getInt("user3")));
					}
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		if(myGame!=null && myGame.getWinnerIndex()>=0){
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(myGame.toJson(userIndex).toString());
		}
		else if(users.size()==3 ){
		synchronized(this){
		if (myGame==null && users.get(0)!=null && users.get(1)!=null && users.get(2)!=null){ //initialize game
			GameClient gameclient = new GameClient();
			ArrayList user1Cards = new ArrayList();
			ArrayList user2Cards = new ArrayList();
			ArrayList user3Cards = new ArrayList();
			ArrayList landlordCards = new ArrayList();
			for (int i = gameclient.getDeck().getCards().size()-1; i>=51; i--){ //three landlord cards saved for whoever picked landlord
				landlordCards.add(gameclient.getDeck().getCards().get(i));
			}
			for (int i = gameclient.getDeck().getCards().size()-3-1; i >= 0; i--){ //assign 17 cards for each user. leave 3 for landlord
			    user1Cards.add(gameclient.getDeck().getCards().get(i));
			    //gameclient.getDeck().getCards().remove(i);
			    i--;
			    user2Cards.add(gameclient.getDeck().getCards().get(i));
			    //gameclient.getDeck().getCards().remove(i);
			    i--;
			    user3Cards.add(gameclient.getDeck().getCards().get(i));
			    //gameclient.getDeck().getCards().remove(i);
			}
			Collections.sort(user1Cards); //sort cards for user experience
			Collections.sort(user2Cards);
			Collections.sort(user3Cards);
			users.get(0).setMyCards(user1Cards); //set cards to users
			users.get(1).setMyCards(user2Cards);
			users.get(2).setMyCards(user3Cards);
			users.get(0).setMyIndex(0); //set user's index 
			users.get(1).setMyIndex(1);
			users.get(2).setMyIndex(2);
			//create a game
			myGame = new Game(0,"pickLandlord","Please call to be the landlord!","Available inputs includes \"Call\" and \"Pass\"",users,landlordCards);
			synchronized (myGame) {
				myGame.addToDB();
			}
			myGame.setGameClient(gameclient);
			myGame.setBid(100); // default 100 bid 
		}else{
			if (myGame.getCountdown()<=0){
				User currentUser = (User)myGame.getUsers().get(myGame.getCurrentUserIndex());
				currentUser.setMyMove("Pass");
				StatLogger.log(myGame.getId(),"User "+currentUser.getUsername()+" passed");
				myGame.setPlayerMove("User "+currentUser.getUsername()+" passed.");
				if(myGame.getRequestMove().equals("pickLandlord")){
					if (myGame.hasNextUserMove()){//next user to pick landlord or increase bid
						myGame.setCurrentUserIndex(myGame.getNextUserMoveIndex());
						myGame.setTip("You may call or increase bid amount or pass. Available inputs:\"Call\",\"bid * 2\",\"bid * 3\",\"Pass\"");
					}
					else{ //pick landlord and bid phase over. continue to next phase
						myGame.setCurrentUserIndex(myGame.getLandlordIndex());
						User landlord = (User)myGame.getUsers().get(myGame.getLandlordIndex());
						for (int k =0; k<myGame.getLandlordCards().size();k++){
							landlord.getMyCards().add(myGame.getLandlordCards().get(k));
						}
						Collections.sort(landlord.getMyCards());
						myGame.setRequestMove("pickCard");
						myGame.setPlayerMove("User "+landlord.getUsername()+" has won the landlord position."
								+ "<br>Three landlord cards are given to the landlord: "+myGame.getLandlordCards()+"<br>Start playing cards:");
						myGame.setTip("Please enter your hand. You may play any hand you want. enter cards separated by comma");
						myGame.resetUserMoves();
					}
				}else if(myGame.getRequestMove().equals("pickCard")){
					currentUser.setMyMove("Pass");
					myGame.setPlayerMove("User "+currentUser.getUsername()+" passed.");
					myGame.setCurrentUserIndex(myGame.getNextUserMoveIndex());
					StatLogger.log(myGame.getId(),"User "+currentUser.getUsername()+" passed");
				}
			}
			
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(myGame.toJson(userIndex).toString());
		}
		}
		}else{
			JSONObject result = new JSONObject();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			result.put("gameStarted", 0);
			response.getWriter().write(result.toString());
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
					if (myUser.getMyMove()!=null && myUser.getMyMove().equals("Call")){
						error = 1;
						myGame.setTip("You already called, please increase bid or pass");
					}else{
						myUser.setMyMove(userInput);
						StatLogger.log(myGame.getId(),"User "+myUser.getUsername()+" called landlor");
						myGame.setPlayerMove("User "+myUser.getUsername()+" called landlord.");
						myGame.setLandlordIndex(myUser.getMyIndex());
					}
				}else if(userInput.equals("bid * 2") || userInput.equals("bid * 3")){
					if (myUser.getMyMove()!=null && (myUser.getMyMove().equals("Call")|| myUser.getMyMove().equals("bid * 2") || myUser.getMyMove().equals("bid * 3"))){
						if (userInput.equals("bid * 2") && ((User)myGame.getUsers().get(0)).getMoney()>=myGame.getBid()*2 && 
								((User)myGame.getUsers().get(1)).getMoney()>=myGame.getBid()*2 && ((User)myGame.getUsers().get(2)).getMoney()>=myGame.getBid()*2){
							myGame.setBid(myGame.getBid()*2);
							myGame.setPlayerMove("User "+myUser.getUsername()+" called bid * 2, current bid: "+myGame.getBid());
							myGame.setLandlordIndex(myUser.getMyIndex());
						}else if (userInput.equals("bid * 3") && ((User)myGame.getUsers().get(0)).getMoney()>=myGame.getBid()*3 && 
								((User)myGame.getUsers().get(1)).getMoney()>=myGame.getBid()*3 && ((User)myGame.getUsers().get(2)).getMoney()>=myGame.getBid()*3){
							myGame.setBid(myGame.getBid()*3);
							myGame.setPlayerMove("User "+myUser.getUsername()+" called bid * 3, current bid: "+myGame.getBid());
							myGame.setLandlordIndex(myUser.getMyIndex());
						}else{ //not enough money
							error = 1;
							myGame.setTip("Someone don't have enough money to bid this high");
						}
					}else{
						error = 1;
						myGame.setTip("You can only pass, since you did not called landlord");
					}
					myUser.setMyMove(userInput);
				}else if(userInput.equals("Pass")){
					myUser.setMyMove(userInput);
					StatLogger.log(myGame.getId(),"User "+myUser.getUsername()+" passed");
					myGame.setPlayerMove("User "+myUser.getUsername()+" passed.");
				}else{
					error = 1;
					myGame.setTip("Invalid Input");
				}
				if (error!=1){//valid input, move on to next User
					//all three user passed
					if (myGame.hasNextUserMove()){//next user to pick landlord or increase bid
						myGame.setCurrentUserIndex(myGame.getNextUserMoveIndex());
						myGame.setTip("You may call or increase bid amount or pass. Available inputs:\"Call\",\"bid * 2\",\"bid * 3\",\"Pass\"");
					}
					else{ //pick landlord and bid phase over. continue to next phase
						myGame.setCurrentUserIndex(myGame.getLandlordIndex());
						User landlord = (User)myGame.getUsers().get(myGame.getLandlordIndex());
						for (int k =0; k<myGame.getLandlordCards().size();k++){
							landlord.getMyCards().add(myGame.getLandlordCards().get(k));
						}
						Collections.sort(landlord.getMyCards());
						myGame.setRequestMove("pickCard");
						myGame.setPlayerMove("User "+landlord.getUsername()+" has won the landlord position."
								+ "<br>Three landlord cards are given to the landlord: "+myGame.getLandlordCards()+"<br>Start playing cards:");
						myGame.setTip("Please enter your hand. You may play any hand you want. enter cards separated by comma");
						myGame.resetUserMoves();
						for(int u =0; u<myGame.getUsers().size();u++){
							User current = (User)myGame.getUsers().get(u);
							current.setMyMove(null);
							current.setMoney(current.getMoney() - myGame.getBid());
						}
						
					}
				}
			}else{ //input is null
				error = 1;
				myGame.setTip("Invalid Input");
			}
		}else if(myGame.getRequestMove().equals("pickCard")){
			if (request.getParameter("userInput")!=null && userIndex == myGame.getCurrentUserIndex()){
				User currentUser = (User)myGame.getUsers().get(userIndex);
				String cardsPattern = "^([DCHSJ]([ABLJQK1-9]|10)){1}(,[DCHSJ]([ABLJQK1-9]|10))*$"; //validate user input
				if (request.getParameter("userInput").equals("Pass")){
					currentUser.setMyMove(request.getParameter("userInput"));
					StatLogger.log(myGame.getId(),"User "+currentUser.getUsername()+" passed");
					myGame.setPlayerMove("User "+currentUser.getUsername()+" passed.");
				}else if (request.getParameter("userInput").matches(cardsPattern)){
					if (currentUser.getMyMove()!=null && currentUser.getMyMove().equals("Pass")){
						error = 1;
						myGame.setTip("You already passed, please wait till next round to play hand");
					}else{
						String [] strCards = request.getParameter("userInput").split(",");
						ArrayList<Card> inputCards = new ArrayList<Card>();
						for (int i =0; i<strCards.length;i++){
							Card current = myGame.getGameClient().getDeck().getCard(strCards[i].substring(0,1),strCards[i].substring(1,strCards[i].length()));
							inputCards.add(current);
						}
						ArrayList<Card> currentUserCards = currentUser.getMyCards();
						if (myGame.getGameClient().getDeck().isValidHand(inputCards, currentUserCards)){
							if(myGame.getGameClient().isCorrectHand(inputCards)){
								if (myGame.getGameClient().isHigherHand(inputCards)){
									myGame.getGameClient().setCurrentHand(inputCards);
									for (int r = 0; r<inputCards.size();r++){
										myGame.getGameClient().getDeck().addUsedCard(inputCards.get(r));
										currentUser.removeCard(inputCards.get(r));
									}
									currentUser.setMyMove("playedHand");
									myGame.setPlayerMove("User "+currentUser.getUsername()+" played "+inputCards);
									myGame.setHighestHandIndex(userIndex);
									StatLogger.log(myGame.getId(),"User "+currentUser.getUsername()+" played "+inputCards);
								}else{
									error = 1;
									myGame.setTip("Your must play a higher hand than the previous user.");
								}
							}else{
								error = 1;
								myGame.setTip("You must play a "+myGame.getGameClient().getCurrentHand()+" or pass.");
							}
						}else{
							error = 1;
							myGame.setTip("Your input cards were an invalid hand.");
						}
					}
				}else{
					error = 1;
					myGame.setTip("Invalid Input");
				}
			}else{
				error = 1;
				myGame.setTip("Input cannot be empty");
			}
			if (error != 1){ //if winner, announce winner. else set next player to player hand
				if (myGame.someoneWon()){
					User winner = (User)myGame.getUsers().get(myGame.getWinnerIndex());
					myGame.setPlayerMove("User "+winner.getUsername()+" won the game.");
					StatLogger.log(myGame.getId(),"User "+winner.getUsername()+" won the game.");
					if (myGame.getWinnerIndex() == myGame.getLandlordIndex()){
						winner.setMoney(winner.getMoney()+myGame.getBid()*3);
					}else{
						for(int u =0;u<myGame.getUsers().size();u++){
							if(u!=myGame.getLandlordIndex()){
								User current = (User)myGame.getUsers().get(u);
								current.setMoney(current.getMoney()+(myGame.getBid()*3/2));
							}
						}
					}
					myGame.setRequestMove("WON");
				}else if (myGame.hasNextUserMove()){//next user to pick landlord or increase bid
					myGame.setCurrentUserIndex(myGame.getNextUserMoveIndex());
					myGame.setTip("Please play a hand of "+myGame.getGameClient().getCurrentHand()+" or pass.");
				}
				else{
					myGame.setCurrentUserIndex(myGame.getHighestHandIndex());
					for(int u =0; u<myGame.getUsers().size();u++){
						if (u!=myGame.getHighestHandIndex()){
							User current = (User)myGame.getUsers().get(u);
							current.setMyMove(null);
						}
					}
					User highestUser = (User)myGame.getUsers().get(myGame.getHighestHandIndex());
					myGame.setTip("You had the highest hand, you may play any hand you want. Please enter cards or pass");
					myGame.getGameClient().setCurrentHand(null); //reset currentHand and cards
					StatLogger.log(myGame.getId(),"User "+highestUser.getUsername()+" had the highest hand.");
				}
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
