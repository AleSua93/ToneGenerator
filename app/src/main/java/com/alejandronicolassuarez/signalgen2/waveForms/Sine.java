package com.alejandronicolassuarez.signalgen2.waveForms;

import android.util.Log;

public class Sine implements Wave {

    private static final String TAG = "Sine";
    private int frequency = 0;
    private double phase = 0;

    @Override
    public void setFrequency(int freq) {
        this.frequency = freq;
    }

    @Override
    public short getNextSample(int index) {
        if (phase < 0.0 && (phase + 2 * Math.PI * frequency / Wave.SAMPLE_RATE) > 0.0) {
            phase = 0;
            Log.i(TAG, "Phase reset");
        } else{
            phase += 2 * Math.PI * frequency / Wave.SAMPLE_RATE;
        }
        short sample =(short) (Short.MAX_VALUE * Math.sin(phase));
        return sample;
    }
}
