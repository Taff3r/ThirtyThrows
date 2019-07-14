package se.umu.cs.site0011.thirty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import site0011.thirty.R;


public class EndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        String summary = getIntent().getStringExtra("SUMMARY");
        TextView tv = findViewById(R.id.tvSummary);
        tv.setMovementMethod(new ScrollingMovementMethod());
        tv.setText(summary);
    }

    /**
     * Starts a new game.
     * @param v
     */
    public void newGame(View v){
        startActivity(new Intent(this, MainActivity.class));
    }
}
