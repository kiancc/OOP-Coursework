import java.io.*;
import java.util.*;
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
    private HorseMetrics horseMetrics;
      
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
        this.horseMetrics = new HorseMetrics();
    }

    public Horse(char horseSymbol, String horseName, double horseConfidence, HorseMetrics horseMetrics)
    {
        this.symbol = horseSymbol;
        this.name = horseName;
        this.confidence = horseConfidence;
        this.distanceTravelled = 0;
        this.fallen = false;
        if (horseMetrics == null) {
            this.horseMetrics = new HorseMetrics();
        } else {
            this.horseMetrics = horseMetrics;
        }
    }
    
    // updates horse metrics 
    public void updateHMetrics(int position, int raceLength) {
        // note time does not work
        double time = (double)distanceTravelled;
        horseMetrics.updateMetrics(distanceTravelled, time, fallen, position);
    }

    // updates win metrics 
    public void win() {
        horseMetrics.updateWin();
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

    /**
     * mutator methods
     */
    
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
    /*
    public void saveHorseInformation() throws IOException {
        try {
            ArrayList<Object> output = new ArrayList<>();
            BufferedReader bReader = new BufferedReader(new FileReader("horses.txt"));
            String line = bReader.readLine();
            boolean inFile = false;

            while (line != null) {
                String[] data = line.split(",");
                if (data[0].charAt(0) == symbol && data[1].equals(name)) {
                    inFile == true;
                    output.add()
                }
                output.add(new Horse());
            }

            FileInputStream fi = new FileInputStream(new File("myObjects.txt"));
			ObjectInputStream oi = new ObjectInputStream(fi);

            FileOutputStream f = new FileOutputStream(new File("myObjects.txt"));
			ObjectOutputStream o = new ObjectOutputStream(f);

			// Write objects to file
			o.writeObject(this);

            o.close();
			f.close();

        } catch (IOException e) {
        }
    }*/
}
