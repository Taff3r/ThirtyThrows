package se.umu.cs.site0011.thirty;

import java.io.Serializable;
import java.util.Random;

public class Die implements Serializable {

    private int currNum = 1; // Default value, since 0 isn't a die face.

    /**
     * Rolls the die, giving it a random number between 1 and 6.
     * @return int, the number that was randomly generated.
     */
    public int roll(){
        Random r = new Random();
        int res = r.nextInt(6) + 1;
        currNum = res;
        return res;
    }

    /**
     * Returns the value of the Die.
     * @return int, value of the Die.
     */
    public int value(){
        return currNum;
    }
}
