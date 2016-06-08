package com.example.user.wase.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


    EditText et_record_name;

    Button btn_check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finishexercise);
        DoingExerciseActivity.thisActivity.finish();

        initWidgets();

    }

    private void initWidgets() {
        et_record_name = (EditText) findViewById(R.id.finishexercise_et_recordname);

        btn_check = (Button) findViewById(R.id.finishexercise_btn_finish);
        btn_check.setBackgroundResource(R.drawable.effect_button_press);
    }



    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.finishexercise_btn_finish:
                if (et_record_name.getText().toString().equals("") || et_record_name.getText() == null) {
                    Snackbar.make(v, "Please write your record name first", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                } else {
                    String recordName = et_record_name.getText().toString();
                    Toast.makeText(getApplicationContext(), "Your record is saved!\n" + recordName, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }



    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Your record is saved!", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}
