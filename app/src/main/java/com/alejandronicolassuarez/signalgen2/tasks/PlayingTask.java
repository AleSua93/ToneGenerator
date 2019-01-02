package com.alejandronicolassuarez.signalgen2.tasks;

import android.media.AudioTrack;
import android.util.Log;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class PlayingTask implements Runnable{

    private static final String TAG = "PlayingTask";
    private int bufferSize = 512;
    private AudioTrack audioTrack;
    private BlockingQueue<Short> blockingQueue;
    private boolean read;

    public PlayingTask(AudioTrack audioTrack, BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
        this.audioTrack = audioTrack;
    }


    @Override
    public void run() {
        read = true;
        try {
            while(read) {
                short[] samples = new short[bufferSize];
                for (int i = 0; i < bufferSize; i++){
                    samples[i] = blockingQueue.take();
                    if(samples[i] == GeneratorTask.EOF) {
                        Log.i(TAG, "EOF received");
                        read = false;
                        audioTrack.pause();
                        break;
                    }

                }
                audioTrack.write(samples, 0, bufferSize);
                audioTrack.play();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
