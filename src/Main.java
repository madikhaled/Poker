import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<PokerHand> hands = new ArrayList<>();
        PokerHand hand1 = new PokerHand("KS TH QC JD AD");
        PokerHand hand2 = new PokerHand("AS AH AC 9D TD");

        System.out.println(hand1.determineHand());
        Result result = hand1.compareWith(hand2);
        hand1.printResult(result);
        System.out.println("Hand 1: \n" + hand1);
        System.out.println("Hand 2: \n" + hand2);
        hands.add(hand1);
        hands.add(hand2);
        Collections.sort(hands, Collections.reverseOrder());
        System.out.println(hands);

        // Read poker hands from input.txt
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                hands.add(new PokerHand(line));
            }
        } catch (IOException e) {
            System.err.println("Error reading input.txt: " + e.getMessage());
        }


        Collections.sort(hands);


        PokerHand winningHand = hands.get(0);
        HandStrength winningHandType = winningHand.determineHand();

        // Write the sorted hands and their combinations to output.txt
        try (PrintWriter writer = new PrintWriter(new FileWriter("output.txt"))) {
            // Print the winning hand
            writer.println("Winning hand: \n" + winningHand);
            writer.println("Winning hand type: " + winningHandType);
            writer.println();

            for (PokerHand hand : hands) {
                HandStrength handType = hand.determineHand(); // Use determineHand method to get hand type
                writer.println(hand);
                writer.println("Hand type: " + handType);
                writer.println();
            }
        } catch (IOException e) {
            System.err.println("Error writing to output.txt: " + e.getMessage());
        }
    }
}
