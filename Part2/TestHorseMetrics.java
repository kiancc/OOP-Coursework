class TestHorseMetrics {
    public static void main(String[] args) {
        testConstructor();
        testUpdateMetrics();
    }

    public static void testUpdateMetrics() {
        System.out.println("Testing updateMetrics...");
        HorseMetrics hm = new HorseMetrics();
        System.out.println("Initialised new HorseMetrics.");
        System.out.println("numFalls: " + hm.getFalls());
        System.out.println("numRaces: " + hm.getRaces());
        System.out.println("numWon: " + hm.getRacesWon());
        System.out.println("positionHistory: " + hm.getPositionHistory());
        System.out.println("totalDistance: " + hm.getTotalDistance());
        System.out.println("totalTime: " + hm.getTotalTime());

        System.out.println("Updating Horse metrics with the following args: ");
        int distance = 20;
        int time = 20;
        boolean fall = true;
        boolean won = true;
        int position = 2;
        System.out.println("distance = " + distance + ", fall = " + fall + ", won = " + won + ", time = " + time + ", position = " + position);
        hm.updateMetrics(distance, time, won, fall, position);

        System.out.println("Values afer calling updateMetrics()");
        System.out.print("numFalls expected: 1, Actual: " + hm.getFalls());
        if (hm.getFalls() != 0) {
            System.out.print(" PASS");
        } else {
            System.out.print(" FAIL");
        }
        System.out.println();
        System.out.print("numRaces expected: 1, Actual: " + hm.getRaces());
        if (hm.getRaces() != 0) {
            System.out.print(" PASS");
        } else {
            System.out.print(" FAIL");
        }
        System.out.println();
        System.out.print("numWon expected: 1, Actual: " + hm.getRacesWon());
        if (hm.getRacesWon() != 0) {
            System.out.print(" PASS");
        } else {
            System.out.print(" FAIL");
        }
        System.out.println();
        System.out.print("positionHistory expected: [2], Actual: " + hm.getPositionHistory());
        if (hm.getPositionHistory().size() != 0) {
            System.out.print(" PASS");
        } else {
            System.out.print(" FAIL");
        }
        System.out.println();
        System.out.print("totalDistance expected: 20, Actual: " + hm.getTotalDistance());
        if (hm.getFalls() != 0) {
            System.out.print(" PASS");
        } else {
            System.out.print(" FAIL");
        }
        System.out.println();
        System.out.print("totalTime expected: 20, Actual: " + hm.getTotalTime());
        if (hm.getFalls() != 0) {
            System.out.print(" PASS");
        } else {
            System.out.print(" FAIL");
        }
        System.out.println();
    }

    public static void testConstructor() {
        System.out.println("Testing Constructor...");
        HorseMetrics hm = new HorseMetrics();

        System.out.print("numFalls expected: 0, Actual: " + hm.getFalls());
        if (hm.getFalls() == 0) {
            System.out.print(" PASS");
        } else {
            System.out.print(" FAIL");
        }
        System.out.println();
                
        System.out.print("numRaces expected: 0, Actual: " + hm.getRaces());
        if (hm.getRaces() == 0) {
            System.out.print(" PASS");
        } else {
            System.out.print(" FAIL");
        }
        System.out.println();
                
        System.out.print("numWon expected: 0, Actual: " + hm.getRacesWon());
        if (hm.getRacesWon() == 0) {
            System.out.print(" PASS");
        } else {
            System.out.print(" FAIL");
        }
        System.out.println();
                
        System.out.print("positionHistory expected: [], Actual: " + hm.getPositionHistory());
        if (hm.getPositionHistory().empty()) {
            System.out.print(" PASS");
        } else {
            System.out.print(" FAIL");
        }
        System.out.println();
                
        System.out.print("totalDistance expected: 0, Actual: " + hm.getTotalDistance());
        if (hm.getTotalDistance() == 0) {
            System.out.print(" PASS");
        } else {
            System.out.print(" FAIL");
        }
        System.out.println();

        System.out.print("totalTime expected: 0, Actual: " + hm.getTotalTime());
        if (hm.getTotalTime() == 0) {
            System.out.print(" PASS");
        } else {
            System.out.print(" FAIL");
        }
        System.out.println();
        System.out.println("");
    }
}