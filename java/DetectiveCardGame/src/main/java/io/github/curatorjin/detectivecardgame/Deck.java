package io.github.curatorjin.detectivecardgame;

import java.util.*;

public class Deck {
    public static List<Card> originDeck = new ArrayList<>();
    public static List<Card> answer = new ArrayList<>();
    public static Map<String, Map<String, Integer>> deskDeck = new HashMap<>();

    static {
        String[] strings = new String[]{"现场", "犯人", "动机", "凶器", "宇宙", "池塘", "家", "山", "蝗虫", "JK", "不良少年", "科长", "情杀",
                "觊觎金钱", "随机杀人", "仇杀", "豆腐", "毒药", "刀", "麻绳"};
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == 0) {
                    originDeck.add(new Card(strings[i], strings[j + i * 4 + 4]));
                } else {
                    for (int k = 0; k < 4; k++) {
                        originDeck.add(new Card(strings[i], strings[j + i * 4 + 4]));
                    }
                }
            }
        }
        Collections.shuffle(originDeck);
        Random random = new Random();
        Card card;
        for (int i = 0; i < 4; i++) {
            int j = random.nextInt(13);
            if (j == 0) {
                card = new Card(strings[i], strings[4 + i * 4]);
            } else {
                if (j % 4 == 0) {
                    card = new Card(strings[i], strings[j / 4 + 4 + i * 4]);
                } else {
                    card = new Card(strings[i], strings[j % 4 + 4 + i * 4]);
                }
            }
            answer.add(card);
            originDeck.remove(card);
        }
    }

    public Map<String, Map<String, Integer>> count = new HashMap<>();
    private List<Card> cards = new ArrayList<>(); // 牌组内的卡

    public static void putToDeskDeck(Card card) {
        deskDeck.computeIfAbsent(card.getKind(), k -> new HashMap<>());
        Integer num = deskDeck.get(card.getKind()).get(card.getContent());
        if (num == null) {
            num = 1;
        } else {
            num += 1;
        }
        deskDeck.get(card.getKind()).put(card.getContent(), num);
    }

    public static void viewDeskDeck() {
        Set<String> kindSet = deskDeck.keySet();
        for (String string : kindSet) {
            System.out.println(string + ":");
            Map<String, Integer> countMap = deskDeck.get(string);
            Set<String> contentSet = countMap.keySet();
            for (String string2 : contentSet) {
                System.out.println(string2 + ":" + countMap.get(string2) + "张");
            }
        }
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void modifyPut(Card card) {
        if (cards.contains(card)) {
            System.out.println(card.toString() + "\t凑成了一对被放到桌面");
            removeCard(card);
            putToDeskDeck(card);
            putToDeskDeck(card);
        } else {
            cards.add(card);
            count.computeIfAbsent(card.getKind(), k -> new HashMap<>());
            Integer num = count.get(card.getKind()).get(card.getContent());
            if (num == null) {
                num = 1;
            } else {
                num += 1;
            }
            count.get(card.getKind()).put(card.getContent(), num);
        }
    }

    public void removeCard(Card card) {
        cards.remove(card);
        count.computeIfAbsent(card.getKind(), k -> new HashMap<>());
        Integer num = count.get(card.getKind()).get(card.getContent());
        if (num == null || num == 0) {
            num = 0;
        } else {
            num -= 1;
        }
        count.get(card.getKind()).put(card.getContent(), num);
    }

    public List<Card> getKind(String kind) {
        List<Card> list = new ArrayList<>();
        for (Card card : cards) {
            if (card.getKind().equals(kind)) {
                list.add(card);
            }
        }
        return list;
    }


    public void viewDeck() {
        Set<String> kindSet = count.keySet();
        for (String string : kindSet) {
            System.out.println(string + ":");
            Map<String, Integer> countMap = count.get(string);
            Set<String> contentSet = countMap.keySet();
            for (String string2 : contentSet) {
                if (countMap.get(string2) > 0) {
                    System.out.println(string2 + ":" + countMap.get(string2) + "张");
                }
            }
        }
    }

    public void showCount() {
        count.forEach((key, value) -> {
            int cardNum = 0;
            for (Map.Entry<String, Integer> kindEntry : value.entrySet()) {
                cardNum += kindEntry.getValue();
            }
            if (cardNum > 0) {
                System.out.println(key + ":" + cardNum + "张");
            }
        });
    }
}
