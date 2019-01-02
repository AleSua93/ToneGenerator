package com.alejandronicolassuarez.signalgen2;

import android.widget.SeekBar;
import android.widget.TextView;

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
        wave.setFrequency(progress);
        freqTextView.setText(String.valueOf(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
