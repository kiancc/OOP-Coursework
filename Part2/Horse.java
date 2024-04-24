import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Write a description of class Horse here.
 * 
 * @author (Kian Christian Chong) 
 * @version (10/03/2024 Version 1)
 */
public class Horse implements Serializable 
{
    //Fields of class Horse
    private String name;
    private char symbol;
    private int distanceTravelled;
    private boolean fallen;
    private double confidence;
    private HorseMetrics metrics;
    private Color color;
    private boolean finished;
      
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
        this.color = new Color(255, 255, 255);
        this.finished = false;
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

    public Boolean getFinished() {
        return this.finished;
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
        this.finished = false;
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

    public void setColor(Color color) {
        this.color = color;
    }

    public void setFinished() {
        this.finished = true;
    }

    /**
     * IO methods
     */


}
