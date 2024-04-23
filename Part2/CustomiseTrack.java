import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomiseTrack extends JPanel {
    private JSlider slider;
    private JLabel sliderValueLabel;
    private JTextField lanesField;
    private JButton applyButton;
    private RaceGUI raceGUI; // Reference to the main GUI

    public CustomiseTrack(RaceGUI raceGUI) {
        // Save the reference to the main GUI
        this.raceGUI = raceGUI;

        // Set the layout for the panel
        setLayout(new GridLayout(10, 10));

        // Create labels and fields for customization options
        JLabel distanceLabel = new JLabel("Race Distance:");

        JLabel lanesLabel = new JLabel("Number of Lanes:");

        JLabel colourLabel = new JLabel("Choose Colour");
        lanesField = new JTextField("5");

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
        String[] items = {"Green", "Red", "Blue", "Orange"};
        JComboBox<String> dropDownMenu = new JComboBox<>(items);

        applyButton = new JButton("Apply");

        // Add the labels, fields, and button to the panel
        add(distanceLabel);
        add(sliderPanel);
        add(sliderValueLabel);
        add(lanesLabel);
        add(lanesField);
        add(colourLabel);
        add(dropDownMenu);
        add(new JLabel()); // Empty space for layout alignment
        add(applyButton);

        // Add an ActionListener to the apply button
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Parse the user input
                int distance = Integer.parseInt(sliderValueLabel.getText());
                int lanes = Integer.parseInt(lanesField.getText());

                // Apply the customization to the race panel
                raceGUI.applyCustomisation(distance, lanes);

                // Close the customization panel (if it's in a dialog, you can hide it)
                // Assuming the panel is in a dialog:
                // Window window = SwingUtilities.getWindowAncestor(CustomizeTrackPanel.this);
                // if (window != null) {
                //     window.setVisible(false);
                // }
            }
        });
    }

    public void setSlider() {
        this.slider = new JSlider(JSlider.HORIZONTAL, 100, 500, 300);
        slider.setMajorTickSpacing(100); 
        slider.setMinorTickSpacing(10); 
        slider.setPaintTicks(true); 
        slider.setPaintLabels(true); 
    }
}
