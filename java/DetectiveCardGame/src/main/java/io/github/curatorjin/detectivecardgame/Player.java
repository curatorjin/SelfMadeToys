package io.github.curatorjin.detectivecardgame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

abstract class Player {
	
	private Deck handStuff = new Deck();
	
	public Deck getHandDeck() {
		return handStuff;
	}
	
	public List<Card> getHandStuff() {
		return handStuff.getCards();
	}

	public List<Card> getKind(String kind){
		List<Card> list = new ArrayList<>();
		for (Card card:handStuff.getCards()) {
			if (card.getKind().equals(kind)) {
				list.add(card);
			}
		}
		return list;
	}
	
	abstract void draw(Player player, BufferedReader br);
}
