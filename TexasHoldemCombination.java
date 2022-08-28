package tag2;

import tag2.tools.CardDeck52;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public final class TexasHoldemCombination implements Comparable<TexasHoldemCombination> {
    public enum CombinationType {
        HighCard,
        OnePair,
        TwoPair,
        ThreeOfAKind,
        Straight,
        Flush,
        FullHouse,
        FourOfAKind,
        StraightFlush,
        RoyalFlush
    }


    CombinationType combinationType;
    List<CardDeck52.Card> combinationCards;

    // a)
    TexasHoldemCombination(List<CardDeck52.Card> tableCards, TexasHoldemHand hand) {
        // TODO
        /**
         * table cards = null
         * table cards = 3
         * table cards = 4
         * table cards = 5
         *
         * hand = 2
         *
         * Alles in eine Liste zusammmenpacken.
         *
         * Predicates erstellen auf die man filtern kann
         * muss bei schwächstem starten und sich dann immer weiter verbessern
         *
         * Höchste Karte kann nicht gefiltert werden, da der Stream dann enden würde,
         * daher muss hier orElse verwendet werden.
         *
         *
         */

        List<CardDeck52.Card> blatt = tableCards;
        blatt.add(hand.card1);
        blatt.add(hand.card2);

        if(findFlush(List.copyOf(blatt).stream())==1){
            if(findStraight(List.copyOf(blatt).stream())){
                this.combinationCards = blatt;
                this.combinationType = CombinationType.StraightFlush;
            }
        }
        if(isFullHouse((List.copyOf(blatt).stream())) == 2){

        }

    }

    public long findPairs (Stream<CardDeck52.Card> stream){
        return
                stream
                        .map(card -> card.value)
                        .peek(System.out :: println)
                        .collect(Collectors.groupingBy(card -> card,Collectors.counting()))
                        .entrySet().stream()
                        .map(entry -> entry.getValue())
                        .max(Comparator.reverseOrder()).orElseThrow();
    }

    public long isFullHouse(Stream<CardDeck52.Card> stream){
        Map<Integer,Long> hash =
        stream
                .map(card -> card.value)
                .peek(System.out :: println)
                .collect(Collectors.groupingBy(card -> card,Collectors.counting()));

        return
                hash.values().stream()
                        .filter(val -> (val == 2 || val == 3))
                        .distinct()
                        .count();
    }

    public boolean findStraight (Stream<CardDeck52.Card> stream) {
        return
                isContinous(stream
                        .map(card -> card.value)
                        .sorted(Comparator.naturalOrder())
                        .peek(System.out::println));


    }

    public long findFlush (Stream<CardDeck52.Card> stream) {
        return
                stream
                        .map(card -> card.sign)
                        .distinct()
                        .count();
    }

    public boolean isContinous(Stream<Integer> intStr){
        Integer start = intStr.findFirst().orElseThrow();
        return LongStream.range(start,intStr.count()).equals(intStr);

    }

    // b)
    @Override
    public final int compareTo(TexasHoldemCombination that) {
        // TODO

        return 0;
    }

    // c)
    public static Stream<TexasHoldemCombination> generate() {
        // TODO

        return null;
    }

    public static void main(String[] args) {
        CardDeck52 deck = new CardDeck52();
        boolean hasTable = Math.random() >= 0.5;
        List<CardDeck52.Card> tableCards = hasTable ?
                deck.deal(ThreadLocalRandom.current().nextInt(3, 5 + 1)) :
                Collections.emptyList();

        TexasHoldemHand hand = new TexasHoldemHand();
        TexasHoldemHand hand2 = new TexasHoldemHand();
        hand.takeDeal(deck.deal());
        hand2.takeDeal(deck.deal());
        hand.takeDeal(deck.deal());
        hand2.takeDeal(deck.deal());

        Stream.of(hand.eval(tableCards), hand2.eval(tableCards))
                .sorted(Comparator.reverseOrder())
                .forEach(combination -> System.out.println(
                        "CombinationType: " + combination.combinationType +
                        ", CombinationCards: " + combination.combinationCards));

        TexasHoldemCombination combination = hand.eval(tableCards);
        System.out.println("Table Cards (" + tableCards.size() + "): " + tableCards);
        System.out.println("Hand: " + Arrays.toString(hand.get()));
        System.out.println("Combination (" + combination.combinationCards.size() + "): " + combination.combinationType + " -> " + combination.combinationCards);
    }
}
