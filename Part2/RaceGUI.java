import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class RaceGUI extends JFrame {
    private RacePanel racePanel; // The race panel
    private JLabel raceWinnerLabel;
    private ArrayList<Horse> horses;

    // Constructor
    public RaceGUI() {
        // Set the title of the frame
        setTitle("Horse Race");
        // Set the default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        raceWinnerLabel = new JLabel("", SwingConstants.CENTER);
        raceWinnerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        raceWinnerLabel.setForeground(Color.RED);

        // Create a GuiRacePanel with the desired parameters
        racePanel = new RacePanel(100, 5); // Modify the parameters as needed
        racePanel.setRaceGUI(this);

        JPanel trackPanel = new JPanel();
        //frame.add(win);

        try {
            readInHorses(racePanel);
        } catch (IOException e) {

        }

        // Create a button to start the race
        JButton startButton = new JButton("Start Race");

        // Add an ActionListener to the button
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Start the race when the button is clicked
                try {
                    raceWinnerLabel.setText("");
                    racePanel.startRace();
                } catch (IOException ee) {

                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);

        // Customize track button
        JButton customizeTrackButton = new JButton("Customize Track");
        customizeTrackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customiseTrack();
            }
        });

        buttonPanel.add(customizeTrackButton);
        
        // Betting System track button
        JButton bettingStatisticsButton = new JButton("Betting & Statistics");
        bettingStatisticsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bettingStatistics();
            }
        });

        buttonPanel.add(bettingStatisticsButton);

        // Add the race panel and button panel to the frame
        add(racePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(raceWinnerLabel, BorderLayout.NORTH);

        // Set the frame size
        setSize(800, 600);

        // Make the frame visible
        setVisible(true);
    }

    public void updateRaceWinner(String input) {
        raceWinnerLabel.setText(input);
    }

    // Main method to run the GUI
    public static void main(String[] args) {
        new RaceGUI(); // Create an instance of RaceGUI
    }

    /*
    * Read in horses and store in Race
    */
    private void  readInHorses(RacePanel racePanel) throws IOException {
        String filePath = "horses.dat";
        horses = new ArrayList<>();
        try {
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            // Read the object from the file
            Horse horseObj = (Horse) objectIn.readObject();

            while (horseObj != null) {
                horses.add(horseObj);
                racePanel.addHorse(horseObj);
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
                e.printStackTrace();
        }
    }

    // Apply customisations
    public void applyCustomisation(int distance, int lanes) {
        racePanel.updateRaceLength(distance);
        racePanel.updateNumLanes(lanes);
    }

    
    // open Customisation
    private void customiseTrack() {
        // Create the customization panel and wrap it in a dialog
        CustomiseTrack ccustomiseTrack = new CustomiseTrack(this);
        JDialog customiseDialog = new JDialog(this, "Customise Track", true);
        customiseDialog.add(ccustomiseTrack);
        customiseDialog.pack();
        customiseDialog.setLocationRelativeTo(this); // Center the dialog
        customiseDialog.setVisible(true);
    }

    // open Customisation
    private void bettingStatistics() {
        // Create the customization panel and wrap it in a dialog
        BettingStatistics bettingStatistics = new BettingStatistics(this);
        JDialog customiseDialog = new JDialog(this, "Betting & Statistics", true);
        customiseDialog.add(bettingStatistics);
        customiseDialog.pack();
        customiseDialog.setLocationRelativeTo(this); // Center the dialog
        customiseDialog.setVisible(true);
    }

    public ArrayList<Horse> getHorses() {
        return this.horses;
    }
}