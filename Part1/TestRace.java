public class TestRace {
    public static void main(String[] args) {
        Race race = new Race(20, 5);
        Horse horse1 = new Horse('A', "ALPHA", 0.1);
        Horse horse2 = new Horse('B', "BETA", 0.1);
        Horse horse3 = new Horse('C', "CHARLIE", 0.1);
        Horse horse4 = new Horse('D', "DELTA", 0.75);
        Horse horse5 = new Horse('E', "ECHO", 0.7);
        //testAddHorses(race);
        horse1.fall();
        horse2.fall();
        horse3.fall();
        race.addHorse(horse1);
        race.addHorse(horse2);
        race.addHorse(horse3);

        //race.addHorse(horse4);
        //race.addHorse(horse5);
        race.startRace();
    }

    public static void testAddHorses(Race race) {
        System.out.println("Testing adding horses to Horse ArrayList with 8 Lanes");
        for (int i = 0; i < 8; i++) {
            System.out.println("Adding New Horse. Symbol: " + i + ", Name: " + i + ", Confidednce: " + 0.5);
            race.addHorse(new Horse((char)(i+'0'), i + "", 0.5));
        }
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
