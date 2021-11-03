package com.example.obstaclesovoidinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private final int MAX_LIVES = 4;

    private AlertDialog builder;
    private ImageButton panel_BTN_Right;
    private ImageButton panel_BTN_Left;
    private ImageView[] panel_IMG_engines;
    private ImageView[] panel_IMG_airplane;
    private ImageView[] panel_IMG_left_obstacles, panel_IMG_mid_obstacles, panel_IMG_right_obstacles;
    private ImageView[][] panel_IMG_Vertical_Arrays;

    private Timer timer = new Timer();
    int i = 4;
    int randChoose;

    private int current = 1;
    private int lives = MAX_LIVES;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView panel_IMG_background = findViewById(R.id.panel_IMG_background);
        Glide
                .with(this)
                .load(R.drawable.skybackground)
                .centerCrop()
                .into(panel_IMG_background);


        findViews();

        initButtons();



    }



    @Override
    protected void onStart() {
        super.onStart();
        startUI();
    }

    private void startUI() {
        timer = new Timer();

        //Random Choose of the column from which the obstacle will appear
        randChoose = (int) Math.floor(Math.random()*3);
        panel_IMG_Vertical_Arrays[randChoose][i].setVisibility(View.VISIBLE);

        // Timer Set and start running
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        update_UI_logic();

                    }
                });
            }
        }, 0, 500);
    }

    //The Game obstacles and crush logic
    private void update_UI_logic() {
        if(i>0){
            panel_IMG_Vertical_Arrays[randChoose][i].setVisibility(View.INVISIBLE);
            panel_IMG_Vertical_Arrays[randChoose][i-1].setVisibility(View.VISIBLE);
            i--;
        } else {
            if(current==randChoose){
                vibrate(1000);
                lives--;
                panel_IMG_engines[lives].setVisibility(View.INVISIBLE);
                if(lives==0){
                    gameOver();
                }
            }

            panel_IMG_Vertical_Arrays[randChoose][0].setVisibility(View.INVISIBLE);
            randChoose = (int) Math.floor(Math.random()*3);
            i=4;
            panel_IMG_Vertical_Arrays[randChoose][i].setVisibility(View.VISIBLE);
        }
    }

    private void gameOver() {
        timer.cancel();

        playSound(R.raw.audio_mayday);

        panel_IMG_airplane[current].setImageResource(R.drawable.ic_crush);
        vibrate(2000);
        Toast.makeText(getApplicationContext(),"Game Over",Toast.LENGTH_LONG).show();
        finish();
    }

    private void playSound(int audio_mayday) {

        final MediaPlayer mp = MediaPlayer.create(this, audio_mayday);
        mp.start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopUI();
    }

    private void stopUI() {
        timer.cancel();
    }

    //For Future Restart Use
    private void updateLivesView() {
        for (int i = panel_IMG_engines.length - 1; i >= 0; i--) {
            panel_IMG_engines[i].setVisibility(View.VISIBLE);
        }
    }


    private void move(boolean direction) {
        if(direction && current<=1){
            panel_IMG_airplane[current].setVisibility(View.INVISIBLE);
            current++;
            panel_IMG_airplane[current].setVisibility(View.VISIBLE);
        } else if (!direction && current>=1){
            panel_IMG_airplane[current].setVisibility(View.INVISIBLE);
            current--;
            panel_IMG_airplane[current].setVisibility(View.VISIBLE);
        }
    }

    private void findViews() {
        panel_BTN_Right = findViewById(R.id.panel_BTN_right);

        panel_BTN_Left = findViewById(R.id.panel_BTN_left);

        panel_IMG_engines = new ImageView[] {
                findViewById(R.id.engine1),
                findViewById(R.id.engine2),
                findViewById(R.id.engine3),
                findViewById(R.id.engine4)
        };

        panel_IMG_left_obstacles = new ImageView[] {
                findViewById(R.id.panel_IMG_s0_left),
                findViewById(R.id.panel_IMG_s1_l),
                findViewById(R.id.panel_IMG_s2_l),
                findViewById(R.id.panel_IMG_s3_l),
                findViewById(R.id.panel_IMG_s4_l),
        };

        panel_IMG_mid_obstacles = new ImageView[] {
                findViewById(R.id.panel_IMG_s0_mid),
                findViewById(R.id.panel_IMG_s1_m),
                findViewById(R.id.panel_IMG_s2_m),
                findViewById(R.id.panel_IMG_s3_m),
                findViewById(R.id.panel_IMG_s4_m),
        };

        panel_IMG_right_obstacles = new ImageView[] {
                findViewById(R.id.panel_IMG_s0_right),
                findViewById(R.id.panel_IMG_s1_r),
                findViewById(R.id.panel_IMG_s2_r),
                findViewById(R.id.panel_IMG_s3_r),
                findViewById(R.id.panel_IMG_s4_r),
        };

        panel_IMG_Vertical_Arrays = new ImageView[][]{
                panel_IMG_left_obstacles,
                panel_IMG_mid_obstacles,
                panel_IMG_right_obstacles};



        panel_IMG_airplane = new ImageView[]{
                findViewById(R.id.panel_IMG_main_left),
                findViewById(R.id.panel_IMG_main_mid),
                findViewById(R.id.panel_IMG_main_right),
        };
    }

    private void initButtons() {
        panel_BTN_Left.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate(100);
                //Right = True Left = False
                move(false);
            }
        }));

        panel_BTN_Right.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate(100);
                move(true);
            }
        }));
    }

    private void vibrate(int millisecond) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(millisecond, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(millisecond);
        }
    }
}