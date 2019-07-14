package se.umu.cs.site0011.thirty;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import site0011.thirty.R;

public class MainActivity extends AppCompatActivity {
    private Game game;
    private ArrayAdapter<String> adapter;
    private Spinner spinner;
    private GridLayout gl;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.gl = findViewById(R.id.grid);
        this.spinner = findViewById(R.id.spinner);
        if(savedInstanceState != null){
            this.game = (Game) savedInstanceState.getSerializable("GAME");
        }else{
             this.game = new Game(6, 10);
            for(int i = 0; i < gl.getChildCount(); i++){
                game.select(i, false);
            }
        }
        this.setImages();
        this.refreshSpinner();
        // Sets all dice to be unselected

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("GAME", this.game);
        super.onSaveInstanceState(outState);
    }

    /**
     * Selects a die, to keep or add.
     * @param v
     */
    public void selectDie(View v){
        if(!game.canKeep()){
            showToast("Can't keep before rolling!");
            return;
        }
        int pos = getPositionFromView(v);
        boolean isSelected = game.isSelected(pos);

        if(isSelected){
            game.select(pos, false);
            ((ImageView) gl.getChildAt(pos)).setImageResource(getImage(game.getDieValue(pos), false));
        }else{
            game.select(pos, true);
            ((ImageView) gl.getChildAt(pos)).setImageResource(getImage(game.getDieValue(pos), true));
        }


    }

    /**
     * Tries to add a score.
     * @param v
     */
    public void add(View v){
        if(game.canReroll()){
            showToast("More rolls have to be done!");
            return;
        }
        String added = game.add(spinner.getSelectedItem().toString());
        if(added != null){
            showToast("Used choice: " + added);
        }else {
            showToast("Score couldn't be added!");
        }
        setImagesAfterRoll();
    }

    /**
     * Refreshes the choices in the spinner.
     */
    private void refreshSpinner(){
        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item ,game.remainingChoices());
        this.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(adapter);
    }

    /**
     * Moves on to the next turn, or ends the game.
     * @param v
     */
    public void next(View v){
        if(game.canReroll()){
            showToast("Turn is not done!");
        }
        if(game.nextRound()){
            Intent i = new Intent(this, EndActivity.class);
            //i.putExtra("SCORE", game.getScore());
            i.putExtra("SUMMARY", game.toString());
            startActivity(i);
            return;
        }else{
            showToast("Turn: " + game.getTurn() + "\n Current Score: " + game.getScore());
            this.refreshSpinner();
            game.unselectAll();
            game.rollDice();
            this.setImagesAfterRoll();
        }

    }

    /**
     * Rolls the dice, onClick-method
     * @param v
     */
    public void roll(View v){
        if(game.canReroll()){
            game.rollDice();
            setImagesAfterRoll();
        }else{
            showToast("No more rerolls this turn!");
        }
    }

    /**
     * Sets all the images to white again, as well as unselecting all the die. Also displays a Toast.
     */
    private void setImagesAfterRoll(){
        // Set all selected to false.
        for(int i = 0; i < 6; i++){
            game.select(i, false); // Set all to unselected
            ((ImageView) gl.getChildAt(i)).setImageResource(getImage(game.getDieValue(i), false));
        }

    }

    /**
     * Sets all images of dice to white1 when Activity is created.
     */
    private void setImages(){
        for(int i = 0; i < gl.getChildCount(); i++){
            ImageView tv = (ImageView) gl.getChildAt(i);
            tv.setImageResource(getImage(game.getDieValue(i), game.isSelected(i)));
        }
    }

    /**
     * Translates View to position in GridLayout
     * @param v
     * @return int, position of
     */
    private int getPositionFromView(View v) {
        switch (v.getId()) {
            default:
                return 0;
            case R.id.ivR0C1:
                return 1;
            case R.id.ivR0C2:
                return 2;
            case R.id.ivR1C0:
                return 3;
            case R.id.ivR1C1:
                return 4;
            case R.id.ivR1C2:
                return 5;
        }
    }

    /**
     * Translator
     * @param face
     * @param selected
     * @return int, id of the image to set.
     */
    private int getImage(int face, boolean selected) {
        if (!selected) {
            switch (face) {
                case 1:
                    return R.drawable.white1;
                case 2:
                    return R.drawable.white2;
                case 3:
                    return R.drawable.white3;
                case 4:
                    return R.drawable.white4;
                case 5:
                    return R.drawable.white5;
                default:
                    return R.drawable.white6;
            }
        } else {
            switch (face) {
                case 1:
                    return R.drawable.grey1;
                case 2:
                    return R.drawable.grey2;
                case 3:
                    return R.drawable.grey3;
                case 4:
                    return R.drawable.grey4;
                case 5:
                    return R.drawable.grey5;
                default:
                    return R.drawable.grey6;
            }
        }
    }


    /**
     * Replace current Toast if it exists, and make a new one with the text s
     * @param s, text to be displayed.
     */
    private void showToast(String s){
        if(this.toast != null)
        {
            this.toast.cancel();
        }
        this.toast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        this.toast.show();
    }
}
