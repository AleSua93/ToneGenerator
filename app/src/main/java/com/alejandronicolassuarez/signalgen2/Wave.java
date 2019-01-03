package com.alejandronicolassuarez.signalgen2;

import android.util.Log;

public class Wave{

    private static final String TAG = "Wave";
    public static final int SAMPLE_RATE = 44100;
    public static final int MIN_FREQ = 20;
    public static final int MAX_FREQ = 20000;
    public static final int TYPE_SINE = 0;
    public static final int TYPE_SQUARE = 1;
    public static final int TYPE_SAWTOOTH = 2;

    private int frequency = 0;
    private int waveForm;

    private double sinePhase = 0;
    private int numberOfSquareHarmonics = 10;
    private double[] squarePhases = new double[numberOfSquareHarmonics];
    private int numberofSawHarmonics = 10;
    private double[] sawPhases = new double[numberofSawHarmonics];

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
            case Wave.TYPE_SAWTOOTH:
                sample = getNextSawSample();
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
            squarePhases[i] += 2 * Math.PI * frequency*(2*i+1) / Wave.SAMPLE_RATE;

            sample += Math.sin(squarePhases[i])/(2*i+1);
        }

        return (short) (Short.MAX_VALUE/2*sample);
    }

    private short getNextSawSample(){
        double sample = 0;
        double amplitude = 1/2 - 1/Math.PI;
        for (int i = 0; i < numberofSawHarmonics; i++){
            if (frequency * (i+1) >= Wave.MAX_FREQ)
                break;
            sawPhases[i] += 2 * Math.PI * frequency* (i+1) / Wave.SAMPLE_RATE;
            sample += Math.pow(-1, i+1) * Math.sin(sawPhases[i])/(i+1);
        }
        return (short) (Short.MAX_VALUE*amplitude*sample);
    }
}
