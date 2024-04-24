
import java.util.*;
import java.io.Serializable;
import java.io.*;

public class HorseMetrics implements Serializable  {
    private int totalDistance;
    private double totalTime;
    private int numWon;
    private int numFalls;
    private int numRaces;
    private Stack<Integer> positionHistory;

    // default constructor
    HorseMetrics() {
        this.totalDistance = 0;
        this.totalTime = 0;
        this.numWon = 0;
        this.numFalls = 0;
        this.numRaces = 0;
        this.positionHistory = new Stack<Integer>();
    }

    // setter methods

    /*
     * updates horse metrics afer a race
     */
    public void updateMetrics(int distance, double time, boolean fall, int position) {
        totalDistance += distance;
        totalTime += time;
        if (fall) {
            numFalls++;
        }
        positionHistory.push(position);
        numRaces++;
    }

    /*
     * updates the number of wins if the horse wins
     */
    public void updateWin() {
        numWon++;
    }

    // getter methods

    /*
     * returns the total distance
     */
    public int getTotalDistance() {
        return this.totalDistance;
    }

        /*
     * returns the total time
     */
    public double getTotalTime() {
        return this.totalTime;
    }

    /*
     * returns the number of races won
     */
    public int getRacesWon() {
        return this.numWon;
    }
    

    /*
     *  returns the number of falls
     */
    public int getFalls() {
        return this.numFalls;
    }

    /*
     * returns the number of races participated in
     */
    public int getRaces() {
        return this.numRaces;
    }

    /*
     * returns the average finish position
     */
    public double getAvgFinishPosition() {
        int totalPos = 0;
        Iterator<Integer> iter = positionHistory.iterator();
        if (iter.hasNext()) {
            while (iter.hasNext()) {
                totalPos += iter.next();
            }
            return (double)totalPos/(double)positionHistory.size();
        }
        return 0;
    }

    /*
     * returns the average speed
     */
    public double getAverageSpeed() {
        double speed = (double)totalDistance/(double)totalTime; 
        return speed;
    }

    /*
     * returns the stack of positionHistory
     */
    public Stack<Integer> getPositionHistory() {
        return this.positionHistory;
    }


}