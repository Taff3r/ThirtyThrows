package se.umu.cs.site0011.thirty;

import android.os.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The dice throwing Game, can use and number of dice and turns.
 */
public class Game implements Serializable {
    private int rerolls;
    private int turn;
    private int turns;
    private Map<Integer, Boolean> selected = new HashMap<>();
    private ScoreKeeper sk;
    private List<Die> dieList= new ArrayList<>();

    /**
     * Constructor
     * @param noOfDice, number of dice used in the game.
     * @param turns, number of turns in the game.
     */
    public Game(int noOfDice, int turns){
        this.sk = new ScoreKeeper(this, noOfDice);
        this.turns = turns;
        this.rerolls = 0;
        for(int i = 0; i < noOfDice; i++){
            dieList.add(i, new Die());
        }

        for(int k = 0; k < noOfDice; k++){
            selected.put(k, false);
        }
    }

    /**
     * Moves on to the next Round
     * @return boolean, true if the game has ended, false if there are more rounds remaining.
     */
    public boolean nextRound(){
        rerolls = 0;
        turn++;
        sk.newTurn();
        return hasEnded();
    }

    /**
     * Returns the current turn.
     * @return the current turn.
     */
    public int getTurn(){
        return turn;
    }

    /**
     * Returns the current score.
     * @return int, the score
     */
    public int getScore(){
        return sk.getScore();
    }

    /**
     * Checks if there are more rolls available.
     * @return true, if there are more reroll, false if not.
     */
    public boolean canReroll() {
        return rerolls < 3;
    }

    /**
     * Selects or unselects a die.
     * @param pos, the position of the die.
     * @param selected, true if selecting, false if delselecting.
     */
    public void select(int pos, boolean selected){
        this.selected.put(pos, selected);
    }

    /**
     * Unselects all the dice.
     */
    public void unselectAll(){
        for(Entry<Integer, Boolean> e : selected.entrySet()) {
            e.setValue(false);
        }
    }

    /**
     * Checks if a die is selected on position pos.
     * @param pos, the position to check.
     * @return true, if the die is selected at position pos.
     */
    public boolean isSelected(int pos){
        return selected.get(pos);
    }

    /**
     * Returns a List of remaining choices.
     * @return List<String> of remaining choices.
     */
    public List<String> remainingChoices(){
        return sk.available();
    }
    /**
     * Tries to add a score.
     * @return String, the choice added if it was, null if it could not be added.
     */
    public String add(String choice){

        if(sk.add(selected, choice)){
            return choice;
        }
        return null;
    }

    /**
     * Rolls all unselected dice.
     */
    public void rollDice(){
        rerolls++;
        for(Entry<Integer, Boolean> e : selected.entrySet()) {
            if (!e.getValue()) {
                dieList.get(e.getKey()).roll();
            }
        }
    }

    /**
     * Returns value of Die at position pos
     * @param pos, position of the die.
     * @return int, the value of the die.
     */
    public int getDieValue(int pos){
        return dieList.get(pos).value();
    }

    /**
     * Checks if the game has ended
     * @return true, if the game has ended, o/w false
     */
    public boolean hasEnded(){
        return turn == turns;
    }


    /**
     * Check if the player can keep(select) dice.
     * @return true if the player can keep (select) dice, false if not.
     */
    public boolean canKeep(){
        return !(this.rerolls == 0);
    }

    /**
     * Returns a String representation of the Game in its current state.
     * @return String represtation of the Game.
     */
    @Override
    public String toString(){
       return sk.toString();
    }
}
