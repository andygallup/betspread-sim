package main.java;

import java.util.List;

import static main.java.utils.PlayerHandUtils.*;

public class DeviationManager {
    boolean stand17;
    public DeviationManager(boolean stand17){
        this.stand17 = stand17;
    }
    public PlayDecision getDeviationDecision(List<Integer> playerCardList, int dealerCard, double count){
        // check if we surrender
        if(shouldSurrender(playerCardList, dealerCard, count)){
            return PlayDecision.SURRENDER;
        }
        // 8 into 6 at +2
        int playerSum = sumPlayerHand(playerCardList);
        boolean soft = isSoft(playerCardList);
        if(soft){
            if(playerSum == 8 && canDouble(playerCardList) && dealerCard == 2 && !stand17){
                return PlayDecision.DOUBLE;
            }
            if(playerSum == 9 && canDouble(playerCardList) && dealerCard == 6 && !stand17){
                return PlayDecision.DOUBLE;
            }
        }
        else{ // hard totals
            if(playerSum == 8 && canDouble(playerCardList) && dealerCard == 6 && count >= 2){ return PlayDecision.DOUBLE; }
            if(playerSum == 9 && canDouble(playerCardList) && dealerCard == 2 && count >= 1){ return PlayDecision.DOUBLE; }
            if(playerSum == 9 && canDouble(playerCardList) && dealerCard == 7 && count >= 3){ return PlayDecision.DOUBLE; }
            if(playerSum == 10 && canDouble(playerCardList) && dealerCard == 1 && count >= 3 && !stand17){ return PlayDecision.DOUBLE; }
            if(playerSum == 10 && canDouble(playerCardList) && dealerCard == 10 && count >= 4){ return PlayDecision.DOUBLE; }
            if(playerSum == 10 && canDouble(playerCardList) && dealerCard == 1 && count >= 4){ return PlayDecision.DOUBLE; }
            if(playerSum == 11 && canDouble(playerCardList) && dealerCard == 1 && count >= 1 && stand17){ return PlayDecision.DOUBLE; }
            if(playerSum == 11 && canDouble(playerCardList) && dealerCard == 1 && count >= 0 && !stand17){ return PlayDecision.DOUBLE; }
            if(playerSum == 12 && dealerCard == 2 && count >= 3){ return PlayDecision.STAND; }
            if(playerSum == 12 && dealerCard == 3 && count >= 2){ return PlayDecision.STAND; }
            if(playerSum == 12 && dealerCard == 4 && count < 0){ return PlayDecision.HIT; }
            if(playerSum == 12 && dealerCard == 5 && count < -2){ return PlayDecision.HIT; }
            if(playerSum == 12 && dealerCard == 6 && count < -1 && stand17){ return PlayDecision.HIT; }
            if(playerSum == 12 && dealerCard == 6 && count < -3 && !stand17){ return PlayDecision.HIT; }
            if(playerSum == 13 && dealerCard == 2 && count < 0){ return PlayDecision.HIT; }
            if(playerSum == 13 && dealerCard == 3 && count < -2){ return PlayDecision.HIT; }
            if(playerSum == 15 && dealerCard == 10 && count >= 4){ return PlayDecision.STAND; }
            if(playerSum == 16 && dealerCard == 9 && count >= 5){ return PlayDecision.STAND; }
            if(playerSum == 16 && dealerCard == 10 && count > 0){ return PlayDecision.STAND; }
        }

        return PlayDecision.NO_DEVIATION;
    }

    public boolean shouldSurrender(List<Integer> playerCardList, int dealerCard, double count){
        //Surrender the following:
        //16 into 9 10 and 1
        //15 into 10, 1 at +1 or s17, 9 at +2
        //14 into 10 at +3
        //17 into 1 if hit 17
        if(playerCardList.size() == 2) { // if we have two cards
            if(sumPlayerHand(playerCardList) == 16 && !isSoft(playerCardList) && dealerCard == 10){
                return true;
            }
            if(sumPlayerHand(playerCardList) == 16 && !isSoft(playerCardList) && dealerCard == 9){
                return true;
            }
            if(sumPlayerHand(playerCardList) == 16 && !isSoft(playerCardList) && dealerCard == 1){
                return true;
            }
            if(sumPlayerHand(playerCardList) == 15 && !isSoft(playerCardList) && dealerCard == 10 && count >= 0){
                return true;
            }
            if(sumPlayerHand(playerCardList) == 15 && !isSoft(playerCardList) && dealerCard == 9 && count >= 2){
                return true;
            }
            if(sumPlayerHand(playerCardList) == 15 && !isSoft(playerCardList) && dealerCard == 1 && (count >= 1 || (!stand17 && count >=0))){
                return true;
            }
            if(sumPlayerHand(playerCardList) == 14 && !isSoft(playerCardList) && dealerCard == 10 && count >= 3){
                return true;
            }
            if(sumPlayerHand(playerCardList) == 17 && !isSoft(playerCardList) && dealerCard == 1 && !stand17){
                return true;
            }
            // check the split deviation for 8s into 1 in a hit 17 game
            if(handIsSplittable(playerCardList) && playerCardList.get(0) == 8 && dealerCard == 1 && !stand17){
                return true;
            }
        }
        return false;
    }
}
