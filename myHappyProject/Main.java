import com.google.common.collect.Table;
import src.DecisionMaker;
import src.TableManager;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import src.utils.BetConfig;

public class Main {
    public static void main(String[] args) {
        // read in config
        int shoeSize = Integer.parseInt(args[0]);
        boolean stand17 = Boolean.parseBoolean(args[1]);
        double deckPen = Double.parseDouble(args[2]);
        int minBet = Integer.parseInt(args[3]);
        double bankroll = Double.parseDouble(args[4]);
        int handsPerHour = Integer.parseInt(args[5]);
        int hoursPlayed = Integer.parseInt(args[6]);
        int simIterations = Integer.parseInt(args[7]);

        System.out.println("Running with args:");
        System.out.println("shoeSize:" + shoeSize);
        System.out.println("stand17:" + stand17);
        System.out.println("deckPen:" + deckPen);
        System.out.println("minBet:" + minBet);
        System.out.println("bankroll:" + bankroll);
        System.out.println("handsPerHour:" + handsPerHour);
        System.out.println("hoursPlayed:" + hoursPlayed);
        System.out.println("simIterations:" + simIterations);
        Object obj;

        try{
            obj = new JSONParser().parse(new FileReader("/Users/agallup/IdeaProjects/betspread-sim/myHappyProject/betspread.json"));
        }
        catch(Exception e){
            System.out.println("Caught exception when creating JSONParser:");
            throw new RuntimeException(e);
        }
        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;

        Map<Integer, BetConfig> betSpread = new HashMap<Integer, BetConfig>();
        for (int i = 0; i < 5; i++) {
            String key = "" + i;
            System.out.println("key: " + key);
            JSONObject countSpecificJSON = (JSONObject) jo.get(key);
            long seats = (Long) countSpecificJSON.get("seats");
            long bettingUnits = (Long) countSpecificJSON.get("bettingUnitsPerSeat");
            betSpread.put(i, new BetConfig((int)seats, (int) bettingUnits));
        }

        // run the game
        TableManager tableManager = new TableManager(shoeSize, stand17, deckPen, minBet, bankroll, handsPerHour, hoursPlayed, betSpread);
        double avgEndBankroll = 0;
        for (int i = 0; i < simIterations; i++){
            System.out.println(i);
            avgEndBankroll += tableManager.playGame();
        }
        avgEndBankroll = avgEndBankroll/simIterations;
        System.out.println("AVERAGE END BANKROLL: " + avgEndBankroll);
        System.out.println("AVG EV: " + (avgEndBankroll-bankroll)/hoursPlayed);
    }

}