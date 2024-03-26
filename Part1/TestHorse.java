public class TestHorse {
    public static void main(String args[]) {
        //testClassVariableAccess();
        //testGetter();
        //testSetter();
        //testFall();
        //testConstructor();
        //testMoveForward();
        testGoBackToStart();
    }

    public static void testGoBackToStart() {
        Horse horse = new Horse('K', "Roly Poly", 0.7);
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Testing goBackToStart()...");
        System.out.println("Calling horse.fall()");
        horse.fall();
        System.out.println("Calling horse.moveForward()");
        horse.moveForward();
        System.out.println("Calling horse.moveForward()");
        horse.moveForward();
        System.out.println("Distance travelled: " + horse.getDistanceTravelled() + ", fallen: " + horse.hasFallen());
        System.out.println("Calling goBackToStart...");
        horse.goBackToStart();
        if (horse.getDistanceTravelled() == 0 && horse.hasFallen() == false) {
            System.out.println("Distance travelled: " + horse.getDistanceTravelled() + ", fallen: " + horse.hasFallen());
            System.out.println("goBackToStart() Successful!");
        } else {
            System.out.println("goBackToStart() unsuccessful.");
        }
        System.out.println("-----------------------------------------------------------------");
    }

    public static void testMoveForward() {
        Horse horse = new Horse('K', "Roly Poly", 0.7);
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Testing moveForward...");
        System.out.println("Current distance travelled: " + horse.getDistanceTravelled());
        System.out.println("Calling horse.moveForward()");
        horse.moveForward();
        System.out.println("Calling horse.moveForward()");
        horse.moveForward();
        System.out.println("Calling horse.moveForward()");
        horse.moveForward();
        if (horse.getDistanceTravelled() == 3) {
            System.out.println("New distance travelled: " + horse.getDistanceTravelled());
            System.out.println("moveForward() Successful!");
        } else {
            System.out.println("moveForward() unsuccessful.");
        }
        System.out.println("-----------------------------------------------------------------");
    }

    // tests if the setter methods are correctly working
    public static void testSetter() {
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Testing setter methods...");
        System.out.println();
        Horse horse = new Horse('K', "Roly Poly", 0.7);
        System.out.println("Testing setSymbol()");
        System.out.println("Current Symbol: " + horse.getSymbol());
        System.out.println("Calling horse.setSymbol('X')");
        horse.setSymbol('X');
        System.out.println("New Symbol: " + horse.getSymbol());
        if ('X' == horse.getSymbol()) {
            System.out.println("setSymbol() successful!");
        } else {
            System.out.println("setSymbol() unsuccessful.");
        }
        System.out.println();

        System.out.println("Testing setConfidence()");
        System.out.println("Current Confidence: " + horse.getConfidence());
        System.out.println("Calling horse.setConfidence(0.2)");
        horse.setConfidence(0.2);
        System.out.println("New Confidence: " + horse.getConfidence());
        if (0.2 == horse.getConfidence()) {
            System.out.println("setConfidence() successful!");
        } else {
            System.out.println("setConfidence() unsuccessful.");
        }
        System.out.println("-----------------------------------------------------------------");
    }

    // method for testing constructor
    public static void testConstructor() {
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Testing constructor on the following values, Symbol: K, Name: Roly Poly, Confidence: 0.7");

        Horse horse = new Horse('K', "Roly Poly", 0.7);
        // tests if symbol is successfully initialised
        if ('K' == horse.getSymbol()) {
            System.out.println("Symbol: " + horse.getSymbol() + " successfully initialised!");
        }
        else {
            System.out.println("Symbol unsuccessfully initialised :(");
        }

        // tests if name is successfully initialised
        if ("Roly Poly".equals(horse.getName())) {
            System.out.println("Name: " + horse.getName() + " successfully initialised!");
        }
        else {
            System.out.println("Name unsuccessfully initialised :(");
        }

        // tests if confidence has been successfully initialised
        if (0.7 == horse.getConfidence()) {
            System.out.println("Confidence: " + horse.getConfidence() + " successully initialised!");
        }
        else {
            System.out.println("Confidence unsuccessfully initialised :(");
        }

        // tests if distance has been successfully initialised to 0
        if (0 == horse.getDistanceTravelled()) {
            System.out.println("Distance Travelled: " + horse.getDistanceTravelled() + " successully initialised!");
        }
        else {
            System.out.println("Distance Travelled unsuccessfully initialised :(");
        }

        // tests if fallen has been successfully initialised to false
        if (false == horse.hasFallen()) {
            System.out.println("Fallen: " + horse.hasFallen() + " successully initialised!");
        }
        else {
            System.out.println("Fallen Travelled unsuccessfully initialised :(");
        }
        System.out.println("-----------------------------------------------------------------");
    }

    // tests access to class variables
    /*
    public static void testClassVariableAccess() {
        Horse horse = new Horse('K', "Roly Poly", 0.7);
        // if data is correctly protected i.e. private then all of these statements should through a compiler error
        horse.name = "test";
        horse.symbol = 't';
        horse.confidence = 0.9;
        horse.fallen = true;
        horse.distanceTravelled = 10;
    } */

    // tests fall()
    public static void testFall() {
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Testing fall()");

        Horse horse = new Horse('K', "Roly Poly", 0.7);
        System.out.println("horse initialised, fallen status: " + horse.hasFallen());
        horse.fall();
        System.out.println("Calling fall() on horse instance, fallen status: " + horse.hasFallen());
        if (horse.hasFallen() == true) {
            System.out.println("fall() successful!");
        }
        else {
            System.out.println("fall() unsuccessful :(");
        }

        System.out.println("-----------------------------------------------------------------");
    }
}
