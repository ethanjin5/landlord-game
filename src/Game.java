import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;


public class Game {

	private int id;
	private int currentUserIndex; //current user who is making a move
	private int bid; //bid amount
	private String requestMove;
	private String playerMove;
	private String tip;
	private ArrayList <User> users; //list of users
	private int winner; //winner id of the game
	private int winnerIndex = -1; //winner's index
	private int highestHandIndex;
	private int landlordIndex; //landlord user
	private ArrayList landlordCards; //landlord's three cards
	private GameClient gameclient;
	private Date playerActionTime = new Date();
	
	public Game(int currentUserIndex, String requestMove, ArrayList<User> users,
			int winner, int landlordIndex, ArrayList landlordCards) {
		this.currentUserIndex = currentUserIndex;
		this.requestMove = requestMove;
		this.users = users;
		this.winner = winner;
		this.landlordIndex = landlordIndex;
		//TODO: add a game in database
	}
	public Game(int currentUserIndex, String requestMove,String playerMove, String tip, ArrayList<User> users, ArrayList landlordCards) {
		this.currentUserIndex = currentUserIndex;
		this.requestMove = requestMove;
		this.playerMove = playerMove;
		this.tip = tip;
		this.users = users;
		this.landlordCards = landlordCards;
	}
	
	public void setPlayerActionTime(){
		this.playerActionTime = new Date();
	}
	public boolean someoneWon(){
		for (int i =0; i<users.size();i++){
			if (users.get(i).getMyCards().size() == 0){
				this.winnerIndex = users.get(i).getMyIndex();
				this.winner = users.get(i).getUserid();
				return true;
			}
		}
		return false;
	}

	public int getWinnerIndex(){
		return this.winnerIndex;
	}
	public int getHighestHandIndex(){
		return this.highestHandIndex;
	}
	public void setHighestHandIndex(int index){
		this.highestHandIndex = index;
	}
	public void setGameClient(GameClient gameclient){
		this.gameclient = gameclient;
	}
	
	public GameClient getGameClient(){
		return this.gameclient;
	}
	
	public ArrayList getMyCards(){
		return users.get(currentUserIndex).getMyCards();
	}
	
	public int getId() {
		return id;
	}
	public int getCurrentUserIndex() {
		return currentUserIndex;
	}
	public void setCurrentUserIndex(int index){
		setPlayerActionTime();
		this.currentUserIndex = index;
	}
	public int getNextUserIndex(){
		if (currentUserIndex ==2){
			return 0;
		}
		return currentUserIndex+1;
	}
	public String getRequestMove() {
		return requestMove;
	}
	public int getWinner() {
		return winner;
	}
	public int getLandlordIndex() {
		return landlordIndex;
	}
	public void setLandlordIndex(int index){
		this.landlordIndex = index;
	}
	public ArrayList getLandlordCards() {
		return landlordCards;
	}
	public ArrayList getUsers(){
		return this.users;
	}
	public void setRequestMove(String requestMove){
		this.requestMove = requestMove;
	}
	public int getBid(){
		return this.bid;
	}
	public void setBid(int bid){
		this.bid = bid;
	}
	public String getPlayerMove(){
		return this.playerMove;
	}
	public void setPlayerMove(String playerMove){
		this.playerMove = playerMove;
	}
	public String getTip(){
		return this.tip;
	}
	public void setTip(String tip){
		this.tip=tip;
	}
	public boolean hasNextUserMove(){ //check if two users passed
		int nextIndex= getNextUserIndex();
		User nextUser = users.get(nextIndex);
		int nextnextIndex = nextUser.getNextUserIndex();
		int passed = 0;
		if (users.get(nextIndex).getMyMove()==null ||
				users.get(nextnextIndex).getMyMove()==null ||
				users.get(getCurrentUserIndex()).getMyMove()==null){
			return true;
		}
		if (users.get(nextIndex).getMyMove()!=null && 
				users.get(nextIndex).getMyMove().equals("Pass")){
			passed +=1;
		}
		if (users.get(nextnextIndex).getMyMove()!=null && 
				users.get(nextnextIndex).getMyMove().equals("Pass")){
			passed +=1;
		}
		if (users.get(getCurrentUserIndex()).getMyMove()!=null 
				&& users.get(getCurrentUserIndex()).getMyMove().equals("Pass")){
			passed +=1;
		}
		if(passed>=2){
			return false;
		}else{
			return true;
		}
	}
	
	public int getNextUserMoveIndex(){ //get next user index who did not pass
		if (users.get(getNextUserIndex()).getMyMove()==null){
			return getNextUserIndex();
		}
		for(int i=getNextUserIndex();i<=users.size();){
			System.out.println(users.get(i).getMyMove());
			if(users.get(i).getMyMove().equals("Pass")){
				if (i == 2){
					i = 0;
				}else{
					i +=1;
				}
				continue;
			}else{
				return i;
			}
		}
		return 0; 
	}
	
	public void resetUserMoves(){
		for (int i =0;i<users.size();i++){
			users.get(i).setMyMove(null);
		}
	}
	public long getCountdown(){
		Date current = new Date();
		long seconds = -1;
		if (playerActionTime !=null){
			seconds = 90 - (current.getTime()-playerActionTime.getTime())/1000;
		}
		return seconds;
	}
	public JSONObject toJson( int userIndex){
		JSONObject obj = new JSONObject();
        obj.put("currentUserIndex", this.currentUserIndex);
        obj.put("myUserIndex", userIndex);
        obj.put("requeustMove", this.requestMove);
        obj.put("playerMove", this.playerMove);
        obj.put("tip", this.tip);
        obj.put("myCards", users.get(userIndex).getMyCards().toString());
        obj.put("user0Name", users.get(0).getUsername());
        obj.put("user1Name", users.get(1).getUsername());
        obj.put("user2Name", users.get(2).getUsername());
        obj.put("user0CardCount", users.get(0).getMyCards().size());
        obj.put("user1CardCount", users.get(1).getMyCards().size());
        obj.put("user2CardCount", users.get(2).getMyCards().size());
        obj.put("landlordIndex", landlordIndex);
        obj.put("landlordCards", landlordCards.toString());
        obj.put("winnerIndex", winnerIndex);
        obj.put("countdown", getCountdown());
		return obj;
	}
}
