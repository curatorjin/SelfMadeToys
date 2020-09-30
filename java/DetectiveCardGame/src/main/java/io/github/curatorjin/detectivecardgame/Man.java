package io.github.curatorjin.detectivecardgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Man extends Player{
	
	@Override
	public void draw(Player player, BufferedReader br) {
		while(true) {
			System.out.println("请选择要抽取的种类：");
			System.out.println("1.现场\t2.犯人\t3.动机\t4.凶器");
			String input = "";
			try {
				input = br.readLine();
				switch (input) {
				case "1":
					input = "现场";
					break;
				case "2":
					input = "犯人";
					break;
				case "3":
					input = "动机";
					break;
				case "4":
					input = "凶器";
					break;
				default:
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if ("".equals(input)) {
				System.out.println("请输入正确的值！");
				continue;
			}
			List<Card> listForDraw = player.getHandDeck().getKind(input);
			if (listForDraw.size() == 0) {
				System.out.println("对方已经没有这种牌了……");
			}else {
				Random random = new Random();
				int i = random.nextInt(listForDraw.size());
				Card drawnCard = listForDraw.get(i);
				player.getHandDeck().removeCard(drawnCard);
				this.getHandDeck().modifyPut(drawnCard);
				break;
			}
		}
	}

}
