import java.util.ArrayList;
import java.util.Collections;


public class GameClient {
	
	private String currentHand;
	private ArrayList<Card> currentCards;
	private Deck deck;
	
	public GameClient(){
		this.deck = new Deck();
		this.deck.shuffle();
	}
	public Deck getDeck(){
		return this.deck;
	}
	
	public void setCurrentHand(ArrayList<Card> cards){ // use Deck.isValidHand to validate cards before setting
		if (Deck.isSingle(cards)){
			this.currentHand = "Single";
		}else if (Deck.isPair(cards)){
			this.currentHand = "Pair";
		}else if (Deck.isThree(cards)){
			this.currentHand = "Three";
		}else if (Deck.isFullHouse(cards)){
			this.currentHand = "FullHouse";
		}else if (Deck.isStraight(cards)){
			this.currentHand = "Straight";
		}else if (Deck.isBomb(cards)){
			this.currentHand = "Bomb";
		}else if (Deck.isJokerBomb(cards)){
			this.currentHand = "JokerBomb";
		}else{
			this.currentHand = null;
		}
	}
	
	public boolean isCorrectHand(ArrayList<Card> cards){ //check currentHand != null before checking if correct
		if (this.currentHand.equals("Single")){
			return Deck.isSingle(cards);
		}else if (this.currentHand.equals("Pair")){
			return Deck.isPair(cards);
		}else if (this.currentHand.equals("Three")){
			return Deck.isThree(cards);
		}else if (this.currentHand.equals("FullHouse")){
			return Deck.isFullHouse(cards);
		}else if (this.currentHand.equals("Straight")){
			return Deck.isStraight(cards);
		}else if (this.currentHand.equals("Bomb")){
			return Deck.isBomb(cards);
		}else if (this.currentHand.equals("JokerBomb")){
			return Deck.isJokerBomb(cards);
		}else{
			return false;
		}
	}
	
	public boolean isHigherHand(ArrayList<Card> cards){ //check isCorectHand before checking if isHigherHand
		if (Collections.max(cards).getWeight() > Collections.max(this.currentCards).getWeight()){
			return true;
		}
		return false;
	}
}
