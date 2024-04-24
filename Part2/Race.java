import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.util.*;
import java.io.*;

interface RaceListener {
    void onRaceUpdate();
    void onRaceFinished();
}
/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author McFarewell
 * @version 1.0
 */
public class Race
{
    private int raceLength;
    private ArrayList<Horse> horses;
    private int numLanes;
    private int numHorses;
    private int numFallen;
    private String winner;
    private List<RaceListener> listeners = new ArrayList<>();
    private int numFinished;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance, int numLanes)
    {
        // initialise instance variables
        this.raceLength = distance;
        this.numLanes = numLanes;
        this.horses = new ArrayList<Horse>();
        this.winner = "";
    }
    
    /**
     * Adds a horse to the race in a given lane
     * 10/03/2024 edited the method to 
     * 
     * @param theHorse the horse to be added to the race
     * 
     */
    public void addHorse(Horse theHorse)
    {
        if (numHorses == numLanes) {
            System.out.println("The race is full! No more horses can be added.");
        } else {
            this.horses.add(theHorse);
            numHorses++;
        }
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
                      
        while (!finished && !hasAllFallen())
        {
            //move horses
            moveHorses();

            // Notify listeners of the race update
            for (RaceListener listener : listeners) {
                listener.onRaceUpdate();
            }
                        
            //print the race positions
            printRace();
            
            // checks if all the horses have finished
            if (checkAllFinished())
            {
                raceWonBy();
                finished = true;
            }
           
            //wait for 100 milliseconds
            try{ 
                TimeUnit.MILLISECONDS.sleep(100);
            }catch(Exception e){}
        }

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
        for (Horse horse : horses) {
            if (horse != null && horse.getDistanceTravelled() == raceLength && horse.getFinished() == false) {
                horse.setFinished();
                horse = updateHorseMetrics(horse, numFinished+1);
                numFinished++;
                if (numFinished - numFallen == 1) {
                    this.winner = horse.getName();
                }
            }
        }

        if (numFinished == numHorses) {
            return true;
        }
        return false;
    }

    /**
     * Prints Information on whos won
     */
    private void raceWonBy()
    {
        for (Horse horse : horses) {
            if (horse.getName().equals(winner)) {
                // added winning message with horses name
                horse = adjustConfidence(horse, 0.01);
                System.out.println("And the winner is " + horse.getName() + " Confidence: " + horse.getConfidence());
            }
        }

        // Notify listeners that the race is finished
        for (RaceListener listener : listeners) {
            listener.onRaceFinished();
        }
    }

    private Horse updateHorseMetrics(Horse horse, int position) {
        horse.updateHMetrics(position, raceLength);
        return horse;
    }



    /**
     * Resets horses to beginning of race
     */
    private void resetLanes() {
        this.numFinished = 0;
        this.numFallen = 0;
        this.winner = "";
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
               return;
            }
            //the probability that the horse will fall is very small (max is 0.1)
            //but will also will depends exponentially on confidence 
            //so if you double the confidence, the probability that it will fall is *2
            if (Math.random() < (0.1*theHorse.getConfidence()*theHorse.getConfidence()))
            {
                theHorse.fall();
                // ADDED 10/03/2024 decreases horses confidence by an arbitrary amount
                theHorse = adjustConfidence(theHorse, -0.01);
                numFinished++;
                numFallen++;
                return;
            }
        }

        return;
    }
    
    
    /***
     * Print the race on the terminal
     * 10/03/2024 edited to loop through horses and print lane
     */
    private void printRace()
    {
        System.out.print('\u000C');  //clear the terminal window
        
        multiplePrint('=',raceLength+3); //top edge of track
        System.out.println();

        // iterates through horses and prints the lane
        for (int i = 0; i < numLanes; i++) {
            if (i < horses.size() && horses.get(i) != null) {
                printLane(horses.get(i));
                        // prints the horses information
                System.out.print(" " + horses.get(i).getName() + " (Current confidence " + horses.get(i).getConfidence() + ")");
            } else {
                printEmptyLane();
            }
            System.out.println();
        }
        
        multiplePrint('=',raceLength+3); //bottom edge of track
        System.out.println();    
    }

    /**
     * checks if all the horses have fallen in the race
     */
    private boolean hasAllFallen() {
        if (numFallen == numHorses) {
            System.out.println("All the horses have fallen.");
            // Notify listeners that the race is finished
            for (RaceListener listener : listeners) {
                listener.onRaceFinished();
            }
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
    private void printLane(Horse theHorse)
    {
        //calculate how many spaces are needed before
        //and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();
        
        //print a | for the beginning of the lane
        System.out.print('|');
        
        //print the spaces before the horse
        multiplePrint(' ',spacesBefore);
        
        //if the horse has fallen then print dead
        //else print the horse's symbol
        if(theHorse.hasFallen())
        {
            System.out.print('\u274C');
            //\u274C
        }
        else
        {
            System.out.print(theHorse.getSymbol());
        }
        
        //print the spaces after the horse
        multiplePrint(' ',spacesAfter);
        
        //print the | for the end of the track
        System.out.print('|');

    }

        /*** 
     * ADDED 31/03/2024 
     * prints an empty lane
     * 
     */
    private void printEmptyLane()
    {
        //print a | for the beginning of the lane
        System.out.print('|');
        
        //print the spaces of the race length
        multiplePrint(' ', raceLength + 1);
        
        //print the | for the end of the track
        System.out.print('|');

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

    public int getNumLanes() {
        return this.numLanes;
    }

    public int getRaceLength() {
        return this.raceLength;
    }

    public ArrayList<Horse> getHorses() {
        return this.horses;
    }

    public void addRaceListener(RaceListener listener) {
        listeners.add(listener);
    }

    public void updateRaceLength(int raceLength) {
        this.raceLength = raceLength;
    }

    public void updateNumLanes(int numLanes) {
        this.numLanes = numLanes;
    }

    public String getWinner() {
        return this.winner;
    }

    /**
     * File IO
     */

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

}