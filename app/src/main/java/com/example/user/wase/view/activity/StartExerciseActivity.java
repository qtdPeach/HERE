package com.example.user.wase.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.user.wase.R;

/**
 * Created by user on 2016-06-07.
 */
public class StartExerciseActivity extends AppCompatActivity {

    public static Activity thisActivity;
    ImageView startexercise_iv_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startexercise);

        thisActivity = this;

        startexercise_iv_start = (ImageView) findViewById(R.id.startexercise_iv_start);

        startexercise_iv_start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                startexercise_iv_start.setImageResource(R.drawable.start_exercise_anim);

                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startexercise_iv_start.setImageResource(R.drawable.start_exercise);
                        try {
                            Thread.sleep(100);

                            Intent intent_doingexercise = new Intent(getApplicationContext(), DoingExerciseActivity.class);
                            startActivity(intent_doingexercise);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, 50);

                return false;
            }
        });

    }

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.startexercise_btn_back:
                finish();
                break;
        }
    }

}
