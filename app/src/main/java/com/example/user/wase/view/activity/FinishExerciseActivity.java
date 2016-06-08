package com.example.user.wase.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.wase.R;
import com.example.user.wase.utility.TaskScheduler;

/**
 * Created by user on 2016-06-07.
 */
public class FinishExerciseActivity extends AppCompatActivity {


    Button btn_check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finishexercise);
        DoingExerciseActivity.thisActivity.finish();

        initWidgets();

    }

    private void initWidgets() {


        btn_check = (Button) findViewById(R.id.finishexercise_btn_finish);
        btn_check.setBackgroundResource(R.drawable.effect_button_press);
    }



    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.finishexercise_btn_finish:
                Toast.makeText(getApplicationContext(), "Your record is saved!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }



    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Your record is saved!", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}
