import java.util.HashMap;

public class BettingSystem {

    HashMap<Horse, Integer> bets = new HashMap<>();

    public void placeBet(Horse horse, int amount) {
        if (!bets.containsKey(horse)) {
            bets.put(horse, amount);
        } else {
            bets.replace(horse, bets.get(horse), bets.get(horse) + amount);
        }
    }

    public void showOdds() {
        return;
    }

    public void getBets() {
        for (Horse horse : bets.keySet()) {
            System.out.println("Horse: " + horse.getName() + ", Total Bets: " + bets.get(horse));
        }
    }

    public int getPayout() {
        
        return 0;
    }

    
}