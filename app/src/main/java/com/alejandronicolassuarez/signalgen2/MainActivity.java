package com.alejandronicolassuarez.signalgen2;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alejandronicolassuarez.signalgen2.tasks.GeneratorTask;
import com.alejandronicolassuarez.signalgen2.tasks.PlayingTask;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private BlockingQueue<Short> blockingQueue = new LinkedBlockingDeque<>(10);
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    private int bufferSize = 512;
    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Wave wave = new Wave();
        wave.setWaveForm(Wave.TYPE_SINE);

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


        // The generation and reproduction of samples is achieved using a producer-consumer pattern.
        // The first task is to generate the samples and store them in a queue
        final GeneratorTask generatorTask = new GeneratorTask(blockingQueue, wave);
        // The second task is to get samples from the queue and insert them into the audio buffer
        // in chunks of 512 samples, to be reproduced
        final PlayingTask playingTask = new PlayingTask(audioTrack, blockingQueue);

        playButton = findViewById(R.id.start_stop_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (generatorTask.isGenerating()) {
                    generatorTask.stop();
                    playButton.setText(R.string.play_button_text);
                } else{
                    executorService.execute(generatorTask);
                    executorService.execute(playingTask);
                    playButton.setText(R.string.stop_button_text);
                }

            }
        });

        final ImageButton sineButton = findViewById(R.id.sineButton);
        final ImageButton squareButton = findViewById(R.id.squareButton);
        final ImageButton sawButton = findViewById(R.id.sawtoothButton);

        sineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wave.setWaveForm(Wave.TYPE_SINE);
                sineButton.setImageDrawable(getResources().getDrawable(R.drawable.sine_selected));
                squareButton.setImageDrawable(getResources().getDrawable(R.drawable.square));
                sawButton.setImageDrawable(getResources().getDrawable(R.drawable.sawtooth));
            }
        });

        squareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wave.setWaveForm(Wave.TYPE_SQUARE);
                sineButton.setImageDrawable(getResources().getDrawable(R.drawable.sine));
                squareButton.setImageDrawable(getResources().getDrawable(R.drawable.square_selected));
                sawButton.setImageDrawable(getResources().getDrawable(R.drawable.sawtooth));
            }
        });

        sawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wave.setWaveForm(Wave.TYPE_SAWTOOTH);
                sineButton.setImageDrawable(getResources().getDrawable(R.drawable.sine));
                squareButton.setImageDrawable(getResources().getDrawable(R.drawable.square));
                sawButton.setImageDrawable(getResources().getDrawable(R.drawable.sawtooth_selected));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
