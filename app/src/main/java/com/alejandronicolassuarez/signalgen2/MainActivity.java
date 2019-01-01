package com.alejandronicolassuarez.signalgen2;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alejandronicolassuarez.signalgen2.waveForms.Sine;
import com.alejandronicolassuarez.signalgen2.waveForms.Wave;

import org.w3c.dom.Text;

import java.security.Key;
import java.util.HashMap;
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

        final FreqTextView freqNumber = findViewById(R.id.freqNumber);

        final SeekBar freqBar = findViewById(R.id.freqBar);
        freqBar.setMax(Wave.MAX_FREQ);
        freqBar.setOnSeekBarChangeListener(new SeekBarListener(wave, freqNumber));
        freqBar.setProgress(0);
        freqBar.setProgress(500);

        // Listens for input on the frequency text box
        freqNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        keyCode == KeyEvent.KEYCODE_ENTER){
                    if (freqNumber.getParsedInt() != -1){
                        freqBar.setProgress(freqNumber.getParsedInt());
                    } else {
                        Toast.makeText(MainActivity.this,
                                getResources().getString(R.string.invalid_input), Toast.LENGTH_LONG)
                                .show();
                    }
                    return true;
                }
                return false;
            }
        });

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
