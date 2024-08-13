import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import main.java.TableManager;
import main.java.utils.BetConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
public class TableManagerTest {
    Logger logger = Logger.getLogger(DecisionMakerTest.class.getName());
    static BetConfig betConfig = new BetConfig(1, 1);
    static Map<Integer, BetConfig> betSpread = new HashMap<Integer, BetConfig>();
    static TableManager tableManager;
    @BeforeAll
    public static void initTable(){
        betSpread.put(0, betConfig);
        betSpread.put(1, betConfig);
        betSpread.put(2, betConfig);
        betSpread.put(3, betConfig);
        betSpread.put(4, betConfig);
        tableManager = new TableManager(6, true, 0.75, 15, 1000, 100, 10, betSpread);
        tableManager.setShoe2s();
    }

    @Test
    public void testSplitBasic(){
        tableManager.playRound();
    }
}
