import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class TrackCustomisation {
    private int raceLength;
    private char trackBoundary;
    private Color backgroundColor;
    private int numLanes;

    TrackCustomisation(int raceLength, char trackBoundary, Color backgroundColor, int numLanes) {
        this.raceLength = raceLength;
        this.trackBoundary = trackBoundary;
        this.backgroundColor = backgroundColor;
        this.numLanes = numLanes;
    }

    public int getNumLanes() {
        return this.numLanes;
    }

    public int getRaceLength() {
        return this.raceLength;
    }

    public char getTrackBoundary() {
        return this.trackBoundary;
    }

    public Color getBackgroundColor() {
        return this.backgroundColor;
    }
}