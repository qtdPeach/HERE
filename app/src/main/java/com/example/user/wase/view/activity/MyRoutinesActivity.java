package com.example.user.wase.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.user.wase.R;

/**
 * Created by ymbaek on 2016-04-18.
 */
public class MyRoutinesActivity extends AppCompatActivity {

    public static final String TAG = "MyRoutinesActivity";

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myroutines);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("My Exercise Routines");
        toolbar.setNavigationIcon(R.mipmap.ic_menu_arrow_back);
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("My Exercise Routines");
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
    }


    private AlertDialog askDeletion() {
        AlertDialog myDeleteDialogBox = new AlertDialog.Builder(this)
                .setTitle("Delete a routine")
                .setMessage("Are you sure you want to delete the selected routine(s)?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: Delete selected routines
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


    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.setting_myroutine_btn_newroutine:
                break;
            case R.id.setting_myroutine_btn_modifyroutine:
                break;
            case R.id.setting_myroutine_btn_deleteroutine:
                AlertDialog deleteAlert = askDeletion();
                deleteAlert.show();
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
