import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HandRank {
    private  List<Card> cards;
    private  HandStrength strength;

    public HandRank(List<Card> cards) {
        this.cards = cards;
        this.strength = determineHand();
    }
    private HandStrength determineHand() {
        if (isQuinteFlushRoyale()) {
            return HandStrength.QUINTE_FLUSH_ROYALE;
        } else if (isQuinteFlush()) {
            return HandStrength.QUINTE_FLUSH;
        } else if (isCarre()) {
            return HandStrength.CARRE;
        } else if (isFull()) {
            return HandStrength.FULL;
        } else if (isCouleur()) {
            return HandStrength.COULEUR;
        } else if (isSuite()) {
            return HandStrength.SUITE;
        } else if (isBrelan()) {
            return HandStrength.BRELAN;
        } else if (isDoublePaire()) {
            return HandStrength.DOUBLE_PAIRE;
        } else if (isPaire()) {
            return HandStrength.PAIRE;
        } else {
            return HandStrength.CARTE_HAUTE;
        }
    }

    boolean isQuinteFlushRoyale() {
        return isQuinteFlush() && cards.get(0).getValue() == CardValue.As;
    }

    boolean isQuinteFlush() {
        return isSuite() && isCouleur();
    }

    boolean isSuite() {
        List<CardValue> values = cards.stream().map(Card::getValue).collect(Collectors.toList());
        Collections.sort(values);

        return IntStream.range(0, values.size() - 1)
                .allMatch(i -> values.get(i + 1).ordinal() - values.get(i).ordinal() == 1);
    }

    boolean isCouleur() {
        return cards.stream().map(Card::getColor).distinct().count() == 1;
    }

    boolean isCarre() {
        Map<CardValue, Long> valueCount = cards.stream()
                .collect(Collectors.groupingBy(Card::getValue, Collectors.counting()));

        return valueCount.values().stream().anyMatch(count -> count == 4);
    }

    boolean isFull() {
        Map<CardValue, Long> valueCount = cards.stream()
                .collect(Collectors.groupingBy(Card::getValue, Collectors.counting()));

        return valueCount.values().stream().anyMatch(count -> count == 3)
                && valueCount.values().stream().anyMatch(count -> count == 2);
    }

    boolean isBrelan() {
        Map<CardValue, Long> valueCount = cards.stream()
                .collect(Collectors.groupingBy(Card::getValue, Collectors.counting()));

        return valueCount.values().stream().anyMatch(count -> count == 3);
    }

    boolean isDoublePaire() {
        Map<CardValue, Long> valueCount = cards.stream()
                .collect(Collectors.groupingBy(Card::getValue, Collectors.counting()));

        long pairCount = valueCount.values().stream().filter(count -> count == 2).count();
        return pairCount == 2;
    }
    boolean isPaire() {
        Map<CardValue, Long> valueCount = cards.stream()
                .collect(Collectors.groupingBy(Card::getValue, Collectors.counting()));

        return valueCount.values().stream().anyMatch(count -> count == 2);
    }


    private int compareSuiteValues(HandRank otherHandRank) {
        List<CardValue> thisSuiteValues = getSuiteValues();
        List<CardValue> otherSuiteValues = otherHandRank.getSuiteValues();

        for (int i = 0; i < thisSuiteValues.size(); i++) {
            int comparison = thisSuiteValues.get(i).compareTo(otherSuiteValues.get(i));
            if (comparison != 0) {
                return comparison;
            }
        }

        return 0;
    }

    private List<CardValue> getSuiteValues() {
        return cards.stream()
                .sorted(Comparator.comparing(Card::getValue))
                .map(Card::getValue)
                .collect(Collectors.toList());
    }

    private int compareFlushValues(HandRank otherHandRank) {
        List<CardValue> thisFlushValues = getFlushValues();
        List<CardValue> otherFlushValues = otherHandRank.getFlushValues();

        for (int i = 0; i < thisFlushValues.size(); i++) {
            int comparison = thisFlushValues.get(i).compareTo(otherFlushValues.get(i));
            if (comparison != 0) {
                return comparison;
            }
        }

        return 0;
    }

    private List<CardValue> getFlushValues() {
        return cards.stream()
                .sorted((c1, c2) -> {
                    int valueComparison = c2.getValue().compareTo(c1.getValue());
                    if (valueComparison != 0) {
                        return valueComparison;
                    }
                    return c1.getColor().compareTo(c2.getColor());
                })
                .map(Card::getValue)
                .collect(Collectors.toList());
    }

    private int compareFourOfAKindValues(HandRank otherHandRank) {
        Map<CardValue, Long> thisValueCount = getValueCount();
        Map<CardValue, Long> otherValueCount = otherHandRank.getValueCount();

        CardValue thisFourOfAKindValue = getCardValueWithCount(thisValueCount, 4).orElseThrow();
        CardValue otherFourOfAKindValue = getCardValueWithCount(otherValueCount, 4).orElseThrow();

        int comparison = thisFourOfAKindValue.compareTo(otherFourOfAKindValue);
        if (comparison != 0) {
            return comparison;
        }

        // Comparez les kickers
        CardValue thisKickerValue = getCardValueWithCount(thisValueCount, 1).orElseThrow();
        CardValue otherKickerValue = getCardValueWithCount(otherValueCount, 1).orElseThrow();
        return thisKickerValue.compareTo(otherKickerValue);
    }

    private Map<CardValue, Long> getValueCount() {
        return cards.stream()
                .collect(Collectors.groupingBy(Card::getValue, Collectors.counting()));
    }

    private Optional<CardValue> getCardValueWithCount(Map<CardValue, Long> valueCount, long count) {
        return valueCount.entrySet().stream()
                .filter(entry -> entry.getValue() == count)
                .map(Map.Entry::getKey)
                .findAny();
    }

    private int compareFullHouseValues(HandRank otherHandRank) {
        Map<CardValue, Long> thisValueCount = getValueCount();
        Map<CardValue, Long> otherValueCount = otherHandRank.getValueCount();

        CardValue thisThreeOfAKindValue = getCardValueWithCount(thisValueCount, 3).orElseThrow();
        CardValue otherThreeOfAKindValue = getCardValueWithCount(otherValueCount, 3).orElseThrow();

        int comparison = thisThreeOfAKindValue.compareTo(otherThreeOfAKindValue);
        if (comparison != 0) {
            return comparison;
        }

        // Comparez les paires
        CardValue thisPairValue = getCardValueWithCount(thisValueCount, 2).orElseThrow();
        CardValue otherPairValue = getCardValueWithCount(otherValueCount, 2).orElseThrow();
        return thisPairValue.compareTo(otherPairValue);
    }

    private int compareThreeOfAKindValues(HandRank otherHandRank) {
        Map<CardValue, Long> thisValueCount = getValueCount();
        Map<CardValue, Long> otherValueCount = otherHandRank.getValueCount();

        CardValue thisThreeOfAKindValue = getCardValueWithCount(thisValueCount, 3).orElseThrow();
        CardValue otherThreeOfAKindValue = getCardValueWithCount(otherValueCount, 3).orElseThrow();

        int comparison = thisThreeOfAKindValue.compareTo(otherThreeOfAKindValue);
        if (comparison != 0) {
            return comparison;
        }

        // Comparez les paires et les kickers
        List<CardValue> thisRemainingValues = getRemainingValues(thisValueCount, 3);
        List<CardValue> otherRemainingValues = getRemainingValues(otherValueCount, 3);

        return compareHighCardValues(thisRemainingValues, otherRemainingValues);
    }

    private List<CardValue> getRemainingValues(Map<CardValue, Long> valueCount, long countToExclude) {
        return valueCount.entrySet().stream()
                .filter(entry -> entry.getValue() != countToExclude)
                .map(Map.Entry::getKey)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }
    private List<CardValue> getHighCardValues() {
        return cards.stream()
                .map(Card::getValue)
                .collect(Collectors.toList());
    }
    private int compareHighCardValues(List<CardValue> thisHighCards, List<CardValue> otherHighCards) {

        List<CardValue> sortedThisHighCards = new ArrayList<>(thisHighCards);
        Collections.sort(sortedThisHighCards, Collections.reverseOrder());
        List<CardValue> sortedOtherHighCards = new ArrayList<>(otherHighCards);
        Collections.sort(sortedOtherHighCards, Collections.reverseOrder());


        for (int i = 0; i < sortedThisHighCards.size(); i++) {
            int comparison = sortedThisHighCards.get(i).compareTo(sortedOtherHighCards.get(i));
            if (comparison != 0) {
                return comparison;
            }
        }


        return 0;
    }



    private int compareTwoPairsValues(HandRank otherHandRank) {
        Map<CardValue, Long> thisValueCount = getValueCount();
        Map<CardValue, Long> otherValueCount = otherHandRank.getValueCount();

        List<CardValue> thisPairValues = getPairValues(thisValueCount);
        List<CardValue> otherPairValues = getPairValues(otherValueCount);

        int comparison = thisPairValues.get(0).compareTo(otherPairValues.get(0));
        if (comparison != 0) {
            return comparison;
        }

        comparison = thisPairValues.get(1).compareTo(otherPairValues.get(1));
        if (comparison != 0) {
            return comparison;
        }

        // Comparez les kickers
        CardValue thisKickerValue = getCardValueWithCount(thisValueCount, 1).orElseThrow();
        CardValue otherKickerValue = getCardValueWithCount(otherValueCount, 1).orElseThrow();
        return thisKickerValue.compareTo(otherKickerValue);
    }

    private List<CardValue> getPairValues(Map<CardValue, Long> valueCount) {
        return valueCount.entrySet().stream()
                .filter(entry -> entry.getValue() == 2)
                .map(Map.Entry::getKey)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    private int compareOnePairValues(HandRank otherHandRank) {
        Map<CardValue, Long> thisValueCount = getValueCount();
        Map<CardValue, Long> otherValueCount = otherHandRank.getValueCount();

        CardValue thisPairValue = getCardValueWithCount(thisValueCount, 2).orElseThrow();
        CardValue otherPairValue = getCardValueWithCount(otherValueCount, 2).orElseThrow();

        int comparison = thisPairValue.compareTo(otherPairValue);
        if (comparison != 0) {
            return comparison;
        }


        List<CardValue> thisHighCards = getRemainingValues(thisValueCount, 2);
        List<CardValue> otherHighCards = getRemainingValues(otherValueCount, 2);

        return compareHighCardValues(thisHighCards, otherHighCards);
    }

}