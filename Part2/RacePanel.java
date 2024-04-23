import javax.swing.*;
import javax.swing.Timer;
import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author McFarewell
 * @version 1.0
 */
public class RacePanel extends JPanel
{
    private int raceLength;
    private ArrayList<Horse> horses;
    private int numLanes;
    private int numHorses;
    private int numFallen;
    private String winner;
    private Timer timer;
    public boolean raceFinished;
    private RaceGUI raceGUI;
    private String trackColour;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public RacePanel(int distance, int numLanes)
    {
        // initialise instance variables
        this.raceLength = distance;
        this.numLanes = numLanes;
        this.horses = new ArrayList<Horse>();
        this.winner = null;
    }
    
    /**
     * Adds a horse to the race in a given lane
     * 10/03/2024 edited the method to 
     * 
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse)
    {
        this.horses.add(theHorse);
        numHorses++;
    }

    
    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the 
     * race is finished
     */
    public void startRace() throws IOException
    {
        //declare a local variable to tell us when the race is finished
        boolean finished = false;
        
        //reset all the lanes (all horses not fallen and back to 0). 
        resetLanes();
                      
        // Create a Timer with a delay of 100 ms
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Print the race positions
                //printRace();

                moveHorses();

                if (hasAllFallen()) {
                    raceFinished = true;
                    // Stop the timer if all horses have fallen
                    raceGUI.updateRaceWinner("All horses have fallen. There is no winner.");
                    ((Timer) e.getSource()).stop();
                    return;
                } 
                // If any of the horses has won, the race is finished
                if (checkAllFinished()) {
                    raceWonBy();
                    trackPositionsAndUpdateHorseMetrics();
                    raceFinished = true;
                    // Stop the timer when the race is finished
                    raceGUI.updateRaceWinner("The race has finished. The winner is: " + getWinner());
                    ((Timer) e.getSource()).stop();
                    return;
                } 


                repaint();
            }
        });

        timer.start();

        Map<String, Horse> horseInput = read();
        save(horseInput);
    }

    /**
     * Moves horses
     * @return
     */
    private void moveHorses() {
        for (Horse h : horses) {
            if (h != null) {
                moveHorse(h);
            }
        }
    }

    /*
     * Checks if all the horses have finished
     */

    private boolean checkAllFinished() {
        int numFinished = 0;
        String tempWinner = "";
        for (Horse horse : horses) {
            if (horse.getDistanceTravelled() == raceLength) {
                numFinished++;
                tempWinner = horse.getName();
            }
        }
        if (numFinished == 1 && winner == null) {
            winner = tempWinner;
        }

        if (numFinished == numHorses - numFallen) {
            return true;
        }
        return false;
    }

    /*
     * Added 01/04/2024
     * Tracks the positions of the horses and updates horse metrics
     */
    private void trackPositionsAndUpdateHorseMetrics(){
        ArrayList<Integer> distances = new ArrayList<Integer>();
        for (Horse h : horses) {
            if (h != null) {
                distances.add(h.getDistanceTravelled());
            }
        }

        Collections.sort(distances, Collections.reverseOrder());

        for (Horse h : horses) {
            if (h != null) {
                int position = distances.indexOf(h.getDistanceTravelled());
                h.updateHMetrics(position+1, raceLength);
            }
        }
    }

    /**
     * Resets horses to beginning of race
     */
    private void resetLanes() {
        this.numFallen = 0;
        this.winner = null;
        for (Horse h : horses) {
            if (h != null) {
                h.goBackToStart();
            }
        }
    }
    
    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */
    private void moveHorse(Horse theHorse)
    {
        //if the horse has fallen it cannot move, 
        //so only run if it has not fallen
        
        if  (!theHorse.hasFallen() && theHorse.getDistanceTravelled() != raceLength)
        {
            //the probability that the horse will move forward depends on the confidence;
            if (Math.random() < theHorse.getConfidence())
            {
               theHorse.moveForward();
               theHorse.moveForward();
               theHorse.moveForward();
               theHorse.moveForward();
               theHorse.moveForward();

               return;
            }
            //the probability that the horse will fall is very small (max is 0.1)
            //but will also will depends exponentially on confidence 
            //so if you double the confidence, the probability that it will fall is *2
            if (Math.random() < (0.1*theHorse.getConfidence()*theHorse.getConfidence()))
            {
                // ADDED 10/03/2024 ONE LINE BELOW: changes the symbol of the horse if it has fallen
                theHorse.fall();
                // ADDED 10/03/2024 decreases horses confidence by an arbitrary amount
                theHorse = adjustConfidence(theHorse, -0.01);
                numFallen++;
                return;
            }
        }

        return;
    }
        
    /** 
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    private void raceWonBy()
    {
        for (Horse horse : horses) {
            if (horse.getName().equals(winner)) {
                // added winning message with horses name
                horse = adjustConfidence(horse, 0.01);
                horse.win();
                System.out.println("And the winner is " + horse.getName() + " Confidence: " + horse.getConfidence());
            }
        }
    }
    
    /***
     * Print the race on the terminal
     * 10/03/2024 edited to loop through horses and print lane
     */
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int yPos = 200;
        int xPos = (450 - (int)(raceLength * 0.7));
        //int offset = (int)(raceLength/2);

        // Cast Graphics to Graphics2D
        Graphics2D g2d = (Graphics2D) g;
        Graphics2D g2dHorse = (Graphics2D) g;

        // Set the color for the string
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2dHorse.setColor(Color.BLACK);

        // Define the string to draw
        String text = "Hello, world!";
        g2d.drawLine(xPos, 180, xPos + raceLength, 180);
        for (int i = 0; i < numLanes; i++) {
            g2d.drawLine(xPos, yPos+ 10, xPos  + raceLength, yPos + 10);
            if (i < horses.size() && horses.get(i) != null) {
                //g2d.drawString(horses.get(i).getSymbol() + "", horses.get(i).getDistanceTravelled(), yPos);
                printLane(horses.get(i), g2dHorse, xPos, yPos);
                //System.out.print(" " + horses.get(i).getName() + " (Current confidence " + horses.get(i).getConfidence() + ")");
            } else {
                printEmptyLane(g2d, xPos, yPos);
            }
            //g2d.drawLine(xPos, yPos+ 10, raceLength + xPos, yPos + 10);
            yPos += 30;
        }
    }

    /**
     * Updates raceLength
     */
    public void updateRaceLength(int distance) {
        this.raceLength = distance;
    }

    /**
     * Updates numLanes
     */
    public void updateNumLanes(int lanes) {
        this.numLanes = lanes;
    }

    /**
     * checks if all the horses have fallen in the race
     */
    private boolean hasAllFallen() {
        if (numFallen == numHorses) {
            System.out.println("All the horses have fallen.");
            return true;
        }
        return false;
    }
    
    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(Horse horse, Graphics2D g2d, int xPos, int yPos)
    {
        
        //if the horse has fallen then print dead
        //else print the horse's symbol
        if(horse.hasFallen())
        {
            g2d.drawString('\u2322' + "", horse.getDistanceTravelled() + xPos, yPos);
        }
        else
        {
            g2d.drawString(horse.getSymbol() + "", horse.getDistanceTravelled() + xPos, yPos);
        }

    }

        /*** 
     * ADDED 31/03/2024 
     * prints an empty lane
     * 
     */
    private void printEmptyLane(Graphics2D g2d, int xPos, int yPos)
    {
        g2d.drawString(" ", xPos, yPos);
    }

    /**
     * adjusts confidence rating of horse
     */
    private Horse adjustConfidence(Horse horse, double amount) {
        horse.setConfidence(horse.getConfidence() + amount);
        return horse;
    }
    
    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     * 
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }

    public String getWinner() {
        if (winner != null) {
            return this.winner;
        } else {
            return "No one won.";
        }
    }

    /***
     * ADDED 10/03/2024
     * gets input from user
     */
    private String inputString(String message) {
        System.out.println(message);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return input;
    }
    
    public Map<String, Horse> read() throws IOException {
        Map<String, Horse> horseInput = new HashMap<>();
        try {
            String filePath = "horses.dat";
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            // Read the object from the file
            Horse horseObj = (Horse) objectIn.readObject();

            while (horseObj != null) {
                horseInput.put(horseObj.getName(), horseObj);
                horseObj = (Horse) objectIn.readObject();
                System.out.println("Read object: " + horseObj.getName());
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
                System.out.println("No file found. New horses.dat created.");
                return horseInput;
        }     
        return horseInput;
    }

    public void save(Map<String, Horse> horseInput) throws IOException {
        try {
            for (Horse horse : horses) {
                if (horse != null) {
                    if (horseInput.containsKey(horse.getName())) {
                        horseInput.replace(horse.getName(), horse);
                    } else {
                        horseInput.put(horse.getName(), horse);
                    }
                }

            }

            FileOutputStream f = new FileOutputStream(new File("horses.dat"));
			ObjectOutputStream o = new ObjectOutputStream(f);

            for (Horse value : horseInput.values()) {
                System.out.println("Written: " + value.getName());
                o.writeObject(value);
            }

			o.close();
			f.close();


        } catch (IOException e) {
                // Handle IO exceptions
                e.printStackTrace();
        }
    }

    
    // set race GUI
    public void setRaceGUI(RaceGUI raceGUI) {
        this.raceGUI = raceGUI;
    }
    /*
    public static void main(String[] args) throws IOException {
        GuiRacePanel race = new GuiRacePanel(90, 5);
        Horse horse3 = new Horse('C', "BETTY", 0.6, null);
        Horse horse4 = new Horse('D', "NIGEL", 0.75, null);
        Horse horse5 = new Horse('E', "PILLOW", 0.7, null);
        race.addHorse(horse3);
        race.addHorse(horse4);
        race.addHorse(horse5);

        // Create a JFrame
        JFrame frame = new JFrame("Moving String Example");
        JPanel trackPanel = new JPanel();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        race.startRace();

        JLabel win = new JLabel(race.getWinner(), SwingConstants.CENTER);
        //frame.add(win);
        trackPanel.add(race);

        // Add the panel to the frame
        frame.add(race);
        //frame.add(trackPanel, BorderLayout.CENTER);

        // Set the frame size
        frame.setSize(800, 600);

        // Make the frame visible
        frame.setVisible(true);
    }*/
}