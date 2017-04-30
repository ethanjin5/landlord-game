
public final class GameState {
	private int userId;
	private int gameId;
	private String winOrLose;
	private int money;
	private String gameStatString;
	
	public GameState(){	
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getWinOrLose() {
		return winOrLose;
	}

	public void setWinOrLose(String winOrLose) {
		this.winOrLose = winOrLose;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public String getGameStatString() {
		return gameStatString;
	}

	public void setGameStatString(String gameStatString) {
		this.gameStatString = gameStatString;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String toString(){
		return "This game is: "+gameId+", and you won? "+winOrLose+", and all the moves are: "+gameStatString;
	}
	

}
