import java.util.*;

public class PokerHand implements Hand, Comparable<PokerHand>{
    private List<Card> cards;
    private char valueChar;
    private char colorChar;

    public List<Card> getCards() {
        return cards;
    }

    public PokerHand(String hand) {
        this.cards = parseCards(hand);
    }

    private List<Card> parseCards(String hand) {// la methode parseCards prend en parametre une chaine de caractere hand pour la transformer en liste de cartes
        List<Card> parsedCards = new ArrayList<>();
        String[] cardStrings = hand.split(" ");
        for (String cardString : cardStrings) {
            parsedCards.add(parseCard(cardString));
        }
        return parsedCards;
    }

    private Card parseCard(String cardString) {
        valueChar = cardString.charAt(0);
        colorChar = cardString.charAt(1);
        CardValue value = parseCardValue(valueChar);
        CardColor suit = parseCardColor(colorChar);
        return new Card(value, suit, valueChar, colorChar);
    }

    private CardValue parseCardValue(char valueChar) {
        return switch (valueChar) {
            case '2' -> CardValue.Deux;
            case '3' -> CardValue.Trois;
            case '4' -> CardValue.Quatre;
            case '5' -> CardValue.Cinq;
            case '6' -> CardValue.Six;
            case '7' -> CardValue.Sept;
            case '8' -> CardValue.Huit;
            case '9' -> CardValue.Neuf;
            case 'T' -> CardValue.Dix;
            case 'J' -> CardValue.Valet;
            case 'Q' -> CardValue.Reine;
            case 'K' -> CardValue.Roi;
            case 'A' -> CardValue.As;
            default -> throw new IllegalArgumentException("Invalid card value: " + valueChar);
        };
    }

    private CardColor parseCardColor(char colorChar) {
        return switch (colorChar) {
            case 'S' -> CardColor.Pique;
            case 'H' -> CardColor.Coeur;
            case 'D' -> CardColor.Carraux;
            case 'C' -> CardColor.Trefle;
            default -> throw new IllegalArgumentException("Invalid card suit: " + colorChar);
        };
    }


    public HandStrength determineHand() {
        HandRank handRank = new HandRank(cards);

        if (handRank.isQuinteFlushRoyale()) {
            return HandStrength.QUINTE_FLUSH_ROYALE;
        } else if (handRank.isQuinteFlush()) {
            return HandStrength.QUINTE_FLUSH;
        } else if (handRank.isCarre()) {
            return HandStrength.CARRE;
        } else if (handRank.isFull()) {
            return HandStrength.FULL;
        } else if (handRank.isCouleur()) {
            return HandStrength.COULEUR;
        } else if (handRank.isSuite()) {
            return HandStrength.SUITE;
        } else if (handRank.isBrelan()) {
            return HandStrength.BRELAN;
        } else if (handRank.isDoublePaire()) {
            return HandStrength.DOUBLE_PAIRE;
        } else if (handRank.isPaire()) {
            return HandStrength.PAIRE;
        } else {
            return HandStrength.CARTE_HAUTE;
        }
    }


    @Override
public Result compareWith(Hand hand) {
    if (!(hand instanceof PokerHand)) {
        throw new IllegalArgumentException("Invalid hand type");
    }
    PokerHand otherHand = (PokerHand) hand;

    int thisMaxCount = getMaxCardCount(this.cards);
    int otherMaxCount = getMaxCardCount(otherHand.cards);

    if (thisMaxCount > otherMaxCount) {
        return Result.WIN;
    } else if (thisMaxCount < otherMaxCount) {
        return Result.LOSS;
    } else {
        CardValue thisMaxValue = getMaxCardValue(this.cards);
        CardValue otherMaxValue = getMaxCardValue(otherHand.cards);

        if (thisMaxValue.ordinal() > otherMaxValue.ordinal()) {
            return Result.WIN;
        } else if (thisMaxValue.ordinal() < otherMaxValue.ordinal()) {
            return Result.LOSS;
        } else {
            return Result.TIE;
        }
    }
}
    public void printResult(Result result) {
        if (result == Result.WIN) {
            System.out.println("WIN");
        } else if (result == Result.LOSS) {
            System.out.println("LOSS");
        } else {
            System.out.println("TIE");
        }
    }

private int getMaxCardCount(List<Card> cards) {
    Map<CardValue, Integer> cardCounts = new HashMap<>();
    for (Card card : cards) {
        cardCounts.put(card.getValue(), cardCounts.getOrDefault(card.getValue(), 0) + 1);
    }
    return Collections.max(cardCounts.values());
}

private CardValue getMaxCardValue(List<Card> cards) {
    return cards.stream()
            .map(Card::getValue)
            .max(Comparator.comparingInt(CardValue::ordinal))
            .orElseThrow(() -> new IllegalArgumentException("Hand is empty"));
}


   @Override
public int compareTo(PokerHand otherHand) {
    Result result = this.compareWith(otherHand);
    return switch (result) {
        case WIN -> 1;
        case LOSS -> -1;
        case TIE -> 0;
    };
}

@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 5; i++) {
        for (Card card : cards) {
            String[] lines = card.toASCII().split("\n");
            sb.append(lines[i]).append("  ");
        }
        sb.append("\n");
    }
    return sb.toString();
}


}