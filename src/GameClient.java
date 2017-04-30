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
	public String getCurrentHand(){
		return this.currentHand;
	}
	public void setCurrentHand(ArrayList<Card> cards){ // use Deck.isValidHand to validate cards before setting
		if(cards!=null){
			this.currentCards = cards;
			if (deck.isSingle(cards)){
				this.currentHand = "Single";
			}else if (deck.isPair(cards)){
				this.currentHand = "Pair";
			}else if (deck.isThree(cards)){
				this.currentHand = "Three";
			}else if (deck.isFullHouse(cards)){
				this.currentHand = "FullHouse";
			}else if (deck.isStraight(cards)){
				this.currentHand = "Straight";
			}else if (deck.isBomb(cards)){
				this.currentHand = "Bomb";
			}else if (deck.isJokerBomb(cards)){
				this.currentHand = "JokerBomb";
			}else{
				this.currentHand = null;
			}
		}else{ //cards is null, reset cards and hand
			this.currentCards = null;
			this.currentHand = null;
		}
	}
	public ArrayList<Card> getCurrentCards(){
		return this.currentCards;
	}
	
	public boolean isCorrectHand(ArrayList<Card> cards){ //check currentHand != null before checking if correct
		if (this.currentHand!=null){
			if (this.currentHand.equals("Single")){
				return deck.isSingle(cards) || deck.isBomb(cards) || deck.isJokerBomb(cards);
			}else if (this.currentHand.equals("Pair")){
				return deck.isPair(cards)|| deck.isBomb(cards) || deck.isJokerBomb(cards);
			}else if (this.currentHand.equals("Three")){
				return deck.isThree(cards)|| deck.isBomb(cards) || deck.isJokerBomb(cards);
			}else if (this.currentHand.equals("FullHouse")){
				return deck.isFullHouse(cards)|| deck.isBomb(cards) || deck.isJokerBomb(cards);
			}else if (this.currentHand.equals("Straight")){
				return deck.isStraight(cards)|| deck.isBomb(cards) || deck.isJokerBomb(cards);
			}else if (this.currentHand.equals("Bomb")){
				return deck.isBomb(cards)|| deck.isJokerBomb(cards);
			}else if (this.currentHand.equals("JokerBomb")){
				return deck.isJokerBomb(cards);
			}else{
				return false;
			}
		}else{
			return true;
		}
	}
	
	public boolean isHigherHand(ArrayList<Card> cards){ //check isCorectHand before checking if isHigherHand
		if (this.currentHand!=null){
			int currentMax = currentCards.get(0).getWeight();
			int cardsMax = cards.get(0).getWeight();
			for (int i =1;i<currentCards.size();i++){
				if (currentCards.get(i).getWeight()>currentMax){
					currentMax = currentCards.get(i).getWeight();
				}
			}
			for (int i =1;i<cards.size();i++){
				if (cards.get(i).getWeight()>cardsMax){
					cardsMax = cards.get(i).getWeight();
				}
			}
			if (cardsMax > currentMax){
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
		
	}
}
