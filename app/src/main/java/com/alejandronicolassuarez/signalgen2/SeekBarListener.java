package com.alejandronicolassuarez.signalgen2;

import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alejandronicolassuarez.signalgen2.waveForms.Wave;

public class SeekBarListener implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "SeekBarListener";
    private Wave wave;
    private TextView freqTextView;

    public SeekBarListener(Wave wave, TextView view){
        this.wave = wave;
        this.freqTextView = view;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // This maps the progress to an exponential function, so that the slider is not linear
        Log.i(TAG, "onProgressChanged");
        int mappedProgress = (int) Math.ceil(Math.pow(10.0, progress*3.0/20000)*20);
        wave.setFrequency(mappedProgress);
        freqTextView.setText(String.valueOf(mappedProgress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
