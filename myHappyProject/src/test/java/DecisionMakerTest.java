import static org.junit.jupiter.api.Assertions.*;
import static app.utils.PlayerHandUtils.PlayDecision;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.logging.Logger;

import app.DecisionMaker;

class DecisionMakerTest {
    Logger logger = Logger.getLogger(DecisionMakerTest.class.getName());
    static DecisionMaker decisionMaker;
    @BeforeAll
    static void initStand17DecisionMaker(){
        decisionMaker = new DecisionMaker(true);
        decisionMaker.buildBasicStrategyTables();
    }

    @Test
    void checkHardNotNull() {
        for(int player = 0; player < 22; player++){
            for(int dealer = 0; dealer < 11; dealer++){
                assertNotNull(decisionMaker.getHardTableValue(player, dealer));
            }
        }
    }

    @Test
    void checkSoftNotNull() {
        for(int player = 0; player < 12; player++){
            for(int dealer = 0; dealer < 11; dealer++){
                assertNotNull(decisionMaker.getSoftTableValue(player, dealer));
            }
        }
    }

    @Test
    void checkSplitNotNull() {
        for(int player = 0; player < 11; player++){
            for(int dealer = 0; dealer < 11; dealer++){
                assertNotNull(decisionMaker.getSplitTableValue(player, dealer));
            }
        }
    }

    @Test
    void smokeCheckBasicStrategy(){
        ArrayList<Integer> playerHand = new ArrayList<>();
        playerHand.add(3);
        playerHand.add(4);
        assertEquals(decisionMaker.makeDecision(playerHand, 2, 0), PlayDecision.HIT);
        assertNotEquals(decisionMaker.makeDecision(playerHand, 2, 0), PlayDecision.SPLIT);
    }
}
