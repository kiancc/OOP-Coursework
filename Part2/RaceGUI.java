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
                    racePanel.startRace();
                } catch (IOException ee) {

                }
            }
        });

        // Create a panel to hold the button
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);

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

    public static void  readInHorses(RacePanel racePanel) throws IOException {
        ArrayList<Horse> horses = new ArrayList<>();
        String filePath = "horses.dat";
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
}