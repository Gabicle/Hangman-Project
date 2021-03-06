package com.example.hangman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class LetterAdapter extends BaseAdapter {
    private String[] letters;
    private LayoutInflater letterInf;

    public LetterAdapter(Context context) {
        letters=new String[26];
        for (int a = 0; a < letters.length; a++) {
            letters[a] = "" + (char)(a+'A');
        }
        letterInf = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return letters.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //create a button for the letter at this position in the alphabet
        Button letterBtn;
        //inflate the button layout
        if (view == null) letterBtn = (Button) letterInf.inflate(R.layout.letter, viewGroup, false);
        else {
            letterBtn = (Button) view;
        }
        //set the text to this letter
        letterBtn.setText(letters[i]);
        return letterBtn;
    }
}
