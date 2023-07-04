package src;

public class Bet {
    public enum BetStatus{
        PENDING,
        WON,
        BLACKJACK_WIN,
        LOST,
        PUSHED
    }
    private double betAmount;
    private BetStatus betStatus;
    public Bet(double betAmount) {
        this.betAmount = betAmount;
        betStatus = BetStatus.PENDING;
    }
    public void setBetStatus(BetStatus betStatus){
        this.betStatus = betStatus;
    }
    public BetStatus getBetStatus(){
        return this.betStatus;
    }
    public void doubleBet(){
        this.betAmount = this.betAmount*2;
    }
    public double getBetAmount(){
        return this.betAmount;
    }
}
