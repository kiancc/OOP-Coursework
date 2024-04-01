import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Write a description of class Horse here.
 * 
 * @author (Kian Christian Chong) 
 * @version (10/03/2024 Version 1)
 */
public class Horse
{
    //Fields of class Horse
    private String name;
    private char symbol;
    private int distanceTravelled;
    private boolean fallen;
    private double confidence;
    private HorseMetrics metrics;
      
    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
        this.symbol = horseSymbol;
        this.name = horseName;
        this.confidence = horseConfidence;
        this.distanceTravelled = 0;
        this.fallen = false;
        this.metrics = new HorseMetrics();
    }
    
    //Other methods of class Horse
    public void fall()
    {
        this.fallen = true;
    }
    
    /**
     * getter methods for fields
     */
    public double getConfidence()
    {
        return confidence;
    }
    
    public int getDistanceTravelled()
    {
        return distanceTravelled;
    }
    
    public String getName()
    {
        return name;
    }

    public boolean hasFallen()
    {
        return fallen;
    }
    
    public char getSymbol()
    {
        return symbol;
    }

    // added 01/04/2024
    public void displayMetrics() {
        System.out.println("Races Won: " + metrics.getRacesWon());
        System.out.println("Average Finishing Position: " + metrics.getAvgFinishPosition());
        System.out.println("Position History: " + metrics.getPositionHistory());
    }

    /**
     * mutator methods
     */

    // Added 01/04/2024
    // updates horse metrics 
    public void updateHMetrics(int position, int raceLength) {
        // note time does not work
        double time = (double)distanceTravelled;
        metrics.updateMetrics(distanceTravelled, time, fallen, position);
    }

    // Added 01/04/2024
    // updates win metrics 
    public void win() {
        metrics.updateWin();
    }
    
    public void goBackToStart()
    {
        this.distanceTravelled = 0;
        this.fallen = false;
    }

    public void moveForward()
    {
        this.distanceTravelled++;
    }

    public void setConfidence(double newConfidence)
    {
        if (newConfidence > 1 || newConfidence < 0) 
        {
            System.out.println("Confidence has to be between 0 and 1.");
        } 
        else 
        {
            this.confidence = newConfidence;
        }
    }
    
    public void setSymbol(char newSymbol)
    {
        this.symbol = newSymbol;
    }

    /**
     * IO methods
     */

     // reads in saved Horse
    public Horse loadHorse() {
        try (BufferedReader reader = new BufferedReader(new FileReader("levelSave.txt"))) {
            String line;
            String name = "";
            char symbol = '\0';
            double confidence = 0;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                if (i == 0) {
                    symbol = line.charAt(0);
                }
                if (i == 1) {
                    name = line;
                }
                if (i == 2) {
                    confidence = Double.parseDouble(line);
                }
            }
            return new Horse(symbol, name, confidence);
        } catch (IOException e) {
            System.err.println("File does not exist, initialising new level: " + e.getMessage());
        }
        return null;
    }
}
