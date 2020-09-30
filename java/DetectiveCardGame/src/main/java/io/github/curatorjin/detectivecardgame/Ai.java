package io.github.curatorjin.detectivecardgame;

import java.io.BufferedReader;
import java.util.List;
import java.util.Random;

public class Ai extends Player{
	
	@Override
	public void draw(Player player, BufferedReader br) {
		List<Card> stuff = player.getHandStuff();
		if (stuff.size() != 0) {
			Random random = new Random();
			int i = random.nextInt(stuff.size());
			Card drawnCard = stuff.get(i);
			player.getHandDeck().removeCard(drawnCard);
			this.getHandDeck().modifyPut(drawnCard);
		}
	}

}
