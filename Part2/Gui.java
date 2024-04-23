import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;


public class Gui {
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Horse Race Betting Game");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        JPanel trackPanel = new JPanel();
        JPanel statisticsPanel = new JPanel();
        JPanel controlPanel = new JPanel();

        JButton startRaceButton = new JButton("Start Race");
        JButton customizeTrackButton = new JButton("Customize Track");
        JButton customizeHorseButton = new JButton("Customize Horse");
        JButton statisticsAndBettingButton = new JButton("Statistics and Betting");

        ArrayList<Horse> horses = readInHorses();

        controlPanel.add(startRaceButton);
        controlPanel.add(customizeTrackButton);
        controlPanel.add(customizeHorseButton);
        controlPanel.add(statisticsAndBettingButton);

        runRace(frame, horses);

        frame.add(trackPanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.add(statisticsPanel, BorderLayout.EAST);        

        
        //trackCustomisationPanel(frame, customizeTrackButton);
        //customiseHorse(frame, customizeHorseButton);
        //statisticsPanel(frame, statisticsAndBettingButton);

        /*

        statisticsAndBettingButton.addActionListener(e -> {
            statisticsPanel(frame, statisticsAndBettingButton);
        });

        customizeTrackButton.addActionListener(e -> {
            trackCustomisationPanel(frame, customizeTrackButton);
        });

        customizeHorseButton.addActionListener(e -> {
            customiseHorse(frame, customizeHorseButton);
        });
*/
        frame.setVisible(true);
/*
        JTextField display = new JTextField();
        display.setPreferredSize(new Dimension(300, 50));
        display.setEditable(true);
        display.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(display, BorderLayout.NORTH);*/
    }

