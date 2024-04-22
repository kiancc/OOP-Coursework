import java.io.*;
import java.util.*;

public class TestRace {
    public static void main(String[] args) throws IOException  {
        Race race = new Race(20, 20);
        
        Horse horse3 = new Horse('C', "BETTY", 0.6, null);
        Horse horse4 = new Horse('D', "NIGEL", 0.75, null);
        Horse horse5 = new Horse('E', "PILLOW", 0.7, null);
        race.addHorse(horse3, 1);
        race.addHorse(horse4, 2);
        race.addHorse(horse5, 3);
        race.startRace();
        /*
        ArrayList<Horse> horses = readInHorses();
        int i = 1;
        for (Horse horse : horses) {
            race.addHorse(horse, i);
            i++;
        }
        race.startRace(); */
    }

    // tests winner
    public static void testWinner() {
        return;
    }
/*
    public static ArrayList<Horse> readInHorses() throws IOException {
        try {
            BufferedReader bReader = new BufferedReader(new FileReader("horses.txt"));
            String line = bReader.readLine();
            ArrayList<Horse> horses = new ArrayList<>();
            while (line != null) {
                String[] data = line.split(",");
                Horse horse = new Horse(data[0].charAt(0), data[1], Double.parseDouble(data[2]));
                horses.add(horse);
                line = bReader.readLine();
            }
            return horses;
        } catch (IOException e) {
            System.out.println("Cannot find file");
            return null;
        }
    }*/

    public static ArrayList<Horse> readInHorses() throws IOException {
        ArrayList<Horse> horses = new ArrayList<>();
        String filePath = "myObjects.dat";
        try {
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            // Read the object from the file
            Horse horseObj = (Horse) objectIn.readObject();

            while (horseObj != null) {
                horses.add(horseObj);
                horseObj = (Horse) objectIn.readObject();
                System.out.println("Read object: " + horseObj);
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
                    // Handle IO exceptions
                    e.printStackTrace();
            }
            return horses;
        }
}