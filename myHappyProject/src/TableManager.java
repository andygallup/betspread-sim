package src;

import src.utils.BetConfig;

import java.util.*;
import java.util.logging.Logger;

import static java.lang.Math.floor;
import static java.lang.System.exit;
import static src.utils.PlayerHandUtils.*;

public class TableManager {
    private int shoeSize;
    private boolean stand17;
    private double deckPen;
    private int minBet;
    private double bankroll;
    private int handsPerHour;
    private int hoursPlayed;
    private ArrayList<Seat> seats;
    private ArrayList<Integer> dealerHand;
    private ArrayList<Integer> shoe;
    private int runningCount;
    private DecisionMaker decisionMaker;
    private Map<Integer, BetConfig> betSpread;
    Logger logger = Logger.getLogger(TableManager.class.getName());
    public TableManager(int shoeSize, boolean stand17, double deckPen, int minBet, double bankroll, int handsPerHour, int hoursPlayed, Map<Integer, BetConfig> betSpread){
        this.stand17 = stand17;
        this.shoeSize = shoeSize;
        this.deckPen = deckPen;
        this.minBet = minBet;
        this.bankroll = bankroll;
        this.handsPerHour = handsPerHour;
        this.hoursPlayed = hoursPlayed;
        this.runningCount = 0;
        this.decisionMaker = new DecisionMaker(stand17);
        decisionMaker.buildBasicStrategyTables();
        this.betSpread = betSpread;
        shuffleShoe();
    }
    public double playGame(){
        int numRounds = handsPerHour * hoursPlayed;
        for (int i = 0; i < numRounds; i++){
            playRound();
        }
        return bankroll;
    }

