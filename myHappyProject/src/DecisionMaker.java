package src;

import java.util.List;

import static src.utils.PlayerHandUtils.*;

public class DecisionMaker {
    private DeviationManager deviationManager;
    public DecisionMaker(boolean stand17){
        this.deviationManager = new DeviationManager(stand17);
    }
    private PlayDecision[][] hardTable;
    private PlayDecision[][] softTable;
    private PlayDecision[][] splitTable;
    public void buildBasicStrategyTables(){
        // left is playerHand, right is dealerHand
        // Decisions assume S17 - H17 should be considered a deviation
        hardTable = new PlayDecision[22][11];
        softTable = new PlayDecision[12][11];
        splitTable = new PlayDecision[11][11];

        initOopsie();
        buildHardTable();
        buildSoftTable();
        buildSplitTable();
        // put AA and A10 into soft table and throw run time exception thats why its 10x10
    }
    public PlayDecision makeDecision(List<Integer> playerCardList, int dealerCard, float count){
        // Assumes stand 17 table - hit 17 is considered a deviation and handled in DeviationManager
        // Query DeviationManager to check if we do not use basicStrategy
        PlayDecision deviationDecision = deviationManager.getDeviationDecision(playerCardList, dealerCard, count);
        if(deviationDecision != PlayDecision.NO_DEVIATION){
            return deviationDecision;
        }

        // Check basic strategy tables if DeviationManager did not return a deviation
        // Check if we should split
        if(canSplit(playerCardList)){ // if we have two cards
            if(getSplitTableValue(playerCardList.get(0), dealerCard) == PlayDecision.SPLIT) {
                return PlayDecision.SPLIT;
            }
        }
        // Check if it's a soft value
        if(isSoft(playerCardList)){
            return getSoftTableValue(sumPlayerHand(playerCardList), dealerCard);
        }
        return getHardTableValue(sumPlayerHand(playerCardList), dealerCard);
    }
    public PlayDecision getHardTableValue(int playerHandTotal, int dealerHand){
        return hardTable[playerHandTotal][dealerHand];
    }
    public PlayDecision getSoftTableValue(int playerHandTotal, int dealerHand){
        return softTable[playerHandTotal][dealerHand];
    }
    public PlayDecision getSplitTableValue(int playerHandCard, int dealerHand){
        return splitTable[playerHandCard][dealerHand];
    }
    private void initOopsie(){
        // Dealer 0 == OOPSIE
        hardTable[0][0] = PlayDecision.OOPSIE;
        hardTable[1][0] = PlayDecision.OOPSIE;
        hardTable[2][0] = PlayDecision.OOPSIE;
        hardTable[3][0] = PlayDecision.OOPSIE;
        hardTable[4][0] = PlayDecision.OOPSIE;
        hardTable[5][0] = PlayDecision.OOPSIE;
        hardTable[6][0] = PlayDecision.OOPSIE;
        hardTable[7][0] = PlayDecision.OOPSIE;
        hardTable[8][0] = PlayDecision.OOPSIE;
        hardTable[9][0] = PlayDecision.OOPSIE;
        hardTable[10][0] = PlayDecision.OOPSIE;
        hardTable[11][0] = PlayDecision.OOPSIE;
        hardTable[12][0] = PlayDecision.OOPSIE;
        hardTable[13][0] = PlayDecision.OOPSIE;
        hardTable[14][0] = PlayDecision.OOPSIE;
        hardTable[15][0] = PlayDecision.OOPSIE;
        hardTable[16][0] = PlayDecision.OOPSIE;
        hardTable[17][0] = PlayDecision.OOPSIE;
        hardTable[18][0] = PlayDecision.OOPSIE;
        hardTable[19][0] = PlayDecision.OOPSIE;
        hardTable[20][0] = PlayDecision.OOPSIE;
        hardTable[21][0] = PlayDecision.OOPSIE;
        // Player has 0
        hardTable[0][1] = PlayDecision.OOPSIE;
        hardTable[0][2] = PlayDecision.OOPSIE;
        hardTable[0][3] = PlayDecision.OOPSIE;
        hardTable[0][4] = PlayDecision.OOPSIE;
        hardTable[0][5] = PlayDecision.OOPSIE;
        hardTable[0][6] = PlayDecision.OOPSIE;
        hardTable[0][7] = PlayDecision.OOPSIE;
        hardTable[0][8] = PlayDecision.OOPSIE;
        hardTable[0][9] = PlayDecision.OOPSIE;
        hardTable[0][10] = PlayDecision.OOPSIE;
        // Player has 1
        hardTable[1][1] = PlayDecision.OOPSIE;
        hardTable[1][2] = PlayDecision.OOPSIE;
        hardTable[1][3] = PlayDecision.OOPSIE;
        hardTable[1][4] = PlayDecision.OOPSIE;
        hardTable[1][5] = PlayDecision.OOPSIE;
        hardTable[1][6] = PlayDecision.OOPSIE;
        hardTable[1][7] = PlayDecision.OOPSIE;
        hardTable[1][8] = PlayDecision.OOPSIE;
        hardTable[1][9] = PlayDecision.OOPSIE;
        hardTable[1][10] = PlayDecision.OOPSIE;
        //2
        hardTable[2][1] = PlayDecision.OOPSIE;
        hardTable[2][2] = PlayDecision.OOPSIE;
        hardTable[2][3] = PlayDecision.OOPSIE;
        hardTable[2][4] = PlayDecision.OOPSIE;
        hardTable[2][5] = PlayDecision.OOPSIE;
        hardTable[2][6] = PlayDecision.OOPSIE;
        hardTable[2][7] = PlayDecision.OOPSIE;
        hardTable[2][8] = PlayDecision.OOPSIE;
        hardTable[2][9] = PlayDecision.OOPSIE;
        hardTable[2][10] = PlayDecision.OOPSIE;
        //3
        hardTable[3][1] = PlayDecision.OOPSIE;
        hardTable[3][2] = PlayDecision.OOPSIE;
        hardTable[3][3] = PlayDecision.OOPSIE;
        hardTable[3][4] = PlayDecision.OOPSIE;
        hardTable[3][5] = PlayDecision.OOPSIE;
        hardTable[3][6] = PlayDecision.OOPSIE;
        hardTable[3][7] = PlayDecision.OOPSIE;
        hardTable[3][8] = PlayDecision.OOPSIE;
        hardTable[3][9] = PlayDecision.OOPSIE;
        hardTable[3][10] = PlayDecision.OOPSIE;
        // Dealer 0 == OOPSIE
        softTable[0][0] = PlayDecision.OOPSIE;
        softTable[1][0] = PlayDecision.OOPSIE;
        softTable[2][0] = PlayDecision.OOPSIE;
        softTable[3][0] = PlayDecision.OOPSIE;
        softTable[4][0] = PlayDecision.OOPSIE;
        softTable[5][0] = PlayDecision.OOPSIE;
        softTable[6][0] = PlayDecision.OOPSIE;
        softTable[7][0] = PlayDecision.OOPSIE;
        softTable[8][0] = PlayDecision.OOPSIE;
        softTable[9][0] = PlayDecision.OOPSIE;
        softTable[10][0] = PlayDecision.OOPSIE;
        softTable[11][0] = PlayDecision.OOPSIE;
        // Player has 0
        softTable[0][1] = PlayDecision.OOPSIE;
        softTable[0][2] = PlayDecision.OOPSIE;
        softTable[0][3] = PlayDecision.OOPSIE;
        softTable[0][4] = PlayDecision.OOPSIE;
        softTable[0][5] = PlayDecision.OOPSIE;
        softTable[0][6] = PlayDecision.OOPSIE;
        softTable[0][7] = PlayDecision.OOPSIE;
        softTable[0][8] = PlayDecision.OOPSIE;
        softTable[0][9] = PlayDecision.OOPSIE;
        softTable[0][10] = PlayDecision.OOPSIE;
        // Player has 1
        softTable[1][1] = PlayDecision.OOPSIE;
        softTable[1][2] = PlayDecision.OOPSIE;
        softTable[1][3] = PlayDecision.OOPSIE;
        softTable[1][4] = PlayDecision.OOPSIE;
        softTable[1][5] = PlayDecision.OOPSIE;
        softTable[1][6] = PlayDecision.OOPSIE;
        softTable[1][7] = PlayDecision.OOPSIE;
        softTable[1][8] = PlayDecision.OOPSIE;
        softTable[1][9] = PlayDecision.OOPSIE;
        softTable[1][10] = PlayDecision.OOPSIE;
        // Player hand Ace Ace softTables (should be handled by splitTable)
        softTable[2][1] = PlayDecision.OOPSIE;
        softTable[2][2] = PlayDecision.OOPSIE;
        softTable[2][3] = PlayDecision.OOPSIE;
        softTable[2][4] = PlayDecision.OOPSIE;
        softTable[2][5] = PlayDecision.OOPSIE;
        softTable[2][6] = PlayDecision.OOPSIE;
        softTable[2][7] = PlayDecision.OOPSIE;
        softTable[2][8] = PlayDecision.OOPSIE;
        softTable[2][9] = PlayDecision.OOPSIE;
        softTable[2][10] = PlayDecision.OOPSIE;
        // Dealer 0 == OOPSIE
        splitTable[0][0] = PlayDecision.OOPSIE;
        splitTable[1][0] = PlayDecision.OOPSIE;
        splitTable[2][0] = PlayDecision.OOPSIE;
        splitTable[3][0] = PlayDecision.OOPSIE;
        splitTable[4][0] = PlayDecision.OOPSIE;
        splitTable[5][0] = PlayDecision.OOPSIE;
        splitTable[6][0] = PlayDecision.OOPSIE;
        splitTable[7][0] = PlayDecision.OOPSIE;
        splitTable[8][0] = PlayDecision.OOPSIE;
        splitTable[9][0] = PlayDecision.OOPSIE;
        splitTable[10][0] = PlayDecision.OOPSIE;
        // Player has 0
        splitTable[0][1] = PlayDecision.OOPSIE;
        splitTable[0][2] = PlayDecision.OOPSIE;
        splitTable[0][3] = PlayDecision.OOPSIE;
        splitTable[0][4] = PlayDecision.OOPSIE;
        splitTable[0][5] = PlayDecision.OOPSIE;
        splitTable[0][6] = PlayDecision.OOPSIE;
        splitTable[0][7] = PlayDecision.OOPSIE;
        splitTable[0][8] = PlayDecision.OOPSIE;
        splitTable[0][9] = PlayDecision.OOPSIE;
        splitTable[0][10] = PlayDecision.OOPSIE;
    }
    private void buildHardTable(){
        // Player hand 4
        hardTable[4][1] = PlayDecision.HIT;
        hardTable[4][2] = PlayDecision.OOPSIE;
        hardTable[4][3] = PlayDecision.OOPSIE;
        hardTable[4][4] = PlayDecision.OOPSIE;
        hardTable[4][5] = PlayDecision.OOPSIE;
        hardTable[4][6] = PlayDecision.OOPSIE;
        hardTable[4][7] = PlayDecision.OOPSIE;
        hardTable[4][8] = PlayDecision.HIT;
        hardTable[4][9] = PlayDecision.HIT;
        hardTable[4][10] = PlayDecision.HIT;
        // Player hand 5
        hardTable[5][1] = PlayDecision.HIT;
        hardTable[5][2] = PlayDecision.HIT;
        hardTable[5][3] = PlayDecision.HIT;
        hardTable[5][4] = PlayDecision.HIT;
        hardTable[5][5] = PlayDecision.HIT;
        hardTable[5][6] = PlayDecision.HIT;
        hardTable[5][7] = PlayDecision.HIT;
        hardTable[5][8] = PlayDecision.HIT;
        hardTable[5][9] = PlayDecision.HIT;
        hardTable[5][10] = PlayDecision.HIT;
        // Player hand 6
        hardTable[6][1] = PlayDecision.HIT;
        hardTable[6][2] = PlayDecision.HIT;
        hardTable[6][3] = PlayDecision.HIT;
        hardTable[6][4] = PlayDecision.HIT;
        hardTable[6][5] = PlayDecision.HIT;
        hardTable[6][6] = PlayDecision.HIT;
        hardTable[6][7] = PlayDecision.HIT;
        hardTable[6][8] = PlayDecision.HIT;
        hardTable[6][9] = PlayDecision.HIT;
        hardTable[6][10] = PlayDecision.HIT;
        // Player hand 7
        hardTable[7][1] = PlayDecision.HIT;
        hardTable[7][2] = PlayDecision.HIT;
        hardTable[7][3] = PlayDecision.HIT;
        hardTable[7][4] = PlayDecision.HIT;
        hardTable[7][5] = PlayDecision.HIT;
        hardTable[7][6] = PlayDecision.HIT;
        hardTable[7][7] = PlayDecision.HIT;
        hardTable[7][8] = PlayDecision.HIT;
        hardTable[7][9] = PlayDecision.HIT;
        hardTable[7][10] = PlayDecision.HIT;
        // Player hand 8
        hardTable[8][1] = PlayDecision.HIT;
        hardTable[8][2] = PlayDecision.HIT;
        hardTable[8][3] = PlayDecision.HIT;
        hardTable[8][4] = PlayDecision.HIT;
        hardTable[8][5] = PlayDecision.HIT;
        hardTable[8][6] = PlayDecision.HIT; // Deviation at +2
        hardTable[8][7] = PlayDecision.HIT;
        hardTable[8][8] = PlayDecision.HIT;
        hardTable[8][9] = PlayDecision.HIT;
        hardTable[8][10] = PlayDecision.HIT;
        // Player hand 9
        hardTable[9][1] = PlayDecision.HIT;
        hardTable[9][2] = PlayDecision.HIT; // Deviation at +1
        hardTable[9][3] = PlayDecision.DOUBLE;
        hardTable[9][4] = PlayDecision.DOUBLE;
        hardTable[9][5] = PlayDecision.DOUBLE;
        hardTable[9][6] = PlayDecision.DOUBLE;
        hardTable[9][7] = PlayDecision.HIT; // Deviation at +3
        hardTable[9][8] = PlayDecision.HIT;
        hardTable[9][9] = PlayDecision.HIT;
        hardTable[9][10] = PlayDecision.HIT;
        // Player hand 10
        hardTable[10][1] = PlayDecision.HIT; // Deviation at +3/+4
        hardTable[10][2] = PlayDecision.DOUBLE;
        hardTable[10][3] = PlayDecision.DOUBLE;
        hardTable[10][4] = PlayDecision.DOUBLE;
        hardTable[10][5] = PlayDecision.DOUBLE;
        hardTable[10][6] = PlayDecision.DOUBLE;
        hardTable[10][7] = PlayDecision.DOUBLE;
        hardTable[10][8] = PlayDecision.DOUBLE;
        hardTable[10][9] = PlayDecision.DOUBLE;
        hardTable[10][10] = PlayDecision.HIT; // Deviation at +3/+4
        // Player hand 11
        hardTable[11][1] = PlayDecision.HIT; // Deviation at +1
        hardTable[11][2] = PlayDecision.DOUBLE;
        hardTable[11][3] = PlayDecision.DOUBLE;
        hardTable[11][4] = PlayDecision.DOUBLE;
        hardTable[11][5] = PlayDecision.DOUBLE;
        hardTable[11][6] = PlayDecision.DOUBLE;
        hardTable[11][7] = PlayDecision.DOUBLE;
        hardTable[11][8] = PlayDecision.DOUBLE;
        hardTable[11][9] = PlayDecision.DOUBLE;
        hardTable[11][10] = PlayDecision.DOUBLE;
        // Player hand 12
        hardTable[12][1] = PlayDecision.HIT;
        hardTable[12][2] = PlayDecision.HIT; // Deviation at +3
        hardTable[12][3] = PlayDecision.HIT; // Deviation at +2
        hardTable[12][4] = PlayDecision.STAND; // Deviation at -0
        hardTable[12][5] = PlayDecision.STAND; // Deviation at -2
        hardTable[12][6] = PlayDecision.STAND; // Deviation at -1/-3
        hardTable[12][7] = PlayDecision.HIT;
        hardTable[12][8] = PlayDecision.HIT;
        hardTable[12][9] = PlayDecision.HIT;
        hardTable[12][10] = PlayDecision.HIT;
        // Player hand 13
        hardTable[13][1] = PlayDecision.HIT;
        hardTable[13][2] = PlayDecision.STAND; // Deviation at -0
        hardTable[13][3] = PlayDecision.STAND; // Deviation at -2
        hardTable[13][4] = PlayDecision.STAND;
        hardTable[13][5] = PlayDecision.STAND;
        hardTable[13][6] = PlayDecision.STAND;
        hardTable[13][7] = PlayDecision.HIT;
        hardTable[13][8] = PlayDecision.HIT;
        hardTable[13][9] = PlayDecision.HIT;
        hardTable[13][10] = PlayDecision.HIT;
        // Player hand 14
        hardTable[14][1] = PlayDecision.HIT;
        hardTable[14][2] = PlayDecision.STAND;
        hardTable[14][3] = PlayDecision.STAND;
        hardTable[14][4] = PlayDecision.STAND;
        hardTable[14][5] = PlayDecision.STAND;
        hardTable[14][6] = PlayDecision.STAND;
        hardTable[14][7] = PlayDecision.HIT;
        hardTable[14][8] = PlayDecision.HIT;
        hardTable[14][9] = PlayDecision.HIT;
        hardTable[14][10] = PlayDecision.HIT; // Deviation at +3
        // Player hand 15
        hardTable[15][1] = PlayDecision.HIT; // Deviation at -1
        hardTable[15][2] = PlayDecision.STAND;
        hardTable[15][3] = PlayDecision.STAND;
        hardTable[15][4] = PlayDecision.STAND;
        hardTable[15][5] = PlayDecision.STAND;
        hardTable[15][6] = PlayDecision.STAND;
        hardTable[15][7] = PlayDecision.HIT;
        hardTable[15][8] = PlayDecision.HIT;
        hardTable[15][9] = PlayDecision.HIT;  // Deviation at -2
        hardTable[15][10] = PlayDecision.HIT; // Deviation at +4
        // Player hand 16
        hardTable[16][1] = PlayDecision.HIT;
        hardTable[16][2] = PlayDecision.STAND;
        hardTable[16][3] = PlayDecision.STAND;
        hardTable[16][4] = PlayDecision.STAND;
        hardTable[16][5] = PlayDecision.STAND;
        hardTable[16][6] = PlayDecision.STAND;
        hardTable[16][7] = PlayDecision.HIT;
        hardTable[16][8] = PlayDecision.HIT;
        hardTable[16][9] = PlayDecision.HIT;  // Deviation at +5
        hardTable[16][10] = PlayDecision.HIT; // Deviation at +0
        // Player hand 17
        hardTable[17][1] = PlayDecision.STAND;
        hardTable[17][2] = PlayDecision.STAND;
        hardTable[17][3] = PlayDecision.STAND;
        hardTable[17][4] = PlayDecision.STAND;
        hardTable[17][5] = PlayDecision.STAND;
        hardTable[17][6] = PlayDecision.STAND;
        hardTable[17][7] = PlayDecision.STAND;
        hardTable[17][8] = PlayDecision.STAND;
        hardTable[17][9] = PlayDecision.STAND;
        hardTable[17][10] = PlayDecision.STAND;
        // Player hand 18
        hardTable[18][1] = PlayDecision.STAND;
        hardTable[18][2] = PlayDecision.STAND;
        hardTable[18][3] = PlayDecision.STAND;
        hardTable[18][4] = PlayDecision.STAND;
        hardTable[18][5] = PlayDecision.STAND;
        hardTable[18][6] = PlayDecision.STAND;
        hardTable[18][7] = PlayDecision.STAND;
        hardTable[18][8] = PlayDecision.STAND;
        hardTable[18][9] = PlayDecision.STAND;
        hardTable[18][10] = PlayDecision.STAND;
        // Player hand 19
        hardTable[19][1] = PlayDecision.STAND;
        hardTable[19][2] = PlayDecision.STAND;
        hardTable[19][3] = PlayDecision.STAND;
        hardTable[19][4] = PlayDecision.STAND;
        hardTable[19][5] = PlayDecision.STAND;
        hardTable[19][6] = PlayDecision.STAND;
        hardTable[19][7] = PlayDecision.STAND;
        hardTable[19][8] = PlayDecision.STAND;
        hardTable[19][9] = PlayDecision.STAND;
        hardTable[19][10] = PlayDecision.STAND;
        // Player hand 20
        hardTable[20][1] = PlayDecision.STAND;
        hardTable[20][2] = PlayDecision.STAND;
        hardTable[20][3] = PlayDecision.STAND;
        hardTable[20][4] = PlayDecision.STAND;
        hardTable[20][5] = PlayDecision.STAND;
        hardTable[20][6] = PlayDecision.STAND;
        hardTable[20][7] = PlayDecision.STAND;
        hardTable[20][8] = PlayDecision.STAND;
        hardTable[20][9] = PlayDecision.STAND;
        hardTable[20][10] = PlayDecision.STAND;
        // Player hand 21
        hardTable[21][1] = PlayDecision.STAND;
        hardTable[21][2] = PlayDecision.STAND;
        hardTable[21][3] = PlayDecision.STAND;
        hardTable[21][4] = PlayDecision.STAND;
        hardTable[21][5] = PlayDecision.STAND;
        hardTable[21][6] = PlayDecision.STAND;
        hardTable[21][7] = PlayDecision.STAND;
        hardTable[21][8] = PlayDecision.STAND;
        hardTable[21][9] = PlayDecision.STAND;
        hardTable[21][10] = PlayDecision.STAND;
    }
    private void buildSoftTable(){
        // Player hand Ace 2
        softTable[3][1] = PlayDecision.HIT;
        softTable[3][2] = PlayDecision.HIT;
        softTable[3][3] = PlayDecision.HIT;
        softTable[3][4] = PlayDecision.HIT;
        softTable[3][5] = PlayDecision.DOUBLE;
        softTable[3][6] = PlayDecision.DOUBLE;
        softTable[3][7] = PlayDecision.HIT;
        softTable[3][8] = PlayDecision.HIT;
        softTable[3][9] = PlayDecision.HIT;
        softTable[3][10] = PlayDecision.HIT;
        // Player hand Ace 3
        softTable[4][1] = PlayDecision.HIT;
        softTable[4][2] = PlayDecision.HIT;
        softTable[4][3] = PlayDecision.HIT;
        softTable[4][4] = PlayDecision.HIT;
        softTable[4][5] = PlayDecision.DOUBLE;
        softTable[4][6] = PlayDecision.DOUBLE;
        softTable[4][7] = PlayDecision.HIT;
        softTable[4][8] = PlayDecision.HIT;
        softTable[4][9] = PlayDecision.HIT;
        softTable[4][10] = PlayDecision.HIT;
        // Player hand Ace 4
        softTable[5][1] = PlayDecision.HIT;
        softTable[5][2] = PlayDecision.HIT;
        softTable[5][3] = PlayDecision.HIT;
        softTable[5][4] = PlayDecision.DOUBLE;
        softTable[5][5] = PlayDecision.DOUBLE;
        softTable[5][6] = PlayDecision.DOUBLE;
        softTable[5][7] = PlayDecision.HIT;
        softTable[5][8] = PlayDecision.HIT;
        softTable[5][9] = PlayDecision.HIT;
        softTable[5][10] = PlayDecision.HIT;
        // Player hand Ace 5
        softTable[6][1] = PlayDecision.HIT;
        softTable[6][2] = PlayDecision.HIT;
        softTable[6][3] = PlayDecision.HIT;
        softTable[6][4] = PlayDecision.DOUBLE;
        softTable[6][5] = PlayDecision.DOUBLE;
        softTable[6][6] = PlayDecision.DOUBLE;
        softTable[6][7] = PlayDecision.HIT;
        softTable[6][8] = PlayDecision.HIT;
        softTable[6][9] = PlayDecision.HIT;
        softTable[6][10] = PlayDecision.HIT;
        // Player hand Ace 6
        softTable[7][1] = PlayDecision.HIT;
        softTable[7][2] = PlayDecision.HIT;
        softTable[7][3] = PlayDecision.DOUBLE;
        softTable[7][4] = PlayDecision.DOUBLE;
        softTable[7][5] = PlayDecision.DOUBLE;
        softTable[7][6] = PlayDecision.DOUBLE;
        softTable[7][7] = PlayDecision.HIT;
        softTable[7][8] = PlayDecision.HIT;
        softTable[7][9] = PlayDecision.HIT;
        softTable[7][10] = PlayDecision.HIT;
        // Player hand Ace 7
        softTable[8][1] = PlayDecision.HIT;    // Deviation for H17
        softTable[8][2] = PlayDecision.STAND;
        softTable[8][3] = PlayDecision.DOUBLE;
        softTable[8][4] = PlayDecision.DOUBLE;
        softTable[8][5] = PlayDecision.DOUBLE;
        softTable[8][6] = PlayDecision.DOUBLE;
        softTable[8][7] = PlayDecision.STAND;
        softTable[8][8] = PlayDecision.STAND;
        softTable[8][9] = PlayDecision.HIT;
        softTable[8][10] = PlayDecision.HIT;
        // Player hand Ace 8
        softTable[9][1] = PlayDecision.STAND;
        softTable[9][2] = PlayDecision.STAND;
        softTable[9][3] = PlayDecision.STAND;
        softTable[9][4] = PlayDecision.STAND;
        softTable[9][5] = PlayDecision.STAND;
        softTable[9][6] = PlayDecision.STAND; // Deviation at H17
        softTable[9][7] = PlayDecision.STAND;
        softTable[9][8] = PlayDecision.STAND;
        softTable[9][9] = PlayDecision.STAND;
        softTable[9][10] = PlayDecision.STAND;
        // Player hand Ace 9
        softTable[10][1] = PlayDecision.STAND;
        softTable[10][2] = PlayDecision.STAND;
        softTable[10][3] = PlayDecision.STAND;
        softTable[10][4] = PlayDecision.STAND;
        softTable[10][5] = PlayDecision.STAND;
        softTable[10][6] = PlayDecision.STAND;
        softTable[10][7] = PlayDecision.STAND;
        softTable[10][8] = PlayDecision.STAND;
        softTable[10][9] = PlayDecision.STAND;
        softTable[10][10] = PlayDecision.STAND;
        // Player hand Ace 10
        softTable[11][1] = PlayDecision.STAND;
        softTable[11][2] = PlayDecision.STAND;
        softTable[11][3] = PlayDecision.STAND;
        softTable[11][4] = PlayDecision.STAND;
        softTable[11][5] = PlayDecision.STAND;
        softTable[11][6] = PlayDecision.STAND;
        softTable[11][7] = PlayDecision.STAND;
        softTable[11][8] = PlayDecision.STAND;
        softTable[11][9] = PlayDecision.STAND;
        softTable[11][10] = PlayDecision.STAND;
    }
    private void buildSplitTable(){
        // Player hand Ace Ace
        splitTable[1][1] = PlayDecision.SPLIT;
        splitTable[1][2] = PlayDecision.SPLIT;
        splitTable[1][3] = PlayDecision.SPLIT;
        splitTable[1][4] = PlayDecision.SPLIT;
        splitTable[1][5] = PlayDecision.SPLIT;
        splitTable[1][6] = PlayDecision.SPLIT;
        splitTable[1][7] = PlayDecision.SPLIT;
        splitTable[1][8] = PlayDecision.SPLIT;
        splitTable[1][9] = PlayDecision.SPLIT;
        splitTable[1][10] = PlayDecision.SPLIT;
        // Player hand 2 2
        splitTable[2][1] = PlayDecision.DO_NOT_SPLIT;
        splitTable[2][2] = PlayDecision.SPLIT;
        splitTable[2][3] = PlayDecision.SPLIT;
        splitTable[2][4] = PlayDecision.SPLIT;
        splitTable[2][5] = PlayDecision.SPLIT;
        splitTable[2][6] = PlayDecision.SPLIT;
        splitTable[2][7] = PlayDecision.SPLIT;
        splitTable[2][8] = PlayDecision.DO_NOT_SPLIT;
        splitTable[2][9] = PlayDecision.DO_NOT_SPLIT;
        splitTable[2][10] = PlayDecision.DO_NOT_SPLIT;
        // Player hand 3 3
        splitTable[3][1] = PlayDecision.DO_NOT_SPLIT;
        splitTable[3][2] = PlayDecision.SPLIT;
        splitTable[3][3] = PlayDecision.SPLIT;
        splitTable[3][4] = PlayDecision.SPLIT;
        splitTable[3][5] = PlayDecision.SPLIT;
        splitTable[3][6] = PlayDecision.SPLIT;
        splitTable[3][7] = PlayDecision.SPLIT;
        splitTable[3][8] = PlayDecision.DO_NOT_SPLIT;
        splitTable[3][9] = PlayDecision.DO_NOT_SPLIT;
        splitTable[3][10] = PlayDecision.DO_NOT_SPLIT;
        // Player hand 4 4
        splitTable[4][1] = PlayDecision.DO_NOT_SPLIT;
        splitTable[4][2] = PlayDecision.DO_NOT_SPLIT;
        splitTable[4][3] = PlayDecision.DO_NOT_SPLIT;
        splitTable[4][4] = PlayDecision.DO_NOT_SPLIT;
        splitTable[4][5] = PlayDecision.SPLIT;
        splitTable[4][6] = PlayDecision.SPLIT;
        splitTable[4][7] = PlayDecision.DO_NOT_SPLIT;
        splitTable[4][8] = PlayDecision.DO_NOT_SPLIT;
        splitTable[4][9] = PlayDecision.DO_NOT_SPLIT;
        splitTable[4][10] = PlayDecision.DO_NOT_SPLIT;
        // Player hand 5 5
        splitTable[5][1] = PlayDecision.DO_NOT_SPLIT;
        splitTable[5][2] = PlayDecision.DO_NOT_SPLIT;
        splitTable[5][3] = PlayDecision.DO_NOT_SPLIT;
        splitTable[5][4] = PlayDecision.DO_NOT_SPLIT;
        splitTable[5][5] = PlayDecision.DO_NOT_SPLIT;
        splitTable[5][6] = PlayDecision.DO_NOT_SPLIT;
        splitTable[5][7] = PlayDecision.DO_NOT_SPLIT;
        splitTable[5][8] = PlayDecision.DO_NOT_SPLIT;
        splitTable[5][9] = PlayDecision.DO_NOT_SPLIT;
        splitTable[5][10] = PlayDecision.DO_NOT_SPLIT;
        // Player hand 6 6
        splitTable[6][1] = PlayDecision.DO_NOT_SPLIT;
        splitTable[6][2] = PlayDecision.SPLIT;
        splitTable[6][3] = PlayDecision.SPLIT;
        splitTable[6][4] = PlayDecision.SPLIT;
        splitTable[6][5] = PlayDecision.SPLIT;
        splitTable[6][6] = PlayDecision.SPLIT;
        splitTable[6][7] = PlayDecision.DO_NOT_SPLIT;
        splitTable[6][8] = PlayDecision.DO_NOT_SPLIT;
        splitTable[6][9] = PlayDecision.DO_NOT_SPLIT;
        splitTable[6][10] = PlayDecision.DO_NOT_SPLIT;
        // Player hand 7 7
        splitTable[7][1] = PlayDecision.DO_NOT_SPLIT;
        splitTable[7][2] = PlayDecision.SPLIT;
        splitTable[7][3] = PlayDecision.SPLIT;
        splitTable[7][4] = PlayDecision.SPLIT;
        splitTable[7][5] = PlayDecision.SPLIT;
        splitTable[7][6] = PlayDecision.SPLIT;
        splitTable[7][7] = PlayDecision.SPLIT;
        splitTable[7][8] = PlayDecision.DO_NOT_SPLIT;
        splitTable[7][9] = PlayDecision.DO_NOT_SPLIT;
        splitTable[7][10] = PlayDecision.DO_NOT_SPLIT;
        // Player hand 8 8
        splitTable[8][1] = PlayDecision.SPLIT; // Deviation for H17
        splitTable[8][2] = PlayDecision.SPLIT;
        splitTable[8][3] = PlayDecision.SPLIT;
        splitTable[8][4] = PlayDecision.SPLIT;
        splitTable[8][5] = PlayDecision.SPLIT;
        splitTable[8][6] = PlayDecision.SPLIT;
        splitTable[8][7] = PlayDecision.SPLIT;
        splitTable[8][8] = PlayDecision.SPLIT;
        splitTable[8][9] = PlayDecision.SPLIT;
        splitTable[8][10] = PlayDecision.SPLIT;
        // Player hand 9 9
        splitTable[9][1] = PlayDecision.DO_NOT_SPLIT;
        splitTable[9][2] = PlayDecision.SPLIT;
        splitTable[9][3] = PlayDecision.SPLIT;
        splitTable[9][4] = PlayDecision.SPLIT;
        splitTable[9][5] = PlayDecision.SPLIT;
        splitTable[9][6] = PlayDecision.SPLIT;
        splitTable[9][7] = PlayDecision.DO_NOT_SPLIT;
        splitTable[9][8] = PlayDecision.SPLIT;
        splitTable[9][9] = PlayDecision.SPLIT;
        splitTable[9][10] = PlayDecision.DO_NOT_SPLIT;
        // Player hand 10
        splitTable[10][1] = PlayDecision.DO_NOT_SPLIT;
        splitTable[10][2] = PlayDecision.DO_NOT_SPLIT;
        splitTable[10][3] = PlayDecision.DO_NOT_SPLIT;
        splitTable[10][4] = PlayDecision.DO_NOT_SPLIT; // Deviation at +6
        splitTable[10][5] = PlayDecision.DO_NOT_SPLIT; // Deviation at +5
        splitTable[10][6] = PlayDecision.DO_NOT_SPLIT; // Deviation at +4
        splitTable[10][7] = PlayDecision.DO_NOT_SPLIT;
        splitTable[10][8] = PlayDecision.DO_NOT_SPLIT;
        splitTable[10][9] = PlayDecision.DO_NOT_SPLIT;
        splitTable[10][10] = PlayDecision.DO_NOT_SPLIT;
    }
}
