package com.alejandronicolassuarez.signalgen2;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.alejandronicolassuarez.signalgen2.waveForms.Wave;

public class FreqTextView extends android.support.v7.widget.AppCompatEditText {

    private static final String TAG = "FreqTextView";

    public FreqTextView(Context context) {
        super(context);
    }

    public FreqTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FreqTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // Passes an invalid value of -1 if the string can't be parsed to int
    public int getParsedInt(){
        int parsedInt;
        try {
            parsedInt = Integer.parseInt(this.getText().toString());
        } catch (NumberFormatException nfe){
            parsedInt = -1;
        }

        if (parsedInt < Wave.MIN_FREQ || parsedInt > Wave.MAX_FREQ){
            parsedInt = -1;
        }
        return parsedInt;
    }

}
