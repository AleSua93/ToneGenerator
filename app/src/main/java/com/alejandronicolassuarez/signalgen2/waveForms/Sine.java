package com.alejandronicolassuarez.signalgen2.waveForms;

public class Sine implements Wave {

    private static final String TAG = "Sine";
    private int frequency = 1000;

    @Override
    public int getFrequency(){
        return frequency;
    }

    @Override
    public void setFrequency(int freq) {
        this.frequency = freq;
    }

    @Override
    public short getNextSample(int index) {
        short sample = (short)(Math.sin(2*Math.PI*frequency*index/Wave.SAMPLE_RATE)*Short.MAX_VALUE);
        return sample;
    }

}
