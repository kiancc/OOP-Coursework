import java.util.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class GUI extends JFrame implements RaceListener {  
    private int customRaceLength;
    private int customNumLanes;
    private int betAmount;
    private double horseOdds;
    private int wallet;
    private String horseBet;
    private char customTrackBoundary;  
    private Race race;
    private ArrayList<Horse> horses;
    private TrackCustomisation customisation;
    private Color customBackgroundColor;
    private JLabel winnerLabel;
    private JLabel walletLabel;
    private JTextArea raceTrackTextArea;
    private JButton customiseTrackButton;
    private JButton startRaceButton;
    private JButton customiseHorseButton;
    private JButton bettingStaisticsButton;

    public GUI() {
        setTitle("Horse Race");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        customTrackBoundary = '=';
        customRaceLength = 30;
        customNumLanes = 3;

        // read in horses
        try {
            horses = readInHorses();
        } catch (IOException e) {
            System.out.println("Could not read in Horses! Default horses added.");
            horses.add(new Horse('A', "Alpha", 0.7));
            horses.add(new Horse('B', "Beta", 0.8));
            horses.add(new Horse('C', "Charlie", 0.6));
        } 

        // Create the race object
        race = new Race(customRaceLength, horses.size()); // Adjust the parameters as needed

        wallet = 100; // default amount for wallet

        walletLabel = new JLabel("Wallet: " + wallet);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(walletLabel, BorderLayout.NORTH);

        addHorsesToRace(race);

        winnerLabel = new JLabel("");

        // Create the components
        JPanel trackRacePanel = new JPanel();
        raceTrackTextArea = new JTextArea(10, customRaceLength);
        raceTrackTextArea.setEditable(false);
        raceTrackTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        trackRacePanel.add(raceTrackTextArea, BorderLayout.CENTER);
        trackRacePanel.add(winnerLabel, BorderLayout.SOUTH);
        add(trackRacePanel, BorderLayout.CENTER);

        initialiseRace();

        startRaceButton = new JButton("Start Race");
        startRaceButton.addActionListener(new StartRaceListener());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startRaceButton);
        add(buttonPanel, BorderLayout.SOUTH);

        /* implements track customisation */
        customiseTrackButton = new JButton("Customize Track");
        customiseTrackButton.addActionListener(new CustomiseTrackListener());
        buttonPanel.add(customiseTrackButton);

        /* implements horse customisation */
        customiseHorseButton = new JButton("Customize Horse");
        customiseHorseButton.addActionListener(new CustomiseHorseListener());
        buttonPanel.add(customiseHorseButton);

        /* implements betting and statistics */
        bettingStaisticsButton = new JButton("Betting & Statistics");
        bettingStaisticsButton.addActionListener(new BettingStatisticsListener());
        buttonPanel.add(bettingStaisticsButton);

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        race.addRaceListener(this);
    }

    /* --------------------------------------------------  */
    // Code for betting statistics
    
    private class BettingStatisticsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showBettingStatisticsDialog();
        }
    }

    private void showBettingStatisticsDialog() {
        JPanel customisationPanel = new JPanel(new GridLayout(11, 2));

        // Create labels and fields for customization options
        JLabel horseConfidence = new JLabel("Confidence: ");
        JLabel numFalls = new JLabel("Number of Falls: ");
        JLabel numWins = new JLabel("Number of Wins: ");
        JLabel averageFinishPos = new JLabel("Average Finishing Position: ");
        JLabel totalRaces = new JLabel("Total Race Participations: ");
        JLabel averageSpeed = new JLabel("Average Speed: ");
        JLabel selectHorseLabel = new JLabel("Select Horse:");
        JLabel oddsLabel = new JLabel("Odds: ");
        String[] horsesStrings = getHorseNames(horses);
        JComboBox<String> selectHorseComboBox = new JComboBox<>();
        selectHorseComboBox.setModel(new DefaultComboBoxModel<>(horsesStrings));
        //selectHorseComboBox.setSelectedItem(customTrackBoundary);
        selectHorseComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedBoundary = (String) e.getItem();
                    Horse horse = selectedHorse(selectedBoundary);
                    double confidence = getSelectedHorseConfidence(horse);
                    int falls = getSelectedHorseFalls(horse);
                    int wins = getSelectedHorseWins(horse);
                    double averagePos = getSelectedHorseAveragePosition(horse);
                    int total = getSelectedHorseTotalRaces(horse);
                    double avgSpeed = horse.getHorseMetrics().getAverageSpeed();
                    
                    horseConfidence.setText("Confidence: " + String.format("%.2f", confidence));
                    numFalls.setText("Number of Falls: " + falls);
                    numWins.setText("Number of Wins: " + wins);
                    averageFinishPos.setText("Average Finishing Position: " + String.format("%.2f", averagePos));
                    totalRaces.setText("Total Race Participations: " + total);
                    averageSpeed.setText("Average Speed : " + String.format("%.2f", avgSpeed));
                    double odd = getOdds(confidence, wins, total, averagePos);
                    horseOdds = odd;
                    oddsLabel.setText("Odds: " + String.format("%.2f", odd));
                    horseBet = horse.getName();
                }
            }
        });

        customisationPanel.add(selectHorseLabel);
        customisationPanel.add(selectHorseComboBox);

        JLabel betAmountLabel = new JLabel("Bet Amount: ");
        JTextField betAmountField = new JTextField("");
        customisationPanel.add(betAmountLabel);
        customisationPanel.add(betAmountField);
        customisationPanel.add(horseConfidence);
        customisationPanel.add(numFalls);
        customisationPanel.add(numWins);
        customisationPanel.add(averageFinishPos);
        customisationPanel.add(averageSpeed);
        customisationPanel.add(totalRaces);
        customisationPanel.add(oddsLabel);

        int result = JOptionPane.showConfirmDialog(GUI.this, customisationPanel, "Customise and Add Horse", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            int bet = Integer.parseInt(betAmountField.getText());
            try {
                if (betAmountField.getText().equals("")) {
                    JOptionPane.showMessageDialog(this, "Please Enter Bet Amount", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    showBettingStatisticsDialog();
                }
                else if (bet < 0 ) {
                    JOptionPane.showMessageDialog(this, "Bet must be greater than 0", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    showBettingStatisticsDialog();
                } else if (bet > wallet){
                    JOptionPane.showMessageDialog(this, "You do not have enough money.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    showBettingStatisticsDialog();               
                } else {
                    applyBet(bet);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(GUI.this, "Invalid input for race length.", "Error", JOptionPane.ERROR_MESSAGE);
                showBettingStatisticsDialog();
            }
        }
    }

    private double getOdds(double confidence, int wins, int totalRaces, double averageFinishPos) {
        double odds = (1/confidence);
        return odds;
    }

    private double getSelectedHorseConfidence(Horse horse) {
        return horse.getConfidence();
    }

    private int getSelectedHorseFalls(Horse horse) {
        return horse.getHorseMetrics().getFalls();
    }

    private int getSelectedHorseWins(Horse horse) {
        return horse.getHorseMetrics().getRacesWon();
    }

    private double getSelectedHorseAveragePosition(Horse horse) {
        return horse.getHorseMetrics().getAvgFinishPosition();
    }

    private int getSelectedHorseTotalRaces(Horse horse) {
        return horse.getHorseMetrics().getRaces();
    }

    private Horse selectedHorse(String name) {
        for (Horse horse : horses) {
            if (horse.getName() == name) {
                return horse;
            }
        }
        return null;
    }

    private String[] getHorseNames(ArrayList<Horse> horses) {
        ArrayList<String> names = new ArrayList<>();
        for (Horse horse : horses) {
            if (horse != null) {
                names.add(horse.getName());
            }
        }

        String[] namesString = new String[names.size()];
        namesString = names.toArray(namesString);
        return namesString;
    }

    private void applyBet(int bet) {
        wallet -= bet;
        betAmount = bet;
        walletLabel.setText("Wallet: " + wallet); // Update the walletLabel
        initialiseRace();
    }

    /* --------------------------------------------------  */


    /* --------------------------------------------------  */
    // Code for customising horse

    private class CustomiseHorseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showHorseCustomisationDialog();
        }
    }

    private void showHorseCustomisationDialog() {
        JPanel customisationPanel = new JPanel(new GridLayout(4, 2));

        JLabel horseNameLabel = new JLabel("Horse Name: ");
        JTextField horseNameField = new JTextField("");
        customisationPanel.add(horseNameLabel);
        customisationPanel.add(horseNameField);

        JLabel horseSymbolLabel = new JLabel("Horse Symbol:");
        JTextField horseSymbolField = new JTextField("");
        customisationPanel.add(horseSymbolLabel);
        customisationPanel.add(horseSymbolField);

        int result = JOptionPane.showConfirmDialog(GUI.this, customisationPanel, "Customize Horse", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int horseNameLength = horseNameField.getText().length();
                if (horseNameLength < 0 || horseNameLength > 50) {
                    JOptionPane.showMessageDialog(this, "Horse name must be between " + 1 + " and " + 50 + " characters.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    showHorseCustomisationDialog();
                } else {
                    //customisation = new TrackCustomisation(newRaceLength, newTrackBoundary, customBackgroundColor, newNumLanes);
                    Horse newHorse = new Horse(horseSymbolField.getText().charAt(0), horseNameField.getText(), 0.6);
                    applyHorseCustomisation(newHorse);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(GUI.this, "Invalid input for race length.", "Error", JOptionPane.ERROR_MESSAGE);
                showHorseCustomisationDialog();
            }
        }
    }

    private void applyHorseCustomisation(Horse horse) {
        int newNumLanes = race.getNumLanes() + 1;
        race.updateNumLanes(newNumLanes);
        race.addHorse(horse);
        initialiseRace();
    }

    /* --------------------------------------------------  */

    /* --------------------------------------------------  */
    // Code for customising track

    private class CustomiseTrackListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showCustomisationDialog();
        }
    }

    private void showCustomisationDialog() {
        JPanel customisationPanel = new JPanel(new GridLayout(4, 2));

        JLabel raceLengthLabel = new JLabel("Race Length (10 - 100):");
        JTextField raceLengthField = new JTextField(String.valueOf(race.getRaceLength()));
        customisationPanel.add(raceLengthLabel);
        customisationPanel.add(raceLengthField);

        JLabel trackBoundaryLabel = new JLabel("Fence Character:");
        JTextField trackBoundaryField = new JTextField(String.valueOf(customTrackBoundary));
        customisationPanel.add(trackBoundaryLabel);
        customisationPanel.add(trackBoundaryField);

        JLabel numLanesLabel = new JLabel("Number of Lanes (1 - 10):"); 
        JTextField numLanesField = new JTextField(String.valueOf(race.getNumLanes()));
        customisationPanel.add(numLanesLabel);
        customisationPanel.add(numLanesField);

        JLabel backgroundColorLabel = new JLabel("Background Color:");
        JButton colorChooserButton = new JButton("Choose Color");
        colorChooserButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(GUI.this, "Choose Background Color", customBackgroundColor);
            if (color != null) {
                customBackgroundColor = color;
            }
        });
        customisationPanel.add(backgroundColorLabel);
        customisationPanel.add(colorChooserButton);

        int result = JOptionPane.showConfirmDialog(GUI.this, customisationPanel, "Customize Track", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int newRaceLength = Integer.parseInt(raceLengthField.getText());
                int newNumLanes = Integer.parseInt(numLanesField.getText());
                char newTrackBoundary = trackBoundaryField.getText().charAt(0);
                if (newRaceLength < 10 || newRaceLength > 100) {
                    JOptionPane.showMessageDialog(this, "Race length must be between " + 10 + " and " + 100 + ".", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    showCustomisationDialog();
                }
                else if (newNumLanes < race.getHorses().size()) {
                    JOptionPane.showMessageDialog(this, "The number of lanes cannot be less than the number of horses participating (" + race.getHorses().size()+ ")", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    showCustomisationDialog();
                }
                else if (newNumLanes < 1 || newNumLanes > 10) {
                    JOptionPane.showMessageDialog(this, "The number of lanes must be between " + 1 + " and " + 10 + ".", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    showCustomisationDialog();
                } else {
                    customisation = new TrackCustomisation(newRaceLength, newTrackBoundary, customBackgroundColor, newNumLanes);
                    applyCustomisation();
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(GUI.this, "Invalid input for race length.", "Error", JOptionPane.ERROR_MESSAGE);
                showCustomisationDialog();
            }
        }
    }

    private void applyCustomisation() {
        if (customisation != null) {
            race.updateRaceLength(customisation.getRaceLength());
            race.updateNumLanes(customisation.getNumLanes());
            raceTrackTextArea.setSize(10, customisation.getRaceLength() + 4);
            customRaceLength = customisation.getRaceLength();
            customTrackBoundary = customisation.getTrackBoundary();
            raceTrackTextArea.setBackground(customisation.getBackgroundColor());
            initialiseRace();
        }
    }
    /* --------------------------------------------------  */

    /* --------------------------------------------------  */
    // printing race

    private class StartRaceListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            startRaceButton.setEnabled(false);
            applyCustomisation();
            winnerLabel.setText("");
            Thread raceThread = new Thread(() -> {
                try {
                    race.startRace();
                    updateRaceTrack();
                } catch (IOException ex) {
                    // handle the exception here
                    ex.printStackTrace();
                }

            });
            
            raceThread.start();
        }
    }

    @Override
    public void onRaceUpdate() {
        updateRaceTrack();
    }

        @Override
    public void onRaceFinished() {
        if (!race.getWinner().isEmpty()) {
            winnerLabel.setText("Winner: " + race.getWinner());
            if (!race.getWinner().equals("")) {
                if (horseBet != null && race.getWinner().equals(horseBet)) {
                    wallet += betAmount * horseOdds;
                    walletLabel.setText("Wallet: " + wallet); // Update the walletLabel
                    JOptionPane.showMessageDialog(GUI.this, (String.format("%2f" + "Congratulations! You won " + betAmount * horseOdds + " credits.")));
                }
            }
        } else {
            winnerLabel.setText("No winner");
        }
        enableStartRaceButton();
        horseOdds = 0;
        betAmount = 0;
    }

    private void enableStartRaceButton() {
        startRaceButton.setEnabled(true);
    }

    private void updateRaceTrack() {
        StringBuilder raceTrackBuilder = new StringBuilder();
        multiplePrint(raceTrackBuilder, customTrackBoundary, race.getRaceLength() + 3);
        raceTrackBuilder.append("\n");

        for (int i = 0; i < race.getNumLanes(); i++) {
            if (i < race.getHorses().size() && race.getHorses().get(i) != null) {
                printLane(raceTrackBuilder, race.getHorses().get(i));
                raceTrackBuilder.append(" ").append(race.getHorses().get(i).getName()).append(" (Current confidence ").append(String.format("%.2f", race.getHorses().get(i).getConfidence())).append(")");
            } else {
                printEmptyLane(raceTrackBuilder);
            }
            raceTrackBuilder.append("\n");
        }

        multiplePrint(raceTrackBuilder, customTrackBoundary, race.getRaceLength() + 3);
        raceTrackBuilder.append("\n");

        raceTrackTextArea.setText(raceTrackBuilder.toString());
    }

    private void printLane(StringBuilder sb, Horse theHorse) {
        sb.append('|');
        multiplePrint(sb, ' ', theHorse.getDistanceTravelled());
        if (theHorse.hasFallen()) {
            sb.append('\u2322');
        } else {
            sb.append(theHorse.getSymbol());
        }
        multiplePrint(sb, ' ', race.getRaceLength() - theHorse.getDistanceTravelled());
        sb.append('|');
    }

    private void printEmptyLane(StringBuilder sb) {
        sb.append('|');
        multiplePrint(sb, ' ', race.getRaceLength() + 1);
        sb.append('|');
    }

    private void multiplePrint(StringBuilder sb, char aChar, int times) {
        for (int i = 0; i < times; i++) {
            sb.append(aChar);
        }
    }

    /* --------------------------------------------------  */

    private void initialiseRace() {
        StringBuilder raceTrackBuilder = new StringBuilder();
        multiplePrint(raceTrackBuilder, customTrackBoundary, race.getRaceLength() + 3);
        raceTrackBuilder.append("\n");

        for (int i = 0; i < race.getNumLanes(); i++) {
            printEmptyLane(raceTrackBuilder);
            raceTrackBuilder.append("\n");
        }

        multiplePrint(raceTrackBuilder, customTrackBoundary, race.getRaceLength() + 3);
        raceTrackBuilder.append("\n");

        raceTrackTextArea.setText(raceTrackBuilder.toString());
    }

    // reads in horses from file
    private ArrayList<Horse> readInHorses() throws IOException {
        String filePath = "horses.dat";
        ArrayList<Horse> newHorses = new ArrayList<>();
        //horses = new ArrayList<>();
        try {
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            // Read the object from the file
            Horse horseObj = (Horse) objectIn.readObject();

            while (horseObj != null) {
                //horses.add(horseObj);
                newHorses.add(horseObj);
                horseObj = (Horse) objectIn.readObject();
                System.out.println("Read object: " + horseObj);
            }

            // Close the ObjectInputStream and FileInputStream
            objectIn.close();
            fileIn.close();
        } catch (EOFException e) {
                // This exception is thrown when the end of the file is reached
                System.out.println("End of file reached.");
        } catch (ClassNotFoundException e) {
                // This exception is thrown if the class of the serialized object cannot be found

                e.printStackTrace();
        } catch (IOException e) {
                // Handle IO exceptions
                newHorses.add(new Horse('♔', "Alpha", 0.7));
                newHorses.add(new Horse('♘', "Beta", 0.7));
                newHorses.add(new Horse('♛', "Charlie", 0.7));
                System.out.println("No horses.dat file found. Initialising with basic horses.");
                //e.printStackTrace();
        }

        return newHorses;
    }

    // add horses to race
    private void addHorsesToRace(Race race) {
        if (horses != null) {
            for (Horse horse : horses) {
                race.addHorse(horse);
            }
        }
    }

    public static void startRaceGUI(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            gui.setVisible(true);
        });
    }

}