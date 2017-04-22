import java.util.ArrayList;
import java.util.List;

public class Room {
	private int roomId; 
	private String name; 
	private User user1; 
	private User user2;
	private User user3;
	private List<User> userList = new ArrayList<User>();
	
	//private ChessBoard chessBoard; 
	
	public Room() {
		
		//chessBoard = new ChessBoard();
	}

	public Room(int roomId, String name, User user1) {
		
		this.roomId = roomId;
		this.name = name;
		this.userList.add(0, user1);
		//chessBoard = new ChessBoard();
	}

	
	public boolean isFull(){
		return this.userList.size() == 3;
	}

	
	public void addPlayer(User player) {
		this.userList.add(player);
	}
	
	public User getP1() {
		return this.userList.get(0);

	}
	
	public void setP1(User player) {
		this.userList.add(0, player);
	}

	public User getP2() { 
		return this.userList.get(1); 
	}
	
	public void setP2(User player) {
		this.userList.add(1, player);
	}
	
	public User getP3() { 
		return this.userList.get(2); 
	}
	
	public void setP3(User player) {
		this.userList.add(2, player);
	}

//	public ChessBoard getChessBoard() {
//		return chessBoard;
//	}
//	
//	public void setChessBoard(ChessBoard chessBoard) { 
//		this.chessBoard = chessBoard;
//	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return roomId+":"+
				name +"["+user1+", "+user2+", "+user3+"]";
	}

	public boolean equals(Object obj) {
		if(obj==null)
			return false;
		if(obj==this)
			return true;
		if (obj instanceof Room) {
			Room other = (Room) obj;
			return this.roomId == other.roomId;
		}
		return false;
	}
}