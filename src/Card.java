
public class Card implements Comparable<Card>{

private String rank; //rank of the card
private String suit; //suit of the card : D - Diamond, C - Club, H - Hearts, S - Spade, S - small joker, B - big joker
private int weight;

public Card(String suit, String rank,int weight){
	this.rank=rank;
	this.suit = suit;
	this.weight = weight;
}

public String getRank() {
	return rank;
}

public String getSuit() {
	return suit;
}

public int getWeight() {
	return weight;
}

public String toString(){
	return suit+rank;
}

@Override 
public int compareTo(Card a){
	if (this.weight>a.getWeight()){
		return 1;
	}else if (this.weight == a.getWeight()){
		return 0;
	}else{
		return -1;
	}
}

}
