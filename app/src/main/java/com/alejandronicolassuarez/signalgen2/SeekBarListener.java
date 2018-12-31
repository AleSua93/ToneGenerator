package com.alejandronicolassuarez.signalgen2;

import android.util.Log;
import android.widget.SeekBar;

import com.alejandronicolassuarez.signalgen2.waveForms.Wave;

public class SeekBarListener implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "SeekBarListener";
    private Wave wave;

    public SeekBarListener(Wave wave){
        this.wave = wave;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (progress == 0){
            progress = 1;
        }
        Log.i(TAG, "onProgressChanged: " + progress);
        wave.setFrequency(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
