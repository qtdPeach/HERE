package com.example.user.wase.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.user.wase.R;
import com.example.user.wase.model.MyHereAgent;
import com.example.user.wase.model.MyRecord;
import com.example.user.wase.model.MyRoutine;

/**
 * Created by ymbaek on 2016-04-18.
 */
public class MyEquipmentsActivity extends AppCompatActivity {

    public static final String TAG = "MyEquipmentsActivity";
    public static final String TAG_DB = "MyEquipmentsDBTag";


    //Toolbar
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myequipments);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("My HERE Agents");
        toolbar.setNavigationIcon(R.mipmap.ic_menu_arrow_back);
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("My HERE Agents");
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);

    }


    public void mOnClick(View v) {
        switch (v.getId()) {

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
