package src;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import src.utils.BetConfig;
import src.utils.TooltipLabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.round;

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
        Object obj;
        // Pull in betspread config from betspread.json
        try{
            obj = new JSONParser().parse(new InputStreamReader(this.getClass().getResource("/betspread.json").openStream()));
        }
        catch(Exception e){
            throw new RuntimeException("Caught exception when creating JSONParser:", e);
        }
        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;
        String betspreadString = "Betspread: \n";
        for (int i = 0; i < 5; i++) {
            String key = "" + i;
            JSONObject countSpecificJSON = (JSONObject) jo.get(key);
            long seats = (Long) countSpecificJSON.get("seats");
            long bettingUnits = (Long) countSpecificJSON.get("bettingUnitsPerSeat");
            betspreadString = betspreadString + "TC " + i + ": " + seats + " spot(s) betting " + bettingUnits + " units." + System.lineSeparator();
        }

        initUI(betspreadString);
    }

    private void initUI(String betspreadString) {
        frame = new JFrame("Blackjack Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 400);

        // Create the mainPanel
        JPanel mainPanel = new JPanel(new GridBagLayout());

        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add outer padding of 10
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        mainPanel.add(new TooltipLabel(
                        "Sim Iterations:",
                        "Number of times to run the simulation. " +
                                "Recommended minimum of 100 for reasonably accurate answers. " +
                                "More simulations will result in more accuracy, but longer run times")
                        .getPanel(),
                gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        simIterationsField = new JTextField(5);
        mainPanel.add(simIterationsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.6;
        mainPanel.add(new TooltipLabel(
                        "Shoe Size:",
                        "Enter the number of decks in the shoe.")
                        .getPanel(),
                gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        shoeSizeField = new JTextField(5);
        mainPanel.add(shoeSizeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.6;
        mainPanel.add(new TooltipLabel(
                        "Stand on 17:",
                        "Whether the dealer will stand on soft 17, or hit on soft 17. " +
                                "Checked field indicates they will stand")
                        .getPanel(),
                gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        stand17Checkbox = new JCheckBox();
        mainPanel.add(stand17Checkbox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.6;
        mainPanel.add(new TooltipLabel(
                        "Deck Penetration (0-1):",
                        "Average percentage of the shoe that is dealt before the shoe is shuffled " +
                                "(0.75 = 75% of the shoe is dealt before shuffle)")
                        .getPanel(),
                gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        deckPenetrationField = new JTextField(5);
        mainPanel.add(deckPenetrationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.6;
        mainPanel.add(new TooltipLabel(
                        "Minimum Bet:",
                        "Minimum table bet. " +
                                "For the purposes of this sim, this is also considered the betting unit for any configured bet spread")
                        .getPanel(),
                gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        minBetField = new JTextField(5);
        mainPanel.add(minBetField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.6;
        mainPanel.add(new TooltipLabel(
                "Bankroll:",
                "Starting bankroll")
                .getPanel(),
                gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        bankrollField = new JTextField(5);
        mainPanel.add(bankrollField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.6;
        mainPanel.add(new TooltipLabel(
                "Hands per Hour:",
                "Average number of hands expected to be played per hour")
                .getPanel(),
                gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        handsPerHourField = new JTextField(5);
        mainPanel.add(handsPerHourField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.6;
        mainPanel.add(new TooltipLabel(
                "Hours Played:",
                "Number of hours to be played per sim iteration")
                .getPanel(),
                gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        hoursPlayedField = new JTextField(5);
        mainPanel.add(hoursPlayedField, gbc);

        gbc.gridx = 0;
        gbc.gridy += 1;
        gbc.gridwidth = 2;
        JTextArea betspreadTextArea = new JTextArea();
        betspreadTextArea.setText(betspreadString);
        betspreadTextArea.setEditable(false);
        mainPanel.add(betspreadTextArea, gbc);


        gbc.gridy += gbc.gridheight;
        gbc.gridheight = 1;
        JButton simulateButton = new JButton("Simulate");
        simulateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulate();
            }
        });

        mainPanel.add(simulateButton, gbc);

        gbc.gridy += gbc.gridheight;
        gbc.gridwidth = 2;
        outputTextArea = new JTextArea();

        outputTextArea.setEditable(false);
        mainPanel.add(new JScrollPane(outputTextArea), gbc);

        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private ArrayList<Double> runSim(int simIterations, int shoeSize, boolean stand17, double deckPen, int minBet, double bankroll, int handsPerHour, int hoursPlayed) {
        Object obj;
        // Pull in betspread config from betspread.json
        try{
            obj = new JSONParser().parse(new InputStreamReader(this.getClass().getResource("/betspread.json").openStream()));
        }
        catch(Exception e){
            throw new RuntimeException("Caught exception when creating JSONParser:", e);
        }
        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;
        String betspreadString = "";
        Map<Integer, BetConfig> betSpread = new HashMap<Integer, BetConfig>();
        for (int i = 0; i < 5; i++) {
            String key = "" + i;
            JSONObject countSpecificJSON = (JSONObject) jo.get(key);
            long seats = (Long) countSpecificJSON.get("seats");
            long bettingUnits = (Long) countSpecificJSON.get("bettingUnitsPerSeat");
            betSpread.put(i, new BetConfig((int)seats, (int) bettingUnits));
            betspreadString = betspreadString + "TC " + i + ": " + seats + " spot(s) betting " + bettingUnits + " units.\n";
        }

        // run the game
        TableManager tableManager;
        double avgEndBankroll = 0;
        double highestBankroll = 0;
        double avgEv = 0;
        double ror = 0;
        double endBankroll = 0;
        double numberOfTimesBroke = 0;
        for (int i = 0; i < simIterations; i++){
            tableManager = new TableManager(shoeSize, stand17, deckPen, minBet, bankroll, handsPerHour, hoursPlayed, betSpread);
            endBankroll = tableManager.playGame();
            if (endBankroll <= 3*minBet) {
                numberOfTimesBroke += 1;
            }
            if (endBankroll > highestBankroll) {
                highestBankroll = endBankroll;
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
        outputArr.add(numberOfTimesBroke);
        outputArr.add(highestBankroll);

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
        outputTextArea.append("---------------------------------------\n");


        // Run your simulations and update the outputTextArea with the results
        ArrayList<Double> result;
        result = runSim(simIterations, shoeSize, stand17, deckPen, minBet, bankroll, handsPerHour, hoursPlayed);
        outputTextArea.append("\nSimulation results:");
        outputTextArea.append("\nAverage end bankroll: " + result.get(0));
        outputTextArea.append("\nEV/hour: " + result.get(1));
        outputTextArea.append("\nRisk of ruin: " + result.get(2));

        outputTextArea.append("\n\nSimulation statistics: ");
        outputTextArea.append("\nNumber of hands played: " + (simIterations*handsPerHour*hoursPlayed));
        outputTextArea.append("\nNumber of simulations that ended in 'ruin': " + round(result.get(3)));
        outputTextArea.append("\nHighest end bankroll: " + result.get(4));
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
