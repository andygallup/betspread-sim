package src;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Optional;

public class Seat {
    private ArrayList<Hand> hands;
    private int bet;
    private Optional<Bet> insuranceBet;
    public Seat(int bet, ArrayList<Integer> cards){
        Bet betObj = new Bet(bet);
        this.bet = bet;
        insuranceBet = Optional.empty();

        // Create initial hand
        Hand hand = new Hand(betObj, cards);
        hands.add(hand);
    }
    public void takeInsurance(){
        insuranceBet = Optional.of(new Bet((double) (bet) / 2.0));
    }
    public Optional<Bet> getInsuranceBet(){
        return insuranceBet;
    }
    public ArrayList<Hand> getHands(){
        return hands;
    }
}
