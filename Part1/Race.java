import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.util.*;

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
    public void startRace()
    {
        //declare a local variable to tell us when the race is finished
        boolean finished = false;
        
        //reset all the lanes (all horses not fallen and back to 0). 
        resetLanes();
                      
        while (!finished && !hasAllFallen())
        {
            //move horses
            moveHorses();
                        
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

        if (hasAllFallen()) {
            System.out.println("All the horses have fallen.");
        }
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
            if (horse != null && horse.getDistanceTravelled() == raceLength) {
                numFinished++;
                tempWinner = horse.getName();
            }
        }
        if (numFinished == 1 && winner.equals("")) {
            winner = tempWinner;
        }

        if (numFinished == numHorses - numFallen) {
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
    }


    /**
     * Resets horses to beginning of race
     */
    private void resetLanes() {
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
            System.out.print('\u2322');
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

}