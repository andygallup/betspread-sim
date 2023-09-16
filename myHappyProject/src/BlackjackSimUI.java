package src;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import src.utils.BetConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlackjackSimUI {
    private JFrame frame;
    private JTextField simIterationsField;
    private JTextField shoeSizeField;
    private JCheckBox stand17Checkbox;
    private JTextField deckPenetrationField;
    private JTextField minBetField;
    private JTextField bankrollField;
    private JTextField handsPerHourField;
    private JTextField hoursPlayedField;
    private JTextArea outputTextArea;

    public BlackjackSimUI() {
        frame = new JFrame("Blackjack Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout(10, 10));

        // Create the main panel for outer padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add outer padding of 10

        JPanel inputPanel = new JPanel(new GridLayout(9, 2, 5, 5));

        inputPanel.add(new JLabel("Sim Iterations:"));
        simIterationsField = new JTextField();
        inputPanel.add(simIterationsField);

        inputPanel.add(new JLabel("Shoe Size:"));
        shoeSizeField = new JTextField();
        inputPanel.add(shoeSizeField);

        inputPanel.add(new JLabel("Stand on 17:"));
        stand17Checkbox = new JCheckBox();
        inputPanel.add(stand17Checkbox);

        inputPanel.add(new JLabel("Deck Penetration (0-1):"));
        deckPenetrationField = new JTextField();
        inputPanel.add(deckPenetrationField);

        inputPanel.add(new JLabel("Minimum Bet:"));
        minBetField = new JTextField();
        inputPanel.add(minBetField);

        inputPanel.add(new JLabel("Bankroll:"));
        bankrollField = new JTextField();
        inputPanel.add(bankrollField);

        inputPanel.add(new JLabel("Hands per Hour:"));
        handsPerHourField = new JTextField();
        inputPanel.add(handsPerHourField);

        inputPanel.add(new JLabel("Hours Played:"));
        hoursPlayedField = new JTextField();
        inputPanel.add(hoursPlayedField);

        JButton simulateButton = new JButton("Simulate");
        simulateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulate();
            }
        });

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(simulateButton, BorderLayout.CENTER);

        outputTextArea = new JTextArea();
        outputTextArea.setText("Waiting for input...");
        outputTextArea.setEditable(false);
        mainPanel.add(new JScrollPane(outputTextArea), BorderLayout.SOUTH);
        frame.add(mainPanel);

        frame.setVisible(true);
    }

    private ArrayList<Double> runSim(int simIterations, int shoeSize, boolean stand17, double deckPen, int minBet, double bankroll, int handsPerHour, int hoursPlayed) {
        Object obj;
        // Pull in betspread config from betspread.json
        try{
            obj = new JSONParser().parse(new FileReader("/Users/agallup/IdeaProjects/betspread-sim/myHappyProject/betspread.json"));
        }
        catch(Exception e){
            throw new RuntimeException("Caught exception when creating JSONParser:", e);
        }
        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;

        Map<Integer, BetConfig> betSpread = new HashMap<Integer, BetConfig>();
        for (int i = 0; i < 5; i++) {
            String key = "" + i;
            JSONObject countSpecificJSON = (JSONObject) jo.get(key);
            long seats = (Long) countSpecificJSON.get(  "seats");
            long bettingUnits = (Long) countSpecificJSON.get("bettingUnitsPerSeat");
            betSpread.put(i, new BetConfig((int)seats, (int) bettingUnits));
        }

        // run the game
        TableManager tableManager;
        double avgEndBankroll = 0;
        double avgEv = 0;
        double ror = 0;
        double endBankroll = 0;
        double numberOfTimesBroke = 0;
        for (int i = 0; i < simIterations; i++){
            tableManager = new TableManager(shoeSize, stand17, deckPen, minBet, bankroll, handsPerHour, hoursPlayed, betSpread);
            endBankroll = tableManager.playGame();
            if (endBankroll <= 3*minBet){
                numberOfTimesBroke += 1;
            }
            avgEndBankroll += endBankroll;
        }
        avgEndBankroll = avgEndBankroll/simIterations;
        avgEv = (avgEndBankroll-bankroll)/hoursPlayed;
        ror = (numberOfTimesBroke/(double)simIterations);
        ArrayList<Double> outputArr = new ArrayList<Double>();
        outputArr.add(avgEndBankroll);
        outputArr.add(avgEv);
        outputArr.add(ror);

        return outputArr;
    }

    private void simulate() {
        outputTextArea.setText("");
        // Get input values from the text fields and checkboxes
        int simIterations = Integer.parseInt(simIterationsField.getText());
        int shoeSize = Integer.parseInt(shoeSizeField.getText());
        boolean stand17 = stand17Checkbox.isSelected();
        double deckPen = Double.parseDouble(deckPenetrationField.getText());
        int minBet = Integer.parseInt(minBetField.getText());
        double bankroll = Double.parseDouble(bankrollField.getText());
        int handsPerHour = Integer.parseInt(handsPerHourField.getText());
        int hoursPlayed = Integer.parseInt(hoursPlayedField.getText());

        // Perform blackjack simulation with the provided input
        outputTextArea.setText("Simulating...\n");
        outputTextArea.append("Shoe Size: " + shoeSize + "\n");
        outputTextArea.append("Stand on 17: " + stand17 + "\n");
        outputTextArea.append("Deck Penetration: " + deckPen + "\n");
        outputTextArea.append("Minimum Bet: " + minBet + "\n");
        outputTextArea.append("Bankroll: " + bankroll + "\n");
        outputTextArea.append("Hands per Hour: " + handsPerHour + "\n");
        outputTextArea.append("Hours Played: " + hoursPlayed + "\n");
        outputTextArea.append("Simulation Iterations: " + simIterations + "\n");


        // Run your simulations and update the outputTextArea with the results
        ArrayList<Double> result;
        result = runSim(simIterations, shoeSize, stand17, deckPen, minBet, bankroll, handsPerHour, hoursPlayed);
        outputTextArea.append("\nSimulation results:");
        outputTextArea.append("\nAverage End Bankroll: " + result.get(0));
        outputTextArea.append("\nAverage EV/hour: " + result.get(1));
        outputTextArea.append("\nRisk of Ruin: " + result.get(2));

        //resize to show all content
        frame.pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BlackjackSimUI();
            }
        });
    }
}
