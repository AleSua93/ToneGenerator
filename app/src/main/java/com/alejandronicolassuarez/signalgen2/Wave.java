package com.alejandronicolassuarez.signalgen2;

import android.util.Log;

public class Wave{

    private static final String TAG = "Wave";
    public static final int SAMPLE_RATE = 44100;
    public static final int MIN_FREQ = 20;
    public static final int MAX_FREQ = 20000;
    public static final int TYPE_SINE = 0;
    public static final int TYPE_SQUARE = 1;

    private int frequency = 0;
    private int waveForm;

    private double sinePhase = 0;
    private int numberOfSquareHarmonics = 10;
    private double[] phases = new double[numberOfSquareHarmonics];

    public void setFrequency(int freq) {
        this.frequency = freq;
    }

    public int getWaveForm() {
        return waveForm;
    }

    public void setWaveForm(int waveForm) {
        this.waveForm = waveForm;
    }

    public short getNextSample() {
        short sample = 0;

        switch(this.getWaveForm()){
            case Wave.TYPE_SINE:
                sample = (short) (Short.MAX_VALUE*getNextSineSample());
                break;
            case Wave.TYPE_SQUARE:
                sample = getNextSquareSample();
                break;
        }
        return sample;
    }

    private double getNextSineSample(){
        if (sinePhase < 0.0 && (sinePhase + 2 * Math.PI * frequency / Wave.SAMPLE_RATE) > 0.0) {
            sinePhase = 0;
            Log.i(TAG, "Phase reset");
        } else{
            sinePhase += 2 * Math.PI * frequency / Wave.SAMPLE_RATE;
        }
        return Math.sin(sinePhase);
    }

    private short getNextSquareSample(){
        double sample = 0;
        for (int i = 0; i < numberOfSquareHarmonics; i++){
            if (frequency*(2*i+1) >= Wave.MAX_FREQ)
                break;
            phases[i] += 2 * Math.PI * frequency*(2*i+1) / Wave.SAMPLE_RATE;
            sample += Math.sin(phases[i]);
        }
        return (short) (Short.MAX_VALUE/numberOfSquareHarmonics*sample);
    }
}
