package onlyone;

import java.util.List;

public class Main {
	public static void main(String[] args) {
		Player[] players = new Player[] {new Man(),new Ai(),new Ai(),new Ai()};
		for (int i = 0; i < Deck.originDeck.size(); i++) {
			//иб┼к
			players[i%4].getHandDeck().put(Deck.originDeck.get(i));
		}
		for (Player player : players) {
			player.modify();
		}
		Deck.viewDeskDeck();
		System.out.println(Deck.answer);
		for (Player player : players) {
			System.out.println("===============");
			List<Card> list = player.getHandDeck().getCards();
			for (Card card : list) {
				System.out.println(card);
			}
		}
		
	}


}
