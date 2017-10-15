package simple_equality_problem;
import java.util.Random;

/**
 * Created by A Appelqvist
 */

public class Util {
    private static Random rnd;

    public static void init(){
        rnd = new Random();
    }

    public static int randInt(int from, int to){
        return rnd.nextInt(to+1)+from;
    }

    public static float randFloat(){
        return rnd.nextFloat();
    }
}
