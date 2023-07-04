package src;

import src.utils.BetConfig;

import java.sql.Array;
import java.util.*;

import static src.utils.PlayerHandUtils.*;

public class TableManager {
    private int shoeSize;
    private boolean stand17;
    private double deckPen;
    private int minBet;
    private double startingBankroll;
    private int handsPerHour;
    private int hoursPlayed;
    private ArrayList<Seat> seats;
    private ArrayList<Integer> dealerHand;
    private ArrayList<Integer> shoe;
    private int runningCount;
    private DecisionMaker decisionMaker;
    private Map<Integer, BetConfig> betSpread;
    public TableManager(int shoeSize, boolean stand17, double deckPen, int minBet, double startingBankroll, int handsPerHour, int hoursPlayed, Map<Integer, BetConfig> betSpread){
        this.stand17 = stand17;
        this.shoeSize = shoeSize;
        this.deckPen = deckPen;
        this.minBet = minBet;
        this.startingBankroll = startingBankroll;
        this.handsPerHour = handsPerHour;
        this.hoursPlayed = hoursPlayed;
        this.runningCount = 0;
        this.decisionMaker = new DecisionMaker(stand17);
        this.betSpread = betSpread;
        shuffleShoe();

        // for rounds

    }

    public void playRound(){
        // should shuffle?
        if(shouldShuffle()) { shuffleShoe();}
        // Clear dealer hand and seats (clears hands)
        dealerHand = new ArrayList<Integer>();
        seats = new ArrayList<Seat>();
        // determine number of seats and bets based on count
        int seatsToPlay = betSpread.get(calculateTrueCount(runningCount, shoe.size())).getSeats();
        int bet = minBet * betSpread.get(calculateTrueCount(runningCount, shoe.size())).getBettingUnits();
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
            //payout();
            // TODO: Add down card to count
            return;
        }
        handlePlayerBlackjack();
        // play seats
        playSeats(dealerUpCard);
        // TODO: Add down card to count
        // pay out
        // clear seats and hands
        // TODO: If you run out of money don't bet
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
                if(seat.getHands().size() >= 4) { decisionMaker.setSplitIsLegal(false); }
                do{
                    decision = decisionMaker.makeDecision(seat.getHands().get(i).getCards(), dealerUpCard, calculateTrueCount(runningCount, shoe.size()));
                    switch (decision) {
                        case SPLIT:
                            numHands++;
                            return;
                        case DOUBLE:
                            return;
                        case SURRENDER:
                            return;
                        case HIT:
                            return;
                        case STAND:
                    }
                } while(decision==PlayDecision.HIT || decision==PlayDecision.SPLIT);
            }
            decisionMaker.setSplitIsLegal(true);
        }
    }
}
