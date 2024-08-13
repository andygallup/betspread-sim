package app;

import java.util.ArrayList;

public class Hand {
    // Hand represents a set of cards and a bet.
    // Hand operations are handled at the Seat level.
    private Bet bet;
    private ArrayList<Integer> cards;
    public Hand(Bet bet, ArrayList<Integer> cards){
        this.bet = bet;
        this.cards = cards;
    }
    public Bet getBet(){
        return bet;
    }
    public void setBetStatus(Bet.BetStatus betStatus){
        this.bet.setBetStatus(betStatus);
    }
    public ArrayList<Integer> getCards(){
        return cards;
    }
    public void setCards(ArrayList<Integer> newCards){
        this.cards = newCards;
    }
}
