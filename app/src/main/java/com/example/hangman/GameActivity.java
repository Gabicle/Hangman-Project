package com.example.hangman;

import static android.view.ViewGroup.*;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private String[] words;
    private Random randomWord;
    private String currentWord;
    private LinearLayout wordLayout;
    private TextView[] charViews;
    private GridView letters;
    private LetterAdapter letterAdapter;
    //body part images
    private ImageView[] bodyParts;
    //number of body parts
    private int numParts=6;
    //current part - increments with wrong answers
    private int currPart;
    //number of characters in current word
    private int numChars;
    //number correctly guessed
    private int numCorr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


//        to read the collection of words
        Resources res = getResources();
        words = res.getStringArray(R.array.words);

        randomWord = new Random();
        currentWord = "";

        wordLayout = findViewById(R.id.word);

        letters = findViewById(R.id.letters);


        bodyParts = new ImageView[numParts];
        bodyParts[0] = findViewById(R.id.head);
        bodyParts[1] = findViewById(R.id.body);
        bodyParts[2] = findViewById(R.id.arm1);
        bodyParts[3] = findViewById(R.id.arm2);
        bodyParts[4] = findViewById(R.id.leg1);
        bodyParts[5] = findViewById(R.id.leg2);

        playGame();
    }

//    Game helper method
    private void playGame(){

        String newWord = words[randomWord.nextInt(words.length)];

        //prevents picking same word twice
        while(newWord.equals(currentWord)){
            newWord = words[randomWord.nextInt(words.length)];
        }

        currentWord = newWord.toUpperCase();

//        stores textview letters for target word
        charViews = new TextView[currentWord.length()];
        wordLayout.removeAllViews();

        for (int charc=0; charc < currentWord.length(); charc++){
            charViews[charc] = new TextView(this);
            charViews[charc].setText(""+currentWord.charAt(charc));
            charViews[charc].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            charViews[charc].setGravity(Gravity.CENTER);
            charViews[charc].setTextColor(Color.WHITE);
            charViews[charc].setBackgroundResource(R.drawable.letter_background);
            //add to layout
            wordLayout.addView(charViews[charc]);
        }

        letterAdapter = new LetterAdapter(this);
        letters.setAdapter(letterAdapter);

        currPart=0;
        numChars=currentWord.length();
        numCorr=0;

        for(int p = 0; p < numParts; p++) {
            bodyParts[p].setVisibility(View.INVISIBLE);
        }

    }

    public void letterClicked(View view){
        String letter = ((TextView)view).getText().toString();
        char letterCharacterOriginal = letter.charAt(0);
        char letterCharacter = Character.toUpperCase(letterCharacterOriginal);
        view.setEnabled(false);
        view.setBackgroundResource(R.drawable.letter_selectedstate);

        boolean isCorrect = false;
        for (int i=0; i<currentWord.length(); i++){
            if(currentWord.charAt(i) == letterCharacter){
                isCorrect = true;
                numCorr++;
                charViews[i].setTextColor(Color.BLACK);
            }

        }

        if(isCorrect){
            if (numCorr == numChars){
                    disableButtons();

                AlertDialog.Builder winalert = new AlertDialog.Builder(this);
                winalert.setTitle("HOORAY!");
                winalert.setMessage("You Win!\n\n The word was: \n\n" + currentWord);
                winalert.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GameActivity.this.playGame();
                    }
                });

                winalert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       GameActivity.this.finish();
                    }
                });
                winalert.show();

            }
        } else if (currPart < numParts){
            bodyParts[currPart].setVisibility(View.VISIBLE);
            currPart++;
        } else{
            disableButtons();

            AlertDialog.Builder loseAlert = new AlertDialog.Builder(this);
            loseAlert.setTitle("Game Over");
            loseAlert.setMessage("You lose! \n\n The answer was: \n\n" + currentWord);
            loseAlert.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    GameActivity.this.playGame();
                }
            });

            loseAlert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    GameActivity.this.finish();
                }
            });

            loseAlert.show();
        }
    }

    public void disableButtons(){
        int noOfLetters = letters.getChildCount();
        for(int i=0; i<noOfLetters; i++){
            letters.getChildAt(i).setEnabled(false);
        }
    }
}
