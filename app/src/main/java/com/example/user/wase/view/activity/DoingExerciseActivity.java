package com.example.user.wase.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.wase.R;

/**
 * Created by user on 2016-06-07.
 */
public class DoingExerciseActivity extends AppCompatActivity {

    ImageView startexercise_iv_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doingexercise);

        StartExerciseActivity.thisActivity.finish();

    }


    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.doingexercise_btn_back:
                AlertDialog quitExercisingAlert = askQuitExercising();
                quitExercisingAlert.show();
                break;
            case R.id.doingexercise_btn_skipgonext:
                Toast.makeText(getApplicationContext(), "Skip this equipment\nor Go to next step", Toast.LENGTH_SHORT).show();
                break;
        }
    }



    private AlertDialog askQuitExercising() {
        AlertDialog myDeleteDialogBox = new AlertDialog.Builder(this)
                .setTitle("Quit Exercising")
                .setMessage("Are you sure you want to stop exercising and go to the main page?")
                .setPositiveButton("Give up", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: Delete selected routines
                        Toast.makeText(getApplicationContext(), "Exercising is stopped.", Toast.LENGTH_SHORT).show();
                        finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return myDeleteDialogBox;
    }

    @Override
    public void onBackPressed() {
        AlertDialog quitExercisingAlert = askQuitExercising();
        quitExercisingAlert.show();
        //super.onBackPressed();
    }
}
