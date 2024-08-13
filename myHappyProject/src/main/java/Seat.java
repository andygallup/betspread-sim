package main.java;

import java.util.ArrayList;
import java.util.Optional;

public class Seat {
    private ArrayList<Hand> hands;
    private int bet;
    private Optional<Bet> insuranceBet;
    public Seat(int bet, ArrayList<Integer> cards){
        hands = new ArrayList<Hand>();
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

    @Override
    public String toString() {
        String ret = "";
        for (Hand hand : hands){
            ret += " HAND: ";
            for (Integer card : hand.getCards()) {
                ret += card + ", " ;
            }
        }
        return ret;
    }
}
