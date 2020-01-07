package io.github.curatorjin.detectivecardgame;

import java.io.BufferedReader;
import java.util.List;
import java.util.Random;

public class Ai extends Player{
	
	@Override
	public Card draw(Player player, BufferedReader br) {
		List<Card> stuff = player.getHandStuff();
		if (stuff.size() == 0) {
			return null;
		}else {
			Random random = new Random();
			int i = random.nextInt(stuff.size());
			return stuff.remove(i);
		}
	}

}
