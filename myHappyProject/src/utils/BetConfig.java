package src.utils;

public class BetConfig {
    int seats;
    int bettingUnits;
    public BetConfig(int seats, int bettingUnits){
        this.seats = seats;
        this.bettingUnits = bettingUnits;
    }
    public int getSeats() {
        return seats;
    }

    public int getBettingUnits() {
        return bettingUnits;
    }
}
