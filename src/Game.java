import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;


public class Game {

	private int id;
	private int currentUserIndex; //current user who is making a move
	private int bid; //bid amount
	private String requestMove;
	private String message;
	private ArrayList <User> users; //list of users
	private String winner; //winner of the game
	private int landlordIndex; //landlord user
	private ArrayList landlordCards; //landlord's three cards
	
	public Game(int id, int currentUserIndex, String requestMove, ArrayList<User> users,
			String winner, int landlordIndex, ArrayList landlordCards) {
		this.id = id;
		this.currentUserIndex = currentUserIndex;
		this.requestMove = requestMove;
		this.users = users;
		this.winner = winner;
		this.landlordIndex = landlordIndex;
	}
	public Game(int currentUserIndex, String requestMove,String message, ArrayList<User> users, ArrayList landlordCards) {
		this.currentUserIndex = currentUserIndex;
		this.requestMove = requestMove;
		this.message = message;
		this.users = users;
		this.landlordCards = landlordCards;
	}
	
	public ArrayList getMyCards(){
		return users.get(currentUserIndex).getMyCards();
	}
	
	public JSONObject toJson( int userIndex){
		JSONObject obj = new JSONObject();
        obj.put("currentUserIndex", this.currentUserIndex);
        obj.put("myUserIndex", userIndex);
        obj.put("requeustMove", this.requestMove);
        obj.put("message", this.message);
        obj.put("myCards", users.get(userIndex).getMyCards().toString());
        obj.put("user0CardCount", users.get(0).getMyCards().size());
        obj.put("user1CardCount", users.get(1).getMyCards().size());
        obj.put("user2CardCount", users.get(2).getMyCards().size());
        obj.put("landlordIndex", landlordIndex);
        obj.put("landlordCards", landlordCards.toString());
		return obj;
	}
	
	public int getId() {
		return id;
	}
	public int getCurrentUserIndex() {
		return currentUserIndex;
	}
	public void setCurrentUserIndex(int index){
		this.currentUserIndex = index;
	}
	public int getNextUserIndex(){
		if (currentUserIndex ==2){
			return 0;
		}
		return currentUserIndex+=1;
	}
	public String getRequestMove() {
		return requestMove;
	}
	public String getWinner() {
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
	public boolean hasNextUserMove(){ //check if other two users both passed
		int nextIndex= getNextUserIndex();
		User nextUser = users.get(nextIndex);
		int nextnextIndex = nextUser.getNextUserIndex();
		if (users.get(nextIndex).getMyMove().equals("Pass") 
				&& users.get(nextnextIndex).getMyMove().equals("Pass")){
			return false;
		}else{
			return true;
		}
	}
	public int getNextUserMoveIndex(){ //get next user index who did not pass
		for(int i=getCurrentUserIndex();i<=users.size();i++){
			if(users.get(i).getMyMove().equals("Pass")){
				if (i==2){
					i=0;
				}
				continue;
			}else{
				return i;
			}
		}
		return -1; 
	}

}
