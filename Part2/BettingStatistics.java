import java.util.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BettingStatistics extends JPanel {
    private JSlider slider;
    private JLabel sliderValueLabel;
    private JTextField lanesField;
    private JButton applyButton;
    private RaceGUI raceGUI; // Reference to the main GUI
    private double wallet;

    private JLabel horseName;
    private JLabel horseConfidence;
    private JLabel odds;
    private JLabel numFalls;
    private JLabel numWins;
    private JLabel averageFinishPos;
    private JLabel totalRaces;


    public BettingStatistics(RaceGUI raceGUI) {
        // Save the reference to the main GUI
        this.raceGUI = raceGUI;

        // Set the layout for the panel

        // Create labels and fields for customization options
        JLabel selectHorse = new JLabel("Select Horse:");
        JLabel selectBet = new JLabel("Bet Amount:");
        horseConfidence = new JLabel("Confidence: ");
        numFalls = new JLabel("Number of Falls: ");
        numWins = new JLabel("Number of Wins: ");
        averageFinishPos = new JLabel("Average Finishing Position: ");
        totalRaces = new JLabel("Total Race Participations: ");

        setSlider();

        // Create a label to display the slider's value
        sliderValueLabel = new JLabel(""+slider.getValue(), SwingConstants.CENTER);

        // Add a ChangeListener to the slider
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Update the label with the slider's current value
                int value = slider.getValue();
                sliderValueLabel.setText(value + "");
                // Add any additional code here to handle the change in value
            }
        });

        // slider and label
        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.add(slider, BorderLayout.CENTER);
        //sliderPanel.add(sliderValueLabel, BorderLayout.SOUTH);

        // items for the drop-down menu

        String[] horses = getHorseNames(raceGUI.getHorses());
        JComboBox<String> dropDownMenu = new JComboBox<>(horses);

        dropDownMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected item
                String selectedItem = (String) dropDownMenu.getSelectedItem();
                displayHorseData(selectedItem);
            }
        });

        applyButton = new JButton("Place Bet");

        // Add the labels, fields, and button to the panel
        add(selectHorse);
        add(dropDownMenu);
        add(selectBet);
        add(sliderPanel);
        add(sliderValueLabel);
        add(horseConfidence);
        add(numFalls);
        add(numWins);
        add(averageFinishPos);
        add(totalRaces);
        
        
        //add(new JLabel()); // Empty space for layout alignment
        add(applyButton);

        // Add an ActionListener to the apply button
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Parse the user input


                // Close the customization panel (if it's in a dialog, you can hide it)
                // Assuming the panel is in a dialog:
                // Window window = SwingUtilities.getWindowAncestor(CustomizeTrackPanel.this);
                // if (window != null) {
                //     window.setVisible(false);
                // }
            }
        });
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

    private void displayHorseData(String selectedItem) {
        // Use a switch-case statement or if-else statements to handle different selections
        ArrayList<Horse> horses = raceGUI.getHorses();

        for (Horse horse : horses) {
            if (horse != null && selectedItem.equals(horse.getName())) {
                HorseMetrics metrics = horse.getHorseMetrics();
                horseConfidence.setText("Confidence: " + horse.getConfidence());
                numFalls.setText("Number of Falls: " + metrics.getFalls());
                numWins.setText("Number of Wins: " + metrics.getRacesWon());
                averageFinishPos.setText("Average Finishing Positition: " + metrics.getAvgFinishPosition());
                totalRaces.setText("Total Race Participations: " + metrics.getRaces());
                break;
            }
        }
    }

    private void setSlider() {
        this.slider = new JSlider(JSlider.HORIZONTAL, 100, 500, 300);
        slider.setMajorTickSpacing(100); 
        slider.setMinorTickSpacing(10); 
        slider.setPaintTicks(true); 
        slider.setPaintLabels(true); 
    }
}
