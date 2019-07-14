package se.umu.cs.site0011.thirty;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a Turn.
 */
public class Turn implements Serializable {
    private int number;
    private String choice;
    private List<Choice> choices = new ArrayList<>();
    private int turnScore;

    /**
     * Constructor
     * @param turnNumber, the turn number.
     */
    public Turn(int turnNumber){
        this.number = turnNumber;
    }

    /**
     * Adds a score to the turn.
     * @param score, to be added.
     */
    public void addScore(int score){
        this.turnScore += score;
    }

    /**
     * Adds a combination of Dice.
     * @param combo
     */
    public void add(String combo){
        choices.add(new Choice(combo));
    }

    /**
     * Returns the current choice. (LOW, 4, 12, etc.)
     * @return the Turns choice.
     */
    public String getChoice(){
        return choice;
    }

    /**
     * Sets the choice,
     * @param choice, to be set.
     * @return true if the choice could be set. false if not.
     */
    public boolean setChoice(String choice){
        if(this.choice == null){
            this.choice = choice;
            return true;
        }
        return false;
    }

    /**
     * Returns a string representation of the Turn.
     * @return String, representation.
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        String p;
        if(this.choice == null){
            p = "None";
        }else{
            p = this.choice;
        }

        sb.append("Turn #" + this.number + " Used choice: "+ p + "\n");
        for(Choice c: choices){
            sb.append(c.toString());
        }

        sb.append("SCORE FOR TURN: " + this.turnScore + "\n\n");
        return sb.toString();
    }


    /**
     * Private inner class.
     */
    private class Choice implements Serializable{
        private String combo;

        public Choice(String combo){
            this.combo = combo;
        }

        @Override
        public String toString() {
            return combo + "\n";
        }
    }
}
