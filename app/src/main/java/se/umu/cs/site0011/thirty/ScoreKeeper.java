package se.umu.cs.site0011.thirty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Class for keeping the score.
 */
public class ScoreKeeper implements Serializable {
    private int score;
    private Map<Integer, Boolean> usedDice = new HashMap<>();
    private List<String> choices = choices();
    private Game game;
    private List<Turn> turnList = new ArrayList<>();
    private Turn currentTurn;

    /**
     * Constructor
     * @param keepFor, reference for the game.
     * @param noOfDice, number of dice used.
     */
    public ScoreKeeper(Game keepFor, int noOfDice) {
        this.game = keepFor;
        for (int i = 0; i < noOfDice; i++) {
            usedDice.put(i, false);
        }

        for (int i = 0; i < noOfDice; i++) {

        }
    }

    /**
     * Returns the current score.
     * @return int, current score.
     */
    public int getScore(){
        return score;
    }


    /**
     * Tries to add choice.
     * @param selected, the dice map.
     * @param choice, the choice (low, 4, 5, etc.)
     * @return true, if successful, else false.
     */
    public boolean add(Map<Integer, Boolean> selected, String choice){

        // Check if it is a new Turn
        if(this.currentTurn == null){
           this.currentTurn = new Turn(game.getTurn() + 1);
           this.currentTurn.setChoice(choice);

           // If not new turn same choice as last time?
        }else if(!currentTurn.getChoice().equals(choice)){
            return false;
        }

        if(diceHasBeenUsed(selected)){
            return false;
        }

        // Special case for LOW
        if(choice.equals("LOW")){
            for(Entry<Integer, Boolean> entry: selected.entrySet()){
                if(entry.getValue()){
                    if(game.getDieValue(entry.getKey()) > 3){
                        return false;
                    }
                }
            }
            addDiceAsUsed(selected);
            int s = sum(selected);
            this.currentTurn.addScore(s);
            this.score = score + s;
            return true;
        }

        if(!choice.equals("" + sum(selected))){
            return false;
        }

        if(choices.contains(choice)){
            addDiceAsUsed(selected);
            int s = sum(selected);
            this.score += s;
            this.currentTurn.addScore(s);
            return true;
        }
        return false;
    }

    /*
     * Sums the die in the map
     */
    private int sum(Map<Integer, Boolean> selected){
        int sum = 0;
        for(Entry<Integer, Boolean> e: selected.entrySet()){
            if(e.getValue()){
                sum += game.getDieValue(e.getKey());
            }
        }
        return sum;
    }

    private void addDiceAsUsed(Map<Integer, Boolean> selected){
        Map<Integer, Integer> addedAsUsed = new HashMap<>();
        for(Entry<Integer, Boolean> entry: selected.entrySet()){
            if(entry.getValue()){
                usedDice.put(entry.getKey(), true);
                addedAsUsed.put(entry.getKey(), game.getDieValue(entry.getKey()));
            }
        }

        addChoiceToTurn(addedAsUsed);
    }

    private void addChoiceToTurn(Map<Integer, Integer> addedDice){
        StringBuilder diceUsed = new StringBuilder("Di(c)e used: \n");
        for(Entry<Integer, Integer> e: addedDice.entrySet()){
            diceUsed.append("#" + (e.getKey() + 1) + " (" + e.getValue() + ")");
        }
        currentTurn.add(diceUsed.toString());

    }

    /**
     * Resets usedDice when new turn has been started.
     */
    public void newTurn(){
        for(Entry<Integer, Boolean> e: usedDice.entrySet()){
            e.setValue(false);
        }
        if(this.currentTurn != null){
            this.remove(this.currentTurn.getChoice());
            this.turnList.add(this.currentTurn);
        }else{
            this.turnList.add(new Turn(game.getTurn()));
        }

        this.currentTurn = null;
    }

    private boolean diceHasBeenUsed(Map<Integer, Boolean> selected){
        for (Entry<Integer, Boolean> e: selected.entrySet()) {
            if(e.getValue()){
                if(this.usedDice.get(e.getKey())){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Removes a choice from the list of choices.
     * @param choice, the choice to be removed.
     */
    private void remove(String choice){
        for (int i = 0; i < choices.size(); i++) {
            if(choices.get(i).equals(choice)){
                choices.remove(i);
            }
        }
    }

    /**
     * Retruns the available choices.
     * @return List<String> available choices.
     */
    public List<String> available(){
        return choices;
    }
    /**
     * Makes the choice list.
     * @return the default choice list.
     */
    private ArrayList<String> choices(){
        ArrayList<String> ret = new ArrayList<>();
        ret.add("LOW");
        for(int i = 4; i <= 12; i++){
            ret.add("" + i);
        }
        return ret;
    }

    /**
     * String representation of the ScoreKeeper
     * @return String, representation.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Turn t: turnList){
            sb.append(t.toString());
        }

        sb.append("\n TOTAL SCORE: " + this.score);
        return sb.toString();
    }
}
