package com.alejandronicolassuarez.signalgen2.waveForms;

public interface Wave {

    static final int SAMPLE_RATE = 44100;

    void setFrequency(int freq);
    int getFrequency();
    short getNextSample(int index);
}
