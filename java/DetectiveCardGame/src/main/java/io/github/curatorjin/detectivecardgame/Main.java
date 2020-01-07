package io.github.curatorjin.detectivecardgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Player[] players = new Player[]{new Man(), new Ai(), new Ai(), new Ai()};
        System.out.println("发牌……");
        for (int i = 0; i < Deck.originDeck.size(); i++) {
            players[i % 4].getHandDeck().put(Deck.originDeck.get(i));
        }
        for (Player player : players) {
            player.modify();
        }
        System.out.println("现在的桌面：");
        Deck.viewDeskDeck();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            int round = 1;
            while (round <= 20) {
                System.out.println("第" + round + "轮……");
                if (players[round % 4] instanceof Man) {
                    System.out.println("请选择要进行的操作：\r\n"
                            + "1.抽牌 \t 2.查看手牌 \t 3.查看桌面牌堆 \t 4.推断");
                    String input = br.readLine();
                    switch (input) {
                        case "1":
                            players[round % 4].draw(players[(round - 1) % 4], br);
                            round++;
                            break;
                        case "2":
                            players[round % 4].getHandDeck().viewDeck();
                            break;
                        case "3":
                            Deck.viewDeskDeck();
                            break;
                        case "4":
                            boolean[] confirmAnswer = new boolean[]{false, false, false, false};
                            List<Card> playerAnswer = new ArrayList<>();
                            //noinspection InfiniteLoopStatement
                            while (true) {
                                if (!confirmAnswer[0]) {
                                    System.out.println("现场是？");
                                    System.out.println("1.宇宙 \t 2.池塘 \t 3.家 \t 4.山");
                                    input = br.readLine();
                                    switch (input) {
                                        case "1":
                                            playerAnswer.add(new Card("现场", "宇宙"));
                                            confirmAnswer[0] = true;
                                            break;
                                        case "2":
                                            playerAnswer.add(new Card("现场", "池塘"));
                                            confirmAnswer[0] = true;
                                            break;
                                        case "3":
                                            playerAnswer.add(new Card("现场", "家"));
                                            confirmAnswer[0] = true;
                                            break;
                                        case "4":
                                            playerAnswer.add(new Card("现场", "山"));
                                            confirmAnswer[0] = true;
                                            break;
                                        default:
                                            System.out.println("请输入正确的数值！");
                                            break;
                                    }
                                }
                                if (!confirmAnswer[1]) {
                                    System.out.println("犯人是？");
                                    System.out.println("1.蝗虫 \t 2.JK \t 3.不良少年 \t 4.科长");
                                    input = br.readLine();
                                    switch (input) {
                                        case "1":
                                            playerAnswer.add(new Card("犯人", "蝗虫"));
                                            confirmAnswer[1] = true;
                                            break;
                                        case "2":
                                            playerAnswer.add(new Card("犯人", "JK"));
                                            confirmAnswer[1] = true;
                                            break;
                                        case "3":
                                            playerAnswer.add(new Card("犯人", "不良少年"));
                                            confirmAnswer[1] = true;
                                            break;
                                        case "4":
                                            playerAnswer.add(new Card("犯人", "科长"));
                                            confirmAnswer[1] = true;
                                            break;
                                        default:
                                            System.out.println("请输入正确的数值！");
                                            break;
                                    }
                                }
                                if (!confirmAnswer[2]) {
                                    System.out.println("动机是？");
                                    System.out.println("1.情杀 \t 2.觊觎金钱 \t 3.随机杀人 \t 4.仇杀");
                                    input = br.readLine();
                                    switch (input) {
                                        case "1":
                                            playerAnswer.add(new Card("动机", "情杀"));
                                            confirmAnswer[2] = true;
                                            break;
                                        case "2":
                                            playerAnswer.add(new Card("动机", "觊觎金钱"));
                                            confirmAnswer[2] = true;
                                            break;
                                        case "3":
                                            playerAnswer.add(new Card("动机", "随机杀人"));
                                            confirmAnswer[2] = true;
                                            break;
                                        case "4":
                                            playerAnswer.add(new Card("动机", "仇杀"));
                                            confirmAnswer[2] = true;
                                            break;
                                        default:
                                            System.out.println("请输入正确的数值！");
                                            break;
                                    }
                                }
                                if (!confirmAnswer[3]) {
                                    System.out.println("凶器是？");
                                    System.out.println("1.豆腐 \t 2.毒药 \t 3.刀 \t 4.麻绳");
                                    input = br.readLine();
                                    switch (input) {
                                        case "1":
                                            playerAnswer.add(new Card("凶器", "豆腐"));
                                            confirmAnswer[3] = true;
                                            break;
                                        case "2":
                                            playerAnswer.add(new Card("凶器", "毒药"));
                                            confirmAnswer[3] = true;
                                            break;
                                        case "3":
                                            playerAnswer.add(new Card("凶器", "刀"));
                                            confirmAnswer[3] = true;
                                            break;
                                        case "4":
                                            playerAnswer.add(new Card("凶器", "麻绳"));
                                            confirmAnswer[3] = true;
                                            break;
                                        default:
                                            System.out.println("请输入正确的数值！");
                                            break;
                                    }
                                }
                                boolean result = checkAnswer(playerAnswer);
                                if (result) {
                                    System.out.println("推断正确！");
                                } else {
                                    System.out.println("回答错误，游戏结束！");
                                    System.out.println("正确的答案是：");
                                    for (Card card : Deck.answer) {
                                        System.out.println(card.getKind() + "：" + card.getContent());
                                    }
                                }
                            }
                        default:
                            System.out.println("请输入正确的数值！");
                    }
                } else {
                    players[round % 4].draw(players[(round - 1) % 4], br);
                    round++;
                }
            }
            System.out.println("超过轮数，游戏结束！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean checkAnswer(List<Card> playerAnswer) {
        List<Card> answer = Deck.answer;
        for (int i = 0; i < 4; i++) {
            if (!playerAnswer.get(i).equals(answer.get(i))) {
                return false;
            }
        }
        return true;
    }


}
