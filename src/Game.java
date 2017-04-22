
public class Game {

	private int id;
	private int currentUser;
	private int user1;
	private int user2;
	private int user3;
	private String user1Cards;
	private String user2Cards;
	private String user3Cards;
	private String winner;
	private int landlord;
	private String landlordCards;
	public Game(int id, int currentUser, int user1, int user2, int user3,
			String user1Cards, String user2Cards, String user3Cards,
			String winner, int landlord, String landlordCards) {
		super();
		this.id = id;
		this.currentUser = currentUser;
		this.user1 = user1;
		this.user2 = user2;
		this.user3 = user3;
		this.user1Cards = user1Cards;
		this.user2Cards = user2Cards;
		this.user3Cards = user3Cards;
		this.winner = winner;
		this.landlord = landlord;
	}
	public Game(int id, int currentUser, int user1, int user2, int user3,
			String user1Cards, String user2Cards, String user3Cards,
			int landlord, String landlordCards) {
		super();
		this.id = id;
		this.currentUser = currentUser;
		this.user1 = user1;
		this.user2 = user2;
		this.user3 = user3;
		this.user1Cards = user1Cards;
		this.user2Cards = user2Cards;
		this.user3Cards = user3Cards;
		this.landlord = landlord;
	}
	

}
