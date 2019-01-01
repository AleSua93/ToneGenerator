package com.alejandronicolassuarez.signalgen2.waveForms;

public interface Wave {

    static final int SAMPLE_RATE = 44100;
    static final int MIN_FREQ = 20;
    static final int MAX_FREQ = 20000;

    void setFrequency(int freq);
    short getNextSample(int index);
}
