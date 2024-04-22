public class TestRace {
    public static void main(String[] args) {
        Race race = new Race(20, 8);
        //race.setLanes(5);
        Horse horse1 = new Horse('A', "ALPHA", 0.8);
        Horse horse2 = new Horse('B', "BETA", 0.4);
        Horse horse3 = new Horse('C', "CHARLIE", 0.6);
        Horse horse4 = new Horse('D', "DELTA", 0.75);
        Horse horse5 = new Horse('E', "ECHO", 0.7);
        race.addHorse(horse1);
        race.addHorse(horse2);
        race.addHorse(horse3);
        race.addHorse(horse4);
        race.addHorse(horse5);
        race.startRace();
    }

    // tests winner
    public static void testWinner() {
        return;
    }

    // tests creation of horses
    public static void testCreateHorses() {
        Horse horse1 = new Horse('A', "ALPHA", 0.8);
        Horse horse2 = new Horse('B', "BETA", 0.4);
        Horse horse3 = new Horse('C', "CHARLIE", 0.6);
    }
}
