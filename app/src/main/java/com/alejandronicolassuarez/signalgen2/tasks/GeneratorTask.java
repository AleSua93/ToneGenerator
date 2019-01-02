package com.alejandronicolassuarez.signalgen2.tasks;

import com.alejandronicolassuarez.signalgen2.Wave;

import java.util.concurrent.BlockingQueue;

public class GeneratorTask implements Runnable {

    private BlockingQueue<Short> blockingQueue;
    private Wave wave;
    private boolean generating;
    // This is passed to the playing task when it's time to stop. Because of the fact that short
    // values are codified with two's complement, the maximum negative value is never assigned to
    // the actual samples (since they are scaled using Short.MAX_VALUE as their maximum value)
    final static Short EOF = Short.MIN_VALUE;

    public GeneratorTask(BlockingQueue<Short> blockingQueue, Wave wave) {
        this.blockingQueue = blockingQueue;
        this.wave = wave;
        this.generating = false;
    }

    public void stop(){
        generating = false;
    }

    public boolean isGenerating() {
        return this.generating;
    }

    @Override
    public void run() {
        generating = true;
        try{
            short sample;
            while(generating){
                sample = wave.getNextSample();
                // Put waits until a slot is free
                blockingQueue.put(sample);
            }
            blockingQueue.put(EOF);
        } catch (InterruptedException ie){
            ie.printStackTrace();
        }


    }
}
