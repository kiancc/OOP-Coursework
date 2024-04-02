public class TestBettingSystem {
    public static void main(String[] args) {
        BettingSystem betSystem = new BettingSystem();
        Horse horse1 = new Horse('k', "Roly Poly", 0.7);
        Horse horse2 = new Horse('l', "Testies", 0.7);
        betSystem.placeBet(horse1, 50);
        betSystem.getBets();
        betSystem.placeBet(horse1, 30);
        betSystem.placeBet(horse2, 50);
        betSystem.placeBet(horse1, 60);
        betSystem.getBets();
    }
}