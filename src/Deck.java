import java.security.SecureRandom;
import java.util.*;

public class Deck {
	private ArrayList<Card> cards;
	private ArrayList<Card> usedCards = new ArrayList<Card>();
	
	
	public Deck(){//create a deck of cards
		this.cards= new ArrayList<Card>();
		String [] flushes = {"D", "S", "H", "C"}; // flush of card
		String [] ranks = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"}; // rank of card
		int [] weight = {12,13,1,2,3,4,5,6,7,8,9,10,11}; // weight of card
		for (int i=0; i<flushes.length;i++){
			for (int j=0; j<13; j++){
				Card card = new Card(flushes[i],ranks[j],weight[j]);
				this.cards.add(card);
			}
		}
		this.cards.add(new Card("J","L",14)); //Little Joker
		this.cards.add(new Card("J","B",15)); //Big Joker
	}
	public Card getCard(String suit,String rank){
		for (int i = 0; i<cards.size();i++){
			if (cards.get(i).getRank().equals(rank) && cards.get(i).getSuit().equals(suit)){
				return cards.get(i);
			}
		}
		return null;
	}
	public ArrayList<Card> getCards(){
		return this.cards;
	}
	public ArrayList<Card> getUsedCards(){
		return this.usedCards;
	}
	public void addUsedCard(Card used){
		this.usedCards.add(used);
	}
	
	public void shuffle(){
		Collections.shuffle(this.cards, new SecureRandom());
	}
	
	public boolean isValidHand(ArrayList<Card> cards, ArrayList<Card> myCards){
		for(int i =0; i<cards.size();i++){
			for (int j=0; j<this.usedCards.size();j++){
				if (cards.get(i).getRank().equals(usedCards.get(j).getRank()) && 
						cards.get(i).getSuit().equals(usedCards.get(j).getSuit())){
					return false;
				}
			}
		}
		for(int i =0; i<cards.size();i++){
			boolean found = false;
			for (int j=0; j<myCards.size();j++){
				if (cards.get(i).getRank().equals(myCards.get(j).getRank()) && 
						cards.get(i).getSuit().equals(myCards.get(j).getSuit())){
					found = true;
				}
			}
			if (!found){
				return false;
			}
		}
		if (isSingle(cards) || isPair(cards) || isThree(cards) || isFullHouse(cards) || isStraight(cards) || isBomb(cards) || isJokerBomb(cards)){
			return true;
		}
		return false;
	}
	
	public boolean isSingle(ArrayList<Card> cards){
		if (cards.size()==1){
			return true;
		}
		return false;
	}
	public boolean isPair(ArrayList<Card> cards){
		if (cards.size()==2 && cards.get(0).getRank().equals(cards.get(1).getRank())){
			return true;
		}
		return false;
	}
	public boolean isThree(ArrayList<Card> cards){
		if (cards.size()==3 && 
				(cards.get(0).getRank().equals(cards.get(1).getRank()) && 
				 cards.get(1).getRank().equals(cards.get(2).getRank()))){
			return true;
		}
		return false;
	}
	public boolean isFullHouse(ArrayList<Card> cards){ //combination of three of a kind + two of a kind
		if (cards.size()!=5){
			return false;
		}
		Map<String,Integer> hm = new HashMap(); //hashmap of the occurence of each rank
	    for(Card card:cards){

	        if(!hm.containsKey(card.getRank())){
	            hm.put(card.getRank(),1);
	        }else{
	            hm.put(card.getRank(), hm.get(card.getRank())+1);
	        }
	    }
	    if (hm.size()!=2){ // vlidate there are two ranks
	    	return false;
	    }
	    if ((int)hm.values().toArray()[0]==3 && (int)hm.values().toArray()[1]==2){
	    	return true;
	    }
	    if ((int)hm.values().toArray()[0]==2 && (int)hm.values().toArray()[1]==3){
	    	return true;
	    }
	    return false; 
	}
	public boolean isStraight(ArrayList<Card> cards){
		Collections.sort(cards);
		for(int i=0;i<cards.size();i++){
			if (cards.get(i).getRank().equals("B") || cards.get(i).getRank().equals("L")){
				return false;
			}
		}
		for (int i=0; i<cards.size()-1;i++){
			if (cards.get(i).getWeight() + 1 != cards.get(i+1).getWeight()) {
				return false;
			}
		}
		return true;
	}
	public boolean isBomb(ArrayList<Card> cards){
		if (cards.size()!=4) {
			return false;
		} 
		for (int i=0; i<cards.size()-1;i++){
			if (!cards.get(i).getRank().equals(cards.get(i+1).getRank())) {
				return false;
			}
		}
		return true;
	}
	public boolean isJokerBomb(ArrayList<Card> cards){
		if (cards.size() != 2){
			return false;
		}
		if (cards.get(0).getRank().equals("L") && cards.get(1).getRank().equals("B")){
			return true;
		}else if (cards.get(1).getRank().equals("L") && cards.get(0).getRank().equals("B")){
			return true;
		}else{
			return false;
		}
	}
	
}