    public static ArrayList<Horse> readInHorses() throws IOException {
        ArrayList<Horse> horses = new ArrayList<>();
        String filePath = "myObjects.dat";
        try {
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            // Read the object from the file
            Horse horseObj = (Horse) objectIn.readObject();

            while (horseObj != null) {
                horses.add(horseObj);
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
        return horses;
    }

    public static void runRace(JFrame frame, ArrayList<Horse> horses) throws IOException {
        try {
            GuiRacePanel race = new GuiRacePanel(100, 5);
            Horse horse3 = new Horse('C', "BETTY", 0.6, null);
            Horse horse4 = new Horse('D', "NIGEL", 0.75, null);
            Horse horse5 = new Horse('E', "PILLOW", 0.7, null);
            race.addHorse(horse3);
            race.addHorse(horse4);
            race.addHorse(horse5);

            race.startRace();

            JLabel win = new JLabel(race.getWinner(), SwingConstants.CENTER);
            //frame.add(win);
            //trackPanel.add(race);

            // Add the panel to the frame
            frame.add(race);
            //frame.add(trackPanel, BorderLayout.CENTER);

            // Create the frame and make it visible
            //HorseRacingDemo demo = new HorseRacingDemo(horses);
            //demo.setVisible(true);
            /*
            Race race = new Race(20, 8);
            int lane = 1;
            for (Horse horse : horses) {
                race.addHorse(horse, lane);
                lane++;
            }
            race.startRace();*/
        } catch (IOException e) {
            System.out.println("Something went wrong.");
        }
    }


    public static void test(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Header, Body, and Footer Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout()); // Set BorderLayout as the layout manager

        // Create header panel and add components
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.LIGHT_GRAY); // Optional: Set background color
        JLabel headerLabel = new JLabel("Header Section");
        headerPanel.add(headerLabel);

        // Create body panel and add components
        JPanel bodyPanel = new JPanel();
        bodyPanel.setBackground(Color.WHITE); // Optional: Set background color
        JLabel bodyLabel = new JLabel("Body Section");
        bodyPanel.add(bodyLabel);

        // Create footer panel and add components
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.DARK_GRAY); // Optional: Set background color
        JLabel footerLabel = new JLabel("Footer Section");
        footerLabel.setForeground(Color.WHITE); // Set label color to white for contrast
        footerPanel.add(footerLabel);

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(bodyPanel, BorderLayout.CENTER);
        frame.add(footerPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    // method for displaying statistics and betting
    public static void statisticsPanel(JFrame frame, JButton button) {
        JPanel statsPanel = new JPanel();
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics & Analytics"));
        

        // Add components to display statistics
        JLabel horseStatsLabel = new JLabel("Horse Performance:");
        JLabel trackRecordsLabel = new JLabel("Track Records:");
        JLabel bettingOddsLabel = new JLabel("Betting Odds:");

        JTextArea horseStatsArea = new JTextArea(10, 20);
        horseStatsArea.setEditable(false);

        JTextArea trackRecordsArea = new JTextArea(10, 20);
        trackRecordsArea.setEditable(false);

        JTextArea bettingOddsArea = new JTextArea(10, 20);
        bettingOddsArea.setEditable(false);

        statsPanel.add(horseStatsLabel);
        statsPanel.add(horseStatsArea);
        statsPanel.add(trackRecordsLabel);
        statsPanel.add(trackRecordsArea);
        statsPanel.add(bettingOddsLabel);
        statsPanel.add(bettingOddsArea);

        statsPanel.setVisible(false);

        button.addActionListener(e -> {
            // Toggle the visibility of the stats panel
            statsPanel.setVisible(!statsPanel.isVisible());
        });

        // Add customizeTrackPanel to the main frame
        frame.add(statsPanel, BorderLayout.CENTER);
    }

    // method for customising your horse
    public static void customiseHorse(JFrame frame, JButton button) {
        JPanel customizeHorsePanel = new JPanel();
        customizeHorsePanel.setBorder(BorderFactory.createTitledBorder("Customize Horse"));

        // Breed selection
        JLabel breedLabel = new JLabel("Breed:");
        String[] breeds = {"Thoroughbred", "Arabian", "Quarter Horse", "Appaloosa"};
        JComboBox<String> breedComboBox = new JComboBox<>(breeds);
        customizeHorsePanel.add(breedLabel);
        customizeHorsePanel.add(breedComboBox);

        // Coat color selection
        JLabel coatColorLabel = new JLabel("Coat Color:");
        String[] colors = {"Chestnut", "Bay", "Gray", "Black"};
        JComboBox<String> colorComboBox = new JComboBox<>(colors);
        customizeHorsePanel.add(coatColorLabel);
        customizeHorsePanel.add(colorComboBox);

        customizeHorsePanel.setVisible(false);

        button.addActionListener(e -> {
        // Toggle the visibility of the stats panel
            customizeHorsePanel.setVisible(!customizeHorsePanel.isVisible());
        });

        // Add customizeTrackPanel to the main frame
        frame.add(customizeHorsePanel, BorderLayout.CENTER);
    }

    // method for customising tracks
    public static void trackCustomisationPanel(JFrame frame, JButton button) {
        JPanel customizeTrackPanel = new JPanel();
        customizeTrackPanel.setBorder(BorderFactory.createTitledBorder("Customize Track"));

        // Customise the number of tracks
        JLabel numTracksLabel = new JLabel("Number of Tracks:");
        JSpinner numTracksSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        customizeTrackPanel.add(numTracksLabel);
        customizeTrackPanel.add(numTracksSpinner);

        // Customise track length
        JLabel trackLengthLabel = new JLabel("Track Length:");
        JSlider trackLengthSlider = new JSlider(JSlider.HORIZONTAL, 10, 100, 50);
        trackLengthSlider.setMajorTickSpacing(40);
        trackLengthSlider.setMinorTickSpacing(10);
        trackLengthSlider.setPaintTicks(true);
        trackLengthSlider.setPaintLabels(true);
        customizeTrackPanel.add(trackLengthLabel);
        customizeTrackPanel.add(trackLengthSlider);

        numTracksSpinner.addChangeListener(e -> {
        int numTracks = (int) numTracksSpinner.getValue();
            // Update the number of tracks in the game
            // TODO:
        });

        trackLengthSlider.addChangeListener(e -> {
            int trackLength = trackLengthSlider.getValue();
            // Update the track length in the game
            // TODO:
        });

        customizeTrackPanel.setVisible(false);

        button.addActionListener(e -> {
            // Toggle the visibility of the stats panel
            customizeTrackPanel.setVisible(!customizeTrackPanel.isVisible());
        });

        // Add customizeTrackPanel to the main frame
        frame.add(customizeTrackPanel, BorderLayout.CENTER);

    }
}