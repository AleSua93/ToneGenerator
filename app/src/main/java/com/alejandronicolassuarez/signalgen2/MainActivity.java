package com.alejandronicolassuarez.signalgen2;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import com.alejandronicolassuarez.signalgen2.waveForms.Sine;
import com.alejandronicolassuarez.signalgen2.waveForms.Wave;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private BlockingQueue<Short> blockingQueue = new LinkedBlockingDeque<>(10);
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    private int bufferSize = 512;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Wave wave = new Sine();
        wave.setFrequency(1000);

        SeekBar freqBar = findViewById(R.id.freqBar);
        freqBar.setMax(10000);
        freqBar.setOnSeekBarChangeListener(new SeekBarListener(wave));

        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                Wave.SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, 2*bufferSize,
                AudioTrack.MODE_STREAM);

        Runnable producerTask = new Runnable(){
            @Override
            public void run() {
                try{
                    int index = 0;
                    short sample;
                    while(true){
                        sample = wave.getNextSample(index);
                        // Put waits until a slot is free
                        blockingQueue.put(sample);

                        index++;
                    }
                } catch (InterruptedException ie){
                    ie.printStackTrace();
                }
            }
        };
        Runnable consumerTask = new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        short[] samples = new short[bufferSize];
                        for (int i = 0; i < bufferSize; i++){
                            samples[i] = blockingQueue.take();
                        }
                        audioTrack.write(samples, 0, 512);
                        audioTrack.play();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        executorService.execute(producerTask);
        executorService.execute(consumerTask);
        executorService.shutdown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
