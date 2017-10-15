package simple_equality_problem;

/**
 * Created by A Appelqvist. 
 */

public class Main {

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        Util.init();
        GA genetic_a = new GA(6,0.25f,0.1f);
        genetic_a.calc();
    }
}