    public void playRound(){
        // can you even play bro?
        if(bankroll <= 3*minBet){
            return;
        }
        // should shuffle?
        if(shouldShuffle()) { shuffleShoe();}
        // Clear dealer hand and seats (clears hands)
        dealerHand = new ArrayList<Integer>();
        seats = new ArrayList<Seat>();
        // determine number of seats and bets based on count
        int betCount = (int)floor(calculateTrueCount(runningCount, shoe.size()));
        if (betCount > 4) { betCount = 4; }
        if (betCount < 0) { betCount = 0; }
        int seatsToPlay = betSpread.get(betCount).getSeats();
        int bet = minBet * betSpread.get(betCount).getBettingUnits();
        // create seats
        for(int i = 0; i < seatsToPlay; i++){
            ArrayList<Integer> tempCards = new ArrayList<Integer>();
            tempCards.add(dealCard());
            tempCards.add(dealCard());
            Seat seat = new Seat(bet, tempCards);
            seats.add(seat);
        }
        // deal dealer hand
        dealerHand.add(dealDownCard());
        int dealerUpCard = dealCard();
        dealerHand.add(dealerUpCard);
        // insurance
        handleInsurance(dealerUpCard);
        // check for blackjack (dealer and player)
        if(handleDealerBlackjack()){
            // if dealer had blackjack, payout and return (round is over)
            payOut();
            return;
        }
        handlePlayerBlackjack();
        // play seats
        playSeats(dealerUpCard);
        // play dealer
        playDealerHand();
        // evaluate bet statuses
        evaluateBets();
        // pay out
        payOut();
    }
    public int dealCard(){
        // Removes a card from the deck and returns the value
        // Also tracks the running count
        int card = shoe.remove(shoe.size() - 1);
        runningCount += returnCountForCard(card);
        return card;
    }
    public int dealDownCard(){
        return shoe.remove(shoe.size() - 1);
    }
    public boolean shouldShuffle(){
        return shoe.size() < shoeSize*52*(1.0-deckPen);
    }
    public void shuffleShoe(){
        //Repopulate the deck
        shoe = new ArrayList<Integer>();
        List<Integer> fresh_deck = Arrays.asList(
                1,1,1,1,
                2,2,2,2,
                3,3,3,3,
                4,4,4,4,
                5,5,5,5,
                6,6,6,6,
                7,7,7,7,
                8,8,8,8,
                9,9,9,9,
                10,10,10,10,
                10,10,10,10,
                10,10,10,10,
                10,10,10,10);
        for (int i = 0; i < shoeSize; i++) {
            shoe.addAll(fresh_deck);
        }

        //Shuffle
        Collections.shuffle(shoe);
        runningCount = 0;
    }
    public void setShoe2s(){
        // Method to ease testing
        shoe = new ArrayList<Integer>();
        List<Integer> fresh_deck = Arrays.asList(
                2, 2, 2, 2,
                2, 2, 2, 2,
                2, 2, 2, 2,
                2, 2, 2, 2, 2,
                2, 2, 2, 2,
                2, 2, 2, 2,
                2,2, 2, 2, 2,
                2, 2, 2, 2,
                2, 2, 2, 2,
                2,2, 2, 2, 2,
                2, 2, 2, 2,
                2, 2, 2, 2,
                2,2, 2, 2, 2,
                2, 2, 2, 2,
                2, 2, 2, 2,
                2);
        for (int i = 0; i < shoeSize; i++) {
            shoe.addAll(fresh_deck);
        }
        runningCount = 0;
    }
    public void handleInsurance(int dealerUpCard){
        // Buy insurance for each seat if necessary (dealer showing Ace and count > 3)
        if(dealerUpCard == 1 && calculateTrueCount(runningCount, shoe.size()) > 3){
            for(Seat seat : seats){
                seat.takeInsurance();
            }
        }
    }
    public boolean handleDealerBlackjack(){
        // Handles dealer blackjack and insurance bets
        if(isBlackjack(dealerHand)){
            for(Seat seat : seats){
                if(seat.getInsuranceBet().isPresent()){
                    seat.getInsuranceBet().get().setBetStatus(Bet.BetStatus.WON);
                }
                Hand seatHand = seat.getHands().get(0);
                if(isBlackjack(seatHand.getCards())){
                    seatHand.setBetStatus(Bet.BetStatus.PUSHED);
                }
                else{
                    seatHand.setBetStatus(Bet.BetStatus.LOST);
                }
            }
            return true;
        }
        else{
            for(Seat seat : seats) {
                if (seat.getInsuranceBet().isPresent()) {
                    seat.getInsuranceBet().get().setBetStatus(Bet.BetStatus.LOST);
                }
            }
            return false;
        }
    }
    public void handlePlayerBlackjack(){
        for(Seat seat : seats){
            Hand seatHand = seat.getHands().get(0);
            if(isBlackjack(seatHand.getCards())){
                seatHand.setBetStatus(Bet.BetStatus.BLACKJACK_WIN);
            }
        }

    }
    public void playSeats(int dealerUpCard){
        for(Seat seat : seats){
            int numHands = seat.getHands().size();
            for(int i = 0; i < numHands; i++) {
                PlayDecision decision;
                Hand currHand = seat.getHands().get(i);
                do{
                    if(seat.getHands().size() >= 4) { decisionMaker.setSplitIsLegal(false); }
                    decision = decisionMaker.makeDecision(currHand.getCards(), dealerUpCard, calculateTrueCount(runningCount, shoe.size()));
                    switch (decision) {
                        case SPLIT:
                            Bet newBet = new Bet(currHand.getBet().getBetAmount());
                            ArrayList<Integer> newCards = new ArrayList<Integer>();
                            newCards.add(currHand.getCards().remove(0));
                            newCards.add(dealCard());
                            Hand newHand = new Hand(newBet, newCards);
                            currHand.getCards().add(dealCard());
                            seat.getHands().add(newHand);
                            numHands++;
                            break;
                        case DOUBLE:
                            currHand.getBet().doubleBet();
                            currHand.getCards().add(dealCard());
                            break;
                        case SURRENDER:
                            currHand.setBetStatus(Bet.BetStatus.SURRENDERED);
                            break;
                        case HIT:
                            currHand.getCards().add(dealCard());
                            break;
                        case STAND:
                            break;
                        case OOPSIE:
                            System.out.println("SEAT: " + seat.toString());
                            System.out.println("DEALER HAND: " + dealerHand.get(1));
                            System.out.println("RUNNING COUNT: " + runningCount);
                            System.out.println("TRUE COUNT: " + calculateTrueCount(runningCount, shoe.size()));
                            System.out.println("CARDS LEFT IN SHOE: " + shoe.size());
                            System.out.println("BANKROLL: " + bankroll);
                            System.out.println("OOPSIE");
                            exit(1);
                        default:
                            throw new RuntimeException("DEFAULTED ON DECISION MAKING");
                    }
                } while(decision != PlayDecision.STAND && decision != PlayDecision.DOUBLE && decision != PlayDecision.SURRENDER && playerHandValue(currHand.getCards()) < 22);
            }
            decisionMaker.setSplitIsLegal(true);
        }
    }
    public void playDealerHand(){
        while(playerHandValue(dealerHand) < 17){
            dealerHand.add(dealCard());
        }
        if(playerHandValue(dealerHand) == 17 && isSoft(dealerHand) && !stand17){
            dealerHand.add(dealCard());
        }
    }
    public void evaluateBets(){
        for (Seat seat : seats){
            for (Hand hand : seat.getHands()){
                Bet currBet = hand.getBet();
                if (currBet.getBetStatus() == Bet.BetStatus.PENDING) {
                    if (playerHandValue(hand.getCards()) >= 22) {
                        currBet.setBetStatus(Bet.BetStatus.LOST);
                        continue;
                    }
                    if (playerHandValue(dealerHand) >= 22) {
                        currBet.setBetStatus(Bet.BetStatus.WON);
                        continue;
                    }
                    if (playerHandValue(hand.getCards()) > playerHandValue(dealerHand)){
                        currBet.setBetStatus(Bet.BetStatus.WON);
                        continue;
                    }
                    if (playerHandValue(hand.getCards()) == playerHandValue(dealerHand)){
                        currBet.setBetStatus(Bet.BetStatus.PUSHED);
                        continue;
                    }
                    if (playerHandValue(hand.getCards()) < playerHandValue(dealerHand)){
                        currBet.setBetStatus(Bet.BetStatus.LOST);
                    }
                }
            }
        }
    }
    public void payOut(){
        runningCount += returnCountForCard(dealerHand.get(0));
        for(Seat seat : seats) {
            // pay insurance bets as they are handled on a per seat, not per hand basis
            if(seat.getInsuranceBet().isPresent()){
                double insurancePayoutAmount = seat.getInsuranceBet().get().getBetAmount();
                if(seat.getInsuranceBet().get().getBetStatus() == Bet.BetStatus.WON){
                    bankroll += insurancePayoutAmount * 2; //insurance pays 2:1
                }
                else if (seat.getInsuranceBet().get().getBetStatus() == Bet.BetStatus.LOST) {
                    bankroll -= insurancePayoutAmount;
                }
                else{
                    System.out.println("BET STATUS: " + seat.getInsuranceBet().get().getBetStatus());
                    throw new RuntimeException("Insurance paying something not WON/LOST");
                }
            }
            for (Hand hand : seat.getHands()) {
                double betAmount = hand.getBet().getBetAmount();
                if(hand.getBet().getBetStatus() == Bet.BetStatus.BLACKJACK_WIN){
                    bankroll += betAmount * 1.5;
                }
                else if(hand.getBet().getBetStatus() == Bet.BetStatus.WON){
                    bankroll += betAmount;
                }
                else if(hand.getBet().getBetStatus() == Bet.BetStatus.PUSHED){
                    // DO NOTHING
                }
                else if(hand.getBet().getBetStatus() == Bet.BetStatus.LOST){
                    bankroll -= betAmount;
                }
                else if(hand.getBet().getBetStatus() == Bet.BetStatus.SURRENDERED){
                    bankroll -= betAmount/2;
                }
                else{
                    System.out.println("BET STATUS: " + hand.getBet().getBetStatus());
                    throw new RuntimeException("Invalid bet status during payout");
                }
            }
        }
    }
}
