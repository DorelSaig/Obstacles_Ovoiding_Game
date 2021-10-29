package com.example.obstaclesovoidinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private final int MAX_LIVES = 3;

    private AlertDialog builder;
    private Button panel_BTN_Right;
    private Button panel_BTN_Left;
    private ImageView[] panel_IMG_hearts;
    private ImageView[] panel_IMG_temps;
    private ImageView[] panel_IMG_left_obstacles, panel_IMG_mid_obstacles, panel_IMG_right_obstacles;
    private ImageView[][] panel_IMG_Vertical_Arrays;
    private int current = 1;
    private int lives = MAX_LIVES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        findviews();

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



        panel_IMG_temps = new ImageView[]{
                findViewById(R.id.panel_IMG_main_left),
                findViewById(R.id.panel_IMG_main_mid),
                findViewById(R.id.panel_IMG_main_right),
        };

        panel_BTN_Left.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Right = True Left = False
                move(false);
            }
        }));

        panel_BTN_Right.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move(true);
            }
        }));

    }

    Handler handler;
    Runnable runnable;
    int delay = 500;
    int i = 4;
    int randChoose;

    @Override
    protected void onResume() {
        Handler handler = new Handler();

        randChoose = (int) Math.floor(Math.random()*3);
        //start handler as activity become visible
        panel_IMG_Vertical_Arrays[randChoose][i].setVisibility(View.VISIBLE);


        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                //do something

                if(i>0){
                    panel_IMG_Vertical_Arrays[randChoose][i].setVisibility(View.INVISIBLE);
                    panel_IMG_Vertical_Arrays[randChoose][i-1].setVisibility(View.VISIBLE);
                    i--;
                } else {
                    if(current==randChoose){
                        lives--;
                        panel_IMG_hearts[lives].setVisibility(View.INVISIBLE);
                        if(lives==0){
                            Toast.makeText(getApplicationContext(),"Game Over",Toast.LENGTH_LONG).show();
                            finish();

                        }
                    }

                    panel_IMG_Vertical_Arrays[randChoose][0].setVisibility(View.INVISIBLE);
                    randChoose = (int) Math.floor(Math.random()*3);
                    i=4;
                    panel_IMG_Vertical_Arrays[randChoose][i].setVisibility(View.VISIBLE);
                }

                handler.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }



    // If onPause() is not included the threads will double up when you
// reload the activity

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void updateLivesView() {
        for (int i = panel_IMG_hearts.length - 1; i >= 0; i--) {
            panel_IMG_hearts[i].setVisibility(View.VISIBLE);
        }
    }


    private void move(boolean direction) {
        if(direction && current<=1){
            panel_IMG_temps[current].setVisibility(View.INVISIBLE);
            current++;
            panel_IMG_temps[current].setVisibility(View.VISIBLE);
        } else if (!direction && current>=1){
            panel_IMG_temps[current].setVisibility(View.INVISIBLE);
            current--;
            panel_IMG_temps[current].setVisibility(View.VISIBLE);
        }
    }


    private void findviews() {
        panel_BTN_Right = findViewById(R.id.panel_BTN_right);
        panel_BTN_Left = findViewById(R.id.panel_BTN_left);
        panel_IMG_hearts = new ImageView[] {
                findViewById(R.id.heart1),
                findViewById(R.id.heart2),
                findViewById(R.id.heart3),
        };
    }
}