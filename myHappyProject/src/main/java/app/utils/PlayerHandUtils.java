package app.utils;

import java.util.List;

public class PlayerHandUtils {
    public static enum PlayDecision {
        HIT,
        STAND,
        SPLIT,
        DO_NOT_SPLIT,
        DOUBLE,
        NO_DEVIATION,
        SURRENDER,
        BUY_INSURANCE,
        OOPSIE,
    }
    public static int sumPlayerHand(List<Integer> playerCardList){
        int sum = 0;
        for(int i = 0; i < playerCardList.size(); i++){
            sum += playerCardList.get(i);
        }
        return sum;
    }
    public static boolean isSoft(List<Integer> playerCardList){
        return sumPlayerHand(playerCardList) <= 11 && playerCardList.contains(1);
    }
    public static int playerHandValue(List<Integer> playerCardList){
        int sum = sumPlayerHand(playerCardList);
        return isSoft(playerCardList) ? sum + 10 : sum;
    }
    public static boolean isBlackjack(List<Integer> playerCardList){
        return playerHandValue(playerCardList) == 21 && playerCardList.size() == 2;
    }
    public static boolean handIsSplittable(List<Integer> playerCardList){
        return playerCardList.size() == 2 && playerCardList.get(0) == playerCardList.get(1);
    }
    public static boolean canDouble(List<Integer> playerCardList){
        return playerCardList.size() == 2 && !isBlackjack(playerCardList);
    }
    public static double calculateTrueCount(int runningCount, int cardsLeft){
        return (double)(runningCount)/((double)(cardsLeft)/52.0);
    }
    public static int returnCountForCard(int card){
        if(card == 10 || card == 1){
            return -1;
        }
        else if(card == 7 || card == 8 || card == 9){
            return 0;
        }
        else{
            return 1;
        }
    }
}
